import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThis();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideScript_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideName_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotInsideValidContext_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.PLUS);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssignmentToPrototype_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    parent.addChildToBack(lhs);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideNestedAssignmentToPrototype_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.GETPROP));
    lhs.getFirstChild().addChildToBack(new Node(Token.NAME, "prototype"));
    parent.addChildToBack(lhs);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideLeftSideOfAssignment_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    assignNode.addChildToBack(lhs);
    assignNode.addChildToBack(n);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(assignNode);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideRightSideOfAssignmentWithGetProperty_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "obj"));
    assignNode.addChildToBack(lhs);
    assignNode.addChildToBack(n);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(assignNode);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_NodeIsThisAndInValidContext_ReportsError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);

    checkGlobalThis.visit(t, thisNode, parent);

    // Verify that an error is reported
    assertEquals(t.getReportedErrors().size(), 1);
  }

  @Test
  public void testVisit_NodeIsThisAndNotInValidContext_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(thisNode);

    checkGlobalThis.visit(t, thisNode, parent);

    // Verify that no errors are reported
    assertEquals(t.getReportedErrors().size(), 0);
  }

  @Test
  public void testVisit_NodeIsAssignLhsChild_SetsAssignLhsChildToNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    assignNode.addChildToBack(lhs);
    Node n = new Node(Token.THIS);

    checkGlobalThis.visit(t, n, assignNode);

    // Verify that assignLhsChild is set to null
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_NodeIsNotAssignLhsChild_DoesNotChangeAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignNode1 = new Node(Token.ASSIGN);
    Node lhs1 = new Node(Token.THIS);
    assignNode1.addChildToBack(lhs1);
    Node assignNode2 = new Node(Token.ASSIGN);
    Node rhs2 = new Node(Token.THIS);
    assignNode2.addChildToBack(new Node(Token.NAME, "someVariable"));
    assignNode2.addChildToBack(rhs2);
    checkGlobalThis.visit(new NodeTraversal(null, null), new Node(Token.THIS), assignNode1);
    checkGlobalThis.visit(new NodeTraversal(null, null), new Node(Token.THIS), assignNode2);

    // Verify that assignLhsChild is not changed by the second visit
    assertNotNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_NodeIsAssignLhsChild_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_NodeIsNotAssignLhsChild_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    checkGlobalThis.assignLhsChild = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);

    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_NodeIsThisAndShouldReportThis_ReportsError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.assignLhsChild = thisNode;

    checkGlobalThis.visit(t, thisNode, parent);

    // Verify that an error is reported
    assertEquals(t.getReportedErrors().size(), 1);
  }

  @Test
  public void testVisit_NodeIsThisAndShouldNotReportThis_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    checkGlobalThis.assignLhsChild = thisNode;

    checkGlobalThis.visit(t, thisNode, parent);

    // Verify that no errors are reported
    assertEquals(t.getReportedErrors().size(), 0);
  }
}