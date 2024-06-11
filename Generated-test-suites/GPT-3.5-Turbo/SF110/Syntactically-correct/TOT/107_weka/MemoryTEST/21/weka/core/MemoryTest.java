import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class MemoryTest {
    
    private Memory memory;
    
    @Before
    public void setup() {
        memory = new Memory();
    }
    
    @Test
    public void testIsEnabled() {
        Assert.assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        Assert.assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Assert.assertFalse(memory.getUseGUI());
        
        Memory memoryWithGUI = new Memory(true);
        Assert.assertTrue(memoryWithGUI.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        long initial = Runtime.getRuntime().totalMemory();
        Assert.assertEquals(initial, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        Assert.assertEquals(totalMemory, memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        Assert.assertEquals(maxMemory, memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        // Test when memory is enabled and current memory is less than initial + 200000L
        memory.setEnabled(true);
        long initial = memory.getInitial();
        long current = memory.getCurrent();
        long max = memory.getMax();
        Assert.assertTrue(memory.isOutOfMemory());
        
        // Test when memory is enabled and current memory is equal to initial + 200000L
        memory.setEnabled(true);
        Runtime.getRuntime().gc(); // Trigger garbage collection to potentially free up memory
        long oldMax = Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().maxMemory(1000L); // Reduce maximum memory
        Assert.assertFalse(memory.isOutOfMemory());
        Runtime.getRuntime().maxMemory(oldMax); // Restore original maximum memory
        
        // Test when memory is disabled
        memory.setEnabled(false);
        Assert.assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes1 = 1048576; // 1 MB
        double expectedResult1 = 1.0;
        Assert.assertEquals(expectedResult1, Memory.toMegaByte(bytes1), 0.001);
        
        long bytes2 = 524288; // 0.5 MB
        double expectedResult2 = 0.5;
        Assert.assertEquals(expectedResult2, Memory.toMegaByte(bytes2), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Test when memory is enabled and GUI is not used
        memory.setEnabled(true);
        memory.setUseGUI(false);
        Assert.assertEquals("", memory.showOutOfMemory());
        
        // Test when memory is enabled and GUI is used
        memory.setEnabled(true);
        memory.setUseGUI(true);
        Assert.assertEquals("", memory.showOutOfMemory());
    }
    
    @Test
    public void testStopThreads() {
        // Test when no threads need to be stopped
        Assert.assertEquals("", memory.stopThreads());
        
        // Test when there are threads with "Thread" in their names
        Thread[] threads1 = new Thread[10];
        Arrays.fill(threads1, new Thread());
        threads1[0].setName("Thread 1");
        threads1[1].setName("Thread 2");
        threads1[2].setName("Thread 3");
        memory.stopThreads();
        for (Thread thread : threads1) {
            Assert.assertFalse(thread.isAlive());
        }
        
        // Test when there are threads with "AWT-EventQueue" in their names
        Thread[] threads2 = new Thread[10];
        Arrays.fill(threads2, new Thread());
        threads2[0].setName("AWT-EventQueue 1");
        threads2[1].setName("AWT-EventQueue 2");
        threads2[2].setName("AWT-EventQueue 3");
        memory.stopThreads();
        for (Thread thread : threads2) {
            Assert.assertFalse(thread.isAlive());
        }
    }
    
    @Test
    public void testGetRevision() {
        Assert.assertEquals("", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Test printing initial and max memory
        Assert.assertEquals("", Memory.main(new String[0]));
    }
    
}