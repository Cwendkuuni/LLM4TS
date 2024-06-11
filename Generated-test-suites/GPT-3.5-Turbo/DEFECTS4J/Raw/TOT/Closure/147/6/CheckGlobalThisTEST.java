import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_WithFunctionAnnotatedWithConstructor() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_WithFunctionAnnotatedWithThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_WithFunctionWhereThisAnnotationIsNotApplicable() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_WithFunctionInsideBlock() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testVisit_WithThisOnLeftSideOfAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(1, compiler.getErrorCount());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors().get(0).getType());
  }
  
 
  @Test
  public void testVisit_WithThisWithPropertyAccess() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(1, compiler.getErrorCount());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors().get(0).getType());
  }
  
  @Test
  public void testVisit_WithoutThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_WithThisOnRightSideOfAssignmentToPrototypeProperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(lhs);
    parent.addChildToBack(new Node(Token.NAME, "property"));
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_WithThisOnRightSideOfAssignmentToNestedPrototypeProperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node llhs = new Node(Token.NAME, "prototype");
    llhs.addChildToBack(new Node(Token.NAME, "property"));
    lhs.addChildToBack(llhs);
    parent.addChildToFront(lhs);
    parent.addChildToBack(new Node(Token.NAME, "subproperty"));
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithJsDoc() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithoutJsDoc_AnnotatedNodeIsName() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithoutJsDoc_AnnotatedNodeIsAssignment() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithoutJsDoc_AnnotatedParentIsVar() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    parent.setParent(gramps);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_WithoutFunctionJsDoc_WithoutAnnotatedNode() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node gramps = new Node(Token.SCRIPT);
    parent.setParent(gramps);
    n.setParent(parent);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertNull(result);
  }
  
}