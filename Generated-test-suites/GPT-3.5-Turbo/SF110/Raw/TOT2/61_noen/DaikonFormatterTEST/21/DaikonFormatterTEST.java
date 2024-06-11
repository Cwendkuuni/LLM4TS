package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;
import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstructorWithSimple() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertTrue(formatter.fileName.equals("test.txt"));
        assertNull(formatter.inputs);
        assertTrue(formatter.simple);
    }

    @Test
    public void testDaikonFormatterConstructorWithInputs() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("test.txt", inputs);
        assertTrue(formatter.fileName.equals("test.txt"));
        assertEquals(inputs, formatter.inputs);
        assertFalse(formatter.simple);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        ProgramRun run = new ProgramRun();
        String expected = "expected output";
        assertEquals(expected, formatter.observations(run));
    }
}