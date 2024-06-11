import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor1() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor2() {
        Collection<String> inputSet = new ArrayList<String>();
        inputSet.add("input1");
        inputSet.add("input2");

        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", inputSet);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit\n";
        String header = formatter.header();
        assertEquals(expected, header);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "";
        String footer = formatter.footer();
        assertEquals(expected, footer);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "dtrace";
        String extension = formatter.fileNameExtension();
        assertEquals(expected, extension);
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        ProgramRun run = new ProgramRun();
        Event event = new Event("event1");
        EventAttribute attribute = new EventAttribute("attribute1", "java.lang.String", "value1");
        event.addAttribute(attribute);
        run.addEvent(event);

        String expected = "\"event1:::ENTER\"\nattribute1\"value1\"\n1\n";
        String observations = formatter.observations(run);
        assertEquals(expected, observations);
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("event1");
        EventAttribute attribute = new EventAttribute("attribute1", "java.lang.String", "value1");
        event.addAttribute(attribute);

        formatter.simplePoint(event, "10", out);

        String expected = "\"event1:::ENTER\"\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 10\n\nattribute1\n\"value1\"\n1\n";
        String result = out.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("event1");
        EventAttribute attribute = new EventAttribute("attribute1", "java.lang.String", "value1");
        event.addAttribute(attribute);

        formatter.point("event1", event, "11", out);

        String expected = "\"event1:::ENTER\"\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 11\n\nattribute1\n\"value1\"\n1\n";
        String result = out.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("event1");
        EventAttribute attribute = new EventAttribute("attribute1", "java.lang.String", "value1");
        event.addAttribute(attribute);

        formatter.declarations(event, "event1:::ENTER", out, "11");

        String expected = "\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 11\n";
        String result = out.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("event1");
        EventAttribute attribute = new EventAttribute("attribute1", "java.lang.String", "value1");
        event.addAttribute(attribute);

        formatter.attributes(event, out);

        String expected = "attribute1\n\"value1\"\n1\n";
        String result = out.toString();
        assertEquals(expected, result);
    }
}