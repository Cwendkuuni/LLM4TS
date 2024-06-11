import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        String instruction = "halt";
        Main.processInstruction(instruction);
        // add assertion here
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testGetFileName() {
        // add test case here
    }

    @Test
    public void testLoadInstructions() {
        // add test case here
    }

    @Test
    public void testRemoveComment() {
        // add test case here
    }

    @Test
    public void testResetInstructions() {
        // add test case here
    }

    @Test
    public void testSplitArguments() {
        // add test case here
    }

    @Test
    public void testGetValue() {
        // add test case here
    }

    @Test
    public void testGetJumpValue() {
        // add test case here
    }

    @Test
    public void testReadValue() {
        // add test case here
    }

    @Test
    public void testGetStringValue() {
        // add test case here
    }

    @Test
    public void testIsNumeric() {
        // add test case here
    }
}