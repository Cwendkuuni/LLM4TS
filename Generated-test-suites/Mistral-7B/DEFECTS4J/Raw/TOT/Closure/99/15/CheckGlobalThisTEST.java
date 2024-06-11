import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testUnsafeUseOfThisInAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "var a = function() { this.x = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertTrue(checker.hasErrorWithMessage(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testUnsafeUseOfThisInPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { return this.x; } Foo.prototype.x = 1;";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertTrue(checker.hasErrorWithMessage(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testConstructorFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "@this function Foo() { this.x = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotationButNoThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "@this function Foo() {}";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testAssignmentToPrototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } Foo.prototype.x = function() {};";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } Foo.prototype.x = 1;";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypePropertyWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } Foo.prototype.x = function() { this.y = 1; }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypePropertyWithThisAnnotationButNoThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "function Foo() { this.x = 1; } Foo.prototype.x = function() { @this }";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotationButNoFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "@this function Foo() {}";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotationButFunctionIsEmpty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.addPass(check);

    String code = "@this function Foo() {}";
    Node ast = new Parser().parse(code, null, null);
    checker.process(ast, null);
    assertFalse(checker.hasErrors());
  }
}