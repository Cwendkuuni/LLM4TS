import org.junit.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addValue(option, "value");
    assertEquals("value", commandLine.getValues(option).get(0));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("option"));
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addValue(option, "value");
    assertEquals("value", commandLine.getValues(option, null).get(0));
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addValue(option, "value");
    assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addProperty(option, "property", "value");
    assertEquals("value", commandLine.getProperty(option, "property", null));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addProperty(option, "property", "value");
    assertEquals("value", commandLine.getProperty(option, "property", null));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addProperty(option, "property", "value");
    assertFalse(commandLine.getProperties(option).isEmpty());
    assertEquals(1, commandLine.getProperties(option).size());
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addValue(option, "value");
    assertEquals("value", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addOption(option);
    assertFalse(commandLine.getOptions().isEmpty());
    assertEquals(1, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.addOption(option);
    assertFalse(commandLine.getOptionTriggers().isEmpty());
    assertEquals(1, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    List<String> defaults = new ArrayList<>();
    defaults.add("default");
    commandLine.setDefaultValues(option, defaults);
    assertEquals("default", commandLine.getValues(option, null).get(0));
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
    Option option = new Option("option", "Option");
    List<String> arguments = new ArrayList<>();
    arguments.add("argument");
    commandLine.addOption(option);
    commandLine.addValue(option, "value");
    commandLine.setNormalisedArguments(arguments);
    assertFalse(commandLine.getNormalised().isEmpty());
    assertEquals(1, commandLine.getNormalised().size());
  }
}