import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalContext_ShouldReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node node = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_NotFunctionNode_ShouldReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node node = new Node(Token.CONST);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ShouldReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);
    functionNode.setJSDocInfo(constructorJsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo thisJsDoc = new JSDocInfo();
    thisJsDoc.recordThisType();
    functionNode.setJSDocInfo(thisJsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation_ShouldReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node functionNode = new Node(Token.FUNCTION);
    Node variableNode = new Node(Token.NAME);
    Node assignNode = new Node(Token.ASSIGN);
    variableNode.addChildToBack(functionNode);
    assignNode.addChildToBack(variableNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_ShouldReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.NAME);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.setString("this");
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, leftSideNode, assignmentNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignment_PrototypePropertyAccess_ShouldReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.addChildToFront(new Node(Token.NAME, new Node(Token.NAME, new Node(Token.THIS))));
    leftSideNode.addChildToBack(new Node(Token.STRING, new Node(Token.STRING, "prototype")));
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rightSideNode, assignmentNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignment_PrototypeSubpropertyAccess_ShouldReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.addChildToFront(new Node(Token.NAME, new Node(Token.NAME, new Node(Token.THIS))));
    leftSideNode.addChildToBack(new Node(Token.STRING, new Node(Token.STRING, "prototype.foo")));
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rightSideNode, assignmentNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignment_PropertyAccess_ShouldReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.addChildToFront(new Node(Token.NAME, new Node(Token.NAME, new Node(Token.THIS))));
    leftSideNode.addChildToBack(new Node(Token.STRING, new Node(Token.STRING, "property")));
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rightSideNode, assignmentNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_GlobalThis_ShouldReportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node node = new Node(Token.THIS);
    node.setType(Token.THIS);

    // Act
    checkGlobalThis.visit(nodeTraversal, node, null);
    DiagnosticType actualDiagnosticType = checkGlobalThis.getDiagnosticType();

    // Assert
    assertEquals(CheckGlobalThis.GLOBAL_THIS, actualDiagnosticType);
  }

  @Test
  public void testVisit_GlobalThis_InsideAssignmentToLeftSide_ShouldReportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.NAME);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.setString("this");
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    checkGlobalThis.shouldTraverse(nodeTraversal, leftSideNode, assignmentNode);
    checkGlobalThis.visit(nodeTraversal, leftSideNode, assignmentNode);
    DiagnosticType actualDiagnosticType = checkGlobalThis.getDiagnosticType();

    // Assert
    assertEquals(CheckGlobalThis.GLOBAL_THIS, actualDiagnosticType);
  }

  @Test
  public void testVisit_GlobalThis_OutsideAssignment_ShouldNotReportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(null);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.NAME);
    Node rightSideNode = new Node(Token.STRING);
    leftSideNode.setString("this");
    assignmentNode.addChildToFront(leftSideNode);
    assignmentNode.addChildToBack(rightSideNode);

    // Act
    checkGlobalThis.shouldTraverse(nodeTraversal, rightSideNode, assignmentNode);
    checkGlobalThis.visit(nodeTraversal, rightSideNode, assignmentNode);
    DiagnosticType actualDiagnosticType = checkGlobalThis.getDiagnosticType();

    // Assert
    assertEquals(null, actualDiagnosticType);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionNodeWithJSDoc_ShouldReturnJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    functionNode.setJSDocInfo(expectedJsDoc);

    // Act
    JSDocInfo actualJsDoc = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJsDoc, actualJsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionNodeWithParentJSDoc_ShouldReturnParentJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.NAME);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    parentNode.setJSDocInfo(expectedJsDoc);
    parentNode.addChildToBack(functionNode);

    // Act
    JSDocInfo actualJsDoc = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJsDoc, actualJsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionNodeWithAncestorJSDoc_ShouldReturnAncestorJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.NAME);
    Node grandparentNode = new Node(Token.VAR);
    JSDocInfo expectedJsDoc = new JSDocInfo();
    grandparentNode.setJSDocInfo(expectedJsDoc);
    parentNode.addChildToBack(functionNode);
    grandparentNode.addChildToBack(parentNode);

    // Act
    JSDocInfo actualJsDoc = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJsDoc, actualJsDoc);
  }
}