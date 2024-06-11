
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setUp() {
        deadAssignmentsElimination = new DeadAssignmentsElimination(new AbstractCompiler());
    }

    @Test
    public void process() {
        // TODO: Add test cases for the process method
    }

    @Test
    public void enterScope() {
        // TODO: Add test cases for the enterScope method
    }

    @Test
    public void exitScope() {
        // TODO: Add test cases for the exitScope method
    }

    @Test
    public void visit() {
        // TODO: Add test cases for the visit method
    }

    @Test
    public void tryRemoveDeadAssignments() {
        // TODO: Add test cases for the tryRemoveDeadAssignments method
    }

    @Test
    public void tryRemoveAssignment() {
        // TODO: Add test cases for the tryRemoveAssignment method
    }

    @Test
    public void isVariableStillLiveWithinExpression() {
        // TODO: Add test cases for the isVariableStillLiveWithinExpression method
    }

    @Test
    public void isVariableReadBeforeKill() {
        // TODO: Add test cases for the isVariableReadBeforeKill method
    }
}
