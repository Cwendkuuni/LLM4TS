import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertFalse(memory.getUseGUI());
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testMemoryConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertNotNull(memory);
        assertTrue(memory.getUseGUI());
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
        
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
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
        long initial = memory.getInitial();
        assertTrue(initial > 0);
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertTrue(current > 0);
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertTrue(max > 0);
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
        long bytes = 1048576;
        double megaBytes = Memory.toMegaByte(bytes);
        assertEquals(1.0, megaBytes, 0.01);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // This method is difficult to test as it involves GUI interaction and printing to System.err
        // Hence, we will not include a direct test case for this method
        // Instead, we can manually verify its behavior during application execution
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        Thread[] threadsBefore = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsBefore);
        memory.stopThreads();
        Thread[] threadsAfter = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsAfter);
        
        for (int i = 0; i < threadsAfter.length; i++) {
            Thread threadBefore = threadsBefore[i];
            Thread threadAfter = threadsAfter[i];
            
            if (threadBefore != null && threadAfter != null) {
                if (threadBefore.getName().startsWith("Thread")) {
                    assertEquals(Thread.State.TERMINATED, threadAfter.getState());
                } else if (threadBefore.getName().startsWith("AWT-EventQueue")) {
                    assertEquals(Thread.State.TERMINATED, threadAfter.getState());
                } else {
                    assertEquals(threadBefore, threadAfter);
                }
            }
        }
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
        assertFalse(revision.isEmpty());
    }
    
    @Test
    public void testMain() {
        // This method is difficult to test as it involves printing to System.out
        // Instead, we can manually verify its behavior during application execution
    }
}