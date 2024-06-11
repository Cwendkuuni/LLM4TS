import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimple() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertNotNull(formatter);
    }

    @Test
    public void testConstructorWithInputs() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("test.txt", inputs);
        assertNotNull(formatter);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        ProgramRun programRun = new ProgramRun();
        String expectedObservations = "";
        assertEquals(expectedObservations, formatter.observations(programRun));
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "\n\nevent1:::ENTER";
        formatter.simplePoint(event, "10", out);
        assertEquals(expectedPoint, out.toString());
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "\n\nevent1:::ENTER";
        formatter.point("event1", event, "11", out);
        assertEquals(expectedPoint, out.toString());
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        String expectedDeclarations = "\n\nppt event1:::ENTER\nppt-type enter";
        formatter.declarations(event, "event1:::ENTER", out, "11");
        assertEquals(expectedDeclarations, out.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("event1");
        event.addAttribute(new EventAttribute("attribute1", "String", "value1"));
        OutputBuffer out = new OutputBuffer();
        String expectedAttributes = "attribute1\n\"value1\"\n1";
        formatter.attributes(event, out);
        assertEquals(expectedAttributes, out.toString());
    }
}