
import org.junit.*;
import static org.junit.Assert.*;

public class ScopedAliasesTest {
  
  @Test
  public void testEnterScope() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(new CompilerInput());
    scopedAliases.enterScope(nodeTraversal);
  }
  
  @Test
  public void testExitScope() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(new CompilerInput());
    scopedAliases.exitScope(nodeTraversal);
  }
  
  @Test
  public void testShouldTraverse() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(new CompilerInput());
    boolean result = scopedAliases.shouldTraverse(nodeTraversal, new Node(), null);
    assertTrue(result);
  }
  
  @Test
  public void testProcess() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    scopedAliases.process(new Node(), new Node());
  }
  
  @Test
  public void testHotSwapScript() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    scopedAliases.hotSwapScript(new Node(), new Node());
  }
  
  @Test
  public void testVisit() {
    ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(new CompilerInput());
    scopedAliases.visit(nodeTraversal, new Node(), null);
  }
}
