import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    // Test for the getDef method
    @Test
    public void testGetDef() {
        MustBeReachingVariableDef testObj = new MustBeReachingVariableDef();
        String name = "variable";
        Node useNode = new Node(Token.EXPR_RESULT);

        // Test for a null definition
        Node result = testObj.getDef(name, useNode);
        assertNull(result);

        // Test for a valid definition
        name = "variable";
        useNode = new Node(Token.EXPR_RESULT);
        Node expected = new Node(Token.VAR);
        testObj.addToDefIfLocal(name, expected, null, new MustDef());
        result = testObj.getDef(name, useNode);
        assertEquals(expected, result);
    }

    // Test for the dependsOnOuterScopeVars method
    @Test
    public void testDependsOnOuterScopeVars() {
        MustBeReachingVariableDef testObj = new MustBeReachingVariableDef();
        String name = "variable";
        Node useNode = new Node(Token.EXPR_RESULT);

        // Test for no dependence on outer scope variables
        boolean result = testObj.dependsOnOuterScopeVars(name, useNode);
        assertFalse(result);

        // Test for dependence on outer scope variables
        name = "variable";
        useNode = new Node(Token.EXPR_RESULT);
        MustDef mustDef = new MustDef();
        Var outerScopeVar = new Var();
        outerScopeVar.scope = new Scope();
        outerScopeVar.scope != testObj.jsScope;
        mustDef.reachingDef.put(outerScopeVar, null);
        testObj.addToDefIfLocal(name, null, null, mustDef);
        result = testObj.dependsOnOuterScopeVars(name, useNode);
        assertTrue(result);
    }
}

