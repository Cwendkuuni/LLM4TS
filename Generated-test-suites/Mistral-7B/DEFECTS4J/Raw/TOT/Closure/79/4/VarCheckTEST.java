package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class VarCheckTest {

  private Compiler compiler;
  private SourceFile input;
  private CompilerResults results;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new Compiler(options);
    input = new SourceFile("testFile.js", "var a;");
    results = new CompilerResults();
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    compiler.compile(input, results, new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    Node undeclaredVar = NodeFactory.createName("b");

    try {
      traversal.traverse(undeclaredVar, null, null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat(containsString("variable b is undefined"));
    }
  }

  @org.junit.Test
  public void testVariableAssignedInCurrentFile() {
    input = new SourceFile("testFile.js", "var a = 1;");
    compiler.compile(input, results, new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    Node assignedVar = NodeFactory.createName("a");

    traversal.traverse(assignedVar, null, null);
    assertThat(assignedVar).isNotNull();
  }

  @org.junit.Test
  public void testModuleDependency() {
    String moduleA = "module A {\n" +
                     "  export function foo();\n" +
                     "}";
    String moduleB = "module B {\n" +
                     "  import foo from './A.js';\n" +
                     "  function bar() {\n" +
                     "    foo();\n" +
                     "  }\n" +
                     "}";

    input = new SourceFile("testFile.js", moduleB);
    compiler.compileMultipleInputs(new String[]{"A.js", "testFile.js"}, results,
        new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);

    Node fooFunction = NodeFactory.createName("foo");
    Node barFunction = NodeFactory.createName("bar");

    try {
      traversal.traverse(barFunction, null, null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat(
          containsString("module A cannot reference foo, defined in module A"));
    }
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    String moduleA = "module A {\n" +
                     "  export function foo();\n" +
                     "}";
    String moduleB = "module B {\n" +
                     "  function bar() {\n" +
                     "    foo();\n" +
                     "  }\n" +
                     "}";

    input = new SourceFile("testFile.js", moduleB);
    compiler.compileMultipleInputs(new String[]{"A.js", "testFile.js"}, results,
        new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);

    Node fooFunction = NodeFactory.createName("foo");
    Node barFunction = NodeFactory.createName("bar");

    try {
      traversal.traverse(barFunction, null, null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat(
          containsString("missing module dependency; module B should depend on module A because it references foo"));
    }
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    String moduleA = "module A {\n" +
                     "  export function foo();\n" +
                     "}";
    String moduleB = "module B {\n" +
                     "  function bar() {\n" +
                     "    foo();\n" +
                     "  }\n" +
                     "}";

    input = new SourceFile("testFile.js", moduleB);
    CompilerOptions options = new CompilerOptions(new CompilerOptions.ErrorConfig(
        JSError.make("test", 0, 0, "JSC_STRICT_MODULE_DEPENDENCY",
            "module B cannot reference foo, defined in module A")));
    compiler = new Compiler(options);
    compiler.compileMultipleInputs(new String[]{"A.js", "testFile.js"}, results,
        new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);

    Node fooFunction = NodeFactory.createName("foo");
    Node barFunction = NodeFactory.createName("bar");

    traversal.traverse(barFunction, null, null);
    assertThat(results.getDiagnostics().size()).isZero();
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    String module = "module {\n" +
                     "  export var a;\n" +
                     "  export function foo() {\n" +
                     "    a = 1;\n" +
                     "  }\n" +
                     "}";

    input = new SourceFile("testFile.js", module);
    compiler.compile(input, results, new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);

    Node fooFunction = NodeFactory.createName("foo");

    try {
      traversal.traverse(fooFunction, null, null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat(containsString("accessing name a in externs has no effect"));
    }
  }

  @org.junit.Test
  public void testUndefinedExternalVariable() {
    String module = "module {\n" +
                     "  export var a;\n" +
                     "}";

    input = new SourceFile("testFile.js", "function foo() {\n" +
                                          "  a = 1;\n" +
                                          "}");
    compiler.compileMultipleInputs(new String[]{"testFile.js", "testFile2.js"}, results,
        new VarCheck(compiler));
    Checker checker = compiler.getChecker();
    Node ast = results.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, checker);

    Node fooFunction = NodeFactory.createName("foo");

    try {
      traversal.traverse(fooFunction, null, null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat(containsString("name a is not undefined in the externs."));
    }
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    input = new SourceFile("testFile.js", "function");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(input, results, new VarCheck(compiler));
    });
  }
}