import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; }";
    Node node = checker.processString(code, null, null);
    assertTrue(node.toString().equals(code));
    assertFalse(checker.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafePropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.prototype.x = 1; }";
    Node node = checker.processString(code, null, null);
    String expectedError = "dangerous use of the global 'this' object";
    assertTrue(checker.getDiagnostics().hasErrorWithMessage(DiagnosticType.WARNING, expectedError));
  }

  @Test
  public void testUnsafeAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this = 1; }";
    Node node = checker.processString(code, null, null);
    String expectedError = "dangerous use of the global 'this' object";
    assertTrue(checker.getDiagnostics().hasErrorWithMessage(DiagnosticType.WARNING, expectedError));
  }

  @Test
  public void testSafePrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.bar = function() {}; }";
    Node node = checker.processString(code, null, null);
    assertTrue(node.toString().equals(code));
    assertFalse(checker.getDiagnostics().hasError());
  }

  @Test
  public void testSafeConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } Foo.prototype.bar = function() {};";
    Node node = checker.processString(code, null, null);
    assertTrue(node.toString().equals(code));
    assertFalse(checker.getDiagnostics().hasError());
  }

  @Test
  public void testSafeFunctionWithNoAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    Checker checker = new Checker(compiler, options);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } function Bar() { this.x = 2; }";
    Node node = checker.processString(code, null, null);
    assertTrue(node.toString().equals(code));
    assertFalse(checker.getDiagnostics().hasError());
  }
}