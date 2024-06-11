import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(-1, Variable.findVariable("var1"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.findVariable("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(-1, Variable.findVariable("var2"));
    }
}