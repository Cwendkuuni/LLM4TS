
import static org.junit.Assert.*;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
    // Test if the DeadAssignmentsElimination class removes local variable assignments correctly
    @Test
    public void testRemoveDeadAssignments() {
        AbstractCompiler compiler = new AbstractCompiler();
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
        
        // Test case: x = 1; - should be removed as x is dead
        Node node1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), Node.newNumber(1));
        ControlFlowGraph<Node> cfg1 = new ControlFlowGraph<Node>(node1);
        LiveVariablesAnalysis.LiveVariableLattice out1 = new LiveVariablesAnalysis.LiveVariableLattice();
        out1.markAllRead();
        FlowState<LiveVariablesAnalysis.LiveVariableLattice> state1 = new FlowState<LiveVariablesAnalysis.LiveVariableLattice>(out1);
        DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch> graphNode1 = new DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch>(node1, state1);
        cfg1.connect(graphNode1, cfg1.getImplicitReturn().getEntryPoint());
        deadAssignmentsElimination.tryRemoveDeadAssignments(new NodeTraversal(compiler, node1), cfg1);
        assertNull(node1.getFirstChild());

        // Test case: x = a; - should not be removed as a is still live
        Node node2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "a"));
        ControlFlowGraph<Node> cfg2 = new ControlFlowGraph<Node>(node2);
        LiveVariablesAnalysis.LiveVariableLattice out2 = new LiveVariablesAnalysis.LiveVariableLattice();
        out2.markAllRead();
        // Mark a as live
        out2.markVariableLive(new jscomp.Scope.Var("a", false));
        FlowState<LiveVariablesAnalysis.LiveVariableLattice> state2 = new FlowState<LiveVariablesAnalysis.LiveVariableLattice>(out2);
        DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch> graphNode2 = new DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch>(node2, state2);
        cfg2.connect(graphNode2, cfg2.getImplicitReturn().getEntryPoint());
        deadAssignmentsElimination.tryRemoveDeadAssignments(new NodeTraversal(compiler, node2), cfg2);
        assertNotNull(node2.getFirstChild());

        // Test case: x = x + 1; - should be removed as x is dead
        Node node3 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.ADD, new Node(Token.NAME, "x"), Node.newNumber(1)));
        ControlFlowGraph<Node> cfg3 = new ControlFlowGraph<Node>(node3);
        LiveVariablesAnalysis.LiveVariableLattice out3 = new LiveVariablesAnalysis.LiveVariableLattice();
        out3.markAllRead();
        FlowState<LiveVariablesAnalysis.LiveVariableLattice> state3 = new FlowState<LiveVariablesAnalysis.LiveVariableLattice>(out3);
        DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch> graphNode3 = new DiGraph.DiGraphNode<Node, ControlFlowGraph.Branch>(node3, state3);
        cfg3.connect(graphNode3, cfg3.getImplicitReturn().getEntryPoint());
        deadAssignmentsElimination.tryRemoveDeadAssignments(new NodeTraversal(compiler, node3), cfg3);
        assertNull(node3.getFirstChild());
    }
}
