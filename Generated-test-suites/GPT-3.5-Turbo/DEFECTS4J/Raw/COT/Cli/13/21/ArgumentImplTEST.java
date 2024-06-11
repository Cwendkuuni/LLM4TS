import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

  private ArgumentImpl argument;

  @Before
  public void setUp() {
    argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ',', null, null, null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("arg", argument.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    assertEquals(' ', argument.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    assertEquals(',', argument.getSubsequentSeparator());
  }

  @Test
  public void testGetConsumeRemaining() {
    assertNull(argument.getConsumeRemaining());
  }

  @Test
  public void testGetDefaultValues() {
    assertNull(argument.getDefaultValues());
  }

  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(0, false);
    commandLine.addValue(option, "value");

    try {
      argument.validate(commandLine, option);
    } catch (OptionException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testProcessValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = Arrays.asList("value1", "value2", "value3").listIterator();
    Option option = new OptionImpl(0, false);

    try {
      argument.processValues(commandLine, arguments, option);
    } catch (OptionException e) {
      fail("Should not throw an exception");
    }

    List<String> values = commandLine.getValues(option);
    assertEquals(1, values.size());
    assertEquals("value1, value2, value3", values.get(0));
  }

  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    assertTrue(argument.canProcess(commandLine, "value"));
  }

  @Test
  public void testGetValidator() {
    assertNull(argument.getValidator());
  }

  @Test
  public void testValidate2() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new OptionImpl(0, false);

    try {
      argument.validate(commandLine, option);
      fail("Should throw an exception");
    } catch (OptionException e) {
      assertEquals("Argument is missing values", e.getMessage());
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
    assertEquals("description", argument.getDescription());
  }

  @Test
  public void testGetMaximum() {
    assertEquals(1, argument.getMaximum());
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
    Option option = new OptionImpl(0, false);

    argument.defaults(commandLine);

    List<String> values = commandLine.getValues(option);
    assertNotNull(values);
    assertEquals(0, values.size());
  }

}