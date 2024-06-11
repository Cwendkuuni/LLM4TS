import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  private Option option;
  private List arguments;
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    option = new Option("o", "option", true, "Option");
    arguments = new ArrayList<>();
    arguments.add("-o");
    commandLine = new WriteableCommandLineImpl(option, arguments);
  }

  @Test
  public void testAddOption() {
    Option anotherOption = new Option("n", "newOption", false, "New Option");
    commandLine.addOption(anotherOption);

    assertTrue(commandLine.hasOption(anotherOption));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option, "value1");

    assertEquals(1, commandLine.getValues(option, null).size());
    assertEquals("value1", commandLine.getValues(option, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    assertEquals(option, commandLine.getOption("o"));
  }

  @Test
  public void testGetValues() {
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    List<String> expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");

    assertEquals(expectedValues, commandLine.getValues(option, null));
  }

  @Test
  public void testGetUndefaultedValues() {
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    List<String> expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");

    assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetProperty() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);

    assertEquals(value, commandLine.getProperty(option, property, null));
  }

  @Test
  public void testAddProperty() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);

    Properties properties = commandLine.optionToProperties.get(option);
    assertNotNull(properties);
    assertEquals(value, properties.getProperty(property));
  }

  @Test
  public void testGetProperty_DefaultValue() {
    String property = "property";
    String defaultValue = "defaultValue";

    assertNull(commandLine.getProperty(option, property, defaultValue));
    assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
  }

  @Test
  public void testGetProperties() {
    String property1 = "property1";
    String value1 = "value1";

    String property2 = "property2";
    String value2 = "value2";

    commandLine.addProperty(option, property1, value1);
    commandLine.addProperty(option, property2, value2);

    assertEquals(2, commandLine.getProperties(option).size());
    assertTrue(commandLine.getProperties(option).contains(property1));
    assertTrue(commandLine.getProperties(option).contains(property2));
  }

  @Test
  public void testGetProperties_NoProperties() {
    assertEquals(0, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-o"));
    assertFalse(commandLine.looksLikeOption("value"));
  }

  @Test
  public void testToString() {
    assertEquals("-o", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option anotherOption = new Option("n", "newOption", false, "New Option");
    commandLine.addOption(anotherOption);

    List<Option> expectedOptions = new ArrayList<>();
    expectedOptions.add(option);
    expectedOptions.add(anotherOption);

    assertEquals(expectedOptions, commandLine.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    Option anotherOption = new Option("n", "newOption", false, "New Option");
    commandLine.addOption(anotherOption);

    assertTrue(commandLine.getOptionTriggers().contains("o"));
    assertTrue(commandLine.getOptionTriggers().contains("n"));
  }

  @Test
  public void testSetDefaultValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("value1");
    defaultValues.add("value2");

    commandLine.setDefaultValues(option, defaultValues);

    assertEquals(defaultValues, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    List<String> expectedArguments = new ArrayList<>();
    expectedArguments.add("-o");

    assertEquals(expectedArguments, commandLine.getNormalised());
  }
}