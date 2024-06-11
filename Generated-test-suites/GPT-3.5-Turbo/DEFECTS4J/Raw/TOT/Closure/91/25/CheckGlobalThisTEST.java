import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalThisOnLeftSideOfAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_GlobalThisOnRightSideOfAssignmentWithGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_GlobalThisOnRightSideOfAssignmentWithPrototypeGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testVisit_GlobalThisOnLeftSideOfAssignment() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert (Error logged)
  }

  @Test
  public void testVisit_GlobalThisOnRightSideOfAssignmentWithGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert (Error logged)
  }

  @Test
  public void testVisit_GlobalThisOnRightSideOfAssignmentWithPrototypeGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert (No error logged)
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    n.setJSDocInfo(expectedJsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expectedJsDoc, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithNamedVariableParentWithJsDoc() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    parent.setJSDocInfo(expectedJsDoc);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expectedJsDoc, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithNamedVariableParentWithoutJsDocWithVariableParentWithJsDoc() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    parent.setJSDocInfo(null);
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(expectedJsDoc);
    parent.setParent(grandparent);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expectedJsDoc, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithNamedVariableParentWithoutJsDocWithoutVariableParent() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(null);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(null, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithAssignParentWithJsDoc() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    parent.setJSDocInfo(expectedJsDoc);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expectedJsDoc, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithAssignParentWithoutJsDoc() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.setJSDocInfo(null);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(null, result);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocWithoutParent() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(null, result);
  }

}