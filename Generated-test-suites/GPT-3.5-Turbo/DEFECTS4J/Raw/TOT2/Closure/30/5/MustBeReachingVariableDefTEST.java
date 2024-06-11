import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    @Test
    public void testComputeMustDef() {
        // Test with a simple assignment
        Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NUMBER, 10));
        MustBeReachingVariableDef.MustDef initial = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef result = new MustBeReachingVariableDef.MustDef();
      
        MustBeReachingVariableDef test = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        result = test.flowThrough(n, initial);
        
        assertEquals(1, result.reachingDef.size());
    }

    @Test
    public void testGetDef() {
        // Test getting definition of a variable
        Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.STRING, "test"));
        MustBeReachingVariableDef test = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        MustBeReachingVariableDef.MustDef initial = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef result = new MustBeReachingVariableDef.MustDef();

        result = test.flowThrough(n, initial);
        Node defNode = test.getDef("b", n);

        assertEquals(n, defNode);
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        // Test a variable that depends on outer scope variables
        Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.NAME, "d"));
        MustBeReachingVariableDef test = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        MustBeReachingVariableDef.MustDef initial = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef result = new MustBeReachingVariableDef.MustDef();

        result = test.flowThrough(n, initial);
        boolean result = test.dependsOnOuterScopeVars("c", n);

        assertTrue(result);
    }
}