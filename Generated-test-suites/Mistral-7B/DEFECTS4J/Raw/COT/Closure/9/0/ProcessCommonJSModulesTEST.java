import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.makeString;
import static com.google.javascript.rhino.NodeFactory.makeName;
import static com.google.javascript.rhino.NodeFactory.makeCall;
import static com.google.javascript.rhino.NodeFactory.makeIfNode;
import static com.google.javascript.rhino.NodeFactory.makeBlock;
import static com.google.javascript.rhino.NodeFactory.makeVar;
import static com.google.javascript.rhino.NodeFactory.makeAssign;
import static com.google.javascript.rhino.NodeFactory.makeGetProp;
import static com.google.javascript.rhino.NodeFactory.makeObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.makeStringLiteral;
import static com.google.javascript.rhino.NodeFactory.makeExprResult;
import static com.google.javascript.rhino.NodeFactory.makeBooleanLiteral;
import static com.google.javascript.rhino.NodeFactory.makeCallStmt;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private Node rootNode;
  private CompilerPass processor;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    rootNode = NodeFactory.makeProgram();
    processor = new ProcessCommonJSModules(compiler, ".", true);
  }

  @Test
  public void testProcess() {
    Node input = NodeFactory.makeScript(
        "var a = require('foo');" + System.lineSeparator() +
        "goog.provide('bar');" + System.lineSeparator() +
        "goog.require('foo');" + System.lineSeparator() +
        "function foo() { };" + System.lineSeparator() +
        "exports.baz = 42;"
    );

    NodeTraversal.traverse(compiler, rootNode, input);
    processor.process(NodeUtil.getExterns(rootNode), rootNode);
    Node expectedOutput = NodeFactory.makeProgram(
        NodeFactory.makeDecl(NodeFactory.makeName("bar"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "var a = module.exports;" + System.lineSeparator() +
            "goog.provide('bar');" + System.lineSeparator() +
            "goog.require('foo');" + System.lineSeparator() +
            "exports.baz = 42;" + System.lineSeparator() +
            "exports.module$exports = module.exports;" + System.lineSeparator() +
            "module.exports = bar;" + System.lineSeparator()
        ))
    );

    assertEquals(expectedOutput.toSource(), rootNode.toSource());
  }

  @Test
  public void testVisitRequireCall() {
    Node input = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("foo"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "exports.a = 42;"
        ))
    );
    Node traverseNode = NodeFactory.makeCall(makeName("require"), makeStringLiteral("foo"));
    Node expectedOutput = NodeFactory.makeCall(makeName("goog.require"), makeStringLiteral("foo"));

    Node scriptNode = NodeFactory.makeScript(
        "require('foo');" + System.lineSeparator() +
        "goog.provide('bar');" + System.lineSeparator() +
        "exports.baz = 42;"
    );

    ProcessCommonJsModulesCallback callback = new ProcessCommonJsModulesCallback();
    traverse(compiler, rootNode, traverseNode, callback);
    assertEquals(expectedOutput, rootNode.getChildAtIndex(1));
  }

  @Test
  public void testVisitScript() {
    Node input = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("foo"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "exports.a = 42;"
        ))
    );

    Node expectedOutput = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("bar"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "var a = module.exports;" + System.lineSeparator() +
            "goog.provide('bar');" + System.lineSeparator() +
            "exports.baz = 42;" + System.lineSeparator() +
            "exports.module$exports = module.exports;" + System.lineSeparator() +
            "module.exports = bar;" + System.lineSeparator()
        ))
    );

    Node scriptNode = NodeFactory.makeScript(
        "require('foo');" + System.lineSeparator() +
        "goog.provide('bar');" + System.lineSeparator() +
        "exports.baz = 42;"
    );

    ProcessCommonJsModulesCallback callback = new ProcessCommonJsModulesCallback();
    Node root = NodeFactory.makeProgram();
    root.addChildToFront(input);
    root.addChildToFront(scriptNode);
    processor.process(NodeUtil.getExterns(root), root);

    assertEquals(expectedOutput.toSource(), root.toSource());
  }

  @Test
  public void testVisitModuleExports() {
    Node input = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("foo"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "module.exports = { a: 42 };"
        ))
    );

    Node expectedOutput = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("bar"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "goog.provide('bar');" + System.lineSeparator() +
            "exports.module$exports = { a: 42 };" + System.lineSeparator() +
            "exports = bar;" + System.lineSeparator()
        ))
    );

    Node scriptNode = NodeFactory.makeScript(
        "goog.provide('bar');" + System.lineSeparator() +
        "module.exports = { a: 42 };"
    );

    ProcessCommonJsModulesCallback callback = new ProcessCommonJsModulesCallback();
    Node root = NodeFactory.makeProgram();
    root.addChildToFront(input);
    root.addChildToFront(scriptNode);
    processor.process(NodeUtil.getExterns(root), root);

    assertEquals(expectedOutput.toSource(), root.toSource());
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node input = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("foo"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "module.exports = { a: 42 };"
        ))
    );

    Node expectedOutput = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("bar"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "goog.provide('bar');" + System.lineSeparator() +
            "exports.module$exports = module.exports;" + System.lineSeparator() +
            "exports = bar;" + System.lineSeparator() +
            "if (module.exports) {" + System.lineSeparator() +
            "  module.exports = bar;" + System.lineSeparator() +
            "}"
        ))
    );

    Node scriptNode = NodeFactory.makeScript(
        "goog.provide('bar');" + System.lineSeparator() +
        "module.exports = { a: 42 };"
    );

    ProcessCommonJsModulesCallback callback = new ProcessCommonJsModulesCallback();
    Node root = NodeFactory.makeProgram();
    root.addChildToFront(input);
    root.addChildToFront(scriptNode);
    processor.process(NodeUtil.getExterns(root), root);

    assertEquals(expectedOutput.toSource(), root.toSource());
  }

  @Test
  public void testSuffixVarsCallback() {
    Node input = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("a"), NodeFactory.makeNumberLiteral(42), NodeFactory.makeScript(
            "exports.b = a;"
        ))
    );

    Node expectedOutput = NodeFactory.makeProgram(
        NodeFactory.makeDecl(makeName("bar"), NodeFactory.makeObjectLiteral(), NodeFactory.makeScript(
            "goog.provide('bar');" + System.lineSeparator() +
            "exports.module$exports = {};" + System.lineSeparator() +
            "exports = bar;" + System.lineSeparator() +
            "exports.module$$a = 42;" + System.lineSeparator() +
            "exports.b = module$$a;"
        ))
    );

    Node scriptNode = NodeFactory.makeScript(
        "goog.provide('bar');" + System.lineSeparator() +
        "exports.b = a;"
    );

    SuffixVarsCallback suffixVarsCallback = new SuffixVarsCallback("module$$");
    Node root = NodeFactory.makeProgram();
    root.addChildToFront(input);
    root.addChildToFront(scriptNode);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", true);
    processor.addPass(suffixVarsCallback);
    processor.process(NodeUtil.getExterns(root), root);

    assertEquals(expectedOutput.toSource(), root.toSource());
  }
}

class ProcessCommonJSModulesCallback extends AbstractPostOrderCallback {
  private static final String EXPORTS = "exports";

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isName()) {
      String name = n.getString();
      if (EXPORTS.equals(name)) {
        n.setString("module$exports");
        n.putProp(Node.ORIGINALNAME_PROP, EXPORTS);
      } else {
        Scope.Var var = t.getScope().getVar(name);
        if (var != null && var.isGlobal()) {
          n.setString(name + "$$module$$");
          n.putProp(Node.ORIGINALNAME_PROP, name);
        }
      }
    }
  }
}

class SuffixVarsCallback extends AbstractPostOrderCallback {

  private final String suffix;

  SuffixVarsCallback(String suffix) {
    this.suffix = suffix;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isName()) {
      String name = n.getString();
      if (suffix.equals(name)) {
        return;
      }
      if (EXPORTS.equals(name)) {
        n.setString(name + "$$" + suffix);
        n.putProp(Node.ORIGINALNAME_PROP, name);
      } else {
        Scope.Var var = t.getScope().getVar(name);
        if (var != null && var.isGlobal()) {
          n.setString(name + "$$" + suffix);
          n.putProp(Node.ORIGINALNAME_PROP, name);
        }
      }
    }
  }
}