import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterSimpleConstructor() {
        try {
            DaikonFormatter df = new DaikonFormatter("fileName", true);
            assertNotNull(df);
            assertTrue(df instanceof DaikonFormatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testDaikonFormatterCollectionConstructor() {
        try {
            Collection<String> inputs = new HashSet<String>();
            inputs.add("input1");
            inputs.add("input2");
            DaikonFormatter df = new DaikonFormatter("fileName", inputs);
            assertNotNull(df);
            assertTrue(df instanceof DaikonFormatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
            assertFalse(inputs.isEmpty());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
            assertFalse(outputs.isEmpty());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        String header = df.header();
        assertNotNull(header);
        assertFalse(header.isEmpty());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        String footer = df.footer();
        assertNotNull(footer);
        assertTrue(footer.isEmpty());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        String extension = df.fileNameExtension();
        assertNotNull(extension);
        assertFalse(extension.isEmpty());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        String observations = df.observations(run);
        assertNotNull(observations);
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName", false);
        OutputBuffer out = new OutputBuffer();
        df.simplePoint(event, "10", out);
        assertNotNull(out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName", false);
        OutputBuffer out = new OutputBuffer();
        df.point("name", event, "11", out);
        assertNotNull(out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName", false);
        OutputBuffer out = new OutputBuffer();
        df.declarations(event, "eventName", out, "10");
        assertNotNull(out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter df = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName", false);
        OutputBuffer out = new OutputBuffer();
        df.attributes(event, out);
        assertNotNull(out.toString());
    }
}