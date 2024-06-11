import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeCoverage.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleReturn() {
    String source = "function test() {" +
        "return;" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "return;", ast.toString());
  }

  @Test
  public void testComplexReturn() {
    String source = "function test() {" +
        "var x = 1;" +
        "if (true) {" +
        " return;" +
        "} else {" +
        " alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "if (true) {" +
        " return;", ast.toString());
  }

  @Test
  public void testDeadCodeAfterBreak() {
    String source = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " break;" +
        " alert('unreachable');" +
        "}" +
        "}" +
        "};";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " break;", ast.toString());
  }

  @Test
  public void testDeadCodeAfterContinue() {
    String source = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) {" +
        " continue;" +
        "}" +
        " alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) {" +
        " continue;", ast.toString());
  }

  @Test
  public void testDeadCodeAfterReturnInsideFunction() {
    String source = "function test() {" +
        "function innerFunction() {" +
        " return;" +
        " alert('unreachable');" +
        "}" +
        "innerFunction();" +
        "}" +
        "};";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "function innerFunction() {" +
        " return;", ast.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testDeadCodeAfterThrow() {
    String source = "function test() {" +
        "throw new Error('unexpected error');" +
        " alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    fail("Exception should have been thrown but was not.");
  }

  @Test(expected = AssertionError.class)
  public void testCodeChangeNotReported() {
    // This test should fail because codeChangeReported should be true.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile("function foo() {}", options).getAST();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);
    assertTrue(pass.codeChanged);
  }

  @Test
  public void testNoOpStatements() {
    String source = "function test() {" +
        "a.b.MyClass.prototype.propertyName;" +
        " true;" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, ast);

    assertEquals("function test() {}", ast.toString());
  }

  @Test
  public void testFunctionWithDeadCode() {
    String source = "function test() {" +
        "function innerFunction() {" +
        " return;" +
        " alert('unreachable');" +
        "}" +
        "innerFunction();" +
        "return 1;" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "function innerFunction() {" +
        " return;", ast.toString());
  }

  @Test
  public void testFunctionWithReturnInsideLoop() {
    String source = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) {" +
        " return i;" +
        "}" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) {" +
        " return i;", ast.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAfterReturn() {
    String source = "function test() {" +
        "function innerFunction() {" +
        " alert('unreachable');" +
        "}" +
        "return innerFunction();" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "function innerFunction() {}", ast.toString());
  }

  @Test
  public void testFunctionWithDeadCodeInsideIfElse() {
    String source = "function test() {" +
        "if (true) {" +
        " function innerFunction() {" +
        " alert('unreachable');" +
        "}" +
        "} else {" +
        " return 1;" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = new TestSourceFile(source, options).getAST();

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, ast);

    assertEquals("function test() {" +
        "if (true) {" +
        " function innerFunction() {}", ast.toString());
  }

}