import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination uce;

    @Before
    public void setup() {
        uce = new UnreachableCodeElimination();
    }

    @Test
    public void testProcess() {
        // Test typical use case
        String input = "function test() { return; alert('unreachable'); }";
        String expectedOutput = "function test() { return; }";
        assertEquals(expectedOutput, uce.process(input));

        // Test edge case - empty function
        input = "function test() { }";
        expectedOutput = "function test() { }";
        assertEquals(expectedOutput, uce.process(input));

        // Test error scenario - invalid input
        input = "this is invalid code";
        expectedOutput = "Error: Invalid input";
        assertEquals(expectedOutput, uce.process(input));
    }
}