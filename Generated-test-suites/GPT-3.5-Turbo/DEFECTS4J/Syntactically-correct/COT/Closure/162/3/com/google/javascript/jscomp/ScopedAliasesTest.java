import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testConstructor() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertNotNull(scopedAliases);
  }

  @Test
  public void testProcess() {
    ScopedAliases scopedAliases = new ScopedAliases();
    scopedAliases.process(externs, root);
    // Add assertions here
  }

  @Test
  public void testHotSwapScript() {
    ScopedAliases scopedAliases = new ScopedAliases();
    scopedAliases.hotSwapScript(root, null);
    // Add assertions here
  }

  @Test
  public void testEnterScope() {
    ScopedAliases scopedAliases = new ScopedAliases();
    scopedAliases.enterScope(t);
    // Add assertions here
  }

  @Test
  public void testExitScope() {
    ScopedAliases scopedAliases = new ScopedAliases();
    scopedAliases.exitScope(t);
    // Add assertions here
  }

  @Test
  public void testShouldTraverseTrue() {
    ScopedAliases scopedAliases = new ScopedAliases();
    boolean result = scopedAliases.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testVisit() {
    ScopedAliases scopedAliases = new ScopedAliases();
    scopedAliases.visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testGetAliasDefinitionsInOrder() {
    ScopedAliases scopedAliases = new ScopedAliases();
    Collection<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    // Add assertions here
  }

  @Test
  public void testGetAliasUsages() {
    ScopedAliases scopedAliases = new ScopedAliases();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    // Add assertions here
  }

  @Test
  public void testGetScopeCalls() {
    ScopedAliases scopedAliases = new ScopedAliases();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    // Add assertions here
  }

  @Test
  public void testHasErrors() {
    ScopedAliases scopedAliases = new ScopedAliases();
    boolean hasErrors = scopedAliases.hasErrors();
    // Add assertions here
  }

}