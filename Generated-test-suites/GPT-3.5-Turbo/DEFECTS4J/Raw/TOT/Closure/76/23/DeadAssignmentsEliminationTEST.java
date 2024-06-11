import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination eliminator;

    @Before
    public void setup() {
        eliminator = new DeadAssignmentsElimination();
    }

    @Test
    public void testProcess_WithNoDeadAssignments_NoChangesAreMade() {
        String input = "var x = 1; var y = 2;";
        String expected = "var x = 1; var y = 2;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_WithDeadAssignment_RemoveDeadAssignment() {
        String input = "var x = 1; x = 2;";
        String expected = "var x = 1;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_WithComplexDeadAssignment_RemoveDeadAssignment() {
        String input = "var x = 1; var y = 2; x = y; y = 3;";
        String expected = "var x = 1; var y = 2;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_WithIdentityAssignment_RemoveIdentityAssignment() {
        String input = "var x = 1; x = x;";
        String expected = "var x = 1;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_WithEscapedVariables_NoChangesAreMade() {
        String input = "function foo() { var x = 1; } var y = x;";
        String expected = "function foo() { var x = 1; } var y = x;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_WithConditionalAssignment_ConditionalAssignmentIsNotRemoved() {
        String input = "var x = (true) ? 1 : 2;";
        String expected = "var x = (true) ? 1 : 2;";
        String actual = eliminator.process(input);
        assertEquals(expected, actual);
    }

}