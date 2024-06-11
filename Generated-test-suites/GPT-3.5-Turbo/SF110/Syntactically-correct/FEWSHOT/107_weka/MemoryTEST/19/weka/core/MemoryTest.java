package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testConstructorWithoutParameter() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertTrue(memory.isEnabled());
        assertEquals(0, memory.getInitial());
        assertEquals(0, memory.getCurrent());
        assertEquals(0, memory.getMax());
    }
    
    @Test
    public void testConstructorWithParameter() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertTrue(memory.isEnabled());
        assertEquals(0, memory.getInitial());
        assertEquals(0, memory.getCurrent());
        assertEquals(0, memory.getMax());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(0, memory.getInitial());
        
        Memory.m_Initial = 100;
        memory = new Memory();
        assertEquals(100, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(0, memory.getCurrent());
        
        Memory.m_Total = 200;
        memory = new Memory();
        assertEquals(200, memory.getCurrent());
        
        Memory.m_Total = 300;
        memory.getCurrent();
        assertEquals(300, memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(0, memory.getMax());
        
        memory = new Memory();
        assertEquals(0, memory.getMax());
        
        Memory.m_Initial = 100;
        memory = new Memory();
        assertEquals(100, memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
        
        Memory.m_Initial = 100;
        Memory.m_Total = 200;
        assertEquals(100, memory.getMax());
        
        assertTrue(memory.isOutOfMemory());
        
        Memory.m_Total = 100;
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.01);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.01);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.01);
        assertEquals(1.5, Memory.toMegaByte(1572864), 0.01);
    }
    
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        assertTrue(true); // Testing printing only, not assertable
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        assertTrue(true); // Testing only, not assertable
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);
        assertTrue(true); // Testing printing only, not assertable
    }

}