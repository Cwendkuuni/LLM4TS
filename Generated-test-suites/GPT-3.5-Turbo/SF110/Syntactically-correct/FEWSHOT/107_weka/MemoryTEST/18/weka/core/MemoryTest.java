package weka.core;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testMemory() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testMemoryWithUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.isEnabled());
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testMemoryWithFalseUseGUI() {
        Memory memory = new Memory(false);
        assertTrue(memory.isEnabled());
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
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
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(0.0, memory.toMegaByte(0));
        assertEquals(1.0, memory.toMegaByte(1048576));
        assertEquals(2.0, memory.toMegaByte(2097152));
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        // TODO: How to test the output of System.err.println?
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // TODO: How to test the behavior of stopping threads?
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // TODO: How to test the output of System.out.println?
    }
}