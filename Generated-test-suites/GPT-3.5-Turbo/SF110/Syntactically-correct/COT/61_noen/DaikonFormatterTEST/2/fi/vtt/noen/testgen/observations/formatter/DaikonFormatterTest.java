package fi.vtt.noen.testgen.observations.formatter;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DaikonFormatterTest {

    @Test
    public void testConstructorWithSimpleParameter() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertEquals("test.txt", formatter.getFileName());
        assertEquals(true, formatter.isSimple());
        assertNull(formatter.getInputs());
    }

    @Test
    public void testConstructorWithInputsParameter() throws IOException {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter formatter = new DaikonFormatter("test.txt", inputs);
        assertEquals("test.txt", formatter.getFileName());
        assertFalse(formatter.isSimple());
        assertEquals(inputs, formatter.getInputs());
    }

    @Test
    public void testClassForProperty() throws Exception {
        System.setProperty("test.property", "java.lang.String");
        Class clazz = DaikonFormatter.classForProperty("test.property");
        assertEquals(String.class, clazz);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        System.setProperty("test.property1", "java.lang.String");
        System.setProperty("test.property2", "java.lang.Integer");
        Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("test.property");
        assertTrue(classes.contains(String.class));
        assertTrue(classes.contains(Integer.class));
    }

    @Test
    public void testInputs() throws Exception {
        System.setProperty("InputInterface1", "java.lang.String");
        System.setProperty("InputInterface2", "java.lang.Integer");
        Collection<Class> classes = DaikonFormatter.inputs();
        assertEquals(expectedClasses, classes);
    }

    @Test
    public void testOutputs() throws Exception {
        System.setProperty("OutputInterface1", "java.lang.String");
        System.setProperty("OutputInterface2", "java.lang.Integer");
        Collection<Class> classes = DaikonFormatter.outputs();
        assertEquals(expectedClasses, classes);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String header = formatter.header();
        assertTrue(header.contains("decl-version 2.0"));
        assertTrue(header.contains("var-comparability implicit"));
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String fileNameExt = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExt);
    }

    @Test
    public void testObservations() {
        ProgramRun run = Mockito.mock(ProgramRun.class);
        Event event1 = Mockito.mock(Event.class);
        Event event2 = Mockito.mock(Event.class);
        EventAttribute attribute1 = Mockito.mock(EventAttribute.class);
        EventAttribute attribute2 = Mockito.mock(EventAttribute.class);
        Mockito.when(run.iterator()).thenReturn(Arrays.asList(event1, event2).iterator());
        Mockito.when(event1.getName()).thenReturn("event1");
        Mockito.when(event1.isExit()).thenReturn(false);
        Mockito.when(event1.attributes()).thenReturn(Arrays.asList(attribute1).iterator());
        Mockito.when(attribute1.getName()).thenReturn("attribute1");
        Mockito.when(attribute1.getType()).thenReturn("java.lang.String");
        Mockito.when(attribute1.getValue()).thenReturn("value1");
        Mockito.when(event2.getName()).thenReturn("event2");
        Mockito.when(event2.isExit()).thenReturn(true);
        Mockito.when(event2.attributes()).thenReturn(Arrays.asList(attribute2).iterator());
        Mockito.when(attribute2.getName()).thenReturn("attribute2");
        Mockito.when(attribute2.getType()).thenReturn("java.lang.Integer");
        Mockito.when(attribute2.getValue()).thenReturn("2");
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        String observations = formatter.observations(run);
        assertTrue(observations.contains("event1"));
        assertTrue(observations.contains("attribute1"));
        assertTrue(observations.contains("\"value1\""));
        assertTrue(observations.contains("event2"));
        assertTrue(observations.contains("attribute2"));
        assertTrue(observations.contains("2"));
    }
}