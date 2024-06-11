
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
    private DeadAssignmentsElimination dae;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        dae = new DeadAssignmentsElimination(compiler);
    }

    @Test
    public void testProcess() {
        // TODO: implement test
    }

    @Test
    public void testEnterScope() {
        // TODO: implement test
    }

    @Test
    public void testExitScope() {
        // TODO: implement test
    }

    @Test
    public void testVisit() {
        // TODO: implement test
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // TODO: implement test
    }

    @Test
    public void testTryRemoveAssignment() {
        // TODO: implement test
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // TODO: implement test
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // TODO: implement test
    }
}
