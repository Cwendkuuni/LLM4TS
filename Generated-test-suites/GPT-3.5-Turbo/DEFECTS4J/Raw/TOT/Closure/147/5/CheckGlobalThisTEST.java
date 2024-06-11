import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_notGlobalContext() {
    // Test a non-global context
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_constructorFunction() {
    // Test if the function is a constructor
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    node.putProp(Node.JSDOC_INFO_PROP, "constructor");
    Node parent = new Node(Token.SCRIPT);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_annotatedThis() {
    // Test if the function has @this annotation
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    node.putProp(Node.JSDOC_INFO_PROP, "@this");
    Node parent = new Node(Token.SCRIPT);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_unsafeLocation() {
    // Test if the function is in an unsafe location to put @this annotation
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NOT);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_assignment_nestedAssignment() {
    // Test if the node is on the left side of a nested assignment
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node grandParent = new Node(Token.ASSIGN);
    grandParent.addChildToFront(parent);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, grandParent);
    
    // Then
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_assignment_prototypeProperty() {
    // Test if the node is on the right side of an assignment to a prototype property
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.putProp(Node.JSDOC_INFO_PROP, "prototype");
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.THIS));
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_assignment_prototypeSubProperty() {
    // Test if the node is on the right side of an assignment to a prototype subproperty
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.putProp(Node.JSDOC_INFO_PROP, "prototype");
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.THIS));
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_rightSideAssignment_propertyAccess() {
    // Test if the node is on the right side of an assignment with property access
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.ASSIGN);
    grandParent.addChildToFront(parent);
    grandParent.addChildToBack(new Node(Token.EMPTY));
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_rightSideAssignment_notPropertyAccess() {
    // Test if the node is on the right side of an assignment without property access
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // When
    boolean result = checkGlobalThis.shouldTraverse(t, node, parent);
    
    // Then
    assertFalse(result);
  }
  
  @Test
  public void testVisit_globalThisError() {
    // Test if the global 'this' object is reported as error
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.putProp(Node.JSDOC_INFO_PROP, "prototype");
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.THIS));
    
    // When
    checkGlobalThis.visit(t, node, parent);
    
    // Then
    assertEquals(1, compiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", compiler.getErrors().get(0).getDescription());
  }
  
  @Test
  public void testVisit_notGlobalThisError() {
    // Test if the non-global 'this' object is not reported as error
    // Given
    AbstractCompiler compiler = new AbstractCompiler();;
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    parent.putProp(Node.JSDOC_INFO_PROP, "prototype");
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.THIS));
    
    // When
    checkGlobalThis.visit(t, node, parent);
    
    // Then
    assertEquals(0, compiler.getErrors().size());
  }

  @Test
  public void testVisit_nestedAssignment() {
    // Test if the assignLhsChild is set correctly when traversing the left side of a nested assignment
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.visit(new NodeTraversal(compiler, checkGlobalThis), new Node(Token.ASSIGN), null);
    
    // When
    checkGlobalThis.visit(new NodeTraversal(compiler, checkGlobalThis), new Node(Token.THIS), null);
    
    // Then
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_topLevelAssignment() {
    // Test if the assignLhsChild is set correctly when traversing the left side of a top level assignment
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.visit(new NodeTraversal(compiler, checkGlobalThis), new Node(Token.ASSIGN), null);
    
    // When
    checkGlobalThis.visit(new NodeTraversal(compiler, checkGlobalThis), new Node(Token.THIS), null);
    
    // Then
    assertNull(checkGlobalThis.assignLhsChild);
  }

}