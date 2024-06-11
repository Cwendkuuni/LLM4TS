import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Argument argument = new Argument("testArgument");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
    commandLine.addValue(argument, "value");
    assertEquals("value", commandLine.getValues(argument, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    assertEquals(option, commandLine.getOption(option.getPreferredName()));
  }

  @Test
  public void testGetValues() {
    Argument argument = new Argument("testArgument");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
    commandLine.addValue(argument, "value");
    assertEquals("value", commandLine.getValues(argument, null).get(0));
  }

  @Test
  public void testGetUndefaultedValues() {
    Argument argument = new Argument("testArgument");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
    commandLine.addValue(argument, "value");
    assertEquals("value", commandLine.getUndefaultedValues(argument).get(0));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals(null, commandLine.getProperty("testProperty"));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    commandLine.addProperty("testProperty", "testValue");
    assertEquals("testValue", commandLine.getProperty("testProperty"));
  }

  @Test
  public void testGetPropertyWithDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals("defaultValue", commandLine.getProperty("testProperty", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals(0, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertTrue(commandLine.looksLikeOption("-testOption"));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals("", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals(0, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals(0, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.setDefaultValues(option, null);
    assertEquals(0, commandLine.getValues(option, null).size());
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
    assertEquals(0, commandLine.getNormalised().size());
  }
}