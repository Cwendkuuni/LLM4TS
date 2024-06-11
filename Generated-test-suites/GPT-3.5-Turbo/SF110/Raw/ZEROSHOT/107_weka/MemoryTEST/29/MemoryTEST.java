import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import weka.core.Memory;
import weka.core.Utils;

public class MemoryTest {
    
    private Memory memory;
    
    @Before
    public void setUp() {
        memory = new Memory();
    }
    
    @After
    public void tearDown() {
        memory = null;
    }
    
    @Test
    public void testIsEnabled() {
        assertEquals(true, memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        assertEquals(false, memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        assertEquals((long)Runtime.getRuntime().totalMemory(), memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        assertEquals((long)Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        assertEquals((long)Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertEquals(false, memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0, Double.compare(0.0, Memory.toMegaByte(0)));
        assertEquals(0, Double.compare(1.0, Memory.toMegaByte(1048576)));
    }
    
    @Test
    public void testShowOutOfMemory() {
        // TODO: Write test case for showOutOfMemory()
    }
    
    @Test
    public void testStopThreads() {
        // TODO: Write test case for stopThreads()
    }
    
    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // TODO: Write test case for main()
    }
}