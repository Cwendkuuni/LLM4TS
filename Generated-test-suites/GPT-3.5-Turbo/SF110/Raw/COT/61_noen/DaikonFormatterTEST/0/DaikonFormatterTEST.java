import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.Collection;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;

    @Before
    public void setUp() throws IOException {
        daikonFormatter = new DaikonFormatter("testfile.txt", true);
    }

    @Test
    public void testConstructor() {
        assertNotNull(daikonFormatter);
    }

    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, daikonFormatter.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", daikonFormatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", daikonFormatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        ProgramRun programRun = new ProgramRun();
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");
        programRun.addEvent(event1);
        programRun.addEvent(event2);
        String expected = "";
        expected += "Event1:::ENTER\n";
        expected += "Event1\n";
        expected += "Event2:::ENTER\n";
        expected += "Event2\n";
        assertEquals(expected, daikonFormatter.observations(programRun));
    }
}