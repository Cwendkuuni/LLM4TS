import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setup() {
        deadAssignmentsElimination = new DeadAssignmentsElimination();
    }

    @Test
    public void testProcess() {
        // TODO: Implement test case for process method
    }

    @Test
    public void testEnterScope() {
        // TODO: Implement test case for enterScope method
    }

    @Test
    public void testExitScope() {
        // TODO: Implement test case for exitScope method
    }

    @Test
    public void testVisit() {
        // TODO: Implement test case for visit method
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // TODO: Implement test case for tryRemoveDeadAssignments method
    }

    @Test
    public void testTryRemoveAssignment() {
        // TODO: Implement test case for tryRemoveAssignment method
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // TODO: Implement test case for isVariableStillLiveWithinExpression method
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // TODO: Implement test case for isVariableReadBeforeKill method
    }
}