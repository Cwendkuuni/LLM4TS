import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    // Test addVariable method
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVar"));
        assertFalse(Variable.addVariable("testVar"));
    }

    // Test removeVariable method
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVar");
        Variable.removeVariable("testVar");
        assertEquals(0, Variable.getVariableValue("testVar"));
    }

    // Test getVariableValue method
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVar");
        assertEquals(0, Variable.getVariableValue("testVar"));
        Variable.setVariableValue("testVar", 10);
        assertEquals(10, Variable.getVariableValue("testVar"));
    }

    // Test setVariableValue method
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", 20);
        assertEquals(20, Variable.getVariableValue("testVar"));
    }

    // Test findVariable method
    @Test
    public void testFindVariable() {
        Variable.addVariable("testVar");
        assertEquals(0, Variable.findVariable("testVar"));
        Variable.setVariableValue("testVar", 30);
        assertEquals(30, Variable.findVariable("&testVar"));
    }
}