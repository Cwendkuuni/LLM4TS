import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "";
        assertEquals(expected, formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
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
        Event event = new Event("Event1", false);
        String expected = "\nEvent1:::ENTER\n";
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("Event1", false);
        String expected = "\nEvent1:::ENTER\n";
        OutputBuffer out = new OutputBuffer();
        formatter.point("Event1", event, "11", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("Event1", false);
        String eventName = "Event1";
        String comparability = "11";
        String expected = "\n\nppt Event1\nppt-type enter\n";
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, eventName, out, comparability);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("Event1", false);
        EventAttribute attribute = new EventAttribute("attribute1", "String", "value");
        event.addAttribute(attribute);
        String expected = "attribute1\n\"value\"\n1";
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        assertEquals(expected, out.toString());
    }
}