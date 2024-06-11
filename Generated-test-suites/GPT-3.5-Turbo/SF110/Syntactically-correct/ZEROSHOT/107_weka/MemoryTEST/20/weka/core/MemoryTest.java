package weka.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @After
    public void tearDown() {
        memory = null;
    }

    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.toMegaByte(Memory.m_Initial), memory.getInitial(), 0.001);
    }

    @Test
    public void testGetCurrent() {
        assertEquals(Memory.toMegaByte(memory.m_Total), memory.getCurrent(), 0.001);
    }

    @Test
    public void testGetMax() {
        assertEquals(Memory.toMegaByte(memory.m_Max), memory.getMax(), 0.001);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.001);
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Check if threads are stopped correctly
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
}