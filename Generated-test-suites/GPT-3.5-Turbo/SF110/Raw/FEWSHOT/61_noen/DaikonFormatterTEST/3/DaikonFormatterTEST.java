package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;

    @Before
    public void setUp() {
        formatter = new DaikonFormatter("filename.dtrace", true);
    }

    @Test
    public void testConstructor() {
        assertEquals("filename.dtrace", formatter.getFileName());
        assertTrue(formatter.isSimple());
        assertNotNull(formatter.getDeclarations());
        assertTrue(formatter.getDeclarations().isEmpty());
        assertNull(formatter.getInputs());
    }

    @Test
    public void testConstructorWithInputs() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        formatter = new DaikonFormatter("filename.dtrace", inputs);

        assertEquals("filename.dtrace", formatter.getFileName());
        assertFalse(formatter.isSimple());
        assertNotNull(formatter.getDeclarations());
        assertTrue(formatter.getDeclarations().isEmpty());
        assertNotNull(formatter.getInputs());
        assertEquals(2, formatter.getInputs().size());
        assertTrue(formatter.getInputs().contains("Input1"));
        assertTrue(formatter.getInputs().contains("Input2"));
    }

    @Test
    public void testClassForProperty() {
        try {
            Class clazz = DaikonFormatter.classForProperty("property1");

            assertNull(clazz);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("prefix");

            assertNotNull(classes);
            assertTrue(classes.isEmpty());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();

            assertNotNull(inputs);
            assertTrue(inputs.isEmpty());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();

            assertNotNull(outputs);
            assertTrue(outputs.isEmpty());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\n" +
                                "var-comparability implicit\n";

        String header = formatter.header();

        assertEquals(expectedHeader, header);
    }

    @Test
    public void testFooter() {
        String footer = formatter.footer();

        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        String fileNameExtension = formatter.fileNameExtension();

        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservations() {
        ProgramRun run = generateProgramRun();

        String expectedObservations = "Input1:::ENTER\n" +
                                      "  variable Attribute1\n" +
                                      "    var-kind variable\n" +
                                      "    dec-type java.lang.String\n" +
                                      "    rep-type java.lang.String\n" +
                                      "    comparability 11\n" +
                                      "Attribute1Value1\n" +
                                      "1\n" +
                                      "Output1:::EXIT1\n" +
                                      "  variable Attribute2\n" +
                                      "    var-kind variable\n" +
                                      "    dec-type java.lang.String\n" +
                                      "    rep-type java.lang.String\n" +
                                      "    comparability 11\n" +
                                      "Attribute2Value1\n" +
                                      "1\n";

        String observations = formatter.observations(run);

        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testSimplePoint() {
        Event event = generateEvent("Input1", "Attribute1", "Attribute1Value1");

        String expectedPoint = "Input1:::ENTER\n" +
                               "  variable Attribute1\n" +
                               "    var-kind variable\n" +
                               "    dec-type java.lang.String\n" +
                               "    rep-type java.lang.String\n" +
                               "    comparability 10\n" +
                               "Attribute1Value1\n" +
                               "1\n";

        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);

        assertEquals(expectedPoint, out.toString());
    }

    @Test
    public void testPoint() {
        Event event = generateEvent("Input1", "Attribute1", "Attribute1Value1");
        Event previousEvent = generateEvent("Input2", "Attribute2", "Attribute2Value1");

        String expectedPoint = "Input2,Input1:::ENTER\n" +
                               "  variable Attribute1\n" +
                               "    var-kind variable\n" +
                               "    dec-type java.lang.String\n" +
                               "    rep-type java.lang.String\n" +
                               "    comparability 22\n" +
                               "Attribute1Value1\n" +
                               "1\n";

        OutputBuffer out = new OutputBuffer();
        formatter.point("Input2,Input1", previousEvent, "22", out);

        assertEquals(expectedPoint, out.toString());
    }

    @Test
    public void testDeclarations() {
        Event event = generateEvent("Input1", "Attribute1", "Attribute1Value1");
        String eventName = "Input1:::ENTER";

        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, "11");

        String expectedDeclarations = "ppt Input1:::ENTER\n" +
                                      "ppt-type enter\n" +
                                      "  variable Attribute1\n" +
                                      "    var-kind variable\n" +
                                      "    dec-type java.lang.String\n" +
                                      "    rep-type java.lang.String\n" +
                                      "    comparability 11\n";

        assertEquals(expectedDeclarations, out.toString());
    }

    @Test
    public void testAttributes() {
        Event event = generateEvent("Output1", "Attribute2", "Attribute2Value1");

        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);

        String expectedAttributes = "Attribute2\n" +
                                    "Attribute2Value1\n" +
                                    "1\n";

        assertEquals(expectedAttributes, out.toString());
    }
    
    private ProgramRun generateProgramRun() {
        ProgramRun run = new ProgramRun();
        Event enterEvent = generateEvent("Input1", "Attribute1", "Attribute1Value1");
        Event exitEvent = generateEvent("Output1", "Attribute2", "Attribute2Value1");
        enterEvent.setExit(false);
        exitEvent.setExit(true);
        run.addEvent(enterEvent);
        run.addEvent(exitEvent);
        return run;
    }

    private Event generateEvent(String name, String attributeName, String attributeValue) {
        Event event = new Event();
        event.setName(name);
        EventAttribute attribute = new EventAttribute(attributeName, attributeValue, "java.lang.String");
        event.addAttribute(attribute);
        return event;
    }
}