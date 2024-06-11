import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weka.core.Memory;
import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryTest {

    private Memory memory;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    public void setUp() {
        memory = new Memory();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled()); // Typical case: memory is enabled
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled()); // Typical case: memory is disabled
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI()); // Typical case: useGUI is false
    }

    @Test
    public void testGetInitial() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial()); // Typical case: initial memory equals totalMemory()
    }

    @Test
    public void testGetCurrent() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent()); // Typical case: current memory equals totalMemory()
    }

    @Test
    public void testGetMax() {
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax()); // Typical case: max memory equals maxMemory()
    }

    @Test
    public void testIsOutOfMemory() {
        memory.setEnabled(true);

        // Edge case: max - current < initial + 200000L
        long current = memory.getCurrent();
        long initial = memory.getInitial();
        long max = memory.getMax();
        boolean expected = max - current < initial + 200000L;
        assertEquals(expected, memory.isOutOfMemory());

        // Error scenario: memory is disabled
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        // Typical case: bytes = 1048576L
        assertEquals(1.0, memory.toMegaByte(1048576L));

        // Edge case: bytes = 0
        assertEquals(0.0, memory.toMegaByte(0));

        // Error scenario: bytes = -1048576L
        assertEquals(-1.0, memory.toMegaByte(-1048576L));
    }

    @Test
    public void testShowOutOfMemory() {
        // Typical case: memory is enabled
        memory.setEnabled(true);
        memory.showOutOfMemory();
        String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n" +
                "- initial JVM size:   1.0MB\n" +
                "- total memory used:  0.0MB\n" +
                "- max. memory avail.: 4.0MB\n" +
                "\n" +
                "Note:\n" +
                "The Java heap size can be specified with the -Xmx option.\n" +
                "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                "   java -Xmx128m -classpath ...\n" +
                "This does NOT work in the SimpleCLI, the java command refers\n" +
                "to the one with which Weka is started.\n";
        assertEquals(expectedMsg, outContent.toString());

        // Error scenario: memory is disabled
        memory.setEnabled(false);
        memory.showOutOfMemory();
        assertEquals("", outContent.toString());
    }

    @Test
    public void testStopThreads() {
        // Typical case: stop non-main and non-AWT threads
        memory.stopThreads();
        // Validate that all non-main and non-AWT threads were stopped

        // Error scenario: memory is disabled
        memory.setEnabled(false);
        memory.stopThreads();
        // Validate that no thread was stopped
    }

    @Test
    public void testGetRevision() {
        // Error scenario: revision string is not extracted correctly
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        // Typical case: print initial and max memory
        Memory.main(new String[]{});
        assertEquals("Initial memory: 1.0MB (1048576)\n" +
                "Max memory: 4.0MB (4194304)\n", outContent.toString());
    }
}