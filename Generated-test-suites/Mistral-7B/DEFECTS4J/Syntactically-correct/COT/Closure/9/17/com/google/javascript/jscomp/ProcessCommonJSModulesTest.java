import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node root = mockNode("test.js");
    when(compiler.getRootName()).thenReturn(input.getName());

    processor.process(input, root);

    // Check that no errors were reported
    verifyNoErrors(compiler);

    // Check that expected code changes were made
    assertEquals(expectedCode("testOutput.js"), root.toSource());
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js"));
    assertEquals("module$testDir/module$subDir/module$subModule",
        ProcessCommonJSModules.toModuleName("testDir/subDir/subModule.js"));
    assertEquals("module$testDir/module$subDir",
        ProcessCommonJSModules.toModuleName("testDir/subDir/"));
  }

  @org.junit.Test
  public void testToModuleNameWithRelativeRequire() {
    assertEquals("module$subDir/module$subModule",
        ProcessCommonJSModules.toModuleName("./subDir/subModule.js",
            "test.js"));
    assertEquals("module$parentDir/module$subDir/module$subModule",
        ProcessCommonJSModules.toModuleName("../subDir/subModule.js",
            "test.js"));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    assertEquals("test.js", ProcessCommonJSModules.normalizeSourceName("test.js"));
    assertEquals("testDir/subDir/test.js",
        ProcessCommonJSModules.normalizeSourceName("testDir/subDir/test.js"));
    assertEquals("testDir/subDir/", ProcessCommonNameModules.normalizeSourceName("testDir/subDir/"));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = mockNode("test.js");
    Node script = mockNode("script.js");
    Node requireCall = mockNode("require('module').js");
    Node moduleExports = mockNode("module.exports = {};");

    when(compiler.getRootName()).thenReturn(input.getName());

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    NodeTraversal traverser = mock(NodeTraversal.class);
    when(traverser.getInput()).thenReturn(input);
    when(traverser.getCompiler()).thenReturn(compiler);
    when(traverser.getRoot()).thenReturn(root);
    when(traverser.getParent()).thenReturn(mock(Node.class));

    callback.visit(traverser, requireCall, root);
    verify(requireCall).replaceChildAtIndex(0, mockNode("module$"));

    callback.visit(traverser, script, root);
    verify(script).addChildToFront(
        IR.var(IR.name("test$"), IR.objectlit()).copyInformationFromForTree(script));
    verify(script).addChildToFront(
        IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test$")).copyInformationFromForTree(script));

    callback.visit(traverser, moduleExports, root);
    verify(moduleExports).setString("test$module$exports");
  }

  @org.junit.Test
  public void testSuffixVarsCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = mockNode("test.js");
    Node var1 = mockNode("var1");
    Node var2 = mockNode("var2");

    Scope scope = mock(Scope.class);
    when(scope.getVar("var1")).thenReturn(Scope.Var.create(var1, null, true, null, null));
    when(scope.getVar("var2")).thenReturn(Scope.Var.create(var2, null, true, null, null));

    when(compiler.getRootName()).thenReturn(input.getName());

    ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback =
        new ProcessCommonJSModules.SuffixVarsCallback("suffix");

    NodeTraversal traverser = mock(NodeTraversal.class);
    when(traverser.getScope()).thenReturn(scope);
    when(traverser.getCompiler()).thenReturn(compiler);
    when(traverser.getRoot()).thenReturn(root);
    when(traverser.getParent()).thenReturn(mock(Node.class));

    suffixVarsCallback.visit(traverser, var1, root);
    assertEquals("var1$$suffix", var1.getString());

    suffixVarsCallback.visit(traverser, var2, root);
    assertEquals("var2$$suffix", var2.getString());
  }

  private Node mockNode(String name) {
    Node node = IR.name(name).srcpos(1, 1, 1, 1).copyInformationFromForTree(null);
    when(node.isName()).thenReturn(true);
    return node;
  }

  private void verifyNoErrors(AbstractCompiler compiler) {
    verify(compiler, never()).reportCodeChangeError();
  }

  private String expectedCode(String fileName) {
    return "var test$ = {" +
        "  'goog': {" +
        "    'provide': function(moduleName) { /* empty body */ }," +
        "    'require': function(moduleName) { /* empty body */ }" +
        "  }," +
        "  'module': {" +
        "    'exports': {}" +
        "  }," +
        "};\n" +
        "\n" +
        "goog.provide(\"test$\");\n" +
        "\n" +
        "goog.require(\"script\");\n" +
        "\n" +
        "// testOutput.js:\n" +
        "var test$module$exports = {};\n" +
        "\n" +
        "goog.provide(\"test$module$exports\");\n" +
        "\n" +
        "goog.require(\"script\");";
  }
}