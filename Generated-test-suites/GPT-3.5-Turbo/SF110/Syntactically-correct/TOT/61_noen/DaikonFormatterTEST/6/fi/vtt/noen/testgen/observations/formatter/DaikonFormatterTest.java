import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimpleTrue() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testConstructorWithSimpleFalse() {
        Collection<String> inputs = new ArrayList<String>();
        inputs.add("InputInterface1");
        inputs.add("InputInterface2");

        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
        assertEquals(expectedHeader, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expectedFooter = "";
        assertEquals(expectedFooter, formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expectedExtension = "dtrace";
        assertEquals(expectedExtension, formatter.fileNameExtension());
    }

    @Test
    public void testObservationsWithSimpleTrue() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
            ProgramRun programRun = new ProgramRun();

            // Adding events for observation
            Event event1 = new Event("Event1");
            EventAttribute attribute1 = new EventAttribute("Attribute1", "int", "10");
            event1.addAttribute(attribute1);
            programRun.addEvent(event1);

            Event event2 = new Event("Event2");
            EventAttribute attribute2 = new EventAttribute("Attribute2", "String", "Hello");
            event2.addAttribute(attribute2);
            programRun.addEvent(event2);

            String expectedObservations = "Attribute1\n10\n1\n\nAttribute2\nHello\n1\n";
            assertEquals(expectedObservations, formatter.observations(programRun));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testObservationsWithSimpleFalse() {
        Collection<String> inputs = new ArrayList<String>();
        inputs.add("InputInterface1");
        inputs.add("InputInterface2");

        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
            ProgramRun programRun = new ProgramRun();

            // Adding events for observation
            Event event1 = new Event("InputInterface1");
            EventAttribute attribute1 = new EventAttribute("Attribute1", "int", "10");
            event1.addAttribute(attribute1);
            programRun.addEvent(event1);

            Event event2 = new Event("InputInterface2");
            EventAttribute attribute2 = new EventAttribute("Attribute2", "String", "Hello");
            event2.addAttribute(attribute2);
            programRun.addEvent(event2);

            String expectedObservations = "InputInterface1\nAttribute1\n10\n1\n\nInputInterface2\nAttribute2\nHello\n1\n";
            assertEquals(expectedObservations, formatter.observations(programRun));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("Event1");
        EventAttribute attribute = new EventAttribute("Attribute1", "int", "10");
        event.addAttribute(attribute);
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "Event1:::ENTER\nppt Event1:::ENTER\nppt-type enter\n  variable Attribute1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\n\nEvent1:::ENTER\nAttribute1\n10\n1\n";
        formatter.simplePoint(event, "10", out);
        assertEquals(expectedPoint, out.toString());
    }

    @Test
    public void testPoint() {
        Collection<String> inputs = new ArrayList<String>();
        inputs.add("InputInterface1");
        inputs.add("InputInterface2");

        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
            Event event = new Event("Event1");
            EventAttribute attribute = new EventAttribute("Attribute1", "int", "10");
            event.addAttribute(attribute);
            OutputBuffer out = new OutputBuffer();
            String expectedPoint = "Event1:::ENTER\nppt Event1:::ENTER\nppt-type enter\n  variable Attribute1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\n\nEvent1:::ENTER\nAttribute1\n10\n1\n";
            formatter.point("Event1", event, "10", out);
            assertEquals(expectedPoint, out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("Event1");
        EventAttribute attribute = new EventAttribute("Attribute1", "int", "10");
        event.addAttribute(attribute);
        OutputBuffer out = new OutputBuffer();
        String expectedDeclarations = "\nppt Event1\nppt-type enter\n  variable Attribute1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\n";
        formatter.declarations(event, "Event1", out, "10");
        assertEquals(expectedDeclarations, out.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("Event1");
        EventAttribute attribute = new EventAttribute("Attribute1", "int", "10");
        event.addAttribute(attribute);
        OutputBuffer out = new OutputBuffer();
        String expectedAttributes = "Attribute1\n10\n1\n";
        formatter.attributes(event, out);
        assertEquals(expectedAttributes, out.toString());
    }

    @Test
    public void testClassForPropertyWithNull() {
        try {
            Class result = DaikonFormatter.classForProperty("InvalidProperty");
            assertNull(result);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
            assertNotNull(classes);
            assertTrue(classes.isEmpty());

            Collection<Class> outputs = DaikonFormatter.classesForMultipleProperties("OutputInterface");
            assertNotNull(outputs);
            assertTrue(outputs.isEmpty());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
            assertTrue(inputs.isEmpty());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
            assertTrue(outputs.isEmpty());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

}