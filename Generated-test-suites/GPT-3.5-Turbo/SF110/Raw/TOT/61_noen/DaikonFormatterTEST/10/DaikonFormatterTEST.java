import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class DaikonFormatterTest {

    private Set<String> declarations;
    private final Collection<String> inputs;
    private final Collection<Class> inputClasses;
    private final Collection<Class> outputClasses;
    private final String fileName = "testfile.txt";

    @Before
    public void setUp() throws Exception {
        declarations = new HashSet<String>();
        inputs = new ArrayList<String>();
        inputs.add("input1");
        inputs.add("input2");
        inputClasses = new ArrayList<Class>();
        outputClasses = new ArrayList<Class>();
        outputClasses.add(OutputInterface1.class);
        outputClasses.add(OutputInterface2.class);
    }

    @Test
    public void testDaikonFormatterConstructorWithSimpleTrue() throws IOException {         
        DaikonFormatter df = new DaikonFormatter(fileName, true);
        assertNotNull(df);
        assertNull(df.inputs);
        assertFalse(df.simple);
    }

    @Test
    public void testDaikonFormatterConstructorWithSimpleFalse() throws IOException {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        assertNotNull(df);
        assertEquals(inputClasses, df.inputs);
        assertFalse(df.simple);
    }

    @Test
    public void testDaikonFormatterConstructorWithInputs() throws IOException {
        DaikonFormatter df = new DaikonFormatter(fileName, inputs);
        assertNotNull(df);
        assertEquals(inputs, df.inputs);
        assertFalse(df.simple);
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterConstructorWithInvalidInputs() throws IOException {
        DaikonFormatter df = new DaikonFormatter(fileName, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testDaikonFormatterConstructorWithInvalidFileName() throws IOException {
        DaikonFormatter df = new DaikonFormatter("invalidFile.txt", false);
    }

    @Test
    public void testClassForProperty() throws Exception {
        Class result = DaikonFormatter.classForProperty("InputInterface1");
        assertEquals(InputInterface1.class, result);
    }

    @Test
    public void testClassForPropertyWithInvalidProperty() throws Exception {
        Class result = DaikonFormatter.classForProperty("InvalidProperty");
        assertNull(result);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Collection<Class> result = DaikonFormatter.classesForMultipleProperties("OutputInterface");
        assertEquals(outputClasses, result);
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> result = DaikonFormatter.inputs();
        assertEquals(inputClasses, result);
    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> result = DaikonFormatter.outputs();
        assertEquals(outputClasses, result);
    }

    @Test
    public void testHeader() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, df.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        String expected = "";
        assertEquals(expected, df.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        String expected = "dtrace";
        assertEquals(expected, df.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("input1", new EventAttribute("attribute1", "value1")));
        run.addEvent(new Event("input2", new EventAttribute("attribute2", "value2")));
        String expected = "";
        assertEquals(expected, df.observations(run));
    }

    @Test
    public void testSimplePointForEnterEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event event = new Event("input1", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        df.simplePoint(event, "10", out);
        String expected = "\ninput1:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testSimplePointForExitEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event event = new Event("input1", true, new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        df.simplePoint(event, "10", out);
        String expected = "\ninput1:::EXIT1\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPointForEnterEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event previousEvent = new Event("input1", new EventAttribute("attribute1", "value1"));
        Event event = new Event("input2", new EventAttribute("attribute2", "value2"));
        OutputBuffer out = new OutputBuffer();
        df.point("input1,input2", previousEvent, "22", out);
        String expected = "\ninput1,input2:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 22\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPointForExitEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event previousEvent = new Event("input1", true, new EventAttribute("attribute1", "value1"));
        Event event = new Event("input2", true, new EventAttribute("attribute2", "value2"));
        OutputBuffer out = new OutputBuffer();
        df.point("input1,input2", previousEvent, "22", out);
        String expected = "\ninput1,input2:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 22\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testObservationsForSimpleTrue() {
        DaikonFormatter df = new DaikonFormatter(fileName, true);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("input1", new EventAttribute("attribute1", "value1")));
        run.addEvent(new Event("input1", true, new EventAttribute("attribute2", "value2")));
        String expected = "";
        assertEquals(expected, df.observations(run));
    }

    @Test
    public void testObservationsForSimpleFalse() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("input1", new EventAttribute("attribute1", "value1")));
        run.addEvent(new Event("input2", new EventAttribute("attribute2", "value2")));
        String expected = "\ninput1,input2:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 22\n";
        assertEquals(expected, df.observations(run));
    }

    @Test
    public void testObservationsForSimpleFalseWithExitEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("input1", new EventAttribute("attribute1", "value1")));
        run.addEvent(new Event("input2", true, new EventAttribute("attribute2", "value2")));
        String expected = "\ninput1,input2:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 22\n";
        assertEquals(expected, df.observations(run));
    }

    @Test
    public void testObservationsForSimpleFalseWithExitEventAndInputEvent() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("input1", new EventAttribute("attribute1", "value1")));
        run.addEvent(new Event("exit1", true, new EventAttribute("attribute2", "value2")));
        String expected = "\ninput1:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 11\n";
        assertEquals(expected, df.observations(run));
    }

    @Test
    public void testDeclarationsFirstTime() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event event = new Event("input1", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        df.declarations(event, "input1", out, "10");
        String expected = "\nppt input1\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testDeclarationsAlreadyDeclared() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event event = new Event("input1", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        df.declarations(event, "input1", out, "10");
        df.declarations(event, "input1", out, "10");
        String expected = "\nppt input1\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 10\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributes() {
        DaikonFormatter df = new DaikonFormatter(fileName, false);
        Event event = new Event("input1", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        df.attributes(event, out);
        String expected = "\nattribute1\nvalue11\n";
        assertEquals(expected, out.toString());
    }
}