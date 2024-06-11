import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
    
    private DeadAssignmentsElimination eliminator;
    private AbstractCompiler compiler;
    private Scope scope;
    private ControlFlowGraph<Node> cfg;
    private LiveVariablesAnalysis liveness;
    
    @Before
    public void setUp() {
        eliminator = new DeadAssignmentsElimination(compiler);
        compiler = new AbstractCompiler();
        scope = new Scope();
        cfg = new ControlFlowGraph<>();
        liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    }

    @Test
    public void testEnterScope_GlobalScope() {
        eliminator.enterScope(new NodeTraversal(compiler, null));
        // No asserts needed since the only thing this method does is return when scope.isGlobal() == true
    }
    
    @Test
    public void testEnterScope_ContainsFunction() {
        // TODO: Implement test case
    }
    
    @Test
    public void testEnterScope_NoAssignsToEliminate() {
        // TODO: Implement test case
    }
    
    @Test
    public void testEnterScope_EscapedLocalVariable() {
        // TODO: Implement test case
    }
    
    @Test
    public void testEnterScope_EliminateAssignment() {
        // TODO: Implement test case
    }
    
    @Test
    public void testExitScope() {
        eliminator.exitScope(new NodeTraversal(compiler, null));
        // No asserts needed since the method is empty
    }
    
    @Test
    public void testVisit() {
        eliminator.visit(new NodeTraversal(compiler, null), null, null);
        // No asserts needed since the method is empty
    }
    
    @Test
    public void testProcess() {
        // TODO: Implement test case
    }
    
    @Test
    public void testTryRemoveDeadAssignments() {
        // TODO: Implement test case
    }
    
    @Test
    public void testTryRemoveAssignment() {
        // TODO: Implement test case
    }
    
    @Test
    public void testIsVariableStillLiveWithinExpression_True() {
        // TODO: Implement test case
    }
    
    @Test
    public void testIsVariableStillLiveWithinExpression_False() {
        // TODO: Implement test case
    }
    
    @Test
    public void testIsVariableReadBeforeKill_ReadNode() {
        // TODO: Implement test case
    }
    
    @Test
    public void testIsVariableReadBeforeKill_KillNode() {
        // TODO: Implement test case
    }
    
    @Test
    public void testIsVariableReadBeforeKill_MaybeLive() {
        // TODO: Implement test case
    }
}