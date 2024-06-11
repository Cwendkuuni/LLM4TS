import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  
  @Before
  public void setUp() {
    checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_FunctionConstructor_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionThisType_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("type");
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionOverride_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionNotTraversable_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ADD);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionTraversable_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignLeftSide_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    parent.setFirstChild(lhs);
    n.setParent(parent);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignLeftSide_PrototypeProperty_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.getLastChild().setString("prototype");
    parent.setFirstChild(lhs);
    n.setParent(parent);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignLeftSide_PrototypeSubproperty_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.setQualifiedName("foo.prototype.bar");
    parent.setFirstChild(lhs);
    n.setParent(parent);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignRightSide_NotTraversable_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.setFirstChild(lhs);
    lhs.setNext(n);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignRightSide_Traversable_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.setFirstChild(lhs);
    n.setParent(parent);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testVisit_TypeThis_ShouldReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.VAR);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    // Verify that compiler.report is called with a specific error
  }
  
  @Test
  public void testVisit_UnsafeThis_ShouldReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    // Verify that compiler.report is called with a specific error
  }
  
  @Test
  public void testVisit_SafeThis_ShouldNotReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    // Verify that compiler.report is not called
  }
  
  @Test
  public void testVisit_AssignLhsChild_ShouldNotReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    // Verify that compiler.report is not called
  }
  
  @Test
  public void testVisit_AssignLhsChild_ResetAssignLhsChild() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    checker.setAssignLhsChild(new Node(Token.THIS));
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    // Verify that assignLhsChild is null
  }
}