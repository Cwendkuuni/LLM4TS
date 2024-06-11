import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  private Option testOption;
  private Option argumentOption;
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    testOption = new Option("test");
    argumentOption = new Option("argument");

    List<String> arguments = new ArrayList<>();
    arguments.add("argument1");
    arguments.add("argument2");

    commandLine = new WriteableCommandLineImpl(testOption, arguments);
  }
  
  @Test
  public void testAddOption() {
    commandLine.addOption(testOption);

    assertTrue(commandLine.hasOption(testOption));
    assertTrue(commandLine.getOptions().contains(testOption));
    assertEquals(testOption, commandLine.getOption("test"));
  }
  
  @Test
  public void testAddValue() {
    commandLine.addValue(argumentOption, "value1");
    commandLine.addValue(argumentOption, "value2");

    List<String> expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");

    assertEquals(expectedValues, commandLine.getValues(argumentOption, null));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(testOption, true);

    assertTrue(commandLine.hasOption(testOption));
    assertTrue(commandLine.getSwitch(testOption, false));
  }

  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(testOption));

    commandLine.addOption(testOption);

    assertTrue(commandLine.hasOption(testOption));
  }

  @Test
  public void testGetOption() {
    assertNull(commandLine.getOption("test"));

    commandLine.addOption(testOption);

    assertEquals(testOption, commandLine.getOption("test"));
  }

  @Test
  public void testGetValues() {
    List<String> expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");

    commandLine.addValue(argumentOption, "value1");
    commandLine.addValue(argumentOption, "value2");

    assertEquals(expectedValues, commandLine.getValues(argumentOption, null));
  }

  @Test
  public void testGetUndefaultedValues() {
    List<String> expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");

    commandLine.addValue(argumentOption, "value1");
    commandLine.addValue(argumentOption, "value2");

    assertEquals(expectedValues, commandLine.getUndefaultedValues(argumentOption));
  }

  @Test
  public void testGetSwitch() {
    assertFalse(commandLine.getSwitch(testOption, false));

    commandLine.addSwitch(testOption, true);

    assertTrue(commandLine.getSwitch(testOption, false));
  }

  @Test
  public void testGetProperty() {
    assertNull(commandLine.getProperty("property"));

    commandLine.addProperty("property", "value");

    assertEquals("value", commandLine.getProperty("property"));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty(testOption, "property", "value");

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("property", "value");

    assertEquals(expectedProperties, commandLine.getProperties(testOption));
  }

  @Test
  public void testGetPropertyWithDefaultValue() {
    assertEquals("default", commandLine.getProperty(testOption, "property", "default"));

    commandLine.addProperty(testOption, "property", "value");

    assertEquals("value", commandLine.getProperty(testOption, "property", "default"));
  }

  @Test
  public void testGetProperties() {
    assertEquals(0, commandLine.getProperties().size());

    commandLine.addProperty(testOption, "property1", "value1");
    commandLine.addProperty(testOption, "property2", "value2");

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("property1", "value1");
    expectedProperties.setProperty("property2", "value2");

    assertEquals(expectedProperties.keySet(), commandLine.getProperties().keySet());
  }

  @Test
  public void testLooksLikeOption() {
    assertFalse(commandLine.looksLikeOption("argument1"));

    assertTrue(commandLine.looksLikeOption("--test"));
    assertTrue(commandLine.looksLikeOption("-t"));
    assertTrue(commandLine.looksLikeOption("/test"));
  }

  @Test
  public void testToString() {
    String expectedToString = "argument1 argument2";

    assertEquals(expectedToString, commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    assertEquals(0, commandLine.getOptions().size());

    commandLine.addOption(testOption);
    commandLine.addOption(argumentOption);

    assertTrue(commandLine.getOptions().contains(testOption));
    assertTrue(commandLine.getOptions().contains(argumentOption));
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(0, commandLine.getOptionTriggers().size());

    commandLine.addOption(testOption);
    commandLine.addOption(argumentOption);

    assertTrue(commandLine.getOptionTriggers().contains("test"));
    assertTrue(commandLine.getOptionTriggers().contains("argument"));
  }

  @Test
  public void testSetDefaultValues() {
    List<String> expectedDefaultValues = new ArrayList<>();
    expectedDefaultValues.add("default1");
    expectedDefaultValues.add("default2");

    commandLine.setDefaultValues(argumentOption, expectedDefaultValues);

    assertEquals(expectedDefaultValues, commandLine.getValues(argumentOption, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(testOption, true);

    assertTrue(commandLine.getSwitch(testOption, false));
  }

  @Test
  public void testGetNormalised() {
    List<String> expectedNormalised = new ArrayList<>();
    expectedNormalised.add("argument1");
    expectedNormalised.add("argument2");

    assertEquals(expectedNormalised, commandLine.getNormalised());
  }
  
}