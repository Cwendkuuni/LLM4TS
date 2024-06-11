import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    @Test
    public void testGetDef() {
        // Test when a variable is defined in a block and used within the block
        MustBeReachingVariableDef testObj = new MustBeReachingVariableDef();
        Node defNode = new Node(Token.VAR, new Node(Token.NAME, "x"), new Node(Token.NUMBER, "5"));
        testObj.flowThrough(defNode, MustDef);
        Node useNode = new Node(Token.NAME, "x");
        Node expected = defNode;
        assertEquals(expected, testObj.getDef("x", useNode));

        // Test when a variable is defined in a block and used outside the block
        defNode = new Node(Token.VAR, new Node(Token.NAME, "y"), new Node(Token.NUMBER, "10"));
        testObj.flowThrough(defNode, MustDef);
        useNode = new Node(Token.NAME, "y");
        expected = null;
        assertEquals(expected, testObj.getDef("y", useNode));
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        // Test when a variable depends on an outer scope variable
        MustBeReachingVariableDef testObj = new MustBeReachingVariableDef();
        Node defNode = new Node(Token.VAR, new Node(Token.NAME, "a"), new Node(Token.NAME, "b"));
        testObj.flowThrough(defNode, MustDef);
        Node useNode = new Node(Token.NAME, "a");
        boolean expected = true;
        assertEquals(expected, testObj.dependsOnOuterScopeVars("a", useNode));

        // Test when a variable does not depend on an outer scope variable
        defNode = new Node(Token.VAR, new Node(Token.NAME, "c"), new Node(Token.NUMBER, "5"));
        testObj.flowThrough(defNode, MustDef);
        useNode = new Node(Token.NAME, "c");
        expected = false;
        assertEquals(expected, testObj.dependsOnOuterScopeVars("c", useNode));
    }
}