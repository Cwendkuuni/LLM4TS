import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    Option rootOption = new Option("root");
    commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<String>());
  }

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
    assertEquals(option, commandLine.getOption("testOption"));
  }

  @Test
  public void testAddValue() {
    Option option = new Argument();
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(2, commandLine.getValues(option, null).size());
    assertTrue(commandLine.getValues(option, null).contains("value1"));
    assertTrue(commandLine.getValues(option, null).contains("value2"));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("switchOption");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    assertFalse(commandLine.hasOption(option));
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("testOption"));
  }

  @Test
  public void testGetValues() {
    Option option = new Argument();
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(2, commandLine.getValues(option, null).size());
    assertTrue(commandLine.getValues(option, null).contains("value1"));
    assertTrue(commandLine.getValues(option, null).contains("value2"));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("switchOption");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals("value1", commandLine.getProperty("property1", null));
    assertEquals("value2", commandLine.getProperty("property2", null));
  }

  @Test
  public void testGetProperty() {
    commandLine.addProperty("property1", "value1");
    assertEquals("value1", commandLine.getProperty("property1", null));
    assertNull(commandLine.getProperty("property2", null));
  }

  @Test
  public void testGetProperties() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals(2, commandLine.getProperties().size());
    assertTrue(commandLine.getProperties().contains("property1"));
    assertTrue(commandLine.getProperties().contains("property2"));
  }

  @Test
  public void testLooksLikeOption() {
    assertFalse(commandLine.looksLikeOption("value"));
    commandLine.addOption(new Option("option"));
    assertTrue(commandLine.looksLikeOption("option"));
  }

  @Test
  public void testToString() {
    commandLine.addValue(new Argument(), "value1");
    commandLine.addValue(new Argument(), "value2");
    assertEquals("value1 value2", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("option1");
    Option option2 = new Option("option2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    assertEquals(2, commandLine.getOptions().size());
    assertTrue(commandLine.getOptions().contains(option1));
    assertTrue(commandLine.getOptions().contains(option2));
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("option");
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptionTriggers().size());
    assertTrue(commandLine.getOptionTriggers().contains("option"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Argument();
    commandLine.setDefaultValues(option, new ArrayList<String>());
    assertEquals(0, commandLine.getValues(option, null).size());
    commandLine.setDefaultValues(option, null);
    assertNull(commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("option");
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
    commandLine.setDefaultSwitch(option, null);
    assertNull(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    List<String> arguments = new ArrayList<String>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(new Argument(), arguments);
    assertEquals(2, commandLine.getNormalised().size());
    assertTrue(commandLine.getNormalised().contains("arg1"));
    assertTrue(commandLine.getNormalised().contains("arg2"));
  }
}