import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {
    
    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertFalse(memory.getUseGUI());
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertNotNull(memory);
        assertTrue(memory.getUseGUI());
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testGetAndSetEnabled() {
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
        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576; // 1MB
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
        
        bytes = 2097152; // 2MB
        expected = 2.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // TODO: Implement test for showOutOfMemory method
    }
    
    @Test
    public void testStopThreads() {
        // TODO: Implement test for stopThreads method
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // TODO: Implement test for main method
    }
}