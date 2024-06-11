import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;
  private Option option;

  @Before
  public void setUp() {
    option = new Option("testOption");
    commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
  }

  @Test
  public void testAddOption() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains("value"));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("testOption"));
  }

  @Test
  public void testGetValues() {
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains("value"));
  }

  @Test
  public void testGetUndefaultedValues() {
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getUndefaultedValues(option);
    assertTrue(values.contains("value"));
  }

  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetProperty() {
    commandLine.addProperty(option, "testProperty", "testValue");
    assertEquals("testValue", commandLine.getProperty(option, "testProperty", null));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty(option, "testProperty", "testValue");
    assertEquals("testValue", commandLine.getProperty(option, "testProperty", null));
  }

  @Test
  public void testGetProperties() {
    commandLine.addProperty(option, "testProperty", "testValue");
    assertTrue(commandLine.getProperties(option).contains("testProperty"));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-testOption"));
    assertFalse(commandLine.looksLikeOption("testOption"));
  }

  @Test
  public void testToString() {
    assertEquals("", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    commandLine.addOption(option);
    assertTrue(commandLine.getOptionTriggers().contains("testOption"));
  }

  @Test
  public void testSetDefaultValues() {
    List<String> defaults = new ArrayList<>();
    defaults.add("defaultValue");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(defaults, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    List<String> normalised = new ArrayList<>();
    normalised.add("argument");
    commandLine = new WriteableCommandLineImpl(option, normalised);
    assertEquals(normalised, commandLine.getNormalised());
  }
}