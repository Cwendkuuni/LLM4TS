import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class VariableTest {
	
	@Test
	public void testAddVariable() {
		boolean result = Variable.addVariable("testVar");
		assertTrue(result);
	}
	
	@Test
	public void testAddVariable_duplicate() {
		Variable.addVariable("testVar");
		boolean result = Variable.addVariable("testVar");
		assertFalse(result);
	}
	
	@Test
	public void testRemoveVariable() {
		Variable.addVariable("testVar");
		Variable.removeVariable("testVar");
		int value = Variable.getVariableValue("testVar");
		assertEquals(0, value);
	}
	
	@Test
	public void testGetVariableValue_notFound() {
		int value = Variable.getVariableValue("testVar");
		assertEquals(0, value);
	}
	
	@Test
	public void testGetVariableValue() {
		Variable.addVariable("testVar");
		Variable.setVariableValue("testVar", 10);
		int value = Variable.getVariableValue("testVar");
		assertEquals(10, value);
	}
	
	@Test
	public void testSetVariableValue_notFound() {
		Variable.setVariableValue("testVar", 10);
		int value = Variable.getVariableValue("testVar");
		assertEquals(0, value);
	}
	
	@Test
	public void testSetVariableValue() {
		Variable.addVariable("testVar");
		Variable.setVariableValue("testVar", 10);
		int value = Variable.getVariableValue("testVar");
		assertEquals(10, value);
	}
	
	@Test
	public void testFindVariable_notFound() {
		int index = Variable.findVariable("testVar");
		assertEquals(-1, index);
	}
	
	@Test
	public void testFindVariable() {
		Variable.addVariable("testVar");
		int index = Variable.findVariable("testVar");
		assertNotEquals(-1, index);
	}
	
	@Test
	public void testGetValue() {
		Variable.addVariable("testVar");
		int value = Variable.variableList.get(0).getValue();
		assertEquals(0, value);
	}
	
	@Test
	public void testSetValue() {
		Variable.addVariable("testVar");
		Variable.variableList.get(0).setValue(10);
		int value = Variable.variableList.get(0).getValue();
		assertEquals(10, value);
	}
	
	@Test
	public void testGetName() {
		Variable.addVariable("testVar");
		String name = Variable.variableList.get(0).getName();
		assertEquals("testVar", name);
	}
    
}