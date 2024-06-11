package fi.vtt.noen.testgen.observations.formatter;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.parser.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertNotNull(formatter);
    }

    @Test(expected = RuntimeException.class)
    public void testConstructor_exception() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("fileName", false);
    }

    @Test
    public void testConstructor_withInputs() throws IOException {
        Collection<String> inputs = Arrays.asList("input1", "input2");
        DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
        assertNotNull(formatter);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "decl-version 2.0\n" +
                "var-comparability implicit\n";
        assertEquals(expected, formatter.header());
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
    public void testObservations_simple() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        run.addEvent(event1);
        String expected = "\nevent1:::ENTER\n";
        assertEquals(expected, formatter.observations(run));

        Event event2 = new Event("event2");
        event1.addAttribute(new EventAttribute("attribute1", "value1"));
        event1.addAttribute(new EventAttribute("attribute2", "value2"));
        run.addEvent(event2);
        expected += "\nevent2:::ENTER\n" +
                "attribute1\"value1\"\n" +
                "attribute2\"value2\"\n" +
                "1\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testObservations_notSimple() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", false);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        run.addEvent(event1);

        String expected = "\nevent1:::ENTER\n" +
                "  variable attribute1\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 11\n" +
                "  variable attribute2\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 11\n\n" +
                "event1:::ENTER\n";
        assertEquals(expected, formatter.observations(run));

        Event event2 = new Event("event2");
        event1.addAttribute(new EventAttribute("attribute1", "value1"));
        event1.addAttribute(new EventAttribute("attribute2", "value2"));
        run.addEvent(event2);
        expected += "event2:::ENTER\n" +
                "attribute1value1\n" +
                "attribute21\n";
        assertEquals(expected, formatter.observations(run));
    }

}