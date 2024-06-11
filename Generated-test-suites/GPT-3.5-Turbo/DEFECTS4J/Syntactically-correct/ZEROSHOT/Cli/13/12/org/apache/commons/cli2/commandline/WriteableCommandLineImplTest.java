import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private Option option1;
  private Option option2;
  private Argument argument;
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    option1 = new Option("opt1", "Option 1");
    option2 = new Option("opt2", "Option 2");
    argument = new Argument("arg1", "Argument 1");
    commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());
  }

  @Test
  public void testAddOption() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    assertTrue(commandLine.getOptions().contains(option1));
    assertTrue(commandLine.getOptions().contains(option2));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option1, "value1");
    commandLine.addValue(option1, "value2");
    assertEquals(2, commandLine.getValues(option1, new ArrayList<String>()).size());
    assertTrue(commandLine.getValues(option1, new ArrayList<String>()).contains("value1"));
    assertTrue(commandLine.getValues(option1, new ArrayList<String>()).contains("value2"));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option1, true);
    assertTrue(commandLine.getSwitch(option1, false));
  }

  @Test
  public void testHasOption() {
    commandLine.addOption(option1);
    assertTrue(commandLine.hasOption(option1));
  }

  @Test
  public void testGetOption() {
    commandLine.addOption(option1);
    assertEquals(option1, commandLine.getOption("opt1"));
  }

  @Test
  public void testGetValues() {
    commandLine.addValue(option1, "value1");
    commandLine.addValue(option1, "value2");
    assertEquals(2, commandLine.getValues(option1, new ArrayList<String>()).size());
    assertTrue(commandLine.getValues(option1, new ArrayList<String>()).contains("value1"));
    assertTrue(commandLine.getValues(option1, new ArrayList<String>()).contains("value2"));
  }

  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option1, true);
    assertTrue(commandLine.getSwitch(option1, false));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("prop1", "value1");
    commandLine.addProperty("prop2", "value2");
    assertEquals("value1", commandLine.getProperty("prop1", ""));
    assertEquals("value2", commandLine.getProperty("prop2", ""));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-opt1"));
    assertFalse(commandLine.looksLikeOption("value1"));
  }

  @Test
  public void testToString() {
    commandLine.addValue(argument, "value1");
    commandLine.addValue(argument, "value2");
    assertEquals("value1 value2", commandLine.toString());
  }

  @Test
  public void testGetOptionTriggers() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    assertTrue(commandLine.getOptionTriggers().contains("opt1"));
    assertTrue(commandLine.getOptionTriggers().contains("opt2"));
  }

  @Test
  public void testSetDefaultValues() {
    commandLine.setDefaultValues(option1, new ArrayList<String>());
    assertTrue(commandLine.getValues(option1, new ArrayList<String>()).isEmpty());
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option1, false);
    assertFalse(commandLine.getSwitch(option1, true));
  }

  @Test
  public void testGetNormalised() {
    commandLine.addValue(argument, "value1");
    commandLine.addValue(argument, "value2");
    assertEquals(2, commandLine.getNormalised().size());
    assertTrue(commandLine.getNormalised().contains("value1"));
    assertTrue(commandLine.getNormalised().contains("value2"));
  }

}