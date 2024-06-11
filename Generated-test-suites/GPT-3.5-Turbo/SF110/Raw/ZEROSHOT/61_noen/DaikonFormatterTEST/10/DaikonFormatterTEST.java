import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor_simple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testConstructor_inputs() {
        try {
            Collection<String> inputs = new ArrayList<String>();
            inputs.add("input1");
            inputs.add("input2");
            DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testInputs() {
        try {
            Collection<Class> inputClasses = DaikonFormatter.inputs();
            assertNotNull(inputClasses);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputClasses = DaikonFormatter.outputs();
            assertNotNull(outputClasses);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expectedFooter = "";
        assertEquals(expectedFooter, formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expectedExtension = "dtrace";
        assertEquals(expectedExtension, formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expectedObservations = "";
        assertEquals(expectedObservations, formatter.observations(run));
    }

    @Test
    public void testSimplePoint() {
        Event event = new Event("event1");
        String comparability = "10";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        formatter.simplePoint(event, comparability, out);
        String expectedOutput = "event1:::ENTER\n\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testPoint() {
        Event event = new Event("event1");
        String name = "event1,event2";
        String comparability = "11";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        formatter.point(name, event, comparability, out);
        String expectedOutput = "event1,event2:::ENTER\n\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDeclarations() {
        Event event = new Event("event1");
        String eventName = "event1";
        String comparability = "22";
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        formatter.declarations(event, eventName, out, comparability);
        String expectedOutput = "\n" +
                               "ppt event1\n" +
                               "ppt-type enter\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testAttributes() {
        Event event = new Event("event1");
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        formatter.attributes(event, out);
        String expectedOutput = "event1\n";
        assertEquals(expectedOutput, out.toString());
    }
}