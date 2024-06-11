import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker.CompilationTestBase;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest extends CompilationTestBase {

  @Test
  public void testCheckGlobalThis() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { this.useful = undefined; }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertTrue(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
    checkError(checker, ast.getFirstChild(), GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_NoReporting() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { var a = this; a.useful = undefined; }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
  }

  @Test
  public void testCheckGlobalThis_Prototype() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { Object.prototype.useful = undefined; }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
  }

  @Test
  public void testCheckGlobalThis_PrototypeChain() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { Function.prototype.prototype.useful = undefined; }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_Constructor() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function MyConstructor() { }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    checkerInstance.shouldReportThis(ast.getFirstChild(), ast);
  }

  @Test
  public void testCheckGlobalThis_PrototypeConstructor() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function MyConstructor() { Object.prototype.constructor = MyConstructor; }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
  }

  @Test
  public void testCheckGlobalThis_FunctionExpression() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { return function() { this.useful = undefined; } }();";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertTrue(checkerInstance.shouldReportThis(ast.getLastChild().getLastChild(), ast));
    checkError(checker, ast.getLastChild().getLastChild(), GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_MethodExpression() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { return {useful: function() { }}; }();";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertTrue(checkerInstance.shouldReportThis(ast.getLastChild().getLastChild(), ast.getLastChild()));
    checkError(checker, ast.getLastChild().getLastChild(), GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_LambdaExpression() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { return (function() { this.useful = undefined; })(); }";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertTrue(checkerInstance.shouldReportThis(ast.getLastChild(), ast));
    checkError(checker, ast.getLastChild(), GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_ArrowFunction() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function evil() { return () => { this.useful = undefined; } }();";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertTrue(checkerInstance.shouldReportThis(ast.getLastChild(), ast));
    checkError(checker, ast.getLastChild(), GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotation() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "/** @constructor */ function MyConstructor() {}";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getFirstChild(), ast));
  }

  @Test
  public void testCheckGlobalThis_MethodAnnotation() {
    Checker checker = createChecker();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(checker.getCompiler(), WARNING);
    CompilerOptions options = new CompilerOptions();
    checker.getCompiler().setOptionsForCompilation(options, null, null);
    checker.getCompiler().addPass(checkerInstance);

    String jsCode = "function MyClass() {} MyClass.prototype.myMethod = function() {};";
    Node ast = checkSourceToAst(jsCode);

    checker.checkSourceToRoots(ast);
    checkNoErrors(checker);

    assertFalse(checkerInstance.shouldReportThis(ast.getLastChild(), ast.getLastChild()));
  }

}