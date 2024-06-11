
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor1() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        } catch (IOException e) {
            fail("Constructor threw an exception");
        }
    }

    @Test
    public void testConstructor2() {
        List<String> inputs = new ArrayList<String>();
        inputs.add("input1");
        inputs.add("input2");
        try {
            DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
        } catch (IOException e) {
            fail("Constructor threw an exception");
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit", formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1", false));
        run.addEvent(new Event("event2", true));
        String expected = "";
        expected += "event1:::ENTER\n";
        expected += "\n";
        expected += "event2:::EXIT1\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        String expected = "event:::ENTER\n\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event1 = new Event("event1", false);
        Event event2 = new Event("event2", false);
        OutputBuffer out = new OutputBuffer();
        formatter.point("event1,event2", event1, "22", out);
        String expected = "event1,event2:::ENTER\n\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "event:::ENTER", out, "11");
        String expected = "\nppt event:::ENTER\nppt-type enter";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        event.addAttribute(new EventAttribute("attribute1", "value1", "java.lang.String"));
        event.addAttribute(new EventAttribute("attribute2", "value2", "int"));
        formatter.attributes(event, out);
        String expected = "attribute1\n\"value1\"\n1\nattribute2\nvalue2\n1";
        assertEquals(expected, out.toString());
    }
}
