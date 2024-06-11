import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertNotNull(formatter);
    }
    
    @Test
    public void testConstructorWithInputs() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
        assertNotNull(formatter);
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "";
        assertEquals(expected, formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String comparability = "10";
        String expected = "\n\neventName:::ENTER\n";
        assertEquals(expected, formatter.simplePoint(event, comparability));
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String comparability = "11";
        String expected = "\n\neventName:::ENTER\n";
        assertEquals(expected, formatter.point(event, comparability));
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String eventName = "eventName";
        String comparability = "10";
        String expected = "\n\nppt eventName\nppt-type enter\n";
        assertEquals(expected, formatter.declarations(event, eventName, comparability));
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String expected = "eventName1";
        assertEquals(expected, formatter.attributes(event));
    }
}


@RunWith(JUnit4.class)
public class DaikonFormatterTest {

    @Test
    public void testConstructor() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        assertNotNull(formatter);
    }
    
    @Test
    public void testConstructorWithInputs() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
        assertNotNull(formatter);
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "";
        assertEquals(expected, formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String comparability = "10";
        String expected = "\n\neventName:::ENTER\n";
        assertEquals(expected, formatter.simplePoint(event, comparability));
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String comparability = "11";
        String expected = "\n\neventName:::ENTER\n";
        assertEquals(expected, formatter.point(event, comparability));
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String eventName = "eventName";
        String comparability = "10";
        String expected = "\n\nppt eventName\nppt-type enter\n";
        assertEquals(expected, formatter.declarations(event, eventName, comparability));
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event("eventName");
        String expected = "eventName1";
        assertEquals(expected, formatter.attributes(event));
    }
}

