import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef variableDef;

    @Before
    public void setUp() {
        variableDef = new MustBeReachingVariableDef(null, null, null);
    }

    @Test
    public void testGetDef() {
        Node def = variableDef.getDef("name", new Node(Token.NAME, "useNode"));
        assertNull(def);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        boolean dependsOnOuterScopeVars = variableDef.dependsOnOuterScopeVars("name", new Node(Token.NAME, "useNode"));
        assertFalse(dependsOnOuterScopeVars);
    }
}