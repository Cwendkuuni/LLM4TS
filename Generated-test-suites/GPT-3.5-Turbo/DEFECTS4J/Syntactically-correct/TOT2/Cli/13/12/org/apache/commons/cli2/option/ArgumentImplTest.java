import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
  
  private ArgumentImpl argument;

  @Before
  public void setUp() {
    argument = new ArgumentImpl("testArg", "Test argument", 1, 3, '/', ',', null, "--", null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("testArg", argument.getPreferredName());
  }

  @Test
  public void testProcessValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = new ArrayList<String>().listIterator();
    
    arguments.add("value1");
    arguments.add("value2");
    arguments.add("value3");
    
    argument.processValues(commandLine, arguments, argument);
    
    List<String> values = commandLine.getValues(argument);
    assertEquals(3, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
    assertEquals("value3", values.get(2));
  }

  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    commandLine.addValue(argument, "value");
    
    try {
      argument.validate(commandLine, argument);
      assertTrue(true);
    } catch (OptionException e) {
      fail("OptionException thrown unexpectedly");
    }
  }

  @Test
  public void testGetMaximum() {
    assertEquals(3, argument.getMaximum());
  }

  @Test
  public void testGetMinimum() {
    assertEquals(1, argument.getMinimum());
  }
}