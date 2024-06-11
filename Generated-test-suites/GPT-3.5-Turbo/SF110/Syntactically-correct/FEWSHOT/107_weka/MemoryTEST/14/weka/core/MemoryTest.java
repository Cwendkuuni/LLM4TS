package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testConstructor() {
        Memory mem = new Memory();
        assertFalse(mem.getUseGUI());
        assertEquals(Memory.m_Initial, mem.getInitial());

        mem = new Memory(true);
        assertTrue(mem.getUseGUI());
        assertEquals(Memory.m_Initial, mem.getInitial());
    }

    @Test
    public void testIsEnabled() {
        Memory mem = new Memory();
        assertTrue(mem.isEnabled());

        mem.setEnabled(false);
        assertFalse(mem.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory mem = new Memory();
        assertTrue(mem.isEnabled());

        mem.setEnabled(false);
        assertFalse(mem.isEnabled());

        mem.setEnabled(true);
        assertTrue(mem.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory mem = new Memory();
        assertFalse(mem.getUseGUI());

        mem = new Memory(true);
        assertTrue(mem.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory mem = new Memory();
        assertEquals(Memory.m_Initial, mem.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory mem = new Memory();
        assertEquals(mem.getTotal(), mem.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory mem = new Memory();
        assertEquals(mem.m_Max, mem.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory mem = new Memory();
        assertFalse(mem.isOutOfMemory());

        mem.m_Max = Memory.m_Initial + 200000L;
        assertFalse(mem.isOutOfMemory());

        mem.setEnabled(true);
        assertTrue(mem.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory mem = new Memory();
        mem.stopThreads();
        // No assert as this method does not return anything
    }
}