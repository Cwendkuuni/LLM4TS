import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_WithNoSideEffects_ShouldNotReportWarning() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(0, compiler.getWarnings().size());
  }

  @Test
  public void testProcess_WithSideEffectInExpression_ShouldReportWarning() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node expression = new Node(Token.EXPR_RESULT, new Node(Token.ADD));
    root.getFirstChild().addChildToBack(expression);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", 
        compiler.getWarnings().get(0).getDescription());
  }

  @Test
  public void testProcess_WithSideEffectInAssignment_ShouldReportWarning() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.STRING, "value"));
    root.getFirstChild().addChildToBack(assignment);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", 
        compiler.getWarnings().get(0).getDescription());
  }

  @Test
  public void testProcess_WithSideEffectInMethodCall_ShouldReportWarning() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node methodCall = new Node(Token.EXPR_RESULT, new Node(Token.CALL, new Node(Token.NAME, "method")));
    root.getFirstChild().addChildToBack(methodCall);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", 
        compiler.getWarnings().get(0).getDescription());
  }

  @Test
  public void testProcess_WithSideEffectInForLoop_ShouldNotReportWarning() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node forLoop = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
    root.getFirstChild().addChildToBack(forLoop);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(0, compiler.getWarnings().size());
  }
  
  @Test
  public void testProtectSideEffects_WithProblemNodes_ShouldReplaceNodesInAST() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node problemNode1 = new Node(Token.ADD, new Node(Token.STRING, "hello"), new Node(Token.STRING, "world"));
    Node problemNode2 = new Node(Token.VOID);
    Node problemNode3 = new Node(Token.EXPR_RESULT, new Node(Token.TYPEOF));
    root.getFirstChild().addChildToBack(problemNode1);
    root.getFirstChild().addChildToBack(problemNode2);
    root.getFirstChild().addChildToBack(problemNode3);
    
    // Act
    checkSideEffects.process(externs, root);
    checkSideEffects.protectSideEffects();
    
    // Assert
    Assert.assertTrue(problemNode1.getParent().isFunction());
    Assert.assertEquals(problemNode1, problemNode1.getParent().getLastChild());
    Assert.assertTrue(problemNode2.getParent().isFunction());
    Assert.assertEquals(problemNode2, problemNode2.getParent().getLastChild());
    Assert.assertTrue(problemNode3.getParent().isFunction());
    Assert.assertEquals(problemNode3, problemNode3.getParent().getLastChild());
  }

  @Test
  public void testProtectSideEffects_WithNoProblemNodes_ShouldNotChangeAST() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects =
        new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    
    // Act
    checkSideEffects.process(externs, root);
    checkSideEffects.protectSideEffects();
    
    // Assert
    Assert.assertNull(root.getFirstChild().getFirstChild());
  }
  
  @Test
  public void testStripProtection_WithProtectedNodes_ShouldRemoveProtection() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    Node protectedNode1 = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), new Node(Token.STRING, "hello"));
    Node protectedNode2 = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), new Node(Token.STRING, "world"));
    Node protectedNode3 = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), new Node(Token.STRING, "test"));
    protectedNode1.putBooleanProp(Node.FREE_CALL, true);
    protectedNode2.putBooleanProp(Node.FREE_CALL, true);
    protectedNode3.putBooleanProp(Node.FREE_CALL, true);
    root.getFirstChild().addChildToBack(protectedNode1);
    root.getFirstChild().addChildToBack(protectedNode2);
    root.getFirstChild().addChildToBack(protectedNode3);
    
    // Act
    stripProtection.process(externs, root);
    
    // Assert
    Assert.assertEquals(0, root.getFirstChild().getChildCount());
  }

  @Test
  public void testStripProtection_WithNoProtectedNodes_ShouldNotChangeAST() {
    // Arrange
    Compiler compiler = new Compiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    
    // Act
    stripProtection.process(externs, root);
    
    // Assert
    Assert.assertNull(root.getFirstChild().getFirstChild());
  }

}