package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.Const;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        DaikonFormatter formatter1 = new DaikonFormatter("testFile", true);
        assertNull(formatter1.declarations);
        assertEquals(false, formatter1.simple);

        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");

        DaikonFormatter formatter2 = new DaikonFormatter("testFile", inputs);
        assertNull(formatter2.declarations);
        assertEquals(false, formatter2.simple);
        assertEquals(inputs, formatter2.inputs);
    }

    @Test
    public void testClassForProperty() throws Exception {
        FileInputStream fis = new FileInputStream("testgen.properties");
        Properties configuration = new Properties();
        configuration.load(fis);

        Class expected = Class.forName(configuration.getProperty("InputInterface1"));
        Class actual = DaikonFormatter.classForProperty("InputInterface1");
        assertEquals(expected, actual);

        Class expectedNull = null;
        Class actualNull = DaikonFormatter.classForProperty("InvalidProperty");
        assertEquals(expectedNull, actualNull);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Collection<Class> expected = new ArrayList<>();
        expected.add(Class.forName("Class1"));
        expected.add(Class.forName("Class2"));
        Collection<Class> actual = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(expected, actual);

        Collection<Class> expectedEmpty = new ArrayList<>();
        Collection<Class> actualEmpty = DaikonFormatter.classesForMultipleProperties("InvalidProperty");
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> expected = new ArrayList<>();
        expected.add(Class.forName("Class1"));
        expected.add(Class.forName("Class2"));
        Collection<Class> actual = DaikonFormatter.inputs();
        assertEquals(expected, actual);

    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> expected = new ArrayList<>();
        expected.add(Class.forName("Class1"));
        expected.add(Class.forName("Class2"));
        Collection<Class> actual = DaikonFormatter.outputs();
        assertEquals(expected, actual);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit\n";
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
        run.addEvent(new Event());
        String expected = "";
        String actual = formatter.observations(run);
        assertEquals(expected, actual);
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event();
        event.setName("Event1");
        String comparability = "10";
        String expected = "";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("Event1:::ENTER");
        formatter.declarations(event, "Event1:::ENTER", expectedOut, comparability);
        expectedOut.append("");
        expectedOut.append("Event1:::ENTER");
        formatter.attributes(event, expectedOut);

        OutputBuffer actualOut = new OutputBuffer();
        formatter.simplePoint(event, comparability, actualOut);
        
        String expectedStr = expectedOut.toString();
        String actualStr = actualOut.toString();
        assertEquals(expectedStr, actualStr);
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event();
        event.setName("Event1");
        String name = "Event1,Event2";
        String comparability = "11";
        String expected = "";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("Event1,Event2:::ENTER");
        formatter.declarations(event, "Event1,Event2:::ENTER", expectedOut, comparability);
        expectedOut.append("");
        expectedOut.append("Event1,Event2:::ENTER");
        formatter.attributes(event, expectedOut);

        OutputBuffer actualOut = new OutputBuffer();
        formatter.point(name, event, comparability, actualOut);
        
        String expectedStr = expectedOut.toString();
        String actualStr = actualOut.toString();
        assertEquals(expectedStr, actualStr);
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event();
        event.setName("Event1");
        String eventName = "Event1:::ENTER";
        String comparability = "11";
        String expected = "";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("ppt Event1:::ENTER");
        expectedOut.append("ppt-type enter");

        OutputBuffer actualOut = new OutputBuffer();
        formatter.declarations(event, eventName, actualOut, comparability);
        
        String expectedStr = expectedOut.toString();
        String actualStr = actualOut.toString();
        assertEquals(expectedStr, actualStr);
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event();
        event.setName("Event1");
        event.addAttribute(new EventAttribute("Attr1", "Value1", "Type1"));
        String expected = "";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("Attr1");
        expectedOut.append("Value1");
        expectedOut.append("1");

        OutputBuffer actualOut = new OutputBuffer();
        formatter.attributes(event, actualOut);
        
        String expectedStr = expectedOut.toString();
        String actualStr = actualOut.toString();
        assertEquals(expectedStr, actualStr);
    }
}