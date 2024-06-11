import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  Compiler compiler;
  Node rootNode;

  @Before
  public void setup() throws Exception {
    compiler = new Compiler();
    compiler.initialize();
    String input = "function main() {\n" +
        "require(\"module1\");\n" +
        "require(\"module2\");\n" +
        "}\n" +
        "// module1.js\n" +
        "goog.provide('module1');\n" +
        "var x = 1;\n" +
        "exports = {\n" +
        "  y: 2,\n" +
        "  z: function() {\n" +
        "    return 3;\n" +
        "  }\n" +
        "};\n";
    File testFile = File.createTempFile("ProcessCommonJSModulesTest", ".js");
    testFile.setWritable(true);
    try (java.io.PrintWriter out = new PrintWriter(testFile)) {
      out.print(input);
    }
    String sourceName = testFile.getAbsolutePath();
    rootNode = compiler.parse(sourceName, null, null);
  }

  @After
  public void tearDown() throws Exception {
    compiler.shutdown();
  }

  @Test
  public void testProcess() throws IOException, InterruptedException {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(null, rootNode);
    Node newRoot = pass.getModule().getMainSource();
    assertEquals("function main() {\n" +
        "require(\"module1$module$exports\");\n" +
        "require(\"module2$module$exports\");\n" +
        "goog.provide('main');\n" +
        "goog.require('module1$module$exports');\n" +
        "goog.require('module2$module$exports');\n" +
        "}\n" +
        "// module1.js\n" +
        "goog.provide('module1');\n" +
        "var x$$module1 = 1;\n" +
        "exports = {\n" +
        "  y: 2,\n" +
        "  z: function() {\n" +
        "    return 3;\n" +
        "  }\n" +
        "};\n" +
        "// module2.js\n" +
        "goog.provide('module2');\n" +
        "", new String(newRoot.toPrintableJavaScript()));
  }

  @Test(expected = RuntimeException.class)
  public void testURISyntaxException() throws IOException, InterruptedException {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    String sourceName = "invalid:/";
    pass.process(null, rootNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFilenamePrefix() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, null);
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module1", guessCJSModuleName("module1.js"));
    assertEquals("module2$module3", guessCJSModuleName("module2/module3.js"));
    assertEquals("module2$", guessCJSModuleName("module2/"));
    assertEquals("module2$", guessCJSModuleName("./module2/"));
    assertEquals("module2$", guessCJSModuleName("../module2/"));
    assertEquals("module2$", guessCJSModuleName("./module2"));
    assertEquals("module2$", guessCJSModuleName("../module2"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module1", toModuleName("module1.js"));
    assertEquals("module2$module3", toModuleName("module2/module3.js"));
    assertEquals("module2$", toModuleName("./module2/"));
    assertEquals("module2$", toModuleName("../module2/"));
    assertEquals("module2$", toModuleName("./module2"));
    assertEquals("module2$", toModuleName("../module2"));
    assertEquals("module2$", toModuleName("module2"));
    assertEquals("module1$module2", toModuleName("module1/module2.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("module1", normalizeSourceName("module1.js"));
    assertEquals("module2", normalizeSourceName("module2"));
  }

  @Test
  public void testVisitRequireCall() {
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("module1"));
    Node expected = IR.name("module1$module$exports");
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal.traverse(compiler, rootNode, callback);
    Node actual = callback.getRequireCall(rootNode, requireCall);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitScript() {
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal.traverse(compiler, rootNode, callback);
    Node scriptNode = callback.getScriptNode(rootNode);
    assertEquals("function main() {\n" +
        "goog.provide('main');\n" +
        "goog.require('module1$module$exports');\n" +
        "goog.require('module2$module$exports');\n" +
        "}\n" +
        "// module1.js\n" +
        "goog.provide('module1');\n" +
        "var x$$module1 = 1;\n" +
        "exports = {\n" +
        "  y: 2,\n" +
        "  z: function() {\n" +
        "    return 3;\n" +
        "  }\n" +
        "};\n" +
        "// module2.js\n" +
        "goog.provide('module2');\n", new String(scriptNode.toPrintableJavaScript()));
  }

  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal.traverse(compiler, rootNode, callback);
    Node moduleExports = callback.getModuleExportsNode(rootNode);
    assertEquals("exports", moduleExports.getQualifiedName());
    assertEquals("module$exports", moduleExports.getChildAtIndex(0).getString());
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node scriptNode = rootNode.getChildAtIndex(0);
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal.traverse(compiler, scriptNode, callback);
    Node ifNode = callback.getIfNode(scriptNode);
    assertEquals(3, ifNode.getChildCount());
    Node assignNode = ifNode.getChildAtIndex(1);
    assertEquals("module1$module$exports", assignNode.getChildAtIndex(0)
        .getString());
    assertEquals("module1", assignNode.getChildAtIndex(1).getString());
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback = new ProcessCommonJSModules.SuffixVarsCallback("module");
    Node rootNode = compiler.parse("function main() {\n" +
        "var x = 1;\n" +
        "var y = 2;\n" +
        "var z = 3;\n" +
        "}", null).getChildAtIndex(0);
    NodeTraversal.traverse(compiler, rootNode, suffixVarsCallback);
    assertEquals("function main() {\n" +
        "var x$$module = 1;\n" +
        "var y$$module = 2;\n" +
        "var z$$module = 3;\n" +
        "}", new String(rootNode.toPrintableJavaScript()));
  }
}