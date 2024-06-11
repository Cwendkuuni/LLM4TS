
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {
    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testMemoryConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Memory.toMegaByte(Memory.m_Initial), memory.getInitial(), 0.1);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Memory.toMegaByte(memory.getCurrent()), memory.getTotal(), 0.1);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Memory.toMegaByte(memory.getMax()), memory.getMax(), 0.1);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        assertEquals(1.0, Memory.toMegaByte(bytes), 0.1);
    }
}
