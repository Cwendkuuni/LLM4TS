import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_RegularFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_ConstructorFunction_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfoBuilder().markAsConstructor().build();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfoBuilder().recordThisType().build();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.OBJECTLIT);
    Node parent = new Node(Token.STRING);
    parent.addChildToFront(new Node(Token.NAME));
    grandParent.addChildToFront(parent);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, grandParent));
  }

  @Test
  public void testShouldTraverse_NestedAssignmentToThisProperty_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhsChild = new Node(Token.GETPROP);
    lhsChild.addChildToFront(new Node(Token.NAME));
    lhsChild.addChildToBack(new Node(Token.STRING));
    parent.addChildToBack(lhsChild);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssignment_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhsChild = new Node(Token.STRING);
    lhsChild.addChildToFront(new Node(Token.NAME));
    parent.addChildToFront(lhsChild);
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.STRING));
    parent.addChildToBack(new Node(Token.NAME));
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testShouldReportThis_RegularThis_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  // Add more test cases for visit() method

  @Test
  public void testVisit_ThisOnRightSideOfAssignment_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.visit(t, n, parent));
  }

  @Test
  public void testVisit_ThisWithPropertyAccess_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertFalse(checkGlobalThis.visit(t, n, parent));
  }

  @Test
  public void testVisit_ThisOnLeftSideOfAssignment_ReportsError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.STRING));
    assertTrue(checkGlobalThis.visit(t, n, parent));
  }

  @Test
  public void testVisit_RegularThis_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    assertFalse(checkGlobalThis.visit(t, n, parent));
  }

  // Test a complete flow

  @Test
  public void testCompleteFlow_ThisOnLeftSideOfAssignment_ReportsError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.STRING));
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    assertTrue(checkGlobalThis.visit(t, n, parent));
  }

}