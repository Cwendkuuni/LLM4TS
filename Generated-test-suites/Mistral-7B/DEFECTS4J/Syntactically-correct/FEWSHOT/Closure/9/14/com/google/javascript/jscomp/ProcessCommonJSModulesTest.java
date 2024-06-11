package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  @SuppressWarnings("unused")
  private static final String MODULE_SLASH = "/";
  private static final String MODULE_NAME_SEPARATOR = "$";
  private static final String MODULE_NAME_PREFIX = "module$";

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String source = "// File: test.js\n" +
        "require('./foo');\n" +
        "\n" +
        "var foo = 'bar';\n" +
        "exports.qux = function() {};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilationLevel(CompilerOptions.CompilationLevel.SIMPLE);

    Node externs = IR.newNode();
    Node root = IR.parseCompilationUnit(source, "test.js", externs);

    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules processCommonJSModules =
        new ProcessCommonJSModules(compiler, ".", false);
    processCommonJSModules.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(compiler);
    nodeTraversal.traverse(root);

    Set<String> expectedModules = Sets.newHashSet("foo");
    assertEquals(expectedModules, compiler.getInputs().getRequireMap().getKeys());

    Node requiredModule = IR.getFirstDescendantOfType(root, Node.CALL_EXPR);
    assertSame("require", requiredModule.getFirstChild().getQualifiedName());
    assertSame("foo", toModuleName(requiredModule.getChildAtIndex(1).getString()));

    Node exports = IR.getFirstDescendantOfType(root, Node.VAR_DECL);
    assertEquals("exports", exports.getFirstChild().getQualifiedName());
    assertSame("module$exports", exports.getChildAtIndex(1).getQualifiedName());

    Node fooModule = IR.getFirstDescendantOfType(root, Node.VAR_DECL);
    assertSame("foo", fooModule.getFirstChild().getQualifiedName());
    assertSame("module$foo", fooModule.getChildAtIndex(1).getQualifiedName());
  }

  @Test
  public void testProcessCommonJSModulesWithDependencyReporting() throws Exception {
    String source = "// File: test.js\n" +
        "require('./foo');\n" +
        "\n" +
        "var foo = 'bar';\n" +
        "exports.qux = function() {};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilationLevel(CompilerOptions.CompilationLevel.SIMPLE);
    options.setDetermineDependentFiles(true);

    Node externs = IR.newNode();
    Node root = IR.parseCompilationUnit(source, "test.js", externs);

    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules processCommonJSModules =
        new ProcessCommonJSModules(compiler, ".", true);
    processCommonJSModules.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(compiler);
    nodeTraversal.traverse(root);

    String expectedDependency = "test.js";
    String expectedModuleName = "foo";
    assertEquals(expectedModuleName, compiler.getInputs().getRequireMap().get(expectedDependency));
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeModuleNames() throws Exception {
    String source = "// File: main.js\n" +
        "require('./sub/sub.js');\n" +
        "\n" +
        "// File: sub/sub.js\n" +
        "require('./foo');\n" +
        "\n" +
        "var foo = 'bar';\n" +
        "exports.qux = function() {};\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilationLevel(CompilerOptions.CompilationLevel.SIMPLE);

    Node externs = IR.newNode();
    Node root = IR.parseCompilationUnit(source, "main.js", externs);

    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules processCommonJSModules =
        new ProcessCommonJSModules(compiler, ".", false);
    processCommonJSModules.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(compiler);
    nodeTraversal.traverse(root);

    Set<String> expectedModules = Sets.newHashSet("foo");
    assertEquals(expectedModules, compiler.getInputs().getRequireMap().getKeys());

    String expectedDependency = "sub.js";
    String expectedModuleName = "foo";
    assertEquals(expectedModuleName, compiler.getInputs().getRequireMap().get(expectedDependency));
  }

  @Test
  public void testProcessCommonJSModulesWithGroupName() throws Exception {
    String source = "// File: test.js\n" +
        "exports.qux = require('./foo.js');\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilationLevel(CompilerOptions.CompilationLevel.SIMPLE);

    Node externs = IR.newNode();
    Node root = IR.parseCompilationUnit(source, "test.js", externs);

    Compiler compiler = new Compiler(options);
    ProcessCommonJSModules processCommonJSModules =
        new ProcessCommonJSModules(compiler, ".", false);
    processCommonJSModules.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(compiler);
    nodeTraversal.traverse(root);

    Set<String> expectedModules = Sets.newHashSet("foo.js");
    assertEquals(expectedModules, compiler.getInputs().getRequireMap().getKeys());

    String expectedDependency = "test.js";
    String expectedModuleName = "module$exports";
    String expectedGroup = "test";
    assertEquals(expectedModuleName, compiler.getInputs().getRequireMap().get(expectedDependency));
    assertEquals(expectedGroup, compiler.getInputs().getProvideMap().get(expectedModuleName).getGroup());
  }

  private String toModuleName(String name) {
    return ProcessCommonJSModules.toModuleName(name, "test.js");
  }
}