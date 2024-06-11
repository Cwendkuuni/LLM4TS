import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSSourceFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private ProcessCommonJSModules pass;

  @BeforeEach
  public void setUp() {
    compiler = new Compiler();
    pass = new ProcessCommonJSModules(compiler, ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX);
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String moduleName = pass.guessCJSModuleName(filename);
    assertNotNull(moduleName);
    assertEquals("module$test", moduleName);
  }

  @Test
  public void testToModuleName() {
    String filename = "test/file/name.js";
    String moduleName = ProcessCommonJSModules.toModuleName(filename);
    assertNotNull(moduleName);
    assertEquals("module$name", moduleName);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./file.js";
    String currentFilename = "test/file/name.js";
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertNotNull(moduleName);
    assertEquals("module$file", moduleName);

    requiredFilename = "../file.js";
    moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertNotNull(moduleName);
    assertEquals("module$test$file", moduleName);
  }

  @Test
  public void testProcess() {
    String filenamePrefix = "test/";
    pass = new ProcessCommonJSModules(compiler, filenamePrefix, false);
    Set<String> expectedProvides = ImmutableSet.of("module$test");
    Set<String> expectedRequires = ImmutableSet.of("module$test$required");
    compiler.initModules(
        ImmutableList.of(
            JSSourceFile.fromCode("test.js", "")),
        ImmutableList.of(
            new JSModule("module$test"),
            new JSModule("module$test$required")));
    compiler.getModule("module$test").setHasStarStarDependency();
    pass.process(compiler.getRoot(), compiler.getJsRoot());
    Set<String> provides = compiler.getModule("module$test").getProvides();
    Set<String> requires = compiler.getModule("module$test").getRequires();
    assertEquals(expectedProvides, provides);
    assertEquals(expectedRequires, requires);
  }

  @Test
  public void testVisitRequireCall() {
    String moduleName = "module$test$required";
    String requireStatement = "require('module$test')";
    String googRequireStatement = "goog.require('module$test')";
    Node require = parseExpression(requireStatement);
    Node parent = parseExpressionStatement(requireStatement);
    Node expectedModuleRef = parseExpression(moduleName);
    Node googRequire = parseExpression(googRequireStatement);
    Node expectedScript = parseScript(String.format(
        "var %s = {};%n" + "%s;%n" + "%s;%n", moduleName, googRequire, require));

    NodeTraversal.traverse(compiler, expectedScript, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    Node moduleRef = parent.getFirstChild();
    assertNotNull(moduleRef);
    assertEquals(expectedModuleRef.toString(), moduleRef.toString());
    assertEquals(expectedScript.toString(), expectedScript.toString());
  }

  @Test
  public void testVisitScript() {
    String moduleName = "module$test";
    String script = "function myFunc() {\n" +
      "  var a = 1;\n" +
      "  a++;\n" +
      "}";
    String provideStatement = "goog.provide('module$test')";
    String varStatement = String.format("var %s = {};", moduleName);
    String googProvideStatement = "goog.provide('module$test')";
    String moduleExportsOverride = String.format(
        "if (%s.module$exports) {%n" + "    %s = %s.module$exports;%n" + "}",
        moduleName, moduleName, moduleName);
    Node scriptNode = parseScript(script);
    Node expectedScript = parseScript(String.format(
        "%s;%n" + "%s;%n" + "%s;%n" + "%s", varStatement,
        provideStatement, googProvideStatement, moduleExportsOverride));

    NodeTraversal.traverse(compiler, expectedScript, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    NodeTraversal.traverse(compiler, scriptNode, new ProcessCommonJSModules.SuffixVarsCallback(moduleName));
    assertEquals(expectedScript.toString(), scriptNode.toString());
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    String moduleName = "module$test";
    String script = "function myFunc() {\n" +
      "  var a = 1;\n" +
      "  a++;\n" +
      "}";
    String moduleExportsOverride = String.format(
        "if (%s.module$exports) {%n" + "    %s = %s.module$exports;%n" + "}",
        moduleName, moduleName, moduleName);
    Node scriptNode = parseScript(script);
    Node expectedScript = parseScript(String.format(
        "%s;%n" + "%s", script, moduleExportsOverride));

    NodeTraversal.traverse(compiler, expectedScript, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    NodeTraversal.traverse(compiler, scriptNode, new ProcessCommonJSModules.SuffixVarsCallback(moduleName));
    assertEquals(expectedScript.toString(), scriptNode.toString());
  }

  @Test
  public void testVisitModuleExports() {
    String moduleName = "module$test";
    String moduleExportsStatement = "exports = module.exports = {}";
    Node moduleExportsNode = parseExpression(moduleExportsStatement);
    Node moduleNode = parseExpression("module");
    Node expectedModuleNode = parseExpression(moduleName);
    Node expectedModuleExportsNode = parseExpression("module$exports");
    expectedModuleNode.setOriginalName(moduleNode.getOriginalName());
    expectedModuleNode.copyInformationFrom(moduleNode);
    expectedModuleExportsNode.setOriginalName(moduleExportsNode.getOriginalName());
    expectedModuleExportsNode.copyInformationFrom(moduleExportsNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitModuleExports(moduleExportsNode);
    assertEquals(expectedModuleNode.toString(), moduleNode.toString());
    assertEquals(expectedModuleExportsNode.toString(), moduleExportsNode.toString());
  }

  private Node parseScript(String script) {
    return parseExpression(script).srcrefTreeForTesting(parseExpression(script));
  }

  private Node parseExpression(String expression) {
    return compiler.parseTestCode(expression);
  }

  private Node parseExpressionStatement(String expression) {
    return parseExpression(expression).detachFromParent();
  }
}