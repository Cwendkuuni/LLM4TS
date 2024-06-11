import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionIsConstructor_DoNotTraverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN));
    
    // Assert
    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionHasThisType_DoNotTraverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
    
    // Assert
    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionIsNotConstructorNorHasThisType_Traverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.VAR));
    
    // Assert
    Assert.assertTrue(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_ParentIsNull_Traverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), null);
    
    // Assert
    Assert.assertTrue(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_NonAssignment_DoNotTraverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), new Node(Token.GETPROP));
    
    // Assert
    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_Traverse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  
    // Act
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), new Node(Token.ASSIGN));
    
    // Assert
    Assert.assertTrue(shouldTraverse);
  }

  @Test
  public void testVisit_NodeIsThisAndShouldReportThis_ReportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
  
    // Act
    // Create a mock NodeTraversal object with a makeError method that throws an error
    NodeTraversal t = new NodeTraversal() {
      @Override
      public ErrorReporter makeError(Node n, CheckLevel level, DiagnosticType type) {
        throw new AssertionError("Expected error to be reported");
      }
    };
    checkGlobalThis.visit(t, new Node(Token.THIS), new Node(Token.FUNCTION));
  }

  @Test
  public void testVisit_NodeIsAssignmentLeftSide_ResetAssignLhsChild() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
  
    // Act
    checkGlobalThis.visit(null, new Node(Token.NAME), new Node(Token.ASSIGN));
    
    // Assert
    Assert.assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_NodeIsNotAssignmentLeftSide_DoNotResetAssignLhsChild() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node lhsChild = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = lhsChild;
  
    // Act
    checkGlobalThis.visit(null, new Node(Token.GETPROP), new Node(Token.ASSIGN));
    
    // Assert
    Assert.assertSame(lhsChild, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ReturnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
  
    // Act
    boolean shouldReport = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.FUNCTION));
    
    // Assert
    Assert.assertTrue(shouldReport);
  }

  @Test
  public void testShouldReportThis_AssignLhsChildIsNullAndParentIsNotAssignment_ReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = null;
  
    // Act
    boolean shouldReport = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));
    
    // Assert
    Assert.assertFalse(shouldReport);
  }

  @Test
  public void testShouldReportThis_AssignLhsChildIsNullAndParentIsAssignment_ReturnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = null;
  
    // Act
    boolean shouldReport = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));
    
    // Assert
    Assert.assertFalse(shouldReport);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInfoOfNodeIsNull_ReturnJsDocInfoOfParent() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo(false));
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
  
    // Act
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    
    // Assert
    Assert.assertEquals(parent.getJSDocInfo(), jsDocInfo);
  }

  @Test
  public void testGetFunctionJsDocInfo_JsDocInfoOfNodeIsNullAndParentIsVar_ReturnJsDocInfoOfGrandparent() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(new JSDocInfo(false));
    Node parent = new Node(Token.NAME);
    parent.setParent(grandparent);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
  
    // Act
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    
    // Assert
    Assert.assertEquals(grandparent.getJSDocInfo(), jsDocInfo);
  }

  @Test
  public void testGetFunctionJsDocInfo_JsDocInfoOfNodeIsNotNull_ReturnJsDocInfoOfNode() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    node.setJSDocInfo(new JSDocInfo(false));
  
    // Act
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    
    // Assert
    Assert.assertEquals(node.getJSDocInfo(), jsDocInfo);
  }

}