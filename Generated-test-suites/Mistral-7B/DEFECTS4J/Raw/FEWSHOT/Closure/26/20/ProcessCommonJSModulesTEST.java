package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Input file\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "require('module1');\n" +
        "\n" +
        "function foo() {\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);

    Node externs = Node.rootNode();
    Node root = NodeOperations.parseCompilableJavaScript(input, SourceName.get("test.js"));

    pass.process(externs, root);

    JSModule module = pass.getModule();
    assertNotNull(module);
    Set<String> expectedModules = Sets.newHashSet("module1");
    assertEquals(expectedModules, module.getDependencies());
  }

  @Test
  public void testProcessCommonJSModulesWithProvide() throws Exception {
    String input = "// Input file\n" +
        "goog.provide('module1');\n" +
        "var module = {\n" +
        "  exports: {\n" +
        "    foo: 'bar'\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "require('module2');\n" +
        "\n" +
        "function foo() {\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);

    Node externs = Node.rootNode();
    Node root = NodeOperations.parseCompilableJavaScript(input, SourceName.get("test.js"));

    pass.process(externs, root);

    JSModule module = pass.getModule();
    assertNotNull(module);
    Set<String> expectedModules = Sets.newHashSet("module2");
    assertEquals(expectedModules, module.getDependencies());
  }

  @Test
  public void testProcessCommonJSModulesConflictingGlobalVariable() throws Exception {
    String input = "// Input file\n" +
        "var foo = 'bar';\n" +
        "\n" +
        "require('module1');\n" +
        "\n" +
        "function foo() {\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);

    Node externs = Node.rootNode();
    Node root = NodeOperations.parseCompilableJavaScript(input, SourceName.get("test.js"));

    pass.process(externs, root);

    JSModule module = pass.getModule();
    assertNotNull(module);

    assertEquals("foo$$module1", root.getVariable("foo").getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesWithRequireCallInsideFunction() throws Exception {
    String input = "// Input file\n" +
        "function foo() {\n" +
        "  require('module1');\n" +
        "}\n" +
        "\n" +
        "foo();\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);

    Node externs = Node.rootNode();
    Node root = NodeOperations.parseCompilableJavaScript(input, SourceName.get("test.js"));

    pass.process(externs, root);

    JSModule module = pass.getModule();
    assertNotNull(module);
    Set<String> expectedModules = Sets.newHashSet("module1");
    assertEquals(expectedModules, module.getDependencies());
  }
}