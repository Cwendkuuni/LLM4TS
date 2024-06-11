import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
  
  private Argument argument;
  
  @Before
  public void setUp() {
    argument = new ArgumentImpl("arg", "Argument description", 0, Integer.MAX_VALUE, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("arg", argument.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
  }

  @Test
  public void testGetConsumeRemaining() {
    assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
  }

  @Test
  public void testGetDefaultValues() {
    assertNull(argument.getDefaultValues());
  }

  @Test
  public void testGetValidator() {
    assertNull(argument.getValidator());
  }

  @Test
  public void testProcessValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = Collections.singletonList("value1").listIterator();
    Option option = new OptionImpl(0, false);
    
    try {
      argument.processValues(commandLine, arguments, option);
    } catch (OptionException e) {
      fail("Unexpected OptionException");
    }
    
    assertEquals("value1", commandLine.getValue(option));
    assertFalse(arguments.hasNext());
  }

  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String arg = "value1";
    
    assertTrue(argument.canProcess(commandLine, arg));
  }

  @Test
  public void testGetPrefixes() {
    assertTrue(argument.getPrefixes().isEmpty());
  }

  @Test
  public void testProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator args = Collections.singletonList("value1").listIterator();
    
    try {
      argument.process(commandLine, args);
    } catch (OptionException e) {
      fail("Unexpected OptionException");
    }
    
    assertEquals("value1", commandLine.getValue(argument));
  }

  @Test
  public void testGetTriggers() {
    assertTrue(argument.getTriggers().isEmpty());
  }

  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(0, false);
    
    try {
      argument.validate(commandLine, option);
    } catch (OptionException e) {
      fail("Unexpected OptionException");
    }
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Set<DisplaySetting> helpSettings = Collections.emptySet();
    Comparator comp = null;
    
    argument.appendUsage(buffer, helpSettings, comp);
    
    assertEquals("arg", buffer.toString());
  }

  @Test
  public void testGetDescription() {
    assertEquals("Argument description", argument.getDescription());
  }

  @Test
  public void testHelpLines() {
    int depth = 0;
    Set<DisplaySetting> helpSettings = Collections.emptySet();
    Comparator comp = null;
    
    assertTrue(argument.helpLines(depth, helpSettings, comp).size() == 1);
    assertTrue(argument.helpLines(depth, helpSettings, comp).get(0) instanceof HelpLine);
  }

  @Test
  public void testGetMaximum() {
    assertEquals(Integer.MAX_VALUE, argument.getMaximum());
  }

  @Test
  public void testGetMinimum() {
    assertEquals(0, argument.getMinimum());
  }

  @Test
  public void testIsRequired() {
    assertFalse(argument.isRequired());
  }
  
  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    
    argument.defaults(commandLine);
    
    assertNull(commandLine.getDefaultValues(argument));
  }

}