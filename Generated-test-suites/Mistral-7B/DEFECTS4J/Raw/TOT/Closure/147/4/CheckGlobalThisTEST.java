import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().createCompiler();
    checker = new Checker(compiler, new Parser());
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSafeUsesOfThis() {
    String code = "function Foo() { this.x = 42; }";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for safe use of 'this'",
        compiler.hasErrors());
  }

  @Test
  public void testPrototypeMethod() {
    String code = "function Foo() {}\nFoo.prototype.x = function() { this.y = 42; }";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for prototype methods",
        compiler.hasErrors());
  }

  @Test
  public void testConstructor() {
    String code = "function Foo() { this.x = 42; }";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for constructors",
        compiler.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "/** @constructor */ function Foo() { this.x = 42; }";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for constructor functions with @constructor",
        compiler.hasErrors());
  }

  @Test
  public void testFunctionWithThisTypeAnnotation() {
    String code = "/** @type {Foo} */ function Foo() { this.x = 42; }";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for functions with @type annotation",
        compiler.hasErrors());
  }

  @Test
  public void testSafeAssignment() {
    String code = "var a = function() { this.x = 42; }; a = {}; a.x = 1";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertFalse("No warnings expected for safe assignment",
        compiler.hasErrors());
  }

  @Test
  public void testUnsafeAssignment() {
    String code = "function evil() { this.x = 42; } var a = evil; a.x = 1";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertTrue("One warning expected for unsafe assignment",
        compiler.hasErrors());
  }

  @Test
  public void testUnsafePropertyAccess() {
    String code = "function evil() { this.x = 42; } var a = evil; console.log(a.x)";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertTrue("One warning expected for unsafe property access",
        compiler.hasErrors());
  }

  @Test
  public void testUnsafeFunctionCall() {
    String code = "function evil() { this.x = 42; } console.log(evil())";
    Node ast = Parser.parse(nodeFactory, code);
    checker.check(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    assertTrue("One warning expected for unsafe function call",
        compiler.hasErrors());
  }
}