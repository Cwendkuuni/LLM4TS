
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstructor_withFileNameAndSimpleTrue_shouldCreateObject() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        assertNotNull(formatter);
        assertEquals(new HashSet<String>(), formatter.declarations);
        assertTrue(formatter.simple);
        assertNull(formatter.inputs);
    }

    @Test
    public void testDaikonFormatterConstructor_withFileNameAndSimpleFalse_shouldCreateObject() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", false);
        assertNotNull(formatter);
        assertEquals(new HashSet<String>(), formatter.declarations);
        assertFalse(formatter.simple);
        assertEquals(new HashSet<String>(), formatter.inputs);
    }

    @Test
    public void testDaikonFormatterConstructor_withFileNameAndInputs_shouldCreateObject() throws IOException {
        Collection<String> inputs = new HashSet<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", inputs);
        assertNotNull(formatter);
        assertEquals(new HashSet<String>(), formatter.declarations);
        assertFalse(formatter.simple);
        assertEquals(inputs, formatter.inputs);
    }

    @Test
    public void testHeader_returnHeaderString() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testFooter_returnEmptyString() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension_returnDtrace() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations_withSimpleTrue_returnObservationsString() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        Event event2 = new Event("event2");
        run.addEvent(event1);
        run.addEvent(event2);
        String expected = "event1:::ENTER\n\nevent1\n\n";
        expected += "event2:::ENTER\n\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testObservations_withSimpleFalse_returnObservationsString() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", false);
        Collection<String> inputs = new HashSet<>();
        inputs.add("input1");
        inputs.add("input2");
        formatter.inputs = inputs;

        ProgramRun run = new ProgramRun();
        Event input1 = new Event("input1");
        Event input2 = new Event("input2");
        Event event1 = new Event("event1");
        Event event2 = new Event("event2");
        input1.setExit(true);
        event2.setExit(true);
        run.addEvent(input1);
        run.addEvent(input2);
        run.addEvent(event1);
        run.addEvent(event2);

        String expected = "input1:::EXIT1\n\n";
        expected += "input1:::ENTER\n\n";
        expected += "input2:::ENTER\n\n";
        expected += "event1,input1:::ENTER\n\n";
        expected += "event2,input2:::ENTER\n\n";
        expected += "event2:::EXIT1\n\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testSimplePoint_withEventComparabilityAndOut_expectedSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expected = "event1:::ENTER\nvariable event1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 10\n";
        formatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPoint_withNameEventComparabilityAndOut_expectedPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", false);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expected = "event1:::ENTER\nvariable event1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\n";
        formatter.point("event1", event, "11", out);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testDeclarations_withEventEventNameOutAndComparability_shouldAppendDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", false);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expected = "\nppt event1\nppt-type enter\n  variable test\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\n";
        formatter.declarations(event, "event1", out, "11");
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributes_withEventAndOut_shouldAppendAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", false);
        Event event = new Event("event1");
        event.addAttribute(new EventAttribute("test", "java.lang.String", "value"));
        OutputBuffer out = new OutputBuffer();
        String expected = "test\n\"value\"\n1\n";
        formatter.attributes(event, out);
        assertEquals(expected, out.toString());
    }
}

