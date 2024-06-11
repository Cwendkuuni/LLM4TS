package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
        assertEquals(Memory.m_Runtime.maxMemory(), memory.getMax());
        assertEquals(Memory.m_Runtime.totalMemory(), memory.getCurrent());
    }

    @Test
    public void testMemoryConstructorWithUseGui() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
        assertEquals(Memory.m_Runtime.maxMemory(), memory.getMax());
        assertEquals(Memory.m_Runtime.totalMemory(), memory.getCurrent());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        assertTrue(memory.isEnabled());
        Memory.m_Enabled = false;
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(Memory.m_Enabled);
        memory.setEnabled(false);
        assertFalse(Memory.m_Enabled);
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        memory = new Memory(false);
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
        assertEquals(Memory.m_Runtime.totalMemory(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Runtime.maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(false);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
        assertEquals(4.5, Memory.toMegaByte(4718592), 0.001);
        assertEquals(0.0078125, Memory.toMegaByte(8192), 0.001);
    }
}