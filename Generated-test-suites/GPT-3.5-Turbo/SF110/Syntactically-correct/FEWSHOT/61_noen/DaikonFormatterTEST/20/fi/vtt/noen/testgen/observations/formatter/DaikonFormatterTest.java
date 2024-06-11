
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.model.Const;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;

public class DaikonFormatterTest {
    
    private DaikonFormatter formatter;
    
    @Before
    public void setUp() throws Exception {
        formatter = new DaikonFormatter("testFile", false);
    }
    
    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, formatter.header());
    }
    
    @Test
    public void testFooter() {
        String expectedFooter = "";
        assertEquals(expectedFooter, formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        String expectedFileNameExtension = "dtrace";
        assertEquals(expectedFileNameExtension, formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        
        Event event1 = new Event("Event1");
        event1.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event1.addAttribute(new EventAttribute("Attribute2", "Value2"));
        run.addEvent(event1);
        
        Event event2 = new Event("Event2");
        event2.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event2.addAttribute(new EventAttribute("Attribute2", "Value2"));
        run.addEvent(event2);
        
        String expectedObservations = "Event1:::ENTER\nVariable Attribute1\ntype String\nValue \"Value1\"\n1\nVariable Attribute2\ntype String\nValue \"Value2\"\n1\n\nEvent2:::ENTER\nVariable Attribute1\ntype String\nValue \"Value1\"\n1\nVariable Attribute2\ntype String\nValue \"Value2\"\n1\n\n";
        assertEquals(expectedObservations, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        Event event = new Event("Event");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        String expectedPoint = "Event:::ENTER\nVariable Attribute1\ntype String\nValue \"Value1\"\n1\nVariable Attribute2\ntype String\nValue \"Value2\"\n1";
        assertEquals(expectedPoint, formatter.simplePoint(event, "10"));
    }
    
    @Test
    public void testPoint() {
        Event event = new Event("Event");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        String expectedPoint = "Event:::ENTER\nVariable Attribute1\ntype String\nValue \"Value1\"\n1\nVariable Attribute2\ntype String\nValue \"Value2\"\n1";
        assertEquals(expectedPoint, formatter.point("Event", event, "11"));
    }
    
    @Test
    public void testDeclarations() {
        Event event = new Event("Event");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        String expectedDeclarations = "ppt Event:::ENTER\nppt-type enter\n  variable Attribute1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\n  variable Attribute2\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11";
        formatter.declarations(event, "Event:::ENTER", "11");
        assertEquals(expectedDeclarations, formatter.getDeclarationsString());
    }
    
    @Test
    public void testAttributes() {
        Event event = new Event("Event");
        event.addAttribute(new EventAttribute("Attribute1", "Value1"));
        event.addAttribute(new EventAttribute("Attribute2", "Value2"));
        
        String expectedAttributes = "Attribute1\n\"Value1\"\n1\nAttribute2\n\"Value2\"\n1";
        assertEquals(expectedAttributes, formatter.attributes(event));
    }
    
    @Test
    public void testClassForProperty() {
        // TODO: Implement test case
        assertNotNull(null);
    }
    
    @Test
    public void testClassesForMultipleProperties() {
        // TODO: Implement test case
        assertNotNull(null);
    }
    
    @Test
    public void testInputs() {
        // TODO: Implement test case
        assertNotNull(null);
    }
    
    @Test
    public void testOutputs() {
        // TODO: Implement test case
        assertNotNull(null);
    }
}
