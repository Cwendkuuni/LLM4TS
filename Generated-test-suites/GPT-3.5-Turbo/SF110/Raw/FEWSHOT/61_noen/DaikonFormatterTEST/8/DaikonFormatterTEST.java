package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.cli.BasicFormatter;
import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;

    @Before
    public void setup() throws IOException {
        daikonFormatter = new DaikonFormatter("fileName", false);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(daikonFormatter.declarations);
        assertFalse(daikonFormatter.simple);
        assertNotNull(daikonFormatter.inputs);
    }

    @Test
    public void testConstructorWithSimpleParameter() throws IOException {
        DaikonFormatter daikonFormatterSimple = new DaikonFormatter("fileName", true);

        assertNotNull(daikonFormatterSimple.declarations);
        assertTrue(daikonFormatterSimple.simple);
        assertNull(daikonFormatterSimple.inputs);
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorWithException() throws IOException {
        daikonFormatter = new DaikonFormatter("fileName", false);
    }

    @Test
    public void testClassForProperty() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("name1", "class1");
        configuration.setProperty("name2", "class2");

        assertEquals(Class.forName("class1"), DaikonFormatter.classForProperty("name1"));
        assertEquals(Class.forName("class2"), DaikonFormatter.classForProperty("name2"));
    }

    @Test
    public void testClassForPropertyWithNullClassName() throws Exception {
        assertEquals(null, DaikonFormatter.classForProperty("name1"));
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("InputInterface1", "class1");
        configuration.setProperty("InputInterface2", "class2");
        configuration.setProperty("InputInterface3", "class3");

        Collection<Class> expectedClasses = new ArrayList<>();
        expectedClasses.add(Class.forName("class1"));
        expectedClasses.add(Class.forName("class2"));
        expectedClasses.add(Class.forName("class3"));

        assertEquals(expectedClasses, DaikonFormatter.classesForMultipleProperties("InputInterface"));
    }

    @Test
    public void testClassesForMultiplePropertiesWithNoProperties() throws Exception {
        Collection<Class> expectedClasses = new ArrayList<>();

        assertEquals(expectedClasses, DaikonFormatter.classesForMultipleProperties("InputInterface"));
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> expectedClasses = new ArrayList<>();

        assertEquals(expectedClasses, DaikonFormatter.inputs());
    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> expectedClasses = new ArrayList<>();

        assertEquals(expectedClasses, DaikonFormatter.outputs());
    }

    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";

        assertEquals(expectedHeader, daikonFormatter.header());
    }

    @Test
    public void testFooter() {
        String expectedFooter = "";

        assertEquals(expectedFooter, daikonFormatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        String expectedFileNameExtension = "dtrace";

        assertEquals(expectedFileNameExtension, daikonFormatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        ProgramRun programRun = new ProgramRun();
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");
        Event event3 = new Event("Event3");

        EventAttribute eventAttribute1 = new EventAttribute("Attribute1", "value1");
        EventAttribute eventAttribute2 = new EventAttribute("Attribute2", "value2");

        event1.addAttribute(eventAttribute1);
        event2.addAttribute(eventAttribute2);
        event3.addAttribute(eventAttribute1);
        event3.addAttribute(eventAttribute2);

        programRun.addEvent(event1);
        programRun.addEvent(event2);
        programRun.addEvent(event3);

        String expectedObservations = "Event1:::ENTER\n" +
                "  variable Attribute1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value1\n" +
                "1\n" +
                "  variable Attribute2\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value2\n" +
                "1\n" +
                "\n" +
                "Event2:::ENTER\n" +
                "  variable Attribute2\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value2\n" +
                "1\n" +
                "\n" +
                "Event3:::ENTER\n" +
                "  variable Attribute1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value1\n" +
                "1\n" +
                "  variable Attribute2\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value2\n" +
                "1\n" +
                "\n";

        assertEquals(expectedObservations, daikonFormatter.observations(programRun));
    }

    @Test
    public void testSimplePoint() {
        Event event = new Event("Event");
        EventAttribute eventAttribute = new EventAttribute("Attribute", "value");

        event.addAttribute(eventAttribute);

        String expectedPoint = "Event:::ENTER\n" +
                "  variable Attribute\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 10\n" +
                "    value\n" +
                "1\n" +
                "\n";

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.simplePoint(event, "10", outputBuffer);

        assertEquals(expectedPoint, outputBuffer.toString());
    }

    @Test
    public void testSimplePointWithExitEvent() {
        Event event = new Event("Event");
        event.setExit(true);

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.simplePoint(event, "10", outputBuffer);

        String expectedPoint = "Event:::EXIT1\n" +
                "  variable Attribute\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 10\n" +
                "    value\n" +
                "1\n" +
                "\n";

        assertEquals(expectedPoint, outputBuffer.toString());
    }

    @Test
    public void testPoint() {
        Event event = new Event("Event");
        EventAttribute eventAttribute = new EventAttribute("Attribute", "value");

        event.addAttribute(eventAttribute);

        String expectedPoint = "Event:::ENTER\n" +
                "  variable Attribute\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "    value\n" +
                "1\n" +
                "\n";

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.point("Event", event, "11", outputBuffer);

        assertEquals(expectedPoint, outputBuffer.toString());
    }

    @Test
    public void testDeclarations() {
        Event event = new Event("Event");
        EventAttribute eventAttribute = new EventAttribute("Attribute", "value");

        event.addAttribute(eventAttribute);

        String expectedDeclarations = "\n" +
                "ppt Event:::ENTER\n" +
                "ppt-type enter\n" +
                "  variable Attribute\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 22\n" +
                "\n";

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.declarations(event, "Event", outputBuffer, "22");

        assertEquals(expectedDeclarations, outputBuffer.toString());
    }

    @Test
    public void testDeclarationsWhenEventIsAlreadyDeclared() {
        Event event = new Event("Event");
        EventAttribute eventAttribute = new EventAttribute("Attribute", "value");

        event.addAttribute(eventAttribute);

        daikonFormatter.declarations.add("Event");

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.declarations(event, "Event", outputBuffer, "22");

        assertEquals("", outputBuffer.toString());
    }

    @Test
    public void testAttributes() {
        Event event = new Event("Event");
        EventAttribute eventAttribute = new EventAttribute("Attribute", "value");

        event.addAttribute(eventAttribute);

        String expectedAttributes = "Attribute\n" +
                "value\n" +
                "1\n" +
                "\n";

        OutputBuffer outputBuffer = new OutputBuffer();

        daikonFormatter.attributes(event, outputBuffer);

        assertEquals(expectedAttributes, outputBuffer.toString());
    }
}