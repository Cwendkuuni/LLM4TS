import org.junit.Test;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef mustBeReachingVariableDef;

    @Test
    public void testGetDef_Null() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
        assertNull(mustBeReachingVariableDef.getDef("name", null));
    }

    @Test
    public void testGetDef_VariableDefined() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
        Node node = new Node(Token.NAME, "name");
        mustBeReachingVariableDef.addToDefIfLocal("name", node, null, mustBeReachingVariableDef.createInitialEstimateLattice());
        assertEquals(node, mustBeReachingVariableDef.getDef("name", null));
    }

    @Test
    public void testDependsOnOuterScopeVars_True() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
        Node node = new Node(Token.NAME, "name");
        mustBeReachingVariableDef.addToDefIfLocal("name", node, null, mustBeReachingVariableDef.createInitialEstimateLattice());
        assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("name", null));
    }

    @Test
    public void testDependsOnOuterScopeVars_False() {
        mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
        Node node = new Node(Token.NAME, "name");
        mustBeReachingVariableDef.addToDefIfLocal("name", node, null, mustBeReachingVariableDef.createInitialEstimateLattice());
        assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("otherName", null));
    }

}