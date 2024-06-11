import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    
    @Test
    public void testConstructorSimple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testConstructorInputs() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("input1");
            inputs.add("input2");
            
            DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        // create a ProgramRun instance
        ProgramRun run = new ProgramRun();
        
        // add events to the program run
        Event event1 = new Event("event1");
        EventAttribute attribute11 = new EventAttribute("attribute1", "value1");
        EventAttribute attribute12 = new EventAttribute("attribute2", 2);
        event1.addAttribute(attribute11);
        event1.addAttribute(attribute12);
        run.addEvent(event1);
        
        Event event2 = new Event("event2");
        EventAttribute attribute21 = new EventAttribute("attribute3", "value3");
        EventAttribute attribute22 = new EventAttribute("attribute4", 4);
        event2.addAttribute(attribute21);
        event2.addAttribute(attribute22);
        run.addEvent(event2);
        
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expected = "event1:::ENTER\nvariable attribute1\nvariable attribute2\nattribute1 \"value1\"\nattribute2 2\n1\nevent2:::ENTER\nvariable attribute3\nvariable attribute4\nattribute3 \"value3\"\nattribute4 4\n1\n";
        assertEquals(expected, formatter.observations(run));
    }

}