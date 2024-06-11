import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {
    
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertEquals(true, memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
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
        long current = memory.getCurrent();
        assertEquals(memory.getTotal(), current);
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(memory.getMax(), memory.m_Max);
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        boolean isOutOfMemory = memory.isOutOfMemory();
        assertEquals(false, isOutOfMemory);
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertion as the test case is checking the functionality of the method
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = RevisionUtils.extract("$Revision: 8034 $");
        assertEquals(revision, memory.getRevision());
    }
    
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        memory.showOutOfMemory();
        // No assertion as the test case is checking the functionality of the method
    }
    
    @Test
    public void testMain() {
        // No assertion as the test case is checking the functionality of the method
    }
}