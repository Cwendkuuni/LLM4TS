package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.vtt.noen.testgen.model.ArrayAttribute;
import fi.vtt.noen.testgen.model.Class;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.BasicFormatter;
import fi.vtt.noen.testgen.parser.Const;

public class DaikonFormatterTest {

    // Test case for the constructor DaikonFormatter(String, boolean)
    @Test
    public void testDaikonFormatterStringBoolean() {
        try {
            DaikonFormatter df = new DaikonFormatter("testFile.txt", true);
            assertNotNull(df);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test case for the constructor DaikonFormatter(String, Collection<String>)
    @Test
    public void testDaikonFormatterStringCollectionOfString() {
        try {
            List<String> inputs = new ArrayList<String>();
            inputs.add("Input1");
            inputs.add("Input2");

            DaikonFormatter df = new DaikonFormatter("testFile.txt", inputs);
            assertNotNull(df);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test case for the method inputs()
    @Test
    public void testInputs() {
        try {
            Collection<Class> result = DaikonFormatter.inputs();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    // Test case for the method outputs()
    @Test
    public void testOutputs() {
        try {
            Collection<Class> result = DaikonFormatter.outputs();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    // Test case for the method header()
    @Test
    public void testHeader() {
        DaikonFormatter df = new DaikonFormatter("testFile.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String result = df.header();
        assertEquals(expected, result);
    }

    // Test case for the method footer()
    @Test
    public void testFooter() {
        DaikonFormatter df = new DaikonFormatter("testFile.txt", true);
        String expected = "";
        String result = df.footer();
        assertEquals(expected, result);
    }

    // Test case for the method fileNameExtension()
    @Test
    public void testFileNameExtension() {
        DaikonFormatter df = new DaikonFormatter("testFile.txt", true);
        String expected = "dtrace";
        String result = df.fileNameExtension();
        assertEquals(expected, result);
    }

    // Test case for the method observations(ProgramRun)
    @Test
    public void testObservations() {
        DaikonFormatter df = new DaikonFormatter("testFile.txt", true);
        ProgramRun run = new ProgramRun();

        // Test case for simple observations
        String expectedSimple = "";
        String resultSimple = df.observations(run);
        assertEquals(expectedSimple, resultSimple);

        // Test case for complex observations
        df = new DaikonFormatter("testFile.txt", false);

        // Add events to program run
        Event event1 = new Event();
        event1.setName("Event1");
        EventAttribute attr1 = new EventAttribute("Attr1", "int", "1");
        EventAttribute attr2 = new EventAttribute("Attr2", "String", "value");
        event1.addAttribute(attr1);
        event1.addAttribute(attr2);
        run.addEvent(event1);

        Event event2 = new Event();
        event2.setName("Event2");
        EventAttribute attr3 = new EventAttribute("Attr3", "int", "2");
        EventAttribute attr4 = new EventAttribute("Attr4", "String", "value2");
        event2.addAttribute(attr3);
        event2.addAttribute(attr4);
        run.addEvent(event2);

        Event event3 = new Event();
        event3.setName("Event3");
        EventAttribute attr5 = new EventAttribute("Attr5", "int", "3");
        EventAttribute attr6 = new EventAttribute("Attr6", "String", "value3");
        event3.addAttribute(attr5);
        event3.addAttribute(attr6);
        run.addEvent(event3);

        // Expected output
        String expectedComplex = "\n%ppt Event1:::ENTER\n  variable Attr1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable Attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11\n%ppt Event2:::ENTER\n  variable Attr3\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable Attr4\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11\n%ppt Event3:::ENTER\n  variable Attr5\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable Attr6\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11";
        String resultComplex = df.observations(run);
        assertEquals(expectedComplex, resultComplex);
    }

    // Helper method to test private methods
    @Test
    public void testPrivateMethods() {
        DaikonFormatter df = new DaikonFormatter("testFile.txt", true);

        // Test case for simplePoint()
        Event event = new Event();
        event.setName("Event");
        EventAttribute attr1 = new EventAttribute("Attr1", "int", "1");
        EventAttribute attr2 = new EventAttribute("Attr2", "String", "value");
        event.addAttribute(attr1);
        event.addAttribute(attr2);
        String expectedSimplePoint = "\n%ppt Event:::ENTER\n  variable Attr1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\n  variable Attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10\nEvent:::ENTER";
        OutputBuffer out = new OutputBuffer();
        df.simplePoint(event, "10", out);
        String resultSimplePoint = out.toString();
        assertEquals(expectedSimplePoint, resultSimplePoint);

        // Test case for point()
        String name = "Event";
        String expectedPoint = "\n%ppt Event:::ENTER\n  variable Attr1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable Attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11\nEvent:::ENTER";
        out = new OutputBuffer();
        df.point(name, event, "11", out);
        String resultPoint = out.toString();
        assertEquals(expectedPoint, resultPoint);

        // Test case for declarations()
        out = new OutputBuffer();
        df.declarations(event, "Event", out, "22");
        String expectedDeclarations = "\n\nppt Event\nppt-type enter\n  variable Attr1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 22\n  variable Attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 22";
        String resultDeclarations = out.toString();
        assertEquals(expectedDeclarations, resultDeclarations);

        // Test case for attributes()
        out = new OutputBuffer();
        df.attributes(event, out);
        String expectedAttributes = "\nAttr1\n1\nAttr2\n\"value\"";
        String resultAttributes = out.toString();
        assertEquals(expectedAttributes, resultAttributes);
    }

}