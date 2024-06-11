import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

    @Test
    public void testFlowThrough() {
        // creating a mock ControlFlowGraph, Scope, and AbstractCompiler
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();

        // creating instance of MaybeReachingVariableUse
        MaybeReachingVariableUse mrva = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // creating dummy nodes for testing
        Node blockNode = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(nameNode);
        blockNode.addChildToBack(varNode);

        // creating initial and expected ReachingUses
        MaybeReachingVariableUse.ReachingUses initial = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();
        expected.mayUseMap.put(jsScope.getVar("x"), varNode);

        // executing flowThrough method
        MaybeReachingVariableUse.ReachingUses output = mrva.flowThrough(blockNode, initial);

        // asserting the output matches the expected ReachingUses
        assertEquals(expected, output);
    }

    @Test
    public void testGetUses() {
        // creating a mock ControlFlowGraph, Scope, and AbstractCompiler
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();

        // creating instance of MaybeReachingVariableUse
        MaybeReachingVariableUse mrva = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // creating dummy nodes for testing
        Node blockNode = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(nameNode);
        blockNode.addChildToBack(varNode);

        // creating initial ReachingUses
        MaybeReachingVariableUse.ReachingUses initial = new MaybeReachingVariableUse.ReachingUses();

        // executing flowThrough method to set mayUseMap
        mrva.flowThrough(blockNode, initial);

        // getting uses of "x" in varNode
        Collection<Node> uses = mrva.getUses("x", varNode);

        // checking if the uses contain varNode
        assertTrue(uses.contains(varNode));
    }
}