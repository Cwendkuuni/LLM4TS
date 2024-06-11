import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentImplTest {
  
  // Test case for getPreferredName()
  @Test
  public void testGetPreferredName() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals("arg", argument.getPreferredName());
  }
  
  // Test case for processValues()
  @Test
  public void testProcessValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
    Option option = new OptionImpl(0, false);

    Argument argument = new ArgumentImpl("arg", "description", 2, 2, ' ', ' ', null, "--", null, 0);
    argument.processValues(commandLine, arguments, option);
    
    assertEquals(2, commandLine.getValues(option).size());
    assertEquals("value1", commandLine.getValues(option).get(0));
    assertEquals("value2", commandLine.getValues(option).get(1));
  }
  
  // Test case for canProcess()
  @Test
  public void testCanProcess() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertTrue(argument.canProcess(null, "value1"));
  }
  
  // Test case for getInitialSeparator()
  @Test
  public void testGetInitialSeparator() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals(' ', argument.getInitialSeparator());
  }
  
  // Test case for getSubsequentSeparator()
  @Test
  public void testGetSubsequentSeparator() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ',', null, "--", null, 0);
    assertEquals(',', argument.getSubsequentSeparator());
  }
  
  // Test case for getTriggers()
  @Test
  public void testGetTriggers() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals(Collections.EMPTY_SET, argument.getTriggers());
  }
  
  // Test case for getConsumeRemaining()
  @Test
  public void testGetConsumeRemaining() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals("--", argument.getConsumeRemaining());
  }
  
  // Test case for getDefaultValues()
  @Test
  public void testGetDefaultValues() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", Arrays.asList("default1", "default2"), 0);
    assertEquals(Arrays.asList("default1", "default2"), argument.getDefaultValues());
  }
  
  // Test case for getValidator()
  @Test
  public void testGetValidator() {
    Validator validator = new ValidatorImpl();
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', validator, "--", null, 0);
    assertEquals(validator, argument.getValidator());
  }
  
  // Test case for validate()
  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(0, false);
    option.setValues(Arrays.asList("value1", "value2"));
    
    Argument argument = new ArgumentImpl("arg", "description", 1, 2, ' ', ' ', null, "--", null, 0);
    try {
      argument.validate(commandLine);
    } catch (OptionException e) {
      fail("Validation failed.");
    }
  }
  
  // Test case for appendUsage()
  @Test
  public void testAppendUsage() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    StringBuffer buffer = new StringBuffer();
    argument.appendUsage(buffer, Collections.emptySet(), null);
    assertEquals("arg", buffer.toString());
  }
  
  // Test case for getDescription()
  @Test
  public void testGetDescription() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals("description", argument.getDescription());
  }
  
  // Test case for helpLines()
  @Test
  public void testHelpLines() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    List<HelpLine> helpLines = argument.helpLines(0, Collections.emptySet(), null);
    assertEquals(1, helpLines.size());
    assertEquals(argument, helpLines.get(0).getOption());
    assertEquals(0, helpLines.get(0).getDepth());
  }
  
  // Test case for getMaximum()
  @Test
  public void testGetMaximum() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 2, ' ', ' ', null, "--", null, 0);
    assertEquals(2, argument.getMaximum());
  }
  
  // Test case for getMinimum()
  @Test
  public void testGetMinimum() {
    Argument argument = new ArgumentImpl("arg", "description", 1, 2, ' ', ' ', null, "--", null, 0);
    assertEquals(1, argument.getMinimum());
  }
  
  // Test case for stripBoundaryQuotes()
  @Test
  public void testStripBoundaryQuotes() {
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
    assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
  }
  
  // Test case for isRequired()
  @Test
  public void testIsRequired() {
    Argument argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
    assertTrue(argument.isRequired());
  }
  
  // Test case for defaults()
  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", Arrays.asList("default1", "default2"), 0);
    argument.defaults(commandLine);
    assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(argument));
  }
  
  // Test case for defaultValues()
  @Test
  public void testDefaultValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(0, false);
    
    Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", Arrays.asList("default1", "default2"), 0);
    argument.defaultValues(commandLine, option);
    assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option));
  }
}