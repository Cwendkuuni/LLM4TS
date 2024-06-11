
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    @Test
    public void testProcess() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        Node externs = new Node();
        Node root = new Node();
        deadAssignmentsElimination.process(externs, root);
    }

    @Test
    public void testEnterScope() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        deadAssignmentsElimination.enterScope(t);
    }

    @Test
    public void testExitScope() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        deadAssignmentsElimination.exitScope(t);
    }

    @Test
    public void testVisit() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        Node n = new Node();
        Node parent = new Node();
        deadAssignmentsElimination.visit(t, n, parent);
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        deadAssignmentsElimination.tryRemoveDeadAssignments(t, cfg);
    }

    @Test
    public void testTryRemoveAssignment() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        Node n = new Node();
        FlowState<LiveVariableLattice> state = new FlowState<>(null, null);
        deadAssignmentsElimination.tryRemoveAssignment(t, n, state);
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        Node n = new Node();
        Node exprRoot = new Node();
        String variable = "variable";
        deadAssignmentsElimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);
        Node n = new Node();
        String variable = "variable";
        deadAssignmentsElimination.isVariableReadBeforeKill(n, variable);
    }
}
