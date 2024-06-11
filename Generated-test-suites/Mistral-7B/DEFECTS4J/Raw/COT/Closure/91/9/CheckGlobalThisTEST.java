import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private Checker checker;
  private Compiler compiler;
  private Parser parser;

  @Before
  public void setUp() {
    checker = new Checker();
    compiler = new Compiler();
    parser = new Parser(compiler);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    String code = "function foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithConstructor() {
    String code = "function Foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertFalse(traversal.isDone());
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithThis() {
    String code = "function Foo@this() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertFalse(traversal.isDone());
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithNoThis() {
    String code = "function Foo() { this.x = 1; }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertFalse(traversal.isDone());
  }

  @Test
  public void testShouldNotTraverseFunctionsWithPrototypePropertyAccess() {
    String code = "function Foo() {} Foo.prototype.x = function() {};";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    String code = "var a = this;";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    String code = "var a = { x: this };";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    String code = "var a = { x: this.y };";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    String code = "var a = { x: this.y }; var b = a.x;";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportExceptionOnInvalidContext() {
    String code = "function foo(){ throw new Error(); }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
  }

  @Test
  public void testShouldNotReportThisOnPrototypeMethod() {
    String code = "function Foo() {}; Foo.prototype.m = function() { this.x = 1; }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnAnonymousFunction() {
    String code = "var obj = { m: function() { this.x = 1; } };";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnArrowFunction() {
    String code = "var obj = { m: () => { this.x = 1; } };";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnMethodWithJSDocThisAnnotation() {
    String code = "/** @this {Object} */ function foo() { this.x = 1; }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnMethodWithJSDocConstructorAnnotation() {
    String code = "/** @constructor */ function Foo() { this.x = 1; }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldNotReportThisOnMethodWithJSDocInterfaceAnnotation() {
    String code = "/** @interface */ function Foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnMethodWithJSDocOverrideAnnotation() {
    String code = "/** @override */ function Foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertTrue(traversal.isDone());
  }

  @Test
  public void testShouldReportThisOnMethodWithJSDocIsConstructorAnnotation() {
    String code = "/** @constructor */ function Foo() { this.x = 1; }";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisOnMethodWithJSDocIsInterfaceAnnotation() {
    String code = "/** @interface */ function Foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisOnMethodWithJSDocIsOverrideAnnotation() {
    String code = "/** @override */ function Foo() {}";
    Node node = parser.parse(code, null, null).getTree().getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }
}