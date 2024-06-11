
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {
    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testIsEnabled() {
        boolean enabled = memory.isEnabled();
        
        // Test that isEnabled returns the expected value
        assertTrue(enabled);
    }

    @Test
    public void testSetEnabled() {
        // Test that setEnabled sets the value properly
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        boolean useGUI = memory.getUseGUI();
        
        // Test that getUseGUI returns the expected value
        assertFalse(useGUI);
    }

    @Test
    public void testGetInitial() {
        long initial = memory.getInitial();
        
        // Test that getInitial returns a positive value
        assertTrue(initial >= 0);
    }

    @Test
    public void testGetCurrent() {
        long current = memory.getCurrent();
        
        // Test that getCurrent returns a positive value
        assertTrue(current >= 0);
    }

    @Test
    public void testGetMax() {
        long max = memory.getMax();
        
        // Test that getMax returns a positive value
        assertTrue(max >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        // Test that isOutOfMemory returns the expected value when enabled is true and max - current is less than initial + 200000
        memory.setEnabled(true);
        memory.setMax(1000000L);
        memory.setCurrent(800000L);
        memory.setInitial(500000L);
        assertTrue(memory.isOutOfMemory());
        
        // Test that isOutOfMemory returns false when enabled is false
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double megabytes = memory.toMegaByte(bytes);
        
        // Test that toMegaByte converts bytes to megabytes properly
        assertEquals(1.0, megabytes, 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Test that showOutOfMemory does not throw any exceptions
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Test that stopThreads does not throw any exceptions
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        
        // Test that getRevision returns a non-null value
        assertNotNull(revision);
    }
}
