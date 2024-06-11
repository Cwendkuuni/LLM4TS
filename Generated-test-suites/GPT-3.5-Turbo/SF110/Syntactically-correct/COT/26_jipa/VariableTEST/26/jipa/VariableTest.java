
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Before
    public void setup() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var"));
        assertFalse(Variable.addVariable("var"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var");
        Variable.removeVariable("var");
        assertFalse(Variable.variableList.contains("var"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var");
        Variable.setVariableValue("var", 10);
        assertEquals(10, Variable.getVariableValue("var"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var");
        Variable.setVariableValue("var", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var");
        assertEquals(0, Variable.findVariable("var"));
        assertEquals(-1, Variable.findVariable("nonexistent"));
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testAddVariableWithSpecialCharacters() {
        assertTrue(Variable.addVariable("*var"));
    }

    @Test
    public void testGetVariableValueWithSpecialCharacters() {
        Variable.addVariable("var");
        Variable.setVariableValue("var", 10);
        assertEquals(10, Variable.getVariableValue("*var"));
    }

    @Test
    public void testSetVariableValueWithNonexistentVariable() {
        Variable.setVariableValue("var", 10);
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var");
        Variable.setVariableValue("var", 10);
        assertEquals(10, Variable.findVariable("&var"));
    }

}

