import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testConstructorWithNoParameters() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testConstructorWithUseGUIParameter() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
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
        assertEquals(Memory.m_Initial, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(memory.getTotalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        assertEquals(1.0, Memory.toMegaByte(bytes), 0.0);
        bytes = 2097152;
        assertEquals(2.0, Memory.toMegaByte(bytes), 0.0);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Cannot be tested since it relies on System.out and JOptionPane
        // Testing is done manually
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        // Cannot be tested since it stops threads
        // Testing is done manually
        Memory memory = new Memory();
        memory.stopThreads();
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Cannot be tested since it relies on System.out
        // Testing is done manually
        Memory.main(new String[]{});
    }
}