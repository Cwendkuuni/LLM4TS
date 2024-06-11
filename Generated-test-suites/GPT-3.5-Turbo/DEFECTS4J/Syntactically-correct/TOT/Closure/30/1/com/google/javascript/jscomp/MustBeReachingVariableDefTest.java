import org.junit.Test;

import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

    @Test
    public void testGetDef_localVariable_noReachingDef_returnNull() {
        MustBeReachingVariableDef def = new MustBeReachingVariableDef();
        Node result = def.getDef("varName", useNode);
        assertNull(result);
    }

    @Test
    public void testGetDef_localVariable_withReachingDef_returnNode() {
        MustBeReachingVariableDef def = new MustBeReachingVariableDef();
        Node result = def.getDef("varName", useNode);
        assertNotNull(result);
        assertEquals(expectedNode, result);
    }

    @Test
    public void testGetDef_outerScopeVariable_returnNode() {
        MustBeReachingVariableDef def = new MustBeReachingVariableDef();
        Node result = def.getDef("varName", useNode);
        assertNotNull(result);
        assertEquals(expectedNode, result);
    }

    @Test
    public void testDependsOnOuterScopeVars_variableDependsOnOuterScopeVars_returnTrue() {
        MustBeReachingVariableDef def = new MustBeReachingVariableDef();
        boolean result = def.dependsOnOuterScopeVars("varName", useNode);
        assertTrue(result);
    }

    @Test
    public void testDependsOnOuterScopeVars_variableHasNoDependencyOnOuterScopeVars_returnFalse() {
        MustBeReachingVariableDef def = new MustBeReachingVariableDef();
        boolean result = def.dependsOnOuterScopeVars("varName", useNode);
        assertFalse(result);
    }

}