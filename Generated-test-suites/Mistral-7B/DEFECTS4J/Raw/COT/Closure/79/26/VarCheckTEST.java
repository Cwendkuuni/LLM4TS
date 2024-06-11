package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test_util.AbstractCompilerTestCase;
import com.google.javascript.rhino.Token;

public class VarCheckTest extends AbstractCompilerTestCase {

  private Compiler compiler;
  private VarCheck varCheck;

  @Before
  public void setUp() {
    compiler = newCompiler(false, new CompilerOptions());
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testUndefinedVariable() {
    String input =
        "function foo() {" +
        "  var x; " +
        "  x = 5; " +
        "}" +
        "function bar() {" +
        "  x = 10; " +
        "}" +
        "bar();";

    Node ast = parseAndCompile(compiler, input);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(varCheck, ast, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertTrue(diagnostics.containsAll(Sets.newHashSet(
        "variable x is undefined")));
  }

  @Test
  public void testUndefinedVariableInFunction() {
    String input =
        "function foo() {" +
        "  function bar() {" +
        "    var x; " +
        "  } " +
        "  bar(); " +
        "}" +
        "foo();";

    Node ast = parseAndCompile(compiler, input);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(varCheck, ast, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertTrue(diagnostics.containsAll(Sets.newHashSet(
        "variable x is undefined in function bar")));
  }

  @Test
  public void testUndefinedVariableInGlobalScope() {
    String input =
        "var x; " +
        "function foo() {" +
        "  x = 5; " +
        "}" +
        "foo();";

    Node ast = parseAndCompile(compiler, input);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(varCheck, ast, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertNull(diagnostics);
  }

  @Test
  public void testUndefinedVariableInExterns() {
    String input =
        "/** @suppress {undefinedProperties} */\n" +
        "function foo() {" +
        "  x = 5;" +
        "}" +
        "function bar() {}";

    Node ast = parseAndCompile(compiler, input).getChild(1);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    NodeTraversal.traverse(varCheck, ast, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertFalse(diagnostics.isEmpty());
    assertTrue(diagnostics.contains("name x is not undefined in the externs."));
  }

  @Test
  public void testModuleDependency() {
    String moduleAInput =
        "/** @suppress {unused} */\n" +
        "function foo() {}" +
        "\n" +
        "var x = 5;";

    String moduleBInput =
        "/** @suppress {unused} */\n" +
        "function bar() {" +
        "  import foo; " +
        "  x = 10; " +
        "}" +
        "\n" +
        "function baz() {}";

    Node moduleA = parseAndCompile(compiler, moduleAInput).getChild(0);
    Node moduleB = parseAndCompile(compiler, moduleBInput).getChild(0);
    Node root = Node.newObject(Token.BLOCK, Lists.newArrayList(
        moduleA.getFirstChild(), moduleB.getFirstChild()));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    NodeTraversal.traverse(varCheck, root, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertTrue(diagnostics.contains("module bar cannot reference x, defined in module foo"));
  }

  @Test
  public void testModuleDependencyStrict() {
    String moduleAPackage = "package foo";
    String moduleAInput =
        "/** @suppress {unused} */\n" +
        moduleAPackage + "\n" +
        "function foo() {}" +
        "\n" +
        "var x = 5;";

    String moduleBPackage = "package bar";
    String moduleBInput =
        "/** @suppress {unused} */\n" +
        moduleBPackage + "\n" +
        "function bar() {" +
        "  import foo; " +
        "  x = 10; " +
        "}" +
        "\n" +
        "function baz() {}";

    Node moduleA = parseAndCompile(compiler, moduleAInput).getChild(0);
    Node moduleB = parseAndCompile(compiler, moduleBInput).getChild(0);
    Node root = Node.newObject(Token.BLOCK, Lists.newArrayList(
        moduleA.getFirstChild(), moduleB.getFirstChild()));

    CompilerOptions options = compiler.getOptions();
    options.setError(JSError.make(
        "", 0, 0, VarCheck.VIOLATED_MODULE_DEP_ERROR), CheckLevel.ERROR);
    varCheck = new VarCheck(compiler, true);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    NodeTraversal.traverse(varCheck, root, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testModuleDependencyMissing() {
    String moduleAPackage = "package foo";
    String moduleAInput =
        "/** @suppress {unused} */\n" +
        moduleAPackage + "\n" +
        "function foo() {}" +
        "\n" +
        "var x = 5;";

    String moduleBPackage = "package bar";
    String moduleBInput =
        "/** @suppress {unused} */\n" +
        moduleBPackage + "\n" +
        "function bar() {" +
        "  import foo; " +
        "  x = 10; " +
        "}" +
        "\n" +
        "function baz() {}";

    Node moduleA = parseAndCompile(compiler, moduleAInput).getChild(0);
    Node moduleB = parseAndCompile(compiler, moduleBInput).getChild(0);
    Node root = Node.newObject(Token.BLOCK, Lists.newArrayList(
        moduleA.getFirstChild(), moduleB.getFirstChild()));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    NodeTraversal.traverse(varCheck, root, null);

    Set<String> diagnostics = getDiagnostics(compiler);
    assertTrue(diagnostics.contains("missing module dependency; module bar should depend on module foo because it references x"));
  }
}