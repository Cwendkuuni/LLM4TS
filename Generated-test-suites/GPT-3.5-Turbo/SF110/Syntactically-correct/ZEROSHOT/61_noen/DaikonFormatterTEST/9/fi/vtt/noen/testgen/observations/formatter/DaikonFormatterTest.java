import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DaikonFormatterTest {
    
    @Test
    public void testConstructor1() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("fileName", true);
            assertNotNull(formatter);
            assertEquals(true, formatter.simple);
            assertNull(formatter.inputs);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor2() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("fileName", null);
            assertNotNull(formatter);
            assertEquals(false, formatter.simple);
            assertNull(formatter.inputs);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor3() {
        try {
            Collection<String> inputs = new ArrayList<String>();
            inputs.add("input1");
            inputs.add("input2");
            DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
            assertNotNull(formatter);
            assertEquals(false, formatter.simple);
            assertEquals(inputs, formatter.inputs);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testClassForProperty() {
        try {
            Class<?> clazz = DaikonFormatter.classForProperty("property");
            assertNull(clazz);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("prefix");
            assertNotNull(classes);
            assertEquals(0, classes.size());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testInputs() {
        try {
            Collection<Class<?>> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
            assertEquals(0, inputs.size());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testOutputs() {
        try {
            Collection<Class<?>> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
            assertEquals(0, outputs.size());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String footer = formatter.footer();
        assertEquals("", footer);
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String extension = formatter.fileNameExtension();
        assertEquals("dtrace", extension);
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        String observations = formatter.observations(run);
        assertEquals("", observations);
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        assertEquals("event:::ENTER\n", out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.point("name", event, "11", out);
        assertEquals("name:::ENTER\n", out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "eventName", out, "11");
        assertEquals("\nppt eventName\nppt-type enter\n", out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        assertEquals("event\n1\n", out.toString());
    }
}