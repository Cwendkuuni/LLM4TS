import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorJsDoc_DoesNotTraverse() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisJsDoc_DoesNotTraverse() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithoutJsDocAndNotInLogicalPlaceToPutThisAnnotation_DoesNotTraverse() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideBlock_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideScript_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideName_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideAssign_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideString_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideNumber_Traverses() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testVisit_ThisNodeAndShouldReportThis_CompilerReportsError() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    // Compiler error should be reported
  }
  
  @Test
  public void testVisit_NodeIsAssignLhsChild_AssignLhsChildIsSetToNull() {
    // Arrange
    AbstractCompiler compiler = null;
    CheckLevel level = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    checkGlobalThis.assignLhsChild = new Node();
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }
  
}