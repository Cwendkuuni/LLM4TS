import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Util.toMegaByte(Memory.m_Initial), memory.getInitial(), 0.1);
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Util.toMegaByte(memory.m_Total), memory.getCurrent(), 0.1);
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Util.toMegaByte(memory.m_Max), memory.getMax(), 0.1);
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.1);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.1);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.1);
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertion as this method does not have any return value
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // No assertion as this method only prints to console
        Memory.main(new String[] {});
    }
}