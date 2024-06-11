import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_NoSideEffectsPresent_ShouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    
    // Act
    checkSideEffects.process(null, null);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_SideEffectsPresent_ShouldReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    
    // Create a side-effect free node
    Node sideEffectFreeNode = new Node();
    sideEffectFreeNode.setType(Token.ADD);
    Node prevLineNode = new Node(Token.STRING);
    prevLineNode.setTrailingComment(" continued on the next line but you forgot the +");
    sideEffectFreeNode.addChildToFront(prevLineNode);
    
    checkSideEffects.problemNodes.add(sideEffectFreeNode);
    
    // Act
    checkSideEffects.process(null, null);
    
    // Assert
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testHotSwapScript_NoSideEffectsPresent_ShouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    
    // Act
    checkSideEffects.hotSwapScript(null, null);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testHotSwapScript_SideEffectsPresent_ShouldReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    
    // Create a side-effect free node
    Node sideEffectFreeNode = new Node();
    sideEffectFreeNode.setType(Token.ADD);
    Node prevLineNode = new Node(Token.STRING);
    prevLineNode.setTrailingComment(" continued on the next line but you forgot the +");
    sideEffectFreeNode.addChildToFront(prevLineNode);
    
    checkSideEffects.problemNodes.add(sideEffectFreeNode);
    
    // Act
    checkSideEffects.hotSwapScript(null, null);
    
    // Assert
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }
  
  // Test visit method for different scenarios -- typical use case, edge case, and error scenario

  @Test
  public void testVisit_SideEffectsPresent_ShouldReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.problemNodes.add(n);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_NoSideEffectsPresent_ShouldNotReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_ParentIsNull_ShouldNotReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = null;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_NodeIsEmpty_ShouldNotReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.problemNodes.add(n);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_ParentIsNotExprResultOrBlockAndNotForLoop_ShouldNotReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    parent.setType(Token.FUNCTION);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_ParentIsForLoopWithFourChildren_ShouldNotReport() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    parent.setType(Token.FOR);
    parent.addChildToFront(new Node());
    parent.addChildToFront(new Node());
    parent.addChildToFront(new Node());
    parent.addChildToFront(n);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProtectSideEffects_ProblemNodesIsEmpty_ShouldNotChangeAST() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, true);
    checkSideEffects.problemNodes.clear();
    
    // Act
    checkSideEffects.protectSideEffects();
    
    // Assert
    // No code change should be reported
    assertEquals(0, compiler.getChangeStamp());
  }
  
  @Test
  public void testProtectSideEffects_ProblemNodesNotEmpty_ShouldChangeAST() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, true);
    Node problemNode = new Node();
    checkSideEffects.problemNodes.add(problemNode);
    
    // Act
    checkSideEffects.protectSideEffects();
    
    // Assert
    // Code change should be reported
    assertNotEquals(0, compiler.getChangeStamp());
    // Verify that the problem node has been replaced
    assertNotEquals(problemNode, problemNode.getParent());
  }
  
  @Test
  public void testAddExtern_ExternAlreadyAdded_ShouldNotAddAgain() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.getSynthesizedExternsInput();
    int originalInputLength = input.getAstRoot(compiler).getChildCount();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, true);
    
    // Act
    checkSideEffects.addExtern();
    
    // Assert
    // Verify that the extern has not been added again
    assertEquals(originalInputLength, input.getAstRoot(compiler).getChildCount());
  }
  
  @Test
  public void testAddExtern_ExternNotAdded_ShouldAddExtern() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.getSynthesizedExternsInput();
    int originalInputLength = input.getAstRoot(compiler).getChildCount();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, true);
    
    // Remove existing externs if any
    input.getAstRoot(compiler).detachFromParent();
    
    // Act
    checkSideEffects.addExtern();
    
    // Assert
    // Verify that the extern has been added
    assertEquals(originalInputLength + 1, input.getAstRoot(compiler).getChildCount());
  }
  
  @Test
  public void testStripProtection_TargetIsProtectorFunction_ShouldStripProtection() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node();
    Node root = new Node();
    Node protectorFunction = new Node();
    protectorFunction.setType(Token.NAME);
    protectorFunction.setString(CheckSideEffects.PROTECTOR_FN);
    Node callNode = new Node();
    callNode.setType(Token.CALL);
    callNode.addChildToFront(protectorFunction.cloneTree());
    Node exprNode = new Node();
    exprNode.setType(Token.EXPR_RESULT);
    exprNode.addChildToBack(callNode);
    root.addChildToFront(exprNode);
    
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    
    // Act
    stripProtection.process(externs, root);
    
    // Assert
    // Verify that the protector function and call node have been removed
    assertNull(root.getFirstChild());
  }
  
  @Test
  public void testStripProtection_TargetIsNotProtectorFunction_ShouldNotStripProtection() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node();
    Node root = new Node();
    Node protectorFunction = new Node();
    protectorFunction.setType(Token.NAME);
    protectorFunction.setString("dummyFunction");
    Node callNode = new Node();
    callNode.setType(Token.CALL);
    callNode.addChildToFront(protectorFunction.cloneTree());
    Node exprNode = new Node();
    exprNode.setType(Token.EXPR_RESULT);
    exprNode.addChildToBack(callNode);
    root.addChildToFront(exprNode);
    
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    
    // Act
    stripProtection.process(externs, root);
    
    // Assert
    // Verify that the protector function and call node have not been removed
    assertNotNull(root.getFirstChild());
  }
}