package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {
    @Test
    public void testConstructor1() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        
        assertEquals("fileName", formatter.getFileName());
        assertTrue(formatter.isSimple());
        assertNull(formatter.getInputs());
    }
    
    @Test
    public void testConstructor2() throws IOException {
        Collection<String> inputs = Arrays.asList("input1", "input2");
        DaikonFormatter formatter = new DaikonFormatter("fileName", inputs);
        
        assertEquals("fileName", formatter.getFileName());
        assertFalse(formatter.isSimple());
        assertEquals(inputs, formatter.getInputs());
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        
        assertEquals(expectedHeader, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event());
        String expectedObservations = "";
        
        assertEquals(expectedObservations, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "";
        
        formatter.simplePoint(event, "10", out);
        
        assertEquals(expectedPoint, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "";
        
        formatter.point("name", event, "11", out);
        
        assertEquals(expectedPoint, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        String eventName = "eventName";
        String comparability = "10";
        String expectedDeclarations = "";
        
        formatter.declarations(event, eventName, out, comparability);
        
        assertEquals(expectedDeclarations, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        Event event = new Event();
        OutputBuffer out = new OutputBuffer();
        String expectedAttributes = "";
        
        formatter.attributes(event, out);
        
        assertEquals(expectedAttributes, out.toString());
    }
}