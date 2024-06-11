import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
    
    private DeadAssignmentsElimination deadAssignmentsElimination;
    private AbstractCompiler compiler;
    private ControlFlowGraph<Node> cfg;
    private LiveVariablesAnalysis.LiveVariableLattice liveVariableLattice;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
        cfg = new ControlFlowGraph<Node>();
        liveVariableLattice = new LiveVariablesAnalysis.LiveVariableLattice();
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        deadAssignmentsElimination.process(externs, root);
    }
    
    @Test
    public void testEnterScope() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        deadAssignmentsElimination.enterScope(t);
    }
    
    @Test
    public void testExitScope() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        deadAssignmentsElimination.exitScope(t);
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node();
        Node parent = new Node();
        deadAssignmentsElimination.visit(t, n, parent);
    }
    
    @Test
    public void testTryRemoveDeadAssignments() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        deadAssignmentsElimination.tryRemoveDeadAssignments(t, cfg);
    }
    
    @Test
    public void testTryRemoveAssignment() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node();
        FlowState<LiveVariableLattice> state = new FlowState<LiveVariableLattice>(liveVariableLattice);
        deadAssignmentsElimination.tryRemoveAssignment(t, n, exprRoot, state);
    }
    
    @Test
    public void testIsVariableStillLiveWithinExpression() {
        String variable = "x";
        deadAssignmentsElimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);
    }
    
    @Test
    public void testIsVariableReadBeforeKill() {
        String variable = "x";
        deadAssignmentsElimination.isVariableReadBeforeKill(n, variable);
    }
}