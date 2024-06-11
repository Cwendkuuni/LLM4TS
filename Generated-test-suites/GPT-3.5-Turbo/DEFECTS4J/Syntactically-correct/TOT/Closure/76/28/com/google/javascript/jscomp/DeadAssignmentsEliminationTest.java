import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

    @Test
    public void testProcess_NoDeadAssignments() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case without any dead assignments
        String code = "var x = 1; var y = 2; var z = x + y;";
        String expected = code;

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_DeadAssignment() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with a dead assignment
        String code = "var x = 1; x = 2;";
        String expected = "var x = 2;";

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_VariableEscaped() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with a variable that might be escaped due to closures
        String code = "var x = 1; var y = function() { console.log(x); }; x = 2;";
        String expected = code;

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_IdentityAssignment() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with an identity assignment (x=x)
        String code = "var x = 1; x = x;";
        String expected = "var x = 1;";

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_ConditionalAssignment() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with a conditional assignment
        String code = "var x = 1; if (x == 1) { x = 2; }";
        String expected = code;

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_IncrementAssignment() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with an increment assignment
        String code = "var x = 1; x++;";
        String expected = "var x = 1;";

        assertEquals(expected, elimination.process(code));
    }

    @Test
    public void testProcess_DecrementAssignment() {
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination();

        // Test case with a decrement assignment
        String code = "var x = 1; x--;";
        String expected = "var x = 1;";

        assertEquals(expected, elimination.process(code));
    }

}