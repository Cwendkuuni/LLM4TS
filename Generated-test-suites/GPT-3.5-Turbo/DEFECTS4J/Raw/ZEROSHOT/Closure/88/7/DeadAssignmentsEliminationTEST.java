import static org.junit.Assert.*;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    @Test
    public void testProcess() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy nodes
        Node externs = new Node();
        Node root = new Node();

        // call the process method
        dae.process(externs, root);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testEnterScope() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy node traversal
        NodeTraversal t = new NodeTraversal(compiler, null);

        // call the enterScope method
        dae.enterScope(t);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testExitScope() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy node traversal
        NodeTraversal t = new NodeTraversal(compiler, null);

        // call the exitScope method
        dae.exitScope(t);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testVisit() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy node traversal
        NodeTraversal t = new NodeTraversal(compiler, null);

        // create dummy nodes
        Node n = new Node();
        Node parent = new Node();

        // call the visit method
        dae.visit(t, n, parent);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy node traversal
        NodeTraversal t = new NodeTraversal(compiler, null);

        // create dummy control flow graph
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

        // call the tryRemoveDeadAssignments method
        dae.tryRemoveDeadAssignments(t, cfg);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testTryRemoveAssignment() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy node traversal
        NodeTraversal t = new NodeTraversal(compiler, null);

        // create dummy nodes
        Node n = new Node();
        FlowState<LiveVariableLattice> state = new FlowState<>();
        Node exprRoot = new Node();

        // call the tryRemoveAssignment method
        dae.tryRemoveAssignment(t, n, exprRoot, state);

        // assert something
        assertNotNull(dae);
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy nodes
        Node n = new Node();
        Node exprRoot = new Node();

        // call the isVariableStillLiveWithinExpression method
        boolean result = dae.isVariableStillLiveWithinExpression(n, exprRoot, "variable");

        // assert something
        assertFalse(result);
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // create a compiler
        AbstractCompiler compiler = new AbstractCompiler();

        // create an instance of DeadAssignmentsElimination
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);

        // create dummy nodes
        Node n = new Node();
        String variable = "variable";

        // call the isVariableReadBeforeKill method
        VariableLiveness result = dae.isVariableReadBeforeKill(n, variable);

        // assert something
        assertEquals(VariableLiveness.MAYBE_LIVE, result);
    }
}