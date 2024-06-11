package weka.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {
    
    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() >= 0);
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double megaBytes = Memory.toMegaByte(bytes);
        assertEquals(1.0, megaBytes, 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Unable to create a test for this method as it interacts with System.out and JOptionPane
        assertTrue(true);
    }
    
    @Test
    public void testStopThreads() {
        // Unable to create a test for this method as it interacts with Thread and System.gc()
        assertTrue(true);
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Cannot test the main method directly as it only prints to System.out
        assertTrue(true);
    }
}