import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimpleFlag() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        } catch (IOException e) {
            fail("Failed to create DaikonFormatter");
        }
        // Validate the result
        // ...
    }

    @Test
    public void testConstructorWithInputsCollection() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        
        try {
            DaikonFormatter formatter = new DaikonFormatter("file.txt", inputs);
        } catch (IOException e) {
            fail("Failed to create DaikonFormatter");
        }
        // Validate the result
        // ...
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        
        String expected = "";
        assertEquals(expected, formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
    }

    @Test
    public void testObservationsWithSimpleFlag() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        // Set up the program run
        ProgramRun run = new ProgramRun();
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        run.addEvent(event);
        
        String expected = "Event1:::ENTER\n" +
                          "  variable Attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n" +
                          "  variable Attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testObservationsWithoutSimpleFlag() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        
        DaikonFormatter formatter = new DaikonFormatter("file.txt", inputs);
        // Set up the program run
        ProgramRun run = new ProgramRun();
        Event previousEvent = new Event("PreviousEvent");
        previousEvent.addAttribute(new EventAttribute("Attribute1", "Value1"));
        previousEvent.addAttribute(new EventAttribute("Attribute2", "Value2"));
        run.addEvent(previousEvent);
        
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("Attribute3", "Value3"));
        event.addAttribute(new EventAttribute("Attribute4", "Value4"));
        run.addEvent(event);
        
        String expected = "PreviousEvent:::ENTER\n" +
                          "  variable Attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 11\n" +
                          "  variable Attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 11\n" +
                          "PreviousEvent,Event1:::ENTER\n" +
                          "  variable Attribute3\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 22\n" +
                          "  variable Attribute4\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 22\n";
        assertEquals(expected, formatter.observations(run));
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        formatter.simplePoint(event, "10", out);
        
        String expected = "Event1:::ENTER\n" +
                          "  variable Attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n" +
                          "  variable Attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPoint() {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");
        
        DaikonFormatter formatter = new DaikonFormatter("file.txt", inputs);
        OutputBuffer out = new OutputBuffer();
        Event previousEvent = new Event("PreviousEvent");
        previousEvent.addAttribute(new EventAttribute("Attribute1", "Value1"));
        previousEvent.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        formatter.point("PreviousEvent,Event1", previousEvent, "22", out);
        
        String expected = "PreviousEvent,Event1:::ENTER\n" +
                          "  variable Attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 22\n" +
                          "  variable Attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 22\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        formatter.declarations(event, "Event1:::ENTER", out, "10");
        
        String expected = "ppt Event1:::ENTER\n" +
                          "ppt-type enter\n" +
                          "  variable Attribute1\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n" +
                          "  variable Attribute2\n" +
                          "    var-kind variable\n" +
                          "    dec-type java.lang.String\n" +
                          "    rep-type java.lang.String\n" +
                          "    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("file.txt", true);
        OutputBuffer out = new OutputBuffer();
        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        formatter.attributes(event, out);
        
        String expected = "Attribute1\n" +
                          "Value1\n" +
                          "1\n" +
                          "Attribute2\n" +
                          "Value2\n" +
                          "1\n";
        assertEquals(expected, out.toString());
    }
}