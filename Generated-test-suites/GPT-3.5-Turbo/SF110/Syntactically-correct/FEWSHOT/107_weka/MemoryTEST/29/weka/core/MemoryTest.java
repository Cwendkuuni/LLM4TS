import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Memory.m_Initial, memory.getMax());
    }

    @Test
    public void testConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Memory.m_Initial, memory.getMax());
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

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
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
        assertEquals(0, memory.getCurrent());

        // Test after garbage collection
        memory.showOutOfMemory();
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        // Test when out of memory
        memory.setEnabled(true);
        memory.setMax(memory.getCurrent() - memory.getInitial() - 200000L);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.0001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.0001);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.0001);
    }

    @Test
    public void testStopThreads() {
        // Cannot be tested as it stops threads abruptly
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}