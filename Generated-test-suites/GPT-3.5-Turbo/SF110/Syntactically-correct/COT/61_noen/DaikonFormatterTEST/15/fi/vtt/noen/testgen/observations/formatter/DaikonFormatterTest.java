
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructorSimple() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertNotNull(formatter);
    }

    @Test
    public void testConstructor() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("test.txt", inputs);
        assertNotNull(formatter);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event", false);
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, comparability, out);
        String expected = "event:::ENTER\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event", false);
        String name = "eventName";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        formatter.point(name, event, comparability, out);
        String expected = "eventName:::ENTER\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event", false);
        String eventName = "eventName";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, comparability);
        String expected = "\nppt eventName\nppt-type enter";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event", false);
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        assertEquals("", out.toString());
    }

}
