
import org.junit.Test;

import static org.junit.Assert.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor_simple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
            assertNotNull(formatter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testConstructor_inputs() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("file.txt", new ArrayList<String>());
            assertNotNull(formatter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassForProperty_existingClass() {
        try {
            Class<?> clazz = DaikonFormatter.classForProperty("property");
            assertNotNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassForProperty_nonExistingClass() {
        try {
            Class<?> clazz = DaikonFormatter.classForProperty("nonExistingProperty");
            assertNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("prefix");
            assertNotNull(classes);
            assertFalse(classes.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testInputs() {
        try {
            Collection<Class<?>> classes = DaikonFormatter.inputs();
            assertNotNull(classes);
            assertFalse(classes.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testOutputs() {
        try {
            Collection<Class<?>> classes = DaikonFormatter.outputs();
            assertNotNull(classes);
            assertFalse(classes.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit", header);
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        String footer = formatter.footer();
        assertEquals("", footer);
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        String extension = formatter.fileNameExtension();
        assertEquals("dtrace", extension);
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        ProgramRun run = new ProgramRun();
        String observations = formatter.observations(run);
        assertNotNull(observations);
        assertFalse(observations.isEmpty());
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        String result = out.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        formatter.point("name", event, "11", out);
        String result = out.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "eventName", out, "11");
        String result = out.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        String result = out.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}
