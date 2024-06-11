import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setUp() {
        deadAssignmentsElimination = new DeadAssignmentsElimination(new AbstractCompiler());
    }

    @Test
    public void testEnterScope() {
        // Add test case here
    }

    @Test
    public void testExitScope() {
        // Add test case here
    }

    @Test
    public void testVisit() {
        // Add test case here
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // Add test case here
    }

    @Test
    public void testTryRemoveAssignment() {
        // Add test case here
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // Add test case here
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // Add test case here
    }
}