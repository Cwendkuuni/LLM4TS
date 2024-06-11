import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterSimpleConstructor() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        assertNotNull(formatter);
    }

    @Test
    public void testDaikonFormatterComplexConstructor() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("filename", inputs);
        assertNotNull(formatter);
    }

    @Test
    public void testClassForProperty() throws Exception {
        String property = "property";
        Class clazz = DaikonFormatter.classForProperty(property);
        assertNull(clazz);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        String prefix = "prefix";
        Collection<Class> classes = DaikonFormatter.classesForMultipleProperties(prefix);
        assertNotNull(classes);
        assertEquals(0, classes.size());
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> classes = DaikonFormatter.inputs();
        assertNotNull(classes);
        assertEquals(0, classes.size());
    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> classes = DaikonFormatter.outputs();
        assertNotNull(classes);
        assertEquals(0, classes.size());
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String header = formatter.header();
        assertNotNull(header);
        assertTrue(header.contains("decl-version 2.0"));
        assertTrue(header.contains("var-comparability implicit"));
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String footer = formatter.footer();
        assertNotNull(footer);
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String extension = formatter.fileNameExtension();
        assertNotNull(extension);
        assertEquals("dtrace", extension);
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        ProgramRun run = new ProgramRun();
        String observations = formatter.observations(run);
        assertNotNull(observations);
        assertEquals("", observations);
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        Event event = new Event("event");
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, comparability, out);
        assertNotNull(out);
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        Event event = new Event("event");
        String name = "event";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        formatter.point(name, event, comparability, out);
        assertNotNull(out);
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        Event event = new Event("event");
        String eventName = "event";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, comparability);
        assertNotNull(out);
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        Event event = new Event("event");
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        assertNotNull(out);
    }
}