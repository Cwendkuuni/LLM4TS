package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;

    @Before
    public void setUp() {
        formatter = new DaikonFormatter("testFile.dtrace", true);
    }

    // Test case for constructor with boolean parameter 'simple'
    @Test
    public void testSimpleConstructor() {
        assertTrue(formatter.fileName.equals("testFile.dtrace"));
        assertTrue(formatter.simple);
        assertNull(formatter.inputs);
    }

    // Test case for constructor with Collection parameter 'inputs'
    @Test
    public void testCollectionConstructor() {
        assertNotNull(formatter.declarations);
        assertFalse(formatter.simple);
        assertNull(formatter.inputs);
    }

    // Test case for header() method
    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    // Test case for footer() method
    @Test
    public void testFooter() {
        assertEquals("", formatter.footer());
    }

    // Test case for fileNameExtension() method
    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    // Test case for observations() method with simple=true
    @Test
    public void testObservationsSimpleTrue() {
        ProgramRun run = new ProgramRun();
        assertEquals("", formatter.observations(run));
    }

    // Test case for observations() method with simple=false and empty inputs
    @Test(expected = RuntimeException.class)
    public void testObservationsSimpleFalseEmptyInputs() throws Exception {
        DaikonFormatter.formats = new Properties();
        DaikonFormatter.formats.load(new FileInputStream("testgen.properties"));
        formatter = new DaikonFormatter("testFile.dtrace", false);
        ProgramRun run = new ProgramRun();
        formatter.observations(run);
    }

    // Test case for observations() method with simple=false and non-empty inputs
    @Test
    public void testObservationsSimpleFalseNonEmptyInputs() {
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1"));
        run.addEvent(new Event("event2"));
        run.addEvent(new EventExit("event1_EXIT"));
        run.addEvent(new EventExit("event2_EXIT"));
        String expected = "\n" +
                "ppt event1\n" +
                "ppt-type enter\n" +
                "  variable null\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 22\n" +
                "\n" +
                "event1\n" +
                "null\n" +
                "1\n" +
                "\n" +
                "ppt event2\n" +
                "ppt-type enter\n" +
                "  variable null\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 22\n" +
                "\n" +
                "event2\n" +
                "null\n" +
                "1\n" +
                "\n" +
                "ppt event1,_EXIT\n" +
                "ppt-type exit\n" +
                "  variable null\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 11\n" +
                "\n" +
                "event1,_EXIT\n" +
                "null\n" +
                "1\n" +
                "\n" +
                "ppt event2,_EXIT\n" +
                "ppt-type exit\n" +
                "  variable null\n" +
                "    var-kind variable\n" +
                "    dec-type null\n" +
                "    rep-type null\n" +
                "    comparability 11\n" +
                "\n" +
                "event2,_EXIT\n" +
                "null\n" +
                "1";
        assertEquals(expected, formatter.observations(run));
    }
}