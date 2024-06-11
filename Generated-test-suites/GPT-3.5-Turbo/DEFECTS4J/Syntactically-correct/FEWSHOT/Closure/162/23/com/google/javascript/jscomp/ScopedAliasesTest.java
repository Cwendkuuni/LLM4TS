import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopedAliasesTest {
    
    @Test
    public void testProcess() {
        // TODO: Implement this test
    }
    
    @Test
    public void testHotSwapScript() {
        // TODO: Implement this test
    }
    
    @Test
    public void testEnterScope() {
        // TODO: Implement this test
    }
    
    @Test
    public void testExitScope() {
        // TODO: Implement this test
    }
    
    @Test
    public void testShouldTraverse() {
        // TODO: Implement this test
    }
    
    @Test
    public void testVisit() {
        // TODO: Implement this test
    }
    
    @Test
    public void testGetAliasDefinitionsInOrder() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        assertNotNull(scopedAliases.getAliasDefinitionsInOrder());
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    }
    
    @Test
    public void testGetAliasUsages() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        assertNotNull(scopedAliases.getAliasUsages());
        assertEquals(0, scopedAliases.getAliasUsages().size());
    }
    
    @Test
    public void testGetScopeCalls() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        assertNotNull(scopedAliases.getScopeCalls());
        assertEquals(0, scopedAliases.getScopeCalls().size());
    }
    
    @Test
    public void testHasErrors() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        assertFalse(scopedAliases.hasErrors());
    }
    
    @Test
    public void testIsCallToScopeMethod() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        assertFalse(scopedAliases.isCallToScopeMethod(null));
    }
    
    @Test
    public void testReport() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        // TODO: Implement this test
    }
    
    @Test
    public void testValidateScopeCall() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null);
        scopedAliases.validateScopeCall(nodeTraversal, null, null);
        // TODO: Implement this test
    }
    
    @Test
    public void testFixTypeNode() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        // TODO: Implement this test
    }
}