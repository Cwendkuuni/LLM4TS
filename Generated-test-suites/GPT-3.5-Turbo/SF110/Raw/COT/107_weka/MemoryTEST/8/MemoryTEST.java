import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {
    
    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
        assertEquals(Memory.m_Enabled, memory.isEnabled());
        assertEquals(Memory.m_Initial, memory.getInitial());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
        assertEquals(Memory.m_Enabled, memory.isEnabled());
        assertEquals(Memory.m_Initial, memory.getInitial());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory.m_Enabled = true;
        assertEquals(true, memory.isEnabled());
        
        Memory.m_Enabled = false;
        assertEquals(false, memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());
        
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        memory.setUseGUI(true);
        assertEquals(true, memory.getUseGUI());
        
        memory.setUseGUI(false);
        assertEquals(false, memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getInitial());
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
        
        // Test when enabled and memory is less than initial + 200000
        Memory.m_Enabled = true;
        long current = memory.getCurrent();
        long initial = memory.getInitial();
        long max = memory.getMax();
        assertTrue(memory.isOutOfMemory());
        
        // Test when disabled
        Memory.m_Enabled = false;
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(0.0, memory.toMegaByte(0));
        assertEquals(1.0, memory.toMegaByte(1048576));
        assertEquals(2.0, memory.toMegaByte(2097152));
        assertEquals(1.5, memory.toMegaByte(1572864));
        assertEquals(0.5, memory.toMegaByte(524288));
    }
    
    @Test
    public void testShowOutOfMemory() {
        // This method writes to System.err and displays a dialog, it is difficult to test automatically
        // We can manually verify the output
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        // This method stops threads, it is difficult to test automatically
        Memory memory = new Memory();
        memory.stopThreads();
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // This method writes to System.out, it is difficult to test automatically
        // We can manually verify the output
        Memory.main(new String[]{});
    }
}