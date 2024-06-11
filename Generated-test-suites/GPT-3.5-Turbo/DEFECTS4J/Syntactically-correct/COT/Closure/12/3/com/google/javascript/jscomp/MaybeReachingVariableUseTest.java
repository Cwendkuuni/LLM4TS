
import static org.junit.Assert.*;
import org.junit.Test;

public class MaybeReachingVariableUseTest {
    
    @Test
    public void testComputeMayUse() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
        
        Node n = new Node(Token.WHILE);
        n.addChildToFront(new Node(Token.TRUE)); // condition expression
        n.addChildToBack(new Node(Token.BLOCK)); // body
        maybeReachingVariableUse.computeMayUse(n, n, reachingUses, false);
        
        // test the functionality of computeMayUse method
        
        // assert statements
        assertTrue(reachingUses.mayUseMap.isEmpty());
    }
    
    @Test
    public void testAddToUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
        
        maybeReachingVariableUse.addToUseIfLocal("varName", new Node(Token.ASSIGN), reachingUses);
        
        // test the functionality of addToUseIfLocal method
        
        // assert statements
        assertTrue(reachingUses.mayUseMap.isEmpty());
        
        maybeReachingVariableUse.addToUseIfLocal("otherVarName", new Node(Token.ASSIGN), reachingUses);
        
        // assert statements
        assertFalse(reachingUses.mayUseMap.isEmpty());
    }
    
    @Test
    public void testRemoveFromUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
        
        reachingUses.mayUseMap.put(var, new Node(Token.BLOCK));
        maybeReachingVariableUse.removeFromUseIfLocal("varName", reachingUses);
        
        // test the functionality of removeFromUseIfLocal method
        
        // assert statements
        assertTrue(reachingUses.mayUseMap.isEmpty());
        
        reachingUses.mayUseMap.put(var, new Node(Token.BLOCK));
        maybeReachingVariableUse.removeFromUseIfLocal("otherVarName", reachingUses);
        
        // assert statements
        assertFalse(reachingUses.mayUseMap.isEmpty());
    }
    
    @Test
    public void testGetUses() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
        
        Node defNode = new Node(Token.BLOCK);
        defNode.addChildToFront(new Node(Token.VAR));
        defNode.getFirstChild().addChildToBack(new Node(Token.NAME, "varName"));
        
        Collection<Node> uses = maybeReachingVariableUse.getUses("varName", defNode);
        
        // test the functionality of getUses method
        
        // assert statements
        assertTrue(uses.isEmpty());
    }

}

