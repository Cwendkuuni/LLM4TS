import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  private static final String TEST_FILENAME = "test.js";
  private static final String TEST_MODULE_NAME = "testModule";
  private static final String TEST_REQUIRED_MODULE_NAME = "requiredModule";

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node rootNode;
  private ProcessCommonJSModules processor;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    input = new CompilerInput(TEST_FILENAME, "test.js");
    rootNode = NodeFactory.createRoot();
    processor = new ProcessCommonJSModules(compiler, TEST_FILENAME);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "path/to/testModule.js";
    String expected = TEST_MODULE_NAME + "_";
    String actual = guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithRelativePath() {
    String filename = "./path/to/testModule.js";
    String expected = TEST_MODULE_NAME + "_";
    String actual = guessCJSModuleName(filename, "test.js");
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    String filename = "path\\to\\testModule.js";
    String expected = "path/to/testModule.js";
    String actual = normalizeSourceName(filename);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testToModuleName() {
    String filename = "path/to/testModule.js";
    String expected = TEST_MODULE_NAME + "_";
    String actual = toModuleName(filename);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testToModuleNameWithRequireCall() {
    String filename = "path/to/testModule.js";
    String requiredFilename = "path/to/requiredModule.js";
    String expected = TEST_REQUIRED_MODULE_NAME + "_";
    String actual = toModuleName(requiredFilename, filename);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess() throws Exception {
    rootNode.setSourceFileName(TEST_FILENAME);
    processor.process(input, rootNode);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(TEST_MODULE_NAME, module.getName());

    String expectedModuleName = TEST_MODULE_NAME + "_";
    String expectedProvideStatement =
        "goog.provide('" + TEST_MODULE_NAME + "');\n";
    String expectedRequireStatement =
        "goog.require('" + TEST_REQUIRED_MODULE_NAME + "');\n";
    String expectedScript =
        "var " + TEST_MODULE_NAME + "_" + "_ = " + TEST_MODULE_NAME + ";\n" +
            "goog.exportProperty(this, '" + TEST_MODULE_NAME + "', " +
            TEST_MODULE_NAME + "_);\n";

    Node scriptNode = rootNode.getFirstChild();
    assertEquals(expectedScript, scriptNode.toString());

    assertEquals(expectedProvideStatement,
        rootNode.getChildAtIndex(1).toString());
    assertEquals(expectedRequireStatement,
        rootNode.getChildAtIndex(2).toString());

    Map<String, Node> requireStatements = new HashMap<>();
    NodeTraversal.traverse(compiler, rootNode, new NodeVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node n, Node parent) {
        String name = n.getFirstChild().getString();
        if (name.equals("goog.require")) {
          requireStatements.put(n.getChildAtIndex(1).getString(), n);
        }
        super.visitCall(t, n, parent);
      }
    });
    assertEquals(1, requireStatements.size());

    Node requireNode = requireStatements.values().iterator().next();
    assertEquals(TEST_REQUIRED_MODULE_NAME, requireNode.toString());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testProcessWithInvalidURI() throws Exception {
    String filename = "test.js";
    rootNode.setSourceFileName(filename);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filename);
    processor.process(input, rootNode);
  }

  @org.junit.Test
  public void testVisitRequireCall() {
    Node requireNode = NodeFactory.createCall(
        NodeFactory.createName("require"), NodeFactory.createString(TEST_REQUIRED_MODULE_NAME));
    Node parent = NodeFactory.createRoot();
    processor.new ProcessCommonJsModulesCallback().visitRequireCall(
        new NodeTraversal(compiler, parent), requireNode, parent);

    Node moduleRef = parent.getFirstChild();
    assertEquals(TEST_REQUIRED_MODULE_NAME, moduleRef.toString());

    Node script = rootNode.getFirstChild();
    Node requireStatement = script.getChildAtIndex(2);
    assertTrue(requireStatement.getFirstChild().isCall());
    assertEquals(TEST_REQUIRED_MODULE_NAME,
        requireStatement.getFirstChild().getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testVisitScript() {
    Node scriptNode = NodeFactory.createScript("var test = 'hello';");
    Node parent = NodeFactory.createRoot();
    processor.new ProcessCommonJsModulesCallback().visitScript(
        new NodeTraversal(compiler, parent), scriptNode);

    String expected = TEST_MODULE_NAME + "_test$ = test;\n" +
        "goog.exportProperty(this, '" + TEST_MODULE_NAME + "', " +
        TEST_MODULE_NAME + "_test$);\n";
    assertEquals(expected, scriptNode.toString());
  }

  @org.junit.Test
  public void testVisitModuleExports() {
    Node moduleExportsPropNode = NodeFactory.createGetProp(
        NodeFactory.createName("module.exports"), NodeFactory.createName("module"));
    Node parent = NodeFactory.createRoot();
    processor.new ProcessCommonJsModulesCallback().visitModuleExports(moduleExportsPropNode);

    String expected = TEST_MODULE_NAME + "_module$exports = module.exports;\n";
    assertEquals(expected, moduleExportsPropNode.toString());
  }
}