import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    @Test
    public void testGetDef() {
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
        Node def = mustBeReachingVariableDef.getDef("name", useNode);
        assertNull(def);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
        boolean depends = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", useNode);
        assertFalse(depends);
    }
}