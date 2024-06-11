package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String FILENAME_PREFIX = "test_";
  private static final String CURRENT_FILENAME = "main.js";

  private CompilerInput input;
  private Node rootNode;
  private ProcessCommonJSModules processor;

  @org.junit.Before
  public void setUp() {
    input = new CompilerInput(new File("test.js"), FILENAME_PREFIX, null);
    input.setSourceFileName("main.js");
    rootNode = createNode("(function() {});");
    NodeFactory factory = new NodeFactory(null);
    NodeTraversal traversal = NodeTraversal.createTraverser(factory, null);

    processor = new ProcessCommonJSModules(mock(AbstractCompiler.class),
        FILENAME_PREFIX, true);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    assertThat(processor.guessCJSModuleName("test.js")).isEqualTo("test$");
    assertThat(processor.guessCJSModuleName("test/sub/test.js")).isEqualTo(
        "test$sub$test");
    assertThat(processor.guessCJSModuleName("./test.js")).isEqualTo(
        "test$");
    assertThat(processor.guessCJSModuleName("../test.js")).isEqualTo(
        "test$");
  }

  @org.junit.Test
  public void testProcessNoRequireCalls() {
    processor.process(input, rootNode);

    NodeTraversal.traverse(processor.compiler, rootNode, new AssertNoOpCallback());
  }

  @org.junit.Test
  public void testProcessWithRequireCalls() {
    Node requireCall = createNode("require('test.js')");
    Node scriptNode = createNode("(function(test) {});");
    scriptNode.addChildToFront(requireCall);
    input.setSourceName("main.js");

    processor.process(input, rootNode);

    NodeTraversal.traverse(processor.compiler, rootNode, new RequireCallHandler());
  }

  @org.junit.Test
  public void testProcessWithModuleExports() {
    Node requireCall = createNode("require('test.js')");
    Node exports = createNode("module.exports = { foo: 1 }");
    Node scriptNode = createNode("(function(test) { test.foo = 2; });");
    scriptNode.addChildToFront(requireCall);
    scriptNode.addChildToBack(exports);
    input.setSourceName("main.js");

    processor.process(input, rootNode);

    NodeTraversal.traverse(processor.compiler, rootNode, new ModuleExportsHandler());
  }

  @org.junit.Test
  public void testProcessWithScriptNode() {
    Node scriptNode = createNode("(function() { var test = 1; });");
    input.setSourceName("test.js");

    processor.process(input, rootNode);

    NodeTraversal.traverse(processor.compiler, rootNode, new ScriptNodeHandler());
  }

  private Node createNode(String code) {
    NodeFactory factory = new NodeFactory(null);
    return factory.createScript(code, Token.DUMMY, null, null);
  }

  private static class AssertNoOpCallback extends AbstractPostOrderCallback {}

  private static class RequireCallHandler extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName())) {
        Scope scope = t.getScope();
        Node moduleNameNode = n.getChildAtIndex(1);
        Preconditions.checkState(moduleNameNode.isString(),
            "Expected require call to have a string argument for module name");

        String moduleName = moduleNameNode.getString();
        Node moduleRef = IR.name(processor.toModuleName(moduleName)).srcref(n);
        parent.replaceChild(n, moduleRef);

        Node script = getCurrentScriptNode(parent);
        CompilerInput ci = t.getInput();
        ci.addRequire(moduleName);
        JSModule m = new JSModule(moduleName);
        m.addAndOverrideModule(ci);
        processor.module = m;

        script.addChildToFront(IR.exprResult(
            IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
                IR.string(moduleName))).copyInformationFromForTree(n));
        processor.compiler.reportCodeChange();
      }
    }

    private Node getCurrentScriptNode(Node n) {
      while (true) {
        if (n.isScript()) {
          return n;
        }
        n = n.getParent();
      }
    }
  }

  private static class ScriptNodeHandler extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isScript()) {
        String moduleName = processor.guessCJSModuleName(input.getSourceFileName());
        Node moduleNode = IR.var(IR.name(moduleName), IR.objectlit())
            .copyInformationFromForTree(n);
        Node provideNode = IR.call(IR.getprop(IR.name("goog"),
            IR.string("provide")), IR.string(moduleName))
            .copyInformationFromForTree(n);

        n.addChildToFront(moduleNode);
        n.addChildToFront(provideNode);

        if (processor.reportDependencies) {
          CompilerInput ci = t.getInput();
          ci.addProvide(moduleName);
          JSModule m = new JSModule(moduleName);
          m.addAndOverrideModule(ci);
          processor.module = m;
        }

        processor.compiler.reportCodeChange();
      }
    }
  }

  private static class ModuleExportsHandler extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isGetProp() &&
          "module.exports".equals(n.getQualifiedName())) {
        String moduleName = processor.guessCJSModuleName(input.getSourceFileName());
        Node module = n.getChildAtIndex(0);
        Node exports = n.getChildAtIndex(1);

        module.putProp(Node.ORIGINALNAME_PROP, "module");
        module.setString(moduleName);
        exports.putProp(Node.ORIGINALNAME_PROP, "exports");
        exports.setString("module$exports");

        processor.compiler.reportCodeChange();
      }
    }
  }
}