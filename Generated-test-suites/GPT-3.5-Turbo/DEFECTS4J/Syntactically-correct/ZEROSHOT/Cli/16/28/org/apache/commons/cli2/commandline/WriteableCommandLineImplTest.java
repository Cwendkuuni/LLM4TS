import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    Option rootOption = new Option("root", "Root Option");
    List arguments = new ArrayList();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("test", "Test Option");
    Object value = "value";

    commandLine.addValue(option, value);

    List values = commandLine.getValues(option, null);

    assertTrue(values.contains(value));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("test", "Test Option");
    boolean value = true;

    commandLine.addSwitch(option, value);

    Boolean switchValue = commandLine.getSwitch(option, null);

    assertEquals(Boolean.valueOf(value), switchValue);
  }

  @Test
  public void testHasOption() {
    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    Option returnedOption = commandLine.getOption("test");

    assertEquals(option, returnedOption);
  }

  @Test
  public void testGetValues() {
    Option option = new Option("test", "Test Option");
    Object value = "value";
    List defaults = new ArrayList();
    defaults.add("default");

    commandLine.addValue(option, value);
    commandLine.setDefaultValues(option, defaults);

    List values = commandLine.getValues(option, null);
    List valuesWithDefault = commandLine.getValues(option, defaults);

    assertTrue(values.contains(value));
    assertTrue(valuesWithDefault.contains(value));
    assertTrue(valuesWithDefault.containsAll(defaults));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("test", "Test Option");
    Object value = "value";

    commandLine.addValue(option, value);

    List values = commandLine.getUndefaultedValues(option);

    assertTrue(values.contains(value));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("test", "Test Option");
    boolean defaultValue = true;

    Boolean switchValue = commandLine.getSwitch(option, defaultValue);

    assertEquals(Boolean.valueOf(defaultValue), switchValue);
  }

  @Test
  public void testGetProperty() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(property, value);

    String returnValue = commandLine.getProperty(property);

    assertEquals(value, returnValue);
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("test", "Test Option");
    String property = "property";
    String value = "value";

    commandLine.addProperty(option, property, value);

    Set properties = commandLine.getProperties(option);
    String returnedValue = commandLine.getProperty(option, property, null);

    assertTrue(properties.contains(property));
    assertEquals(value, returnedValue);
  }

  @Test
  public void testGetPropertyDefaultValue() {
    Option option = new Option("test", "Test Option");
    String property = "property";
    String defaultValue = "default";
    String returnedValue = commandLine.getProperty(option, property, defaultValue);

    assertEquals(defaultValue, returnedValue);
  }

  @Test
  public void testGetProperties() {
    Option option = new Option("test", "Test Option");
    String property = "property";
    String value = "value";

    commandLine.addProperty(option, property, value);

    Set properties = commandLine.getProperties(option);

    assertTrue(properties.contains(property));
  }

  @Test
  public void testLooksLikeOption() {
    String trigger = "-t";

    boolean looksLikeOption = commandLine.looksLikeOption(trigger);

    assertEquals(true, looksLikeOption);
  }

  @Test
  public void testToString() {
    String expectedString = "test";

    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    String returnedString = commandLine.toString();

    assertEquals(expectedString, returnedString);
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    List options = commandLine.getOptions();

    assertTrue(options.contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("test", "Test Option");
    commandLine.addOption(option);

    Set triggers = commandLine.getOptionTriggers();

    assertTrue(triggers.contains("test"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("test", "Test Option");
    List defaults = new ArrayList();
    defaults.add("default");

    commandLine.setDefaultValues(option, defaults);

    List returnedDefaults = commandLine.getValues(option, null);

    assertTrue(returnedDefaults.containsAll(defaults));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("test", "Test Option");
    Boolean defaultSwitch = true;

    commandLine.setDefaultSwitch(option, defaultSwitch);

    Boolean returnedDefaultSwitch = commandLine.getSwitch(option, null);

    assertEquals(defaultSwitch, returnedDefaultSwitch);
  }

  @Test
  public void testGetNormalised() {
    List arguments = new ArrayList();
    arguments.add("arg1");

    Option option = new Option("test", "Test Option");

    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

    List normalised = commandLine.getNormalised();

    assertEquals(arguments, normalised);
  }

}