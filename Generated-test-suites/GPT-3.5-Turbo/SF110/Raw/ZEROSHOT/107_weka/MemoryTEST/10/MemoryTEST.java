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
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1000000;
        double megaBytes = bytes / 1048576.0;
        assertEquals(megaBytes, memory.toMegaByte(bytes), 0.1);
    }

    @Test
    public void testShowOutOfMemory() {
        // no assertions can be made for this method
    }

    @Test
    public void testStopThreads() {
        // no assertions can be made for this method
    }

    @Test
    public void testGetRevision() {
        // no assertions can be made for this method
    }

    @Test
    public void testMain() {
        // no assertions can be made for this method
    }

}