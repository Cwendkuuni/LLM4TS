
import org.junit.*;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimpleTrue() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", true);
            assertTrue(formatter.getSimple());
            assertNull(formatter.getInputs());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
  
    @Test
    public void testConstructorWithSimpleFalseAndValidInputs() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("InputInterface1");
            inputs.add("InputInterface2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
            assertFalse(formatter.getSimple());
            assertEquals(inputs, formatter.getInputs());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
  
    @Test(expected = RuntimeException.class)
    public void testConstructorWithSimpleFalseAndInvalidInputs() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("InvalidInterface");   // Invalid input interface
        DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
        assertFalse(formatter.getSimple());
    }
  
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
  
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        assertEquals("", formatter.footer());
    }
  
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
  
    @Test
    public void testObservationsWithSimpleTrue() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        ProgramRun run = new ProgramRun();   // Create a sample ProgramRun object
        String expected = "";   // Empty string should be returned as observations
        assertEquals(expected, formatter.observations(run));
    }
  
    @Test
    public void testObservationsWithSimpleFalseAndInputs() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("InputInterface1");
            inputs.add("InputInterface2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
          
            ProgramRun run = new ProgramRun();   // Create a sample ProgramRun object
            // Add events to the program run
            Event event1 = new Event("Event1");
            event1.addAttribute(new EventAttribute("input1", "int", "1"));
            run.addEvent(event1);
            Event event2 = new Event("Event2");
            event2.addAttribute(new EventAttribute("input2", "int", "2"));
            run.addEvent(event2);
            Event event3 = new Event("Event3");
            event3.setExit(true);
            event3.addAttribute(new EventAttribute("output1", "int", "1"));
            run.addEvent(event3);
          
            String expected = "Event1:::ENTER\n  variable input1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\nEvent2:::ENTER\n  variable input2\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\nEvent3:::EXIT1\n  variable output1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 22";
            assertEquals(expected, formatter.observations(run));
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
  
    @Test
    public void testObservationsWithSimpleFalseAndExitEvent() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("InputInterface1");
            inputs.add("InputInterface2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
          
            ProgramRun run = new ProgramRun();   // Create a sample ProgramRun object
            // Add events to the program run
            Event event1 = new Event("Event1");
            event1.addAttribute(new EventAttribute("input1", "int", "1"));
            run.addEvent(event1);
            Event event2 = new Event("Event2");
            event2.addAttribute(new EventAttribute("output1", "int", "1"));
            run.addEvent(event2);
            Event event3 = new Event("Event3");
            event3.setExit(true);
            event3.addAttribute(new EventAttribute("output2", "int", "2"));   // Output after exit event
            run.addEvent(event3);
          
            String expected = "Event1:::ENTER\n  variable input1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\nEvent2:::ENTER\n  variable output1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\nEvent3:::EXIT1\n  variable output2\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11";
            assertEquals(expected, formatter.observations(run));
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
  
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("input1", "int", "1"));
        event.addAttribute(new EventAttribute("input2", "int", "2"));
        OutputBuffer out = new OutputBuffer();
        String expected = "Event1:::ENTER\n\nEvent1\ninput1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\ninput2\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10";
        formatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }
  
    @Test
    public void testPoint() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("InputInterface1");
        inputs.add("InputInterface2");
        DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("input1", "int", "1"));
        event.addAttribute(new EventAttribute("input2", "int", "2"));
        OutputBuffer out = new OutputBuffer();
        String expected = "Event1:::ENTER\n\nEvent1\ninput1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\ninput2\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11";
        formatter.point("Event1", event, "11", out);
        assertEquals(expected, out.toString());
    }
  
    @Test
    public void testDeclarations() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("InputInterface1");
        inputs.add("InputInterface2");
        DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("input1", "int", "1"));
        OutputBuffer out = new OutputBuffer();
        String expected = "\nppt Event1:::ENTER\nppt-type enter\n  variable input1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 22";
        formatter.declarations(event, "Event1", out, "22");
        assertEquals(expected, out.toString());
    }
  
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("input", "int", "1"));
        event.addAttribute(new EventAttribute("output", "String", "outputValue"));
        OutputBuffer out = new OutputBuffer();
        String expected = "input\n1\n1\noutput\n\"outputValue\"\n1";
        formatter.attributes(event, out);
        assertEquals(expected, out.toString());
    }
  
}

