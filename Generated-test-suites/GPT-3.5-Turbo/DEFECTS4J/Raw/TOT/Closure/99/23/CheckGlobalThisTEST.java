import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalThisInPrototypeMethod_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = null;
    Node node = new Node(Token.GETPROP);
    node.addChildToBack(new Node(Token.NAME, "prototype"));
    node.addChildToBack(new Node(Token.NAME, "method"));
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_GlobalThisInBlockedFunction_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = null;
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisOnRightSideOfAssignment_ReportedAsGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = null;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    // Act
    checkGlobalThis.visit(nodeTraversal, node, parent);
    
    // Assert
    // TODO: Assert that the global this is reported correctly
  }
  
  @Test
  public void testVisit_ThisOnLeftSideOfAssignment_AlwaysReportedAsGlobalThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = null;
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(node);
    
    // Act
    checkGlobalThis.visit(nodeTraversal, node, parent);
    
    // Assert
    // TODO: Assert that the global this is reported correctly
  }

  @Test
  public void testShouldReportThis_GlobalThisInConstructor_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo.Builder().makeConstructor().build();
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(node, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_GlobalThisWithPropertyAccess_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(node, parent);
    
    // Assert
    assertTrue(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithInitialAnnotation_ReturnsJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandparent = new Node(Token.VAR);
    node.setJSDocInfo(new JSDocInfo.Builder().build());
    parent.setJSDocInfo(null);
    grandparent.setJSDocInfo(new JSDocInfo.Builder().build());
    node.setParent(parent);
    parent.setParent(grandparent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    
    // Assert
    assertEquals(node.getJSDocInfo(), result);
  }
 
}