import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;
  private Option option;
  private Argument argument;

  @Before
  public void setUp() {
    option = new Option("o", "option", false, "Option");
    argument = new Argument("arg", false);
    option.addValue("value");
    argument.addValue("argValue");

    commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue"));
    commandLine.addOption(option);
    commandLine.addValue(option, "value");
  }

  @Test
  public void testAddOption() {
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    List<String> values = commandLine.getValues(option, new ArrayList<>());
    assertEquals(Arrays.asList("value"), values);
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, false);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertFalse(switchValue);
  }

  @Test
  public void testHasOption() {
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option returnedOption = commandLine.getOption("o");
    assertEquals(option, returnedOption);
  }

  @Test
  public void testGetValues() {
    List<String> values = commandLine.getValues(option, new ArrayList<>());
    assertEquals(Arrays.asList("value"), values);
  }

  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option, true);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertTrue(switchValue);
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("property", "value");
    String propertyValue = commandLine.getProperty("property", null);
    assertEquals("value", propertyValue);
  }

  @Test
  public void testGetProperty() {
    commandLine.addProperty("property", "value");
    String propertyValue = commandLine.getProperty("property", null);
    assertEquals("value", propertyValue);
  }

  @Test
  public void testGetProperties() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    commandLine.addProperty("property3", "value3");
    assertEquals(3, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("o"));
    assertFalse(commandLine.looksLikeOption("arg"));
  }

  @Test
  public void testToString() {
    assertEquals("argValue", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(1, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
    List<String> defaultValues = commandLine.getValues(option, new ArrayList<>());
    assertEquals(Arrays.asList("default1", "default2"), defaultValues);
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, false);
    Boolean defaultSwitch = commandLine.getSwitch(option, null);
    assertFalse(defaultSwitch);
  }

  @Test
  public void testGetNormalised() {
    List<String> normalised = commandLine.getNormalised();
    assertEquals(Arrays.asList("argValue"), normalised);
  }
}