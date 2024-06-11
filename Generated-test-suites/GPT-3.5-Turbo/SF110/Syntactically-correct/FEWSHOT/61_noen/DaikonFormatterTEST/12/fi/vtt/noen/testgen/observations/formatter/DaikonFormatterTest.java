import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DaikonFormatterTest {
    
    @Test
    public void testConstructor() throws IOException {
        DaikonFormatter formatter1 = new DaikonFormatter("filename", true);
        DaikonFormatter formatter2 = new DaikonFormatter("filename", null);
        assertEquals(true, formatter1.simple);
        assertNull(formatter2.inputs);
    }
    
    @Test
    public void testClassForProperty() throws Exception {
        Class<?> clazz = DaikonFormatter.classForProperty("property1");
        assertEquals(class1, clazz);
    }
    
    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Collection<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("prefix");
        assertEquals(2, classes.size());
        assertTrue(classes.contains(class1));
        assertTrue(classes.contains(class2));
    }
    
    @Test
    public void testInputs() throws Exception {
        Collection<Class<?>> classes = DaikonFormatter.inputs();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(class1));
        assertTrue(classes.contains(class2));
    }
    
    @Test
    public void testOutputs() throws Exception {
        Collection<Class<?>> classes = DaikonFormatter.outputs();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(class3));
        assertTrue(classes.contains(class4));
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testObservationsSimple() {
        DaikonFormatter formatter = new DaikonFormatter("filename", true);
        ProgramRun run = createProgramRun();
        String expected = "<expected output>";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        ProgramRun run = createProgramRun();
        String expected = "<expected output>";
        assertEquals(expected, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        Event event = createEvent();
        OutputBuffer out = new OutputBuffer();
        String expected = "<expected output>";
        formatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        Event event = createEvent();
        OutputBuffer out = new OutputBuffer();
        String expected = "<expected output>";
        formatter.point("name", event, "11", out);
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        Event event = createEvent();
        OutputBuffer out = new OutputBuffer();
        String expected = "<expected output>";
        formatter.declarations(event, "eventName", out, "11");
        assertEquals(expected, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("filename", false);
        Event event = createEvent();
        OutputBuffer out = new OutputBuffer();
        String expected = "<expected output>";
        formatter.attributes(event, out);
        assertEquals(expected, out.toString());
    }
    
    // Helper methods to create test data
    
    private ProgramRun createProgramRun() {
        ProgramRun run = new ProgramRun();
        // add events to the run
        return run;
    }
    
    private Event createEvent() {
        Event event = new Event();
        // add attributes to the event
        return event;
    }
}