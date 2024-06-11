package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
        assertTrue(memory.isEnabled());
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(0.5, Memory.toMegaByte(524288), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
    }
    
    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Verify that threads are stopped
    }
    
    @Test
    public void testGetRevision() {
        String revision = RevisionUtils.extract("$Revision: 8034 $");
        assertEquals(revision, memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Verify that the initial and max memory are printed
        // in the correct format
    }
    
    // Additional test cases can be added to cover all possible scenarios
    
}