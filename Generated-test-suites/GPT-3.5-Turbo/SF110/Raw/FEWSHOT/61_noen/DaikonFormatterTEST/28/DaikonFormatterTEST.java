package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import fi.vtt.noen.testgen.model.*;

public class DaikonFormatterTest {
    @Test
    public void testConstructorWithSimpleBoolean() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        assertTrue(formatter.simple);
        assertNull(formatter.inputs);
    }

    @Test
    public void testConstructorWithInputsCollection() throws IOException {
        List<String> inputs = new ArrayList<>();
        inputs.add("Input1");
        inputs.add("Input2");

        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", inputs);
        assertFalse(formatter.simple);
        assertNotNull(formatter.inputs);
        assertEquals(2, formatter.inputs.size());
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorThrowsException() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
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
    public void testObservationsWithSimpleTrue() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        ProgramRun run = new ProgramRun();
        String expectedObservations = "";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithSimpleFalse() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1"));
        run.addEvent(new Event("event2"));

        String expectedObservations = "\nppt event1:::ENTER\nppt-type enter\nppt event2:::ENTER\nppt-type enter";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithInputsContainsPreviousAndCurrentEvent() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("Input1"));
        run.addEvent(new Event("Input2"));

        String expectedObservations = "\nppt Input1:::ENTER\nppt-type enter\nppt Input2:::ENTER\nppt-type enter";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithPreviousEventIsExit() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1"));
        Event exitEvent = new Event("event1");
        exitEvent.setExit(true);
        run.addEvent(exitEvent);

        String expectedObservations = "\nppt event1:::ENTER\nppt-type enter\nppt event1:::EXIT1\nppt-type exit";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithPreviousEventIsNotInputAndCurrentEventIsInput() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1"));
        run.addEvent(new Event("Input1"));

        String expectedObservations = "\nppt event1,Input1:::ENTER\nppt-type enter";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithInputsContainsCurrentEventAndCurrentEventIsExit() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("Input1"));
        Event exitEvent = new Event("Input1");
        exitEvent.setExit(true);
        run.addEvent(exitEvent);

        String expectedObservations = "\nppt Input1:::ENTER\nppt-type enter\nppt Input1:::EXIT1\nppt-type exit";
        String actualObservations = formatter.observations(run);
        assertEquals(expectedObservations, actualObservations);        
    }
}