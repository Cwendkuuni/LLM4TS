import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import fi.vtt.noen.testgen.model.*;
import java.util.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor1() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        
        assertEquals("testFile.txt", formatter.getFileName());
        assertEquals(true, formatter.isSimple());
        assertNull(formatter.getInputs());
        assertNotNull(formatter.getDeclarations());
    }

    @Test
    public void testConstructor2() throws IOException {
        Collection<String> inputs = new ArrayList<String>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", inputs);
        
        assertEquals("testFile.txt", formatter.getFileName());
        assertEquals(false, formatter.isSimple());
        assertNotNull(formatter.getInputs());
        assertNotNull(formatter.getDeclarations());
    }

    @Test
    public void testClassForProperty() throws Exception {
        String property = "testProperty";
        Class expectedClass = null;
        Class actualClass = DaikonFormatter.classForProperty(property);
        
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        String prefix = "testPrefix";
        Collection<Class> expectedClasses = new ArrayList<Class>();
        Collection<Class> actualClasses = DaikonFormatter.classesForMultipleProperties(prefix);
        
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> expectedInputs = new ArrayList<Class>();
        Collection<Class> actualInputs = DaikonFormatter.inputs();
        
        assertEquals(expectedInputs, actualInputs);
    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> expectedOutputs = new ArrayList<Class>();
        Collection<Class> actualOutputs = DaikonFormatter.outputs();
        
        assertEquals(expectedOutputs, actualOutputs);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        String actualHeader = formatter.header();
        
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        String expectedFooter = "";
        String actualFooter = formatter.footer();
        
        assertEquals(expectedFooter, actualFooter);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        String expectedExtension = "dtrace";
        String actualExtension = formatter.fileNameExtension();
        
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testObservations() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        ProgramRun run = new ProgramRun();
        String expectedObservations = "";
        String actualObservations = formatter.observations(run);
        
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        Event event = new Event("eventName");
        event.addAttribute(new EventAttribute("attributeName", "attributeValue", "attributeType"));
        String expectedPoint = "\neventName:::ENTER\n  variable attributeName\n    var-kind variable\n    dec-type attributeType\n    rep-type attributeType\n    comparability 10\n";
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        String actualPoint = out.toString();
        
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        Event event = new Event("eventName");
        event.addAttribute(new EventAttribute("attributeName", "attributeValue", "attributeType"));
        String expectedPoint = "\neventName:::ENTER\n  variable attributeName\n    var-kind variable\n    dec-type attributeType\n    rep-type attributeType\n    comparability 11\n";
        OutputBuffer out = new OutputBuffer();
        formatter.point("eventName", event, "11", out);
        String actualPoint = out.toString();
        
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        Event event = new Event("eventName");
        String eventName = "eventName";
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, "10");
        String expectedDeclarations = "\nppt eventName\nppt-type enter\n";
        String actualDeclarations = out.toString();
        
        assertEquals(expectedDeclarations, actualDeclarations);
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        Event event = new Event("eventName");
        event.addAttribute(new EventAttribute("attributeName", "attributeValue", "attributeType"));
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        String expectedAttributes = "attributeName\nattributeValue\n1\n";
        String actualAttributes = out.toString();
        
        assertEquals(expectedAttributes, actualAttributes);
    }
}