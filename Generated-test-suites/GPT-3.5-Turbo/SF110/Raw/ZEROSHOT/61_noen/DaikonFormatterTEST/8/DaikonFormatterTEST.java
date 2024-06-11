import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", true);
            assertEquals("testFile", formatter.getFileName());
            assertTrue(formatter.isSimple());
            assertNull(formatter.getInputs());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testConstructorWithInputs() {
        try {
            Collection<String> inputs = new ArrayList<String>();
            inputs.add("input1");
            inputs.add("input2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
            assertEquals("testFile", formatter.getFileName());
            assertFalse(formatter.isSimple());
            assertEquals(inputs, formatter.getInputs());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String actual = formatter.header();
        assertEquals(expected, actual);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        String actual = formatter.footer();
        assertEquals(expected, actual);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "dtrace";
        String actual = formatter.fileNameExtension();
        assertEquals(expected, actual);
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        String actual = formatter.observations(run);
        assertEquals(expected, actual);
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("event1", false);
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, comparability, out);
        String expected = "\nevent1:::ENTER\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("event1", false);
        String name = "event1,event2";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        formatter.point(name, event, comparability, out);
        String expected = "\nevent1,event2:::ENTER\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("event1", true);
        String eventName = "event1";
        String comparability = "22";
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, comparability);
        String expected = "\n\nppt event1\nppt-type exit";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("event1", false);
        OutputBuffer out = new OutputBuffer();
        iterator<EventAttribute> ai = event.attributes();
        formatter.attributes(event, out);
        String expected = "";
        String actual = out.toString();
        assertEquals(expected, actual);
    }
}