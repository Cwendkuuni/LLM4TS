
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

    @Test
    public void testGetDef() {
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
        Node def = mustBeReachingVariableDef.getDef("name", "useNode");
        assertNull(def);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
        boolean dependsOnOuterScopeVars = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", "useNode");
        assertFalse(dependsOnOuterScopeVars);
    }
}
