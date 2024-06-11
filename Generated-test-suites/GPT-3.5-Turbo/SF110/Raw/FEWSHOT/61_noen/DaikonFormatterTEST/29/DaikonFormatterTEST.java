import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testConstructorSimple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
            assertNotNull(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testConstructor() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", new ArrayList<String>());
            assertNotNull(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        String actualHeader = formatter.header();
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        String expectedFooter = "";
        String actualFooter = formatter.footer();
        assertEquals(expectedFooter, actualFooter);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        String expectedExtension = "dtrace";
        String actualExtension = formatter.fileNameExtension();
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testObservations() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
            ProgramRun run = new ProgramRun();
            String expectedObservations = "";
            String actualObservations = formatter.observations(run);
            assertEquals(expectedObservations, actualObservations);
        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        Event event = new Event("TestEvent", false);
        String comparability = "10";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("TestEvent:::ENTER");
        expectedOut.append("  variable name");
        expectedOut.append("    var-kind variable");
        expectedOut.append("    dec-type java.lang.String");
        expectedOut.append("    rep-type java.lang.String");
        expectedOut.append("    comparability 10");
        OutputBuffer actualOut = new OutputBuffer();
        formatter.simplePoint(event, comparability, actualOut);
        assertEquals(expectedOut.toString(), actualOut.toString());
    }

    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        Event previousEvent = new Event("PreviousEvent", false);
        Event event = new Event("Event", false);
        String name = "PreviousEvent,Event";
        String comparability = "11";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("PreviousEvent,Event:::ENTER");
        expectedOut.append("  variable name");
        expectedOut.append("    var-kind variable");
        expectedOut.append("    dec-type java.lang.String");
        expectedOut.append("    rep-type java.lang.String");
        expectedOut.append("    comparability 11");
        OutputBuffer actualOut = new OutputBuffer();
        formatter.point(name, previousEvent, comparability, actualOut);
        assertEquals(expectedOut.toString(), actualOut.toString());
    }

    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        Event event = new Event("TestEvent", false);
        String eventName = "TestEvent:::ENTER";
        String comparability = "10";
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("");
        expectedOut.append("ppt TestEvent:::ENTER");
        expectedOut.append("ppt-type enter");
        expectedOut.append("  variable name");
        expectedOut.append("    var-kind variable");
        expectedOut.append("    dec-type java.lang.String");
        expectedOut.append("    rep-type java.lang.String");
        expectedOut.append("    comparability 10");
        OutputBuffer actualOut = new OutputBuffer();
        formatter.declarations(event, eventName, actualOut, comparability);
        assertEquals(expectedOut.toString(), actualOut.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        Event event = new Event("TestEvent", false);
        event.addAttribute(new EventAttribute("name", "java.lang.String", "TestName"));
        OutputBuffer expectedOut = new OutputBuffer();
        expectedOut.append("name");
        expectedOut.append("\"TestName\"");
        expectedOut.append("1");
        OutputBuffer actualOut = new OutputBuffer();
        formatter.attributes(event, actualOut);
        assertEquals(expectedOut.toString(), actualOut.toString());
    }
}