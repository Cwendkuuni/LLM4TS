
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.MaybeReachingVariableUse;

public class MaybeReachingVariableUseTest {

    @Test
    public void testComputeMayUse() {
        MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();
        
        // Test case where name is a variable name
        MaybeReachingVariableUse mrvu = new MaybeReachingVariableUse();
        Node cfgNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses result = mrvu.flowThrough(cfgNode, expectedOutput);
        assertEquals(expectedOutput, result);
        
        // Test case where name is a while loop condition expression
        Node whileNode = new Node(Token.WHILE);
        whileNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result2 = mrvu.flowThrough(whileNode, expectedOutput);
        assertEquals(expectedOutput, result2);
        
        // Test case where name is an if condition expression
        Node ifNode = new Node(Token.IF);
        ifNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result3 = mrvu.flowThrough(ifNode, expectedOutput);
        assertEquals(expectedOutput, result3);
        
        // Test case where name is a for loop condition expression
        Node forNode = new Node(Token.FOR);
        forNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result4 = mrvu.flowThrough(forNode, expectedOutput);
        assertEquals(expectedOutput, result4);
        
        // Test case where name is a for-in variable
        Node forInNode = new Node(Token.FOR);
        Node lhs = new Node(Token.VAR);
        lhs.addChildToBack(new Node(Token.NAME, "x"));
        lhs.addChildToBack(new Node(Token.NAME, "in"));
        forInNode.addChildToBack(lhs);
        MaybeReachingVariableUse.ReachingUses result5 = mrvu.flowThrough(forInNode, expectedOutput);
        assertEquals(expectedOutput, result5);
        
        // Test case where name is an AND expression
        Node andNode = new Node(Token.AND);
        andNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result6 = mrvu.flowThrough(andNode, expectedOutput);
        assertEquals(expectedOutput, result6);
        
        // Test case where name is an OR expression
        Node orNode = new Node(Token.OR);
        orNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result7 = mrvu.flowThrough(orNode, expectedOutput);
        assertEquals(expectedOutput, result7);
        
        // Test case where name is a HOOK expression
        Node hookNode = new Node(Token.HOOK);
        hookNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result8 = mrvu.flowThrough(hookNode, expectedOutput);
        assertEquals(expectedOutput, result8);
        
        // Test case where name is a VAR expression
        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result9 = mrvu.flowThrough(varNode, expectedOutput);
        assertEquals(expectedOutput, result9);
        
        // Test case where name is an assignment expression
        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToBack(new Node(Token.NAME, "x"));
        MaybeReachingVariableUse.ReachingUses result10 = mrvu.flowThrough(assignNode, expectedOutput);
        assertEquals(expectedOutput, result10);

    }
    
    @Test
    public void testGetUses() {
        MaybeReachingVariableUse mrvu = new MaybeReachingVariableUse();
        String name = "x";
        Node defNode = new Node(Token.BLOCK);
        Collection<Node> result = mrvu.getUses(name, defNode);
        assertNotNull(result);
    }
    
}
