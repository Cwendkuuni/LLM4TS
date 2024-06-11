import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK);
    checkSideEffects.visit(t, n, parent);
    
    // Verify that the method does not throw any exceptions
  }
  
  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Verify that the method does not throw any exceptions
  }
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);
    
    // Verify that the method does not throw any exceptions
  }
  
  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    // Add problem nodes to the list
    Node problemNode1 = new Node(Token.EMPTY);
    checkSideEffects.problemNodes.add(problemNode1);
    Node problemNode2 = new Node(Token.EMPTY);
    checkSideEffects.problemNodes.add(problemNode2);
    
    checkSideEffects.protectSideEffects();
    
    // Verify that the problem nodes have been replaced with call nodes
    assertNotEquals(problemNode1, checkSideEffects.problemNodes.get(0));
    assertNotEquals(problemNode2, checkSideEffects.problemNodes.get(1));
    
    // Verify that the compiler.reportCodeChange method is called
    // (cannot be directly tested)
  }
  
  @Test
  public void testAddExtern() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.addExtern();
    
    // Verify that the extern function is added to the synthesized externs input
    assertNotNull(compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChild());
    
    // Verify that the compiler.reportCodeChange method is called
    // (cannot be directly tested)
  }
  
  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    stripProtection.process(externs, root);
    
    // Verify that the method does not throw any exceptions
  }
  
}