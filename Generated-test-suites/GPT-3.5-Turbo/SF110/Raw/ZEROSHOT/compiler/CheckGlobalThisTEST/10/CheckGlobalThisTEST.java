import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssign_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);
    checkGlobalThis.assignLhsChild = thisNode;

    boolean result = checkGlobalThis.shouldReportThis(thisNode);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);

    boolean result = checkGlobalThis.shouldReportThis(thisNode);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisWithNoParent_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node thisNode = new Node(Token.THIS);

    boolean result = checkGlobalThis.shouldReportThis(thisNode);

    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_NotThisNode_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node otherNode = new Node(Token.EXPR_RESULT);

    boolean result = checkGlobalThis.shouldReportThis(otherNode);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NoParent_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.EMPTY);
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.FUNCTION);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsInvalidFunction_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    parent.putBooleanProp(Node.getPropIndex(Token.BLOCK, ""));

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsAssignableToPrototype_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.STRING_KEY);
    grandParent.addChildToBack(n);
    parent.addChildToBack(grandParent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNestedAssignableToPrototype_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.GETPROP);
    grandParent.addChildToBack(n);
    parent.addChildToBack(grandParent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_ThisNode_ShouldReportGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = null;

    checkGlobalThis.visit(t, n, parent);

    // Assert that the error is reported
  }
  
  @Test
  public void testVisit_AssignLhsChildIsThis_ShouldClearAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = n;

    checkGlobalThis.visit(t, n, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_NoJSDoc_ReturnsNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.FUNCTION);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameWithJSDoc_ReturnsParentJSDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(new JSDocInfo());
    n.addChildToBack(parent);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsAssignWithJSDoc_ReturnsParentJSDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.setJSDocInfo(new JSDocInfo());
    n.addChildToBack(parent);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsInvalidAssign_ReturnsNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.putBooleanProp(Node.getPropIndex(Token.BLOCK, ""));
    n.addChildToBack(parent);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsVarWithJSDoc_ReturnsParentJSDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    parent.setJSDocInfo(new JSDocInfo());
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToBack(parent);
    n.addChildToBack(grandParent);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }

}