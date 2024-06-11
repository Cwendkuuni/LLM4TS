import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_FunctionWithNoAnnotations_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ConstructorFunction_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo(true);
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo(true);
    jsDoc.recordThisType("MyType");
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo(true);
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInNonGlobalContext_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.BLOCK));
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInGlobalContext_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInPrototype_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "a"));
    lhs.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(lhs);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInLentPrototype_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node llhs = new Node(Token.GETPROP);
    llhs.addChildToFront(new Node(Token.NAME, "a"));
    llhs.addChildToFront(new Node(Token.NAME, "prototype"));
    lhs.addChildToFront(llhs);
    parent.addChildToFront(lhs);
    
    // Act
    boolean result = checker.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testVisit_GlobalThisEncountered_LevelIsWarning() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().contains(CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_NonGlobalThisEncountered_NoErrorReported() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "a");
    Node parent = new Node(Token.GETPROP);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    assertFalse(t.getErrors().contains(CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_AssignLhsChildIsNull_AssignLhsChildIsSet() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    parent.addChildToFront(lhs);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    assertNotNull(checker.assignLhsChild);
  }
  
  @Test
  public void testVisit_AssignLhsChildIsNotNull_AssignLhsChildIsNull() {
    // Arrange
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    parent.addChildToFront(lhs);
    checker.assignLhsChild = new Node(Token.THIS);
    
    // Act
    checker.visit(t, n, parent);
    
    // Assert
    assertNull(checker.assignLhsChild);
  }
  
}