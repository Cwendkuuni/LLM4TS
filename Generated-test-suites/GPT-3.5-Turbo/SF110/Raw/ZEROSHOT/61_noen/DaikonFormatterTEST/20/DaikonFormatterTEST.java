import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatterSimple;
    private DaikonFormatter daikonFormatterInput;

    @Before
    public void setup() throws IOException {
        daikonFormatterSimple = new DaikonFormatter("fileName", true);
        daikonFormatterInput = new DaikonFormatter("fileName", getInputs());
    }

    @Test
    public void testConstructorWithSimpleFlag() {
        assertNotNull(daikonFormatterSimple);
        assertFalse(daikonFormatterSimple.getDeclarations().isEmpty());
        assertTrue(daikonFormatterSimple.isSimple());
        assertNull(daikonFormatterSimple.getInputs());
    }

    @Test
    public void testConstructorWithInputs() {
        assertNotNull(daikonFormatterInput);
        assertFalse(daikonFormatterInput.getDeclarations().isEmpty());
        assertFalse(daikonFormatterInput.isSimple());
        assertNotNull(daikonFormatterInput.getInputs());
        assertEquals(getInputs(), daikonFormatterInput.getInputs());
    }

    @Test
    public void testClassForProperty() throws Exception {
        Class clazz = DaikonFormatter.classForProperty("Property");
        assertNull(clazz);

        clazz = DaikonFormatter.classForProperty("InputInterface1");
        assertNotNull(clazz);
        assertEquals("fi.vtt.noen.testgen.observations.data.InputInterface1", clazz.getName());
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("Property");
        assertTrue(classes.isEmpty());

        classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertFalse(classes.isEmpty());
        assertEquals(2, classes.size());
        assertTrue(classes.contains(InputInterface1.class));
        assertTrue(classes.contains(InputInterface2.class));
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> inputs = DaikonFormatter.inputs();
        assertFalse(inputs.isEmpty());
        assertEquals(2, inputs.size());
        assertTrue(inputs.contains(InputInterface1.class));
        assertTrue(inputs.contains(InputInterface2.class));
    }

    @Test
    public void testOutputs() throws Exception {
        Collection<Class> outputs = DaikonFormatter.outputs();
        assertFalse(outputs.isEmpty());
        assertEquals(2, outputs.size());
        assertTrue(outputs.contains(OutputInterface1.class));
        assertTrue(outputs.contains(OutputInterface2.class));
    }

    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, daikonFormatterSimple.header());
        assertEquals(expectedHeader, daikonFormatterInput.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", daikonFormatterSimple.footer());
        assertEquals("", daikonFormatterInput.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", daikonFormatterSimple.fileNameExtension());
        assertEquals("dtrace", daikonFormatterInput.fileNameExtension());
    }

    @Test
    public void testObservations() {
        ProgramRun programRun = new ProgramRun();
        String expectedObservations = "expected observations";

        String observations = daikonFormatterSimple.observations(programRun);
        assertEquals(expectedObservations, observations);

        observations = daikonFormatterInput.observations(programRun);
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testSimplePoint() {
        // TODO: Implement test for simplePoint()
    }

    @Test
    public void testPoint() {
        // TODO: Implement test for point()
    }

    @Test
    public void testDeclarations() {
        // TODO: Implement test for declarations()
    }

    @Test
    public void testAttributes() {
        // TODO: Implement test for attributes()
    }

    private Collection<Class> getInputs() {
        Set<Class> inputs = new HashSet<>();
        inputs.add(InputInterface1.class);
        inputs.add(InputInterface2.class);
        return inputs;
    }
}