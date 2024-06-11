
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

public class DaikonFormatterTest {
  
  private DaikonFormatter daikonFormatter;
  private String fileName;
  private boolean simple;
  private Collection<String> inputs;
  
  @Before
  public void setUp() throws IOException {
    fileName = "test.dtrace";
    simple = true;
    inputs = null;
    daikonFormatter = new DaikonFormatter(fileName, simple);
  }
  
  @Test
  public void testConstructorWithSimpleFlag() throws IOException {
    assertEquals(fileName, daikonFormatter.getFileName());
    assertEquals(this.simple, daikonFormatter.getSimple());
    assertNull(daikonFormatter.getInputs());
  }
  
  @Test
  public void testConstructorWithInputs() throws IOException {
    fileName = "test.dtrace";
    inputs = new HashSet<String>();
    inputs.add("input1");
    inputs.add("input2");
    daikonFormatter = new DaikonFormatter(fileName, inputs);
    
    assertEquals(fileName, daikonFormatter.getFileName());
    assertFalse(daikonFormatter.getSimple());
    assertEquals(inputs, daikonFormatter.getInputs());
  }
  
  @Test(expected = RuntimeException.class)
  public void testConstructorWithException() throws IOException {
    fileName = "test.dtrace";
    inputs = new HashSet<String>();
    inputs.add("input1");
    inputs.add("input2");
    
    // Mocking InterfaceParser.methodNames() to throw an exception
    InterfaceParser interfaceParser = mock(InterfaceParser.class);
    when(interfaceParser.methodNames(inputs)).thenThrow(Exception.class);
    
    new Expectations() {{
        InterfaceParser.methodNames(inputs);
        result = Exception.class;
    }};
    
    daikonFormatter = new DaikonFormatter(fileName, inputs);
  }
  
  @Test
  public void testHeader() {
    assertEquals("decl-version 2.0\nvar-comparability implicit", daikonFormatter.header());
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
    ProgramRun programRun = mock(ProgramRun.class);
    Event event1 = mock(Event.class);
    Event event2 = mock(Event.class);
    Event event3 = mock(Event.class);
    Event event4 = mock(Event.class);
    
    Iterator<Event> iterator = mock(Iterator.class);
    when(programRun.iterator()).thenReturn(iterator);
    when(iterator.hasNext()).thenReturn(true, true, true, true, false);
    when(iterator.next()).thenReturn(event1, event2, event3, event4);
    
    when(event1.getName()).thenReturn("event1");
    when(event1.isExit()).thenReturn(false);
    when(event2.getName()).thenReturn("event2");
    when(event2.isExit()).thenReturn(false);
    when(event3.getName()).thenReturn("event3");
    when(event3.isExit()).thenReturn(true);
    when(event4.getName()).thenReturn("event4");
    when(event4.isExit()).thenReturn(false);
    
    assertEquals("", daikonFormatter.observations(programRun));
  }
}
