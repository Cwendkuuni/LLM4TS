package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DaikonFormatterTest {
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("", true);
        String expected = "decl-version 2.0var-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("", true);
        String expected = "";
        assertEquals(expected, formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("", true);
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
    }
    
    @Test
    public void testObservationsWithSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("", true);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "value1", "type1");
        event1.addAttribute(attribute1);
        run.addEvent(event1);
        
        String expected = "event1:::ENTER\n" +
            "  variable attribute1\n" +
            "    var-kind variable\n" +
            "    dec-type type1\n" +
            "    rep-type type1\n" +
            "    comparability 10\n" +
            "attribute1\n" +
            "value11";
            
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testObservationsWithPoint() {
        DaikonFormatter formatter = new DaikonFormatter("", false);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "value1", "type1");
        event1.addAttribute(attribute1);
        run.addEvent(event1);
        
        String expected = "event1:::ENTER\n" +
            "  variable attribute1\n" +
            "    var-kind variable\n" +
            "    dec-type type1\n" +
            "    rep-type type1\n" +
            "    comparability 11\n" +
            "event1:::ENTER\n" +
            "  variable attribute1\n" +
            "    var-kind variable\n" +
            "    dec-type type1\n" +
            "    rep-type type1\n" +
            "    comparability 22\n" +
            "attribute1\n" +
            "value11";
        
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testObservationsWithExitPoint() {
        DaikonFormatter formatter = new DaikonFormatter("", false);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("event1");
        EventAttribute attribute1 = new EventAttribute("attribute1", "value1", "type1");
        event1.addAttribute(attribute1);
        Event event2 = new Event("event2");
        EventAttribute attribute2 = new EventAttribute("attribute2", "value2", "type2");
        event2.addAttribute(attribute2);
        event2.setExit(true);
        run.addEvent(event1);
        run.addEvent(event2);
        
        String expected = "event1:::ENTER\n" +
            "  variable attribute1\n" +
            "    var-kind variable\n" +
            "    dec-type type1\n" +
            "    rep-type type1\n" +
            "    comparability 11\n" +
            "attribute1\n" +
            "value11" +
            "\nevent2:::ENTER\n" +
            "  variable attribute2\n" +
            "    var-kind variable\n" +
            "    dec-type type2\n" +
            "    rep-type type2\n" +
            "    comparability 22\n" +
            "event2:::ENTER\n" +
            "  variable attribute2\n" +
            "    var-kind variable\n" +
            "    dec-type type2\n" +
            "    rep-type type2\n" +
            "    comparability 11\n" +
            "event2:::EXIT1\n" +
            "  variable attribute2\n" +
            "    var-kind variable\n" +
            "    dec-type type2\n" +
            "    rep-type type2\n" +
            "    comparability 22\n";
        
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testEmptyObservations() {
        DaikonFormatter formatter = new DaikonFormatter("", true);
        ProgramRun run = new ProgramRun();
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }
}