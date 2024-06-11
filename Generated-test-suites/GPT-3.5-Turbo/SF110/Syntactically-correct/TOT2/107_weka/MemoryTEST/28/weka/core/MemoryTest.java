package weka.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {
    
    @Test
    public void testMemoryConstructor() {
        Memory mem = new Memory();
        assertNotNull(mem);
        assertEquals(false, mem.getUseGUI());
    }
    
    @Test
    public void testIsEnabled() {
        Memory mem = new Memory();
        assertEquals(true, mem.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        Memory mem = new Memory();
        mem.setEnabled(false);
        assertEquals(false, mem.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        Memory mem = new Memory();
        assertEquals(false, mem.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory mem = new Memory();
        assertEquals(0L, mem.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory mem = new Memory();
        assertNotEquals(0L, mem.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory mem = new Memory();
        assertNotEquals(0L, mem.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory mem = new Memory();
        assertEquals(mem.isEnabled() && (mem.getMax() - mem.getCurrent() < mem.getInitial() + 200000L), mem.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0.001);
    }
    
    @Test
    public void testStopThreads() {
        Memory mem = new Memory();
        mem.stopThreads();
        // No assertions as it's more of a system operation
    }
    
    @Test
    public void testGetRevision() {
        Memory mem = new Memory();
        assertNotNull(mem.getRevision());
    }
    
    @Test
    public void testMain() {
        final Memory mem = new Memory();
        assertNotNull(mem);
    }
}