import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setUp() {
        deadAssignmentsElimination = new DeadAssignmentsElimination();
    }

    @Test
    public void testProcess() {
        // TODO: Add test cases for process() method
    }

    @Test
    public void testEnterScope() {
        // TODO: Add test cases for enterScope() method
    }

    @Test
    public void testExitScope() {
        // TODO: Add test cases for exitScope() method
    }

    @Test
    public void testVisit() {
        // TODO: Add test cases for visit() method
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // TODO: Add test cases for tryRemoveDeadAssignments() method
    }

    @Test
    public void testTryRemoveAssignment() {
        // TODO: Add test cases for tryRemoveAssignment() method
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // TODO: Add test cases for isVariableStillLiveWithinExpression() method
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // TODO: Add test cases for isVariableReadBeforeKill() method
    }
}