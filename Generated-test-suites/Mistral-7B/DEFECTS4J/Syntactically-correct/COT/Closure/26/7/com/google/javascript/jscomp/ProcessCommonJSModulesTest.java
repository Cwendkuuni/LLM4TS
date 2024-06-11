import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  CompilerOptions options;
  AbstractCompiler compiler;
  CompilerInput input;
  ProcessCommonJSModules processor;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions();
    compiler = new TestCompiler(options);
    input = new CompilerInput("test.js", new File("src/test.js"));
    processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test/sub/module.js";
    String expected = "module$test$sub";
    assertEquals(expected, guessCJSModuleName(filename));
  }

  @Test
  public void testToModuleName() {
    String filename = "test/sub/module.js";
    String expected = "module$test$sub";
    String currentFilename = "test.js";
    assertEquals(expected, toModuleName(filename, currentFilename));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "test.js";
    String expected = "test";
    assertEquals(expected, normalizeSourceName(filename));
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitRequireCall()
      throws IOException, URISyntaxException {
    String requiredFilename = "test/sub/module.js";
    String currentFilename = "test.js";
    NodeTraversal traversal = new NodeTraversal(compiler, input.getRootNode());
    Node requireNode = IR.newCall(IR.getprop(IR.name("require"), null),
        IR.string(toModuleName(requiredFilename, currentFilename)));

    Node scriptNode = IR.newScript();
    Node parentNode = IR.newBlock();
    parentNode.addChildToBack(requireNode);
    parentNode.addChildToBack(scriptNode);

    processor.process(input.getExterns(), input.getRootNode());
    NodeTraversal.traverse(compiler, input.getRootNode(), new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName()) &&
            n.getChildAtIndex(1).isString()) {
          Node require = n;
          assertEquals(requireNode, require);
          assertEquals(0, require.getChildCount());
          assertTrue(parent.getChildCount() > 1);
          Node googRequireCall = parent.getChildAtIndex(0);
          assertEquals("goog.require", googRequireCall.getQualifiedName());
          assertEquals(1, googRequireCall.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              googRequireCall.getChildAtIndex(0).getString());
        }
      }

      private String expectedModuleName(String filename) {
        return toModuleName(filename).substring(MODULE_NAME_PREFIX.length());
      }
    });
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitScript()
      throws IOException, URISyntaxException {
    String moduleName = "test";
    String filename = "test.js";
    Node scriptNode = IR.newScript();
    Node parentNode = IR.newBlock();
    parentNode.addChildToBack(scriptNode);

    processor.process(input.getExterns(), input.getRootNode());
    NodeTraversal.traverse(compiler, input.getRootNode(), new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals(scriptNode, n);
          assertNull(n.getFirstChild());
          Node googProvideCall = n.getChildAtIndex(0);
          assertEquals("goog.provide", googProvideCall.getQualifiedName());
          assertEquals(1, googProvideCall.getChildCount());
          assertEquals(moduleName, googProvideCall.getChildAtIndex(0)
              .getString());
          Node moduleExports = n.getChildAtIndex(1);
          assertEquals("module$exports", moduleExports.getString());
          assertEquals(0, moduleExports.getChildCount());

          Node varNode = n.getChildAtIndex(2);
          assertEquals(moduleName, varNode.getString());
          assertEquals(Node.VAR_DECL, varNode.getType());
          assertNotNull(varNode.getFirstChild());
          assertEquals(Node.VAR, varNode.getFirstChild().getType());
          assertEquals(null, varNode.getFirstChild().getInit());
          assertNull(varNode.getProp(Node.ORIGINALNAME_PROP));
        }
      }
    });
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitModuleExports()
      throws IOException, URISyntaxException {
    String moduleName = "test.sub";
    String filename = "test.js";
    Node scriptNode = IR.newScript();
    Node parentNode = IR.newBlock();
    parentNode.addChildToBack(scriptNode);

    Node moduleExports = IR.newGetprop(IR.name(moduleName), "exports");
    parentNode.addChildToBack(moduleExports);

    processor.process(input.getExterns(), input.getRootNode());
    NodeTraversal.traverse(compiler, input.getRootNode(), new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isGetProp() && "module.exports".equals(n.getQualifiedName())) {
          assertEquals(moduleExports, n);
          assertEquals(1, n.getChildCount());
        }
      }
    });
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitModuleExportsOverride()
      throws IOException, URISyntaxException {
    String moduleName = "test.sub";
    String filename = "test.js";
    Node scriptNode = IR.newScript();
    Node parentNode = IR.newBlock();
    parentNode.addChildToBack(scriptNode);

    Node moduleExports = IR.newGetprop(IR.name(moduleName), "exports");
    Node moduleExportsOverride = IR.newGetprop(IR.name(moduleName),
        "module$exports");
    parentNode.addChildToBack(moduleExportsOverride);
    parentNode.addChildToBack(moduleExports);

    processor.process(input.getExterns(), input.getRootNode());
    NodeTraversal.traverse(compiler, input.getRootNode(), new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isGetProp() && "module.exports".equals(n.getQualifiedName())) {
          fail("Expected module$exports");
        }
        if (n.isGetprop() && "module$exports".equals(n.getQualifiedName())) {
          assertEquals(moduleExportsOverride, n);
          assertEquals(0, n.getChildCount());
        }
      }
    });
  }

  @Test
  public void testProcess() throws IOException, URISyntaxException {
    String requiredFilename = "test/sub/module.js";
    String currentFilename = "test.js";

    String script = "function test() {\n" +
        "  require('" + requiredFilename + "');\n" +
        "}";

    Node rootNode = new NodeFactory().compile(script);
    CompilerInput input = new CompilerInput("test.js", new File("test.js"));

    processor.process(input.getExterns(), rootNode);

    List<String> requires = input.getRequireMap().getKeys();
    assertEquals(1, requires.size());
    assertEquals(expectedModuleName(requiredFilename), requires.get(0));

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    NodeTraversal.traverse(compiler, rootNode, new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "test".equals(n.getString())) {
          Node requireCall = n.getChildAtIndex(1);
          assertEquals(1, requireCall.getChildCount());
          assertEquals("require", requireCall.getFirstChild().getString());
          Node require = requireCall.getChildAtIndex(0);
          assertEquals(1, require.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              require.getChildAtIndex(0).getString());
          assertTrue(parent.getChildCount() > 1);
          Node googRequireCall = parent.getChildAtIndex(0);
          assertEquals("goog.require", googRequireCall.getQualifiedName());
          assertEquals(1, googRequireCall.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              googRequireCall.getChildAtIndex(0).getString());
        }
      }

      private String expectedModuleName(String filename) {
        return toModuleName(filename).substring(MODULE_NAME_PREFIX.length());
      }
    });
  }

  @Test
  public void testProcessWithDependenciesReported()
      throws IOException, URISyntaxException {
    String moduleName = "test";
    String requiredFilename = "test/sub/module.js";
    String currentFilename = "test.js";

    String script = "function test() {\n" +
        "}";

    Node rootNode = new NodeFactory().compile(script);
    CompilerInput input = new CompilerInput("test.js", new File("test.js"));

    processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(input.getExterns(), rootNode);

    List<String> requires = input.getRequireMap().getKeys();
    assertEquals(0, requires.size());

    processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), rootNode);

    requires = input.getRequireMap().getKeys();
    assertEquals(1, requires.size());
    assertEquals(expectedModuleName(requiredFilename), requires.get(0));

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    NodeTraversal.traverse(compiler, rootNode, new Callback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "test".equals(n.getString())) {
          Node requireCall = n.getChildAtIndex(1);
          assertEquals(1, requireCall.getChildCount());
          assertEquals("require", requireCall.getFirstChild().getString());
          Node require = requireCall.getChildAtIndex(0);
          assertEquals(1, require.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              require.getChildAtIndex(0).getString());
          assertTrue(parent.getChildCount() > 1);
          Node googRequireCall = parent.getChildAtIndex(0);
          assertEquals("goog.require", googRequireCall.getQualifiedName());
          assertEquals(1, googRequireCall.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              googRequireCall.getChildAtIndex(0).getString());
        }
      }

      private String expectedModuleName(String filename) {
        return toModuleName(filename).substring(MODULE_NAME_PREFIX.length());
      }
    });
  }

  @Test
  public void testProcessWithRewrittenRequireCalls()
      throws IOException, URISyntaxException {
    String requiredFilename = "test/sub/module.js";
    String currentFilename = "test.js";

    String script = "function test() {\n" +
        "  require('" + requiredFilename + "').doSomething();\n" +
        "}";

    Node rootNode = new NodeFactory().compile(script);
    CompilerInput input = new CompilerInput("test.js", new File("test.js"));

    processor.process(input.getExterns(), rootNode);

    List<String> requires = input.getRequireMap().getKeys();
    assertEquals(1, requires.size());
    assertEquals(expectedModuleName(requiredFilename), requires.get(0));

    String expected = "function test() {\n" +
        "  " + expectedModuleName(requiredFilename) + ".doSomething();\n" +
        "}";

    Node expectedRootNode = new NodeFactory().compile(expected);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    NodeTraversal.traverse(compiler, rootNode, new Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "test".equals(n.getString())) {
          assertTrue(parent.getChildCount() > 1);
          Node requireCall = n.getChildAtIndex(1);
          assertEquals(1, requireCall.getChildCount());
          assertEquals("require", requireCall.getFirstChild().getString());
          Node require = requireCall.getChildAtIndex(0);
          assertEquals(1, require.getChildCount());
          assertEquals(expectedModuleName(requiredFilename),
              require.getChildAtIndex(0).getString());
          Node scriptNode = parent.getChildAtIndex(1);
          assertEquals(expectedRootNode, scriptNode);
        }
      }

      private String expectedModuleName(String filename) {
        return toModuleName(filename).substring(MODULE_NAME_PREFIX.length());
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void testProcessMultipleScripts() throws Exception {
    String script1 = "function test1() {}";
    String script2 = "function test2() {}";
    String filename = "test.js";

    Node rootNode = new NodeFactory().compile(script1 + "\n" + script2);
    CompilerInput input = new CompilerInput(filename, new File(filename));

    processor.process(input.getExterns(), rootNode);
  }

  @Test
  public void testSuffixVarsCallback() throws IOException, URISyntaxException {
    String moduleName = "test";
    String filename = "test.js";

    Node rootNode = IR.newBlock();
    Node scriptNode = IR.newScript();
    rootNode.addChildToBack(scriptNode);

    processor.process(input.getExterns(), rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    NodeTraversal.traverse(compiler, rootNode, new Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName() && moduleName.equals(n.getString())) {
          String originalName = n.getProp(Node.ORIGINALNAME_PROP);
          assertEquals(moduleName, originalName);
          Node varNode = n;
          assertEquals(Node.VAR_DECL, varNode.getType());
          assertEquals(1, varNode.getChildCount());
          assertEquals(Node.VAR, varNode.getChild(0).getType());
          assertEquals(null, varNode.getChild(0).getInit());
          Node suffixVarNode = n.getChildAtIndex(1);
          assertEquals(Node.VAR_DECL, suffixVarNode.getType());
          assertEquals("$$" + moduleName, suffixVarNode.getString());
          assertEquals(null, suffixVarNode.getChild(0));
        }
      }
    });
  }

  private static class Callback extends AbstractPostOrderCallback {
  }
}