import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {
    
    private MustBeReachingVariableDef mustBeReachingVariableDef;
    
    @Before
    public void setUp() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    }
    
    @Test
    public void testIsForward() {
        assertTrue(mustBeReachingVariableDef.isForward());
    }
    
    @Test
    public void testCreateEntryLattice() {
        assertNotNull(mustBeReachingVariableDef.createEntryLattice());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        assertNotNull(mustBeReachingVariableDef.createInitialEstimateLattice());
    }
    
    @Test
    public void testFlowThrough() {
        assertNotNull(mustBeReachingVariableDef.flowThrough(null, null));
    }
    
    @Test
    public void testGetDef() {
        assertNull(mustBeReachingVariableDef.getDef("name", null));
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("name", null));
    }
}