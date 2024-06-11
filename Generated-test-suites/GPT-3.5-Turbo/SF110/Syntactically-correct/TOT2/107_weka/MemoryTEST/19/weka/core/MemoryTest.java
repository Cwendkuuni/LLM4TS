package weka.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {
    
    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
    }
    
    // Test parameterized constructor
    @Test
    public void testParameterizedConstructor() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertTrue(memory.getCurrent() > 0);
    }
    
    // Test isEnabled method
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }
    
    // Test setEnabled method
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    // Test getInitial method
    @Test
    public void testGetInitial() {
        assertEquals(Memory.m_Initial, new Memory().getInitial());
    }
    
    // Test getCurrent method
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertTrue(memory.getCurrent() > 0);
    }
    
    // Test getMax method
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }
    
    // Test isOutOfMemory method
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertTrue(memory.isOutOfMemory());
    }
    
    // Test toMegaByte method
    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0.001);
    }
    
    // Test showOutOfMemory method
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        // Cannot really test the output, just make sure it doesn't throw any exceptions
    }
    
    // Test stopThreads method
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Cannot really test the output, just make sure it doesn't throw any exceptions
    }
    
    // Test getRevision method
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
}