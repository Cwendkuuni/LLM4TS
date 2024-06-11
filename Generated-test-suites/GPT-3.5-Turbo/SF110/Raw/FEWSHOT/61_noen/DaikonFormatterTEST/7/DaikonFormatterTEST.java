import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class DaikonFormatterTest {
    private DaikonFormatter daikonFormatter;
  
    @Before
    public void setUp() {
        daikonFormatter = new DaikonFormatter("test.txt", true);
    }
  
    @Test
    public void testConstructor() throws IOException {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
        assertEquals("test.txt", daikonFormatter.getFileName());
        assertTrue(daikonFormatter.isSimple());
        
        Collection<String> inputs = Arrays.asList("input1", "input2");
        daikonFormatter = new DaikonFormatter("test.txt", inputs);
        assertEquals("test.txt", daikonFormatter.getFileName());
        assertFalse(daikonFormatter.isSimple());
        assertEquals(inputs, daikonFormatter.getInputs());
    }
  
    @Test(expected = RuntimeException.class)
    public void testConstructorException() throws IOException {
        doThrow(IOException.class).when(new FileInputStream(anyString()));
        daikonFormatter = new DaikonFormatter("test.txt", true);
    }
  
    @Test
    public void testClassForProperty() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("property1", "Test");
        doReturn("Test").when(properties).getProperty("property1");
        FileInputStream fileInputStream = mock(FileInputStream.class);
        doReturn(properties).when(fileInputStream).load(any(InputStream.class));
        doReturn(fileInputStream).when(FileInputStream).newInstance(anyString());
      
        Class result = DaikonFormatter.classForProperty("property1");
        assertEquals(Test.class, result);
    }
  
    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Collection<Class> expectedResult = Arrays.asList(Class1.class, Class2.class);
        doReturn(Class1.class).when(DaikonFormatter).classForProperty("prefix1");
        doReturn(Class2.class).when(DaikonFormatter).classForProperty("prefix2");
      
        Collection<Class> result = DaikonFormatter.classesForMultipleProperties("prefix");
        assertEquals(expectedResult, result);
    }
  
    @Test
    public void testInputs() throws Exception {
        Collection<Class> expectedResult = Arrays.asList(InputInterface1.class, InputInterface2.class);
        doReturn(expectedResult).when(DaikonFormatter).classesForMultipleProperties("InputInterface");
      
        Collection<Class> result = DaikonFormatter.inputs();
        assertEquals(expectedResult, result);
    }
  
    @Test
    public void testOutputs() throws Exception {
        Collection<Class> expectedResult = Arrays.asList(OutputInterface1.class, OutputInterface2.class);
        doReturn(expectedResult).when(DaikonFormatter).classesForMultipleProperties("OutputInterface");
      
        Collection<Class> result = DaikonFormatter.outputs();
        assertEquals(expectedResult, result);
    }
  
    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
      
        String result = daikonFormatter.header();
        assertEquals(expectedHeader, result);
    }
  
    @Test
    public void testFooter() {
        String expectedFooter = "";
      
        String result = daikonFormatter.footer();
        assertEquals(expectedFooter, result);
    }
  
    @Test
    public void testFileNameExtension() {
        String expectedFileNameExtension = "dtrace";
      
        String result = daikonFormatter.fileNameExtension();
        assertEquals(expectedFileNameExtension, result);
    }
  
    @Test
    public void testObservations() {
        ProgramRun programRun = mock(ProgramRun.class);
        Event event1 = mock(Event.class);
        Event event2 = mock(Event.class);
        Event event3 = mock(Event.class);
        EventAttribute attribute1 = mock(EventAttribute.class);
        EventAttribute attribute2 = mock(EventAttribute.class);
        EventAttribute attribute3 = mock(EventAttribute.class);
      
        doReturn("event1").when(event1).getName();
        doReturn("event2").when(event2).getName();
        doReturn("event3").when(event3).getName();
        doReturn(false).when(event1).isExit();
        doReturn(false).when(event2).isExit();
        doReturn(true).when(event3).isExit();
        doReturn(new ArrayList<>(Arrays.asList(attribute1))).when(event1).attributes();
        doReturn(new ArrayList<>(Arrays.asList(attribute2))).when(event2).attributes();
        doReturn(new ArrayList<>(Arrays.asList(attribute3))).when(event3).attributes();
        doReturn("attribute1").when(attribute1).getName();
        doReturn("attribute2").when(attribute2).getName();
        doReturn("attribute3").when(attribute3).getName();
        doReturn("java.lang.String").when(attribute1).getType();
        doReturn("java.lang.String").when(attribute2).getType();
        doReturn("java.lang.String").when(attribute3).getType();
        doReturn("value1").when(attribute1).getValue();
        doReturn("value2").when(attribute2).getValue();
        doReturn("value3").when(attribute3).getValue();
      
        String expectedObservations = "event1:::ENTER\nppt event1\nppt-type enter\nvariable attribute1\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability 11\nattribute1\n\"value1\"\n1\n\nevent2:::ENTER\nppt event2\nppt-type enter\nvariable attribute2\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability 11\nattribute2\n\"value2\"\n1\n\nevent3:::EXIT1\nppt event3\nppt-type exit\nvariable attribute3\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability 22\nattribute3\n\"value3\"\n1\n";
      
        String result = daikonFormatter.observations(programRun);
        assertEquals(expectedObservations, result);
    }
  
    @Test
    public void testSimplePoint() {
        Event event = mock(Event.class);
        EventAttribute attribute1 = mock(EventAttribute.class);
      
        doReturn("event").when(event).getName();
        doReturn(false).when(event).isExit();
        doReturn(new ArrayList<>(Arrays.asList(attribute1))).when(event).attributes();
        doReturn("attribute1").when(attribute1).getName();
        doReturn("java.lang.String").when(attribute1).getType();
        doReturn("value1").when(attribute1).getValue();
      
        String expectedObservations = "event:::ENTER\nppt event:::ENTER\nppt-type enter\nvariable attribute1\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability 10\nattribute1\n\"value1\"\n1\n";
      
        daikonFormatter.simplePoint(event, "10", out);
        String result = out.toString();
        assertEquals(expectedObservations, result);
    }
  
  @Test
  public void testPoint() {
    Event event = mock(Event.class);
    EventAttribute attribute1 = mock(EventAttribute.class);
  
    doReturn("event").when(event).getName();
    doReturn(false).when(event).isExit();
    doReturn(new ArrayList<>(Arrays.asList(attribute1))).when(event).attributes();
    doReturn("attribute1").when(attribute1).getName();
    doReturn("java.lang.String").when(attribute1).getType();
    doReturn("value1").when(attribute1).getValue();
  
    String expectedObservations = "event:::ENTER\nppt event:::ENTER\nppt-type enter\nvariable attribute1\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability 11\nattribute1\n\"value1\"\n1\n";
  
    daikonFormatter.point("event", event, "11", out);
    String result = out.toString();
    assertEquals(expectedObservations, result);
  }

  @Test
  public void testDeclarations() {
    Event event = mock(Event.class);
    EventAttribute attribute1 = mock(EventAttribute.class);
  
    doReturn("event").when(event).getName();
    doReturn(false).when(event).isExit();
    doReturn(new ArrayList<>(Arrays.asList(attribute1))).when(event).attributes();
    doReturn("attribute1").when(attribute1).getName();
    doReturn("java.lang.String").when(attribute1).getType();
    doReturn("value1").when(attribute1).getValue();
  
    String expectedObservations = "\nppt event:::ENTER\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability 11\n";
  
    daikonFormatter.declarations(event, "event:::ENTER", out, "11");
    String result = out.toString();
    assertEquals(expectedObservations, result);
}

@Test
public void testAttributes() {
    Event event = mock(Event.class);
    EventAttribute attribute1 = mock(EventAttribute.class);

    doReturn("event").when(event).getName();
    doReturn(false).when(event).isExit();
    doReturn(new ArrayList<>(Arrays.asList(attribute1))).when(event).attributes();
    doReturn("attribute1").when(attribute1).getName();
    doReturn("java.lang.String").when(attribute1).getType();
    doReturn("value1").when(attribute1).getValue();

    String expectedObservations = "attribute1\n\"value1\"\n1\n";

    daikonFormatter.attributes(event, out);
    String result = out.toString();
    assertEquals(expectedObservations, result);
}

}