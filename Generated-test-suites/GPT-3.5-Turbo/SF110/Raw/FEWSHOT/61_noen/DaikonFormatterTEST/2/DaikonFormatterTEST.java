package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    
    @Before
    public void setup() {
        formatter = new DaikonFormatter("testFile", true);
    }
    
    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testSimplePoint() {
        Event event = new Event("eventName");
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        
        formatter.simplePoint(event, comparability, out);
        
        String expected = "eventName:::ENTER\n";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testPoint() {
        Event event = new Event("eventName");
        String name = "name";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        
        formatter.point(name, event, comparability, out);
        
        String expected = "name:::ENTER\n";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        Event event = new Event("eventName");
        String eventName = "eventName";
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        
        formatter.declarations(event, eventName, out, comparability);
        
        String expected = "\nppt eventName\nppt-type enter";
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        Event event = new Event("eventName");
        OutputBuffer out = new OutputBuffer();
        EventAttribute attribute = new EventAttribute("attributeName", "java.lang.String", "\"attributeValue\"");
        event.addAttribute(attribute);
        
        formatter.attributes(event, out);
        
        String expected = "attributeName\n\"attributeValue\"\n1";
        assertEquals(expected, out.toString());
    }
}