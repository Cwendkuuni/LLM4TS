
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testGetEnabled() {
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
        final long initialMemory = Runtime.getRuntime().totalMemory();
        assertEquals(initialMemory, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        assertEquals(memory.getTotal(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        final long maxMemory = Runtime.getRuntime().maxMemory();
        assertEquals(maxMemory, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        assertEquals(1.0, Memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testStopThreads() {
        // Not possible to test directly as it stops other threads
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}
