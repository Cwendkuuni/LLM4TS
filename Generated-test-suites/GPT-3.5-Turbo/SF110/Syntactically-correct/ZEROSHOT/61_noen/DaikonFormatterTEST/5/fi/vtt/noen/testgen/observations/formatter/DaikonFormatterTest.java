package fi.vtt.noen.testgen.observations.formatter;

import org.junit.*;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.parser.*;
import java.io.*;
import fi.vtt.noen.testgen.model.*;
import java.util.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("", false);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("", false);
        String expected = "";
        assertEquals(expected, formatter.footer());
    }
    
    @Test 
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("", false);
        String expected = "dtrace";
        assertEquals(expected, formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        DaikonFormatter formatter = new DaikonFormatter("", false);
        String expected = "";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        Event event = new Event("event", null);
        DaikonFormatter formatter = new DaikonFormatter("", false);
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        String expected = "event:::ENTER\n\nevent:::ENTER\n";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testPoint() {
        Event event = new Event("event", null);
        DaikonFormatter formatter = new DaikonFormatter("", false);
        OutputBuffer out = new OutputBuffer();
        formatter.point("event", event, "11", out);
        String expected = "event:::ENTER\n\nevent:::ENTER\n";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        Event event = new Event("event", null);
        DaikonFormatter formatter = new DaikonFormatter("", false);
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "event", out, "11");
        String expected = "\nppt event\nppt-type enter";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        Event event = new Event("event", null);
        DaikonFormatter formatter = new DaikonFormatter("", false);
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "event", out, "11");
        String expected = "";
        assertEquals(expected, out.toString());
    }
    
}