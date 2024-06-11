package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

public class DaikonFormatterTest {
    
    private DaikonFormatter daikonFormatter;
    
    @Before
    public void setUp() throws IOException {
        daikonFormatter = new DaikonFormatter("test.dtrace", true);
    }
    
    @Test
    public void testConstructor1() throws IOException {
        Assert.assertEquals("test.dtrace", daikonFormatter.fileName);
        Assert.assertEquals(true, daikonFormatter.simple);
        Assert.assertNotNull(daikonFormatter.declarations);
        Assert.assertNull(daikonFormatter.inputs);
    }
    
    @Test
    public void testConstructor2() throws IOException {
        Collection<String> inputs = new ArrayList<String>();
        inputs.add("input1");
        inputs.add("input2");
        
        daikonFormatter = new DaikonFormatter("test.dtrace", inputs);
        
        Assert.assertEquals("test.dtrace", daikonFormatter.fileName);
        Assert.assertEquals(false, daikonFormatter.simple);
        Assert.assertNotNull(daikonFormatter.declarations);
        Assert.assertNotNull(daikonFormatter.inputs);
        Assert.assertEquals(2, daikonFormatter.inputs.size());
        Assert.assertTrue(daikonFormatter.inputs.contains("input1"));
        Assert.assertTrue(daikonFormatter.inputs.contains("input2"));
    }
    
    @Test
    public void testClassForProperty() throws Exception {
        String property = "property";
        Class clazz = DaikonFormatter.classForProperty(property);
        Assert.assertNull(clazz);
    }
    
    @Test
    public void testClassesForMultipleProperties() throws Exception {
        String prefix = "prefix";
        Collection<Class> classes = DaikonFormatter.classesForMultipleProperties(prefix);
        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.isEmpty());
    }
    
    @Test
    public void testInputs() throws Exception {
        Collection<Class> inputs = DaikonFormatter.inputs();
        Assert.assertNotNull(inputs);
        Assert.assertTrue(inputs.isEmpty());
    }
    
    @Test
    public void testOutputs() throws Exception {
        Collection<Class> outputs = DaikonFormatter.outputs();
        Assert.assertNotNull(outputs);
        Assert.assertTrue(outputs.isEmpty());
    }
    
    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        String header = daikonFormatter.header();
        Assert.assertEquals(expectedHeader, header);
    }
    
    @Test
    public void testFooter() {
        String expectedFooter = "";
        String footer = daikonFormatter.footer();
        Assert.assertEquals(expectedFooter, footer);
    }
    
    @Test
    public void testFileNameExtension() {
        String expectedExtension = "dtrace";
        String extension = daikonFormatter.fileNameExtension();
        Assert.assertEquals(expectedExtension, extension);
    }
    
    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        String expectedObservations = "";
        String observations = daikonFormatter.observations(run);
        Assert.assertEquals(expectedObservations, observations);
    }
    
    @Test
    public void testSimplePoint() {
        Event event = new Event("event1");
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        
        daikonFormatter.simplePoint(event, comparability, out);
        
        // Check output buffer
        
        String expectedOutputBuffer = "event1:::ENTER\n  variable event1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 10\n";
        String outputBuffer = out.toString();
        Assert.assertEquals(expectedOutputBuffer, outputBuffer);
    }
    
    @Test
    public void testPoint() {
        Event event = new Event("event1");
        String name = "event2";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        
        daikonFormatter.point(name, event, comparability, out);
        
        // Check output buffer
        
        String expectedOutputBuffer = "event2:::ENTER\n  variable event2\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\n";
        String outputBuffer = out.toString();
        Assert.assertEquals(expectedOutputBuffer, outputBuffer);
    }
    
    @Test
    public void testDeclarations() {
        Event event = new Event("event1");
        String eventName = "event1:::ENTER";
        String comparability = "22";
        OutputBuffer out = new OutputBuffer();
        
        daikonFormatter.declarations(event, eventName, out, comparability);
        
        // Check output buffer
        
        String expectedOutputBuffer = "event1:::ENTER\n  variable event1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 22\n";
        String outputBuffer = out.toString();
        Assert.assertEquals(expectedOutputBuffer, outputBuffer);
        Assert.assertTrue(daikonFormatter.declarations.contains(eventName));
    }
    
    @Test
    public void testAttributes() {
        EventAttribute attribute = new EventAttribute("attribute1", "value1", "type1");
        Event event = new Event("event1");
        event.addAttribute(attribute);
        
        OutputBuffer out = new OutputBuffer();
        daikonFormatter.attributes(event, out);
        
        // Check output buffer
        
        String expectedOutputBuffer = "attribute1\nvalue1\n1\n";
        String outputBuffer = out.toString();
        Assert.assertEquals(expectedOutputBuffer, outputBuffer);
    }
}