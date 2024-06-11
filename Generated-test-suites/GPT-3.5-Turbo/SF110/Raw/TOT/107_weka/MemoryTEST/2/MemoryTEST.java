import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {
    
    // Test case for constructor with default argument
    @Test
    public void testConstructor1() {
        Memory memory = new Memory();
        assertNotNull(memory);
    }
    
    // Test case for constructor with useGUI argument
    @Test
    public void testConstructor2() {
        Memory memory = new Memory(true);
        assertNotNull(memory);
        assertTrue(memory.getUseGUI());
    }
    
    // Test case for isEnabled method
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }
    
    // Test case for setEnabled method
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    // Test case for getUseGUI method
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }
    
    // Test case for getInitial method
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        // Edge case: initial memory should be greater than 0
        assertTrue(initial > 0);
    }
    
    // Test case for getCurrent method
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        // Edge case: current memory should be greater than or equal to 0
        assertTrue(current >= 0);
    }
    
    // Test case for getMax method
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        // Edge case: max memory should be greater than or equal to total memory
        assertTrue(max >= memory.getCurrent());
    }
    
    // Test case for isOutOfMemory method
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        // Edge case: when enabled, with max memory, current memory and initial memory within allowable range
        assertFalse(memory.isOutOfMemory());
        
        // Edge case: when enabled, with max memory, current memory and initial memory outside allowable range
        memory.setEnabled(true);
        memory.setInitial(500000L);
        memory.setMax(1000000L);
        memory.setCurrent(700000L);
        assertTrue(memory.isOutOfMemory());
        
        // Edge case: when disabled, with max memory, current memory and initial memory outside allowable range
        memory.setEnabled(false);
        memory.setInitial(500000L);
        memory.setMax(1000000L);
        memory.setCurrent(700000L);
        assertFalse(memory.isOutOfMemory());
    }
    
    // Test case for toMegaByte method
    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double megaBytes = Memory.toMegaByte(bytes);
        // Typical use case: converting 1 MB bytes to mega bytes should return 1.0
        assertEquals(1.0, megaBytes, 0.1);
    }
    
    // Test case for showOutOfMemory method
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory(true);
        
        // Typical use case: when enabled, the out-of-memory message should be printed to console
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        memory.showOutOfMemory();
        String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   " + Utils.doubleToString(toMegaByte(Memory.m_Initial), 1) + "MB\n" + "- total memory used:  " + Utils.doubleToString(toMegaByte(memory.getCurrent()), 1) + "MB\n" + "- max. memory avail.: " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB\n" + "\n" + "Note:\n" + "The Java heap size can be specified with the -Xmx option.\n" + "E.g., to use 128MB as heap size, the command line looks like this:\n" + "   java -Xmx128m -classpath ...\n" + "This does NOT work in the SimpleCLI, the java command refers\n" + "to the one with which Weka is started.";
        assertEquals(expectedMsg, outContent.toString());
        
        // Typical use case: when enabled and useGUI is true, the out-of-memory message should be shown in dialog box
        final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        memory.showOutOfMemory();
        expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   " + Utils.doubleToString(toMegaByte(Memory.m_Initial), 1) + "MB\n" + "- total memory used:  " + Utils.doubleToString(toMegaByte(memory.getCurrent()), 1) + "MB\n" + "- max. memory avail.: " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB\n" + "\n" + "Note:\n" + "The Java heap size can be specified with the -Xmx option.\n" + "E.g., to use 128MB as heap size, the command line looks like this:\n" + "   java -Xmx128m -classpath ...\n" + "This does NOT work in the SimpleCLI, the java command refers\n" + "to the one with which Weka is started.";
        assertEquals(expectedMsg, errContent.toString());
        
        // Edge case: when disabled, no message should be printed or shown
        memory.setEnabled(false);
        System.setOut(null);
        System.setErr(null);
        memory.showOutOfMemory();
        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
    }
    
    // Test case for stopThreads method
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertion can be made as this method does not return any output
    }
    
    // Test case for getRevision method
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        // Typical use case: revision should not be null or empty
        assertNotNull(revision);
        assertNotEquals("", revision);
    }
    
    // Test case for main method
    @Test
    public void testMain() {
        // No assertion can be made as this method does not return any output
    }
}