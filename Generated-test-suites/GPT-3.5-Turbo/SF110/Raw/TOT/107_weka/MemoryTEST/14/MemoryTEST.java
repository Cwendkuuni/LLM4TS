import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testMemoryConstructorWithDefaultParameters() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testMemoryConstructorWithUseGUIParameter() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }
    
    @Test
    public void testMemoryIsEnabled() {
        Memory memory = new Memory();
        assertEquals(true, memory.isEnabled());
    }
    
    @Test
    public void testMemorySetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testMemoryGetUseGUI() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
    }

    @Test
    public void testMemoryGetInitial() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testMemoryGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testMemoryGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testMemoryIsOutOfMemoryWithEnabledFalse() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryToMegaByte() {
        long bytes = 5242880;
        double expected = 5.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.01);
    }

    @Test
    public void testMemoryShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        
        // Test console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        memory.showOutOfMemory();
        
        String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   " + Utils.doubleToString(Memory.toMegaByte(Memory.m_Initial), 1) + "MB\n" + "- total memory used:  " + Utils.doubleToString(Memory.toMegaByte(memory.getCurrent()), 1) + "MB\n" + "- max. memory avail.: " + Utils.doubleToString(Memory.toMegaByte(memory.getMax()), 1) + "MB\n" + "\n" + "Note:\n" + "The Java heap size can be specified with the -Xmx option.\n" + "E.g., to use 128MB as heap size, the command line looks like this:\n" + "   java -Xmx128m -classpath ...\n" + "This does NOT work in the SimpleCLI, the java command refers\n" + "to the one with which Weka is started.";
        
        assertEquals(expectedMsg, outContent.toString().trim());
        
        // Reset console output
        System.setOut(System.out);
    }

    @Test
    public void testMemoryStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // TODO: Test the behavior of thread stopping and garbage collection
    }

    @Test
    public void testMemoryGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        // TODO: Test the console output of the main method
    }

}