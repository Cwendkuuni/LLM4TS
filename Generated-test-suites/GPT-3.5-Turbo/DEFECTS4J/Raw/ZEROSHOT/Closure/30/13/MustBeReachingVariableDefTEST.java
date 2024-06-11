import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {
    
    private MustBeReachingVariableDef mustBeReachingVariableDef;
    
    @Before
    public void setUp() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    }
    
    @Test
    public void testGetDef() {
        Node def = mustBeReachingVariableDef.getDef("name", new Node());
        assertEquals(null, def);
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        boolean depends = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", new Node());
        assertEquals(false, depends);
    }
}