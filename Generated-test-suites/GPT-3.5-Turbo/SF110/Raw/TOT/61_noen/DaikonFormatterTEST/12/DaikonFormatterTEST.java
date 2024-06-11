import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimpleTrue() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertNotNull(formatter);
        assertEquals("testfile.txt", formatter.getFileName());
        assertTrue(formatter.isSimple());
        assertNull(formatter.getInputs());
        assertNotNull(formatter.getDeclarations());
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testConstructorWithSimpleFalse() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
        assertNotNull(formatter);
        assertEquals("testfile.txt", formatter.getFileName());
        assertFalse(formatter.isSimple());
        assertEquals(inputs, formatter.getInputs());
        assertNotNull(formatter.getDeclarations());
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testObservationsWithSimpleTrue() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testObservationsWithSimpleFalse() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", inputs);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("Input1");
        EventAttribute attribute1 = new EventAttribute("inputAttribute", "String", "exampleInput");
        event1.addAttribute(attribute1);
        run.addEvent(event1);
        
        String expected = "input1:::ENTER\n" +
                          "  variable inputAttribute\n" +
                          "    var-kind variable\n" +
                          "    dec-type String\n" +
                          "    rep-type String\n" +
                          "    comparability 11\n" +
                          "inputAttribute\n" +
                          "\"exampleInput\"\n" +
                          "1";
        
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        EventAttribute attribute = new EventAttribute("attribute1", "int", "10");
        event.addAttribute(attribute);
        
        String expected = "Event1:::ENTER\n" +
                          "  variable attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type int\n" +
                          "    rep-type int\n" +
                          "    comparability 10\n" +
                          "\n" +
                          "Event1\n" +
                          "attribute1\n" +
                          "10";
        
        formatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        OutputBuffer out = new OutputBuffer();
        Event event1 = new Event("Event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        event1.addAttribute(attribute1);
        Event event2 = new Event("Event2");
        EventAttribute attribute2 = new EventAttribute("attribute2", "String", "example");
        event2.addAttribute(attribute2);
        
        String expected = "Event1_Event2:::ENTER\n" +
                          "  variable attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type int\n" +
                          "    rep-type int\n" +
                          "    comparability 11\n" +
                          "  variable attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type String\n" +
                          "    rep-type String\n" +
                          "    comparability 11\n" +
                          "\n" +
                          "Event1_Event2\n" +
                          "attribute1\n" +
                          "10\n" +
                          "attribute2\n" +
                          "\"example\"\n" +
                          "1";
        
        formatter.point("Event1_Event2", event1, "11", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        EventAttribute attribute = new EventAttribute("attribute1", "int", "10");
        event.addAttribute(attribute);
        
        String expected = "\nppt Event1:::ENTER\n" +
                          "ppt-type enter\n" +
                          "  variable attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type int\n" +
                          "    rep-type int\n" +
                          "    comparability 10";
        
        formatter.declarations(event, "Event1", out, "10");
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "int", "10");
        EventAttribute attribute2 = new EventAttribute("attribute2", "String", "example");
        event.addAttribute(attribute1);
        event.addAttribute(attribute2);
        
        String expected = "attribute1\n" +
                          "10\n" +
                          "attribute2\n" +
                          "\"example\"\n" +
                          "1";
        
        formatter.attributes(event, out);
        assertEquals(expected, out.toString());
    }

}