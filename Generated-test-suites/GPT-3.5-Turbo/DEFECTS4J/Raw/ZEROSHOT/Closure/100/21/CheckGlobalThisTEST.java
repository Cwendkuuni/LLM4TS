import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Before
  public void setUp() {
    // TODO: Add setup if needed
  }
  
  @After
  public void tearDown() {
    // TODO: Add teardown if needed
  }
  
  @Test
  public void testShouldTraverse_withFunctionConstructor_noShouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunctionAnnotation_noShouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withAssignmentOnLeftSide_shouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.THIS));
    parent.addChildToFront(lhs);
    parent.addChildToBack(new Node(Token.STRING));
    n.setParent(lhs);
    lhs.addChildToBack(n);
    assignLhsChild = lhs;
    
    // Act
    boolean result = shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withAssignmentOnRightSide_shouldNotTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.THIS));
    parent.addChildToFront(new Node(Token.NAME));
    parent.addChildToBack(lhs);
    n.setParent(parent);
    
    // Act
    boolean result = shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testVisit_withThis_shouldReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(n);
    
    // Act
    visit(t, n, parent);
    
    // Assert
    // TODO: Add assert
  }
  
  @Test
  public void testVisit_withAssignmentLhsChild_shouldSetAssignLhsChildToNull() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    assignLhsChild = parent;
    
    // Act
    visit(t, n, parent);
    
    // Assert
    assertNull(assignLhsChild);
  }
  
  @Test
  public void testShouldReportThis_withAssignLhsChild_shouldReturnTrue() {
    // Arrange
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assignLhsChild = new Node(Token.NAME);
    
    // Act
    boolean result = shouldReportThis(n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_withNonAssignLhsChild_shouldReturnFalse() {
    // Arrange
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    assignLhsChild = null;
    
    // Act
    boolean result = shouldReportThis(n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withFunctionWithJsDocInfo_shouldReturnJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    
    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withParentName_shouldReturnParentJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withParentAssign_shouldReturnParentJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withParentVar_shouldReturnParentJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node name = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    name.setParent(parent);
    n.setParent(name);
    
    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
}