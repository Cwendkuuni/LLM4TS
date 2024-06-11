import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_WithSideEffectStatement_ShouldReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node externs = new Node();
    Node root = new Node();
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings().get(0).getDescription());
  }
  
  @Test
  public void testHotSwapScript_WithSideEffectStatement_ShouldReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node scriptRoot = new Node();
    Node originalRoot = new Node();
    
    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings().get(0).getDescription());
  }
  
  @Test
  public void testVisit_WithSideEffectStatement_ShouldReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    Assert.assertEquals(1, compiler.getWarnings().size());
    Assert.assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings().get(0).getDescription());
  }
  
  @Test
  public void testProcess_WithNonSideEffectStatement_ShouldNotReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node externs = new Node();
    Node root = new Node();
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    Assert.assertEquals(0, compiler.getWarnings().size());
  }
  
  @Test
  public void testHotSwapScript_WithNonSideEffectStatement_ShouldNotReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node scriptRoot = new Node();
    Node originalRoot = new Node();
    
    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert
    Assert.assertEquals(0, compiler.getWarnings().size());
  }
  
  @Test
  public void testVisit_WithNonSideEffectStatement_ShouldNotReportWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    Assert.assertEquals(0, compiler.getWarnings().size());
  }
  
}