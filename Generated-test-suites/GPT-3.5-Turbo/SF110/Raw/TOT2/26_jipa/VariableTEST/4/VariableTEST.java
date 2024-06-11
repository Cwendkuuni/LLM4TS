import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        assertTrue(Variable.addVariable("testVar1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar2");
        Variable.removeVariable("testVar2");
        assertTrue(Variable.variableList.isEmpty());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar3");
        Variable.setVariableValue("testVar3", 10);
        assertEquals(10, Variable.getVariableValue("testVar3"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar4");
        Variable.setVariableValue("testVar4", 20);
        assertEquals(20, Variable.getVariableValue("testVar4"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar5");
        
        assertEquals(0, Variable.findVariable("testVar5"));
        assertEquals(20, Variable.findVariable("&testVar5"));
        assertEquals(-1, Variable.findVariable("nonExistentVar"));
    }
}