import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

  private ArgumentImpl argument;

  @Before
  public void setup() {
    argument = new ArgumentImpl("testArg", "Test Argument", 1, 3, '=', ',', null, "--", null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("testArg", argument.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    assertEquals('=', argument.getInitialSeparator());
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
  public void testGetValidator() {
    assertNull(argument.getValidator());
  }

  @Test
  public void testGetDescription() {
    assertEquals("Test Argument", argument.getDescription());
  }

  @Test
  public void testGetMaximum() {
    assertEquals(3, argument.getMaximum());
  }

  @Test
  public void testGetMinimum() {
    assertEquals(1, argument.getMinimum());
  }

  @Test
  public void testIsRequired() {
    assertTrue(argument.isRequired());
  }

  @Test
  public void testProcessValues() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = Arrays.asList("value1", "value2", "value3").listIterator();
    Option option = new OptionImpl(0, false);
    argument.processValues(commandLine, arguments, option);
    assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getValues(option));
  }

  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    assertTrue(argument.canProcess(commandLine, "test"));
  }

  @Test
  public void testGetPrefixes() {
    assertTrue(argument.getPrefixes().isEmpty());
  }

  @Test
  public void testProcess() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator args = Arrays.asList("value1", "value2", "value3").listIterator();
    argument.process(commandLine, args);
    assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getValues(argument));
  }

  @Test
  public void testValidateWithValidValues() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    commandLine.addValue(argument, "value1");
    commandLine.addValue(argument, "value2");
    commandLine.addValue(argument, "value3");
    argument.validate(commandLine);
  }

  @Test(expected = OptionException.class)
  public void testValidateWithMissingValues() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    argument.validate(commandLine);
  }

  @Test(expected = OptionException.class)
  public void testValidateWithUnexpectedValue() throws OptionException {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    commandLine.addValue(argument, "value1");
    commandLine.addValue(argument, "value2");
    commandLine.addValue(argument, "value3");
    commandLine.addValue(argument, "value4");
    argument.validate(commandLine);
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Set helpSettings = new HashSet<>();
    helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
    helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
    helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);

    argument.appendUsage(buffer, helpSettings, null);

    assertEquals("<testArg1> <testArg2> <testArg3>", buffer.toString());
  }

  @Test
  public void testHelpLines() {
    Set helpSettings = new HashSet<>();
    HelpLine helpLine = new HelpLineImpl(argument, 1);

    assertEquals(Arrays.asList(helpLine), argument.helpLines(1, helpSettings, null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArgumentImplWithInvalidMinimumMaximum() {
    new ArgumentImpl("testArg", "Test Argument", 3, 1, '=', ',', null, "--", null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArgumentImplWithInvalidDefaults() {
    new ArgumentImpl("testArg", "Test Argument", 1, 3, '=', ',', null, "--",
        Arrays.asList("value1", "value2", "value3", "value4"), 0);
  }

  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    assertEquals(0, commandLine.getValues(argument).size());

    argument.defaults(commandLine);

    assertEquals(Arrays.asList(), commandLine.getValues(argument));
  }
}