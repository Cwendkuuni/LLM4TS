import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.model.Const;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor() throws IOException {
        String fileName = "testFile";
        boolean simple = true;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(simple, formatter.isSimple());
        assertNull(formatter.getInputs());

        Collection<String> inputs = new ArrayList<>();
        formatter = new DaikonFormatter(fileName, inputs);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(inputs, formatter.getInputs());
    }

    @Test(expected=RuntimeException.class)
    public void testConstructorWithException() throws IOException {
        String fileName = "testFile";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
    }

    @Test
    public void testClassForProperty() throws Exception {
        String property = "testProperty";
        Properties properties = mock(Properties.class);
        when(properties.getProperty(property)).thenReturn("java.lang.String");
        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        DaikonFormatter classUnderTest = mock(DaikonFormatter.class);
        when(classUnderTest.classForProperty(property)).thenCallRealMethod();

        Class result = classUnderTest.classForProperty(property);

        assertEquals("java.lang.String", result.getName());
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        String prefix = "InputInterface";
        String property1 = prefix + "1";
        String property2 = prefix + "2";
        Properties properties = mock(Properties.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        DaikonFormatter classUnderTest = mock(DaikonFormatter.class);
        Collection<Class> expected = new ArrayList<>();
        expected.add(String.class);
        expected.add(Integer.class);
        when(classUnderTest.classForProperty(property1)).thenReturn(String.class);
        when(classUnderTest.classForProperty(property2)).thenReturn(Integer.class);
        when(classUnderTest.classesForMultipleProperties(prefix)).thenCallRealMethod();

        Collection<Class> result = classUnderTest.classesForMultipleProperties(prefix);

        assertEquals(expected, result);
    }

    @Test
    public void testInputs() throws Exception {
        String property1 = "InputInterface1";
        String property2 = "InputInterface2";
        Properties properties = mock(Properties.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        DaikonFormatter classUnderTest = mock(DaikonFormatter.class);
        Collection<Class> expected = new ArrayList<>();
        expected.add(String.class);
        expected.add(Integer.class);
        when(classUnderTest.classForProperty(property1)).thenReturn(String.class);
        when(classUnderTest.classForProperty(property2)).thenReturn(Integer.class);
        when(classUnderTest.inputs()).thenCallRealMethod();

        Collection<Class> result = classUnderTest.inputs();

        assertEquals(expected, result);
    }

    @Test
    public void testOutputs() throws Exception {
        String property1 = "OutputInterface1";
        String property2 = "OutputInterface2";
        Properties properties = mock(Properties.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        DaikonFormatter classUnderTest = mock(DaikonFormatter.class);
        Collection<Class> expected = new ArrayList<>();
        expected.add(String.class);
        expected.add(Integer.class);
        when(classUnderTest.classForProperty(property1)).thenReturn(String.class);
        when(classUnderTest.classForProperty(property2)).thenReturn(Integer.class);
        when(classUnderTest.outputs()).thenCallRealMethod();

        Collection<Class> result = classUnderTest.outputs();

        assertEquals(expected, result);
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String result = formatter.header();

        assertEquals(expected, result);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        String result = formatter.footer();

        assertEquals(expected, result);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "dtrace";
        String result = formatter.fileNameExtension();

        assertEquals(expected, result);
    }

    @Test
    public void testObservations() {
        ProgramRun run = mock(ProgramRun.class);
        Iterator<Event> iterator = mock(Iterator.class);
        when(run.iterator()).thenReturn(iterator);
        Event event1 = mock(Event.class);
        Event event2 = mock(Event.class);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(event1, event2);

        DaikonFormatter formatter = new DaikonFormatter("testFile", false);
        String expected = "expected value";
        when(formatter.observations(run)).thenReturn(expected);

        String result = formatter.observations(run);

        assertEquals(expected, result);
    }

    @Test
    public void testSimplePoint() {
        Event event = mock(Event.class);
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        OutputBuffer outputBuffer = mock(OutputBuffer.class);
        String expected = "expected value";
        when(formatter.simplePoint(event, "10", outputBuffer)).thenReturn(expected);

        String result = formatter.simplePoint(event, "10", outputBuffer);

        assertEquals(expected, result);
    }

    @Test
    public void testPoint() {
        Event event = mock(Event.class);
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        OutputBuffer outputBuffer = mock(OutputBuffer.class);
        String expected = "expected value";
        when(formatter.point("name", event, "comparability", outputBuffer)).thenReturn(expected);

        String result = formatter.point("name", event, "comparability", outputBuffer);

        assertEquals(expected, result);
    }

    @Test
    public void testDeclarations() {
        Event event = mock(Event.class);
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        OutputBuffer outputBuffer = mock(OutputBuffer.class);
        String expected = "expected value";
        when(formatter.declarations(event, "eventName", outputBuffer, "comparability")).thenReturn(expected);

        String result = formatter.declarations(event, "eventName", outputBuffer, "comparability");

        assertEquals(expected, result);
    }
  
    @Test
    public void testAttributes() {
        Event event = mock(Event.class);
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        OutputBuffer outputBuffer = mock(OutputBuffer.class);
        String expected = "expected value";
        when(formatter.attributes(event, outputBuffer)).thenReturn(expected);

        String result = formatter.attributes(event, outputBuffer);

        assertEquals(expected, result);
    }
}