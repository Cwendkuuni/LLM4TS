import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MaybeReachingVariableUseTest {

    @Test
    public void testComputeMayUse_normalCase() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Node
        Node node = new Node(Token.NAME, "x");
        node.addChildToFront(new Node(Token.STRING, "Hello"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

        // Call the flowThrough method
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(node, input);

        // Assert that the mayUseMap contains the expected Node for the variable "x"
        assertTrue(output.mayUseMap.containsKey(jsScope.getVar("x")));
        assertTrue(output.mayUseMap.get(jsScope.getVar("x")).contains(node));
    }

    @Test
    public void testComputeMayUse_emptyEscaped() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Node
        Node node = new Node(Token.NAME, "x");
        node.addChildToFront(new Node(Token.STRING, "Hello"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        // Add "x" to the escaped set
        reachingVariableUse.escaped.add(jsScope.getVar("x"));

        // Call the flowThrough method
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(node, input);

        // Assert that the mayUseMap does not contain the variable "x"
        assertFalse(output.mayUseMap.containsKey(jsScope.getVar("x")));
    }

    @Test
    public void testComputeMayUse_withConditional() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Node
        Node node = new Node(Token.NAME, "x");
        node.addChildToFront(new Node(Token.STRING, "Hello"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

        // Call the flowThrough method with conditional set to true
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(node, input, true);

        // Assert that the mayUseMap contains the expected Node for the variable "x"
        assertTrue(output.mayUseMap.containsKey(jsScope.getVar("x")));
        assertTrue(output.mayUseMap.get(jsScope.getVar("x")).contains(node));
    }

    @Test
    public void testGetUses_normalCase() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Node
        Node node = new Node(Token.NAME, "x");
        node.addChildToFront(new Node(Token.STRING, "Hello"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        // Add the variable "x" and the Node to the mayUseMap
        input.mayUseMap.put(jsScope.getVar("x"), node);

        // Call the getUses method with the variable "x" and the node
        Collection<Node> uses = reachingVariableUse.getUses("x", node);

        // Assert that the uses collection contains the expected Node
        assertTrue(uses.contains(node));
    }

    @Test
    public void testGetUses_noUses() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Node
        Node node = new Node(Token.NAME, "x");
        node.addChildToFront(new Node(Token.STRING, "Hello"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        // Do not add the variable "x" and the Node to the mayUseMap

        // Call the getUses method with the variable "x" and the node
        Collection<Node> uses = reachingVariableUse.getUses("x", node);

        // Assert that the uses collection is empty
        assertTrue(uses.isEmpty());
    }

    @Test
    public void testGetUses_multipleUses() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
        Scope jsScope = mock(Scope.class);
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        // Create the MaybeReachingVariableUse instance
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Create a sample Nodes
        Node node1 = new Node(Token.NAME, "x");
        node1.addChildToFront(new Node(Token.STRING, "Hello"));

        Node node2 = new Node(Token.NAME, "x");
        node2.addChildToFront(new Node(Token.NUMBER, "10"));

        // Create an empty ReachingUses object
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        // Add the variable "x" and the Nodes to the mayUseMap
        input.mayUseMap.put(jsScope.getVar("x"), node1);
        input.mayUseMap.put(jsScope.getVar("x"), node2);

        // Call the getUses method with the variable "x" and node1
        Collection<Node> uses = reachingVariableUse.getUses("x", node1);

        // Assert that the uses collection contains node1 and node2
        assertTrue(uses.contains(node1));
        assertTrue(uses.contains(node2));
    }
}