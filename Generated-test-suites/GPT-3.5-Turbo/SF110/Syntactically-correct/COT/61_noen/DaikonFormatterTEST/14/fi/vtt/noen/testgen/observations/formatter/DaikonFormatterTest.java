
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DaikonFormatterTest {

    @Test
    public void testConstructorSimple() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        assertNull(formatter.inputs);
        assertEquals(false, formatter.simple);
    }

    @Test
    public void testConstructor() throws Exception {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("filename", inputs);
        assertEquals(inputs, formatter.inputs);
        assertEquals(false, formatter.simple);
    }

    @Test
    public void testClassForProperty() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("property1", "className");
        Class expectedClass = Class.forName("className");
        Class actualClass = DaikonFormatter.classForProperty("property1");
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testClassForPropertyWithNullClassName() throws Exception {
        Properties configuration = new Properties();
        Class actualClass = DaikonFormatter.classForProperty("property1");
        assertNull(actualClass);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("prefix1", "className1");
        configuration.setProperty("prefix2", "className2");
        Collection<Class> expectedClasses = new HashSet<>();
        expectedClasses.add(Class.forName("className1"));
        expectedClasses.add(Class.forName("className2"));
        Collection<Class> actualClasses = DaikonFormatter.classesForMultipleProperties("prefix");
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testClassesForMultiplePropertiesWithMissingClassName() throws Exception {
        Properties configuration = new Properties();
        Collection<Class> actualClasses = DaikonFormatter.classesForMultipleProperties("prefix");
        Collection<Class> expectedClasses = new ArrayList<>();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testInputs() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("InputInterface1", "className1");
        configuration.setProperty("InputInterface2", "className2");
        Collection<Class> expectedClasses = new HashSet<>();
        expectedClasses.add(Class.forName("className1"));
        expectedClasses.add(Class.forName("className2"));
        Collection<Class> actualClasses = DaikonFormatter.inputs();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testInputsEmpty() throws Exception {
        Collection<Class> actualClasses = DaikonFormatter.inputs();
        Collection<Class> expectedClasses = new ArrayList<>();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testOutputs() throws Exception {
        Properties configuration = new Properties();
        configuration.setProperty("OutputInterface1", "className1");
        configuration.setProperty("OutputInterface2", "className2");
        Collection<Class> expectedClasses = new HashSet<>();
        expectedClasses.add(Class.forName("className1"));
        expectedClasses.add(Class.forName("className2"));
        Collection<Class> actualClasses = DaikonFormatter.outputs();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testOutputsEmpty() throws Exception {
        Collection<Class> actualClasses = DaikonFormatter.outputs();
        Collection<Class> expectedClasses = new ArrayList<>();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        String actualHeader = formatter.header();
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String expectedFooter = "";
        String actualFooter = formatter.footer();
        assertEquals(expectedFooter, actualFooter);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String expectedFileNameExtension = "dtrace";
        String actualFileNameExtension = formatter.fileNameExtension();
        assertEquals(expectedFileNameExtension, actualFileNameExtension);
    }

    @Test
    public void testObservations() {
        ProgramRun programRun = new ProgramRun();
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String expectedObservations = "";
        String actualObservations = formatter.observations(programRun);
        assertEquals(expectedObservations, actualObservations);
    }
    
    @Test
    public void testSimplePoint() {
        Event event = new Event();
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        formatter.simplePoint(event, comparability, out);
        String expectedOutput = "ppt eventType:::ENTER\n" +
                "ppt-type enter\n" +
                "  variable eventType\n" +
                "    var-kind variable\n" +
                "    dec-type eventType\n" +
                "    rep-type eventType\n" +
                "    comparability 10\n" +
                "\"null\"1";
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testPoint() {
        Event event = new Event();
        String name = "event";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        formatter.point(name, event, comparability, out);
        String expectedOutput = "ppt event:::ENTER\n" +
                "ppt-type enter\n" +
                "  variable event\n" +
                "    var-kind variable\n" +
                "    dec-type event\n" +
                "    rep-type event\n" +
                "    comparability 11\n" +
                "\"null\"1";
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testDeclarations() {
        Event event = new Event();
        String eventName = "event";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        formatter.declarations(event, eventName, out, comparability);
        String expectedOutput = "\nppt event\n" +
                "ppt-type enter\n" +
                "  variable eventType\n" +
                "    var-kind variable\n" +
                "    dec-type eventType\n" +
                "    rep-type eventType\n" +
                "    comparability 11";
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAttributes() {
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        formatter.attributes(event, out);
        String expectedOutput = "eventType\n" +
                "\"null\"1";
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}
