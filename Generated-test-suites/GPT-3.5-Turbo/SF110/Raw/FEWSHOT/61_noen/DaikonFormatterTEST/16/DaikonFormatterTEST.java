package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterConstructorSimple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        } catch (IOException e) {
            fail("Constructor threw an IOException");
        }
    }

    @Test
    public void testDaikonFormatterConstructor() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("input1");
            inputs.add("input2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
        } catch (IOException e) {
            fail("Constructor threw an IOException");
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        Event event1 = new Event();
        event1.setName("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        event1.addAttribute(attribute1);
        run.addEvent(event1);

        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        expected += "event1:::ENTER\n";
        expected += "  variable attribute1\n";
        expected += "    var-kind variable\n";
        expected += "    dec-type int\n";
        expected += "    rep-type int\n";
        expected += "    comparability 10\n";
        expected += "attribute1\n";
        expected += "10\n";
        expected += "1\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testSimplePoint() {
        Event event = new Event();
        event.setName("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        event.addAttribute(attribute1);

        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        expected += "event1:::ENTER\n";
        expected += "  variable attribute1\n";
        expected += "    var-kind variable\n";
        expected += "    dec-type int\n";
        expected += "    rep-type int\n";
        expected += "    comparability 10\n";
        expected += "\n";
        expected += "event1:::ENTER\n";
        expected += "attribute1\n";
        expected += "10\n";
        expected += "1\n";
        assertEquals(expected, formatter.simplePoint(event, "10", out));
    }

    @Test
    public void testPoint() {
        Event event = new Event();
        event.setName("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        event.addAttribute(attribute1);

        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        expected += "event1:::ENTER\n";
        expected += "  variable attribute1\n";
        expected += "    var-kind variable\n";
        expected += "    dec-type int\n";
        expected += "    rep-type int\n";
        expected += "    comparability 10\n";
        expected += "\n";
        expected += "event1:::ENTER\n";
        expected += "attribute1\n";
        expected += "10\n";
        expected += "1\n";
        assertEquals(expected, formatter.point("event1", event, "10", out));
    }

    @Test
    public void testDeclarations() {
        Event event = new Event();
        event.setName("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        EventAttribute attribute2 = new EventAttribute("attribute2", "String", "\"test\"");
        event.addAttribute(attribute1);
        event.addAttribute(attribute2);

        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        expected += "\n";
        expected += "ppt event1\n";
        expected += "ppt-type enter\n";
        expected += "\n";
        expected += "  variable attribute1\n";
        expected += "    var-kind variable\n";
        expected += "    dec-type int\n";
        expected += "    rep-type int\n";
        expected += "    comparability 10\n";
        expected += "  variable attribute2\n";
        expected += "    var-kind variable\n";
        expected += "    dec-type String\n";
        expected += "    rep-type String\n";
        expected += "    comparability \"test\"\n";
        assertEquals(expected, formatter.declarations(event, "event1", out, "10"));
    }

    @Test
    public void testAttributes() {
        Event event = new Event();
        event.setName("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        EventAttribute attribute2 = new EventAttribute("attribute2", "String", "\"test\"");
        event.addAttribute(attribute1);
        event.addAttribute(attribute2);

        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        expected += "attribute1\n";
        expected += "10\n";
        expected += "1\n";
        expected += "attribute2\n";
        expected += "\"test\"\n";
        expected += "1\n";
        assertEquals(expected, formatter.attributes(event, out));
    }
}