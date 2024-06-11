import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraversePrototypeMethod() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function Foo() { this.x = 1; }");
    NodeTraversal traversal = new NodeTraversal(node, null);
    checker.shouldTraverse(traversal, node, null);
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function Foo() {}");
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("@this class Foo { function bar() {} }");
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldTraverse(traversal, null, node));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutJSDocInfo() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function foo() {}", Token.FUNCTION);
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function foo() {}", Token.FUNCTION);
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assign = new Node("var x = this;");
    Node parent = new Node(ASSIGN, new Node(IDENT, "x"), assign);
    Node node = new Node(ASSIGN, assign, new Node(IDENT, "this"));
    NodeTraversal traversal = new NodeTraversal(parent, null);
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assign = new Node(ASSIGN, new Node(IDENT, "Foo.prototype.x"), new Node(IDENT, "this"));
    Node parent = new Node(GETPROP, new Node(IDENT, "Foo.prototype"), new Node(IDENT, "x"));
    Node node = new Node(ASSIGN, assign, new Node(IDENT, "this"));
    NodeTraversal traversal = new NodeTraversal(parent, null);
    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assign = new Node(ASSIGN, new Node(GETPROP, new Node(IDENT, "Foo.prototype"), new Node(IDENT, "x")), new Node(IDENT, "this"));
    Node parent = new Node(ASSIGN, new Node(IDENT, "Foo.prototype"), assign);
    Node node = new Node(ASSIGN, assign, new Node(IDENT, "this"));
    NodeTraversal traversal = new NodeTraversal(parent, null);
    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test(expected = AssertionError.class)
  public void testCheckerThrowsExceptionOnUnreachableCode() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node(ASSIGN, new Node(IDENT, "x"), new Node(IDENT, "this"));
    NodeTraversal traversal = new NodeTraversal(node, null);
    checker.visit(traversal, node, null);
  }

  @Test(expected = AssertionError.class)
  public void testCheckerThrowsExceptionOnNullParent() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node(ASSIGN, new Node(IDENT, "x"), new Node(IDENT, "this"));
    NodeTraversal traversal = new NodeTraversal(null, node);
    checker.visit(traversal, node, null);
  }

  @Test
  public void testCheckerDoesNotReportThisOnPrototypePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function Foo() {}", new Node(GETPROP, new Node(IDENT, "Foo.prototype"), new Node(IDENT, "x")));
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldReportThis(node, null));
  }

  @Test
  public void testCheckerReportsThisOnAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assign = new Node(ASSIGN, new Node(IDENT, "x"), new Node(IDENT, "this"));
    Node parent = new Node(ASSIGN, new Node(IDENT, "x"), new Node(IDENT, "assignLhsChild"));
    Node node = assign;
    NodeTraversal traversal = new NodeTraversal(parent, null);
    checker.visit(traversal, node, parent);
    assertTrue(checker.compiler.getDiagnostics().contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckerReportsThisOnPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parent = new Node(ASSIGN, new Node(IDENT, "x"), new Node(IDENT, "assignLhsChild"));
    Node node = new Node(GETPROP, parent, new Node(IDENT, "prototype"));
    NodeTraversal traversal = new NodeTraversal(parent, null);
    checker.visit(traversal, node, parent);
    assertTrue(checker.compiler.getDiagnostics().contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckerDoesNotReportThisOnFunctionAnnotatedWithConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("@constructor function Foo() {}", Token.FUNCTION);
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldReportThis(node, null));
  }

  @Test
  public void testCheckerDoesNotReportThisOnFunctionAnnotatedWithThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("@this class Foo { function bar() {} }", Token.NAME);
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldReportThis(node, null));
  }

  @Test
  public void testCheckerDoesNotReportThisOnFunctionWithoutLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node node = new Node("function foo() {}", Token.FUNCTION);
    NodeTraversal traversal = new NodeTraversal(node, null);
    assertFalse(checker.shouldReportThis(node, null));
  }

  @Test
  public void testCheckerReportsThisOnFunctionWithoutLogicalPlaceForThisAnnotationButWithParentBlock() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parent = new Node(BLOCK, null);
    Node node = new Node("function foo() {}", parent);
    NodeTraversal traversal = new NodeTraversal(parent, null);
    assertTrue(checker.shouldReportThis(node, parent));
  }

  @Test
  public void testCheckerReportsThisOnFunctionWithoutLogicalPlaceForThisAnnotationButWithParentScript() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parent = new Node(SCRIPT, null);
    Node node = new Node("function foo() {}", parent);
    NodeTraversal traversal = new NodeTraversal(parent, null);
    assertTrue(checker.shouldReportThis(node, parent));
  }

  private static JSDocInfo getFunctionJsDocInfo(Node node) {
    return new CheckGlobalThis().getFunctionJsDocInfo(node);
  }
}