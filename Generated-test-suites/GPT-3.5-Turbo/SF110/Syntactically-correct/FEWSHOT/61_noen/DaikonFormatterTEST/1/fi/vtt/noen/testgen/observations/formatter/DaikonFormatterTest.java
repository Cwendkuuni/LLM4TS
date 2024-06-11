package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testConstructorSimple() {
        // Test when simple is true
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("testfile.txt", formatter.getFileName());
        assertNull(formatter.getInputs());
        assertNull(formatter.getDeclarations());
        assertEquals(true, formatter.isSimple());

        formatter = new DaikonFormatter("testfile.txt", false);
        assertEquals("testfile.txt", formatter.getFileName());
        assertNull(formatter.getInputs());
        assertNull(formatter.getDeclarations());
        assertEquals(false, formatter.isSimple());
    }

    @Test
    public void testConstructorInputs() {
        // Test when inputs are provided
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", Arrays.asList("input1", "input2"));
        assertEquals("testfile.txt", formatter.getFileName());
        assertEquals(Arrays.asList("input1", "input2"), formatter.getInputs());
        assertNull(formatter.getDeclarations());
        assertEquals(false, formatter.isSimple());
    }

    @Test
    public void testClassForProperty() {
        try {
            assertEquals(null, DaikonFormatter.classForProperty("property1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClassesForMultipleProperties() {
        try {
            assertEquals(new ArrayList<>(), DaikonFormatter.classesForMultipleProperties("prefix"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputs() {
        try {
            assertEquals(new ArrayList<>(), DaikonFormatter.inputs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOutputs() {
        try {
            assertEquals(new ArrayList<>(), DaikonFormatter.outputs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit", formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("event1");
        ProgramRun run = new ProgramRun();
        run.addEvent(event);
        assertEquals("", formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        formatter.simplePoint(event, "10", out);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        formatter.point("event1", event, "11", out);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, "event1", out, "11");
        assertEquals("", out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        assertEquals("", out.toString());
    }
}