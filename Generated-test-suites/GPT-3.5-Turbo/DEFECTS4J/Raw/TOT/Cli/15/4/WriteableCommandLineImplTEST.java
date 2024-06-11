import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    Option option = new Argument();
    List<String> arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(option, arguments);
  }

  // Test addOption method
  @Test
  public void testAddOption() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  // Test addValue method
  @Test
  public void testAddValue() {
    Option option = new Argument();
    Object value = "value1";
    commandLine.addValue(option, value);
    assertEquals(value, commandLine.getValues(option, null).get(0));
  }

  // Test addSwitch method
  @Test
  public void testAddSwitch() {
    Option option = new Argument();
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertEquals(value, commandLine.getSwitch(option, null));
  }

  // Test hasOption method
  @Test
  public void testHasOption() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  // Test getOption method
  @Test
  public void testGetOption() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption(option.getPreferredName()));
  }

  // Test getValues method
  @Test
  public void testGetValues() {
    Option option = new Argument();
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(values, commandLine.getValues(option, null));
  }

  // Test getUndefaultedValues method
  @Test
  public void testGetUndefaultedValues() {
    Option option = new Argument();
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(values, commandLine.getUndefaultedValues(option));
  }

  // Test getSwitch method with option not present
  @Test
  public void testGetSwitchOptionNotPresent() {
    Option option = new Argument();
    boolean defaultValue = true;
    assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));
  }

  // Test getSwitch method with option present
  @Test
  public void testGetSwitchOptionPresent() {
    Option option = new Argument();
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertEquals(value, commandLine.getSwitch(option, null));
  }

  // Test getProperty method with property not present
  @Test
  public void testGetPropertyNotPresent() {
    String property = "property1";
    assertNull(commandLine.getProperty(property));
  }

  // Test addProperty method
  @Test
  public void testAddProperty() {
    Option option = new Argument();
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(option, property, value);
    assertEquals(value, commandLine.getProperty(option, property, null));
  }

  // Test getProperty method with property present
  @Test
  public void testGetPropertyPresent() {
    Option option = new Argument();
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(option, property, value);
    assertEquals(value, commandLine.getProperty(option, property, null));
  }

  // Test getProperties method with option not present
  @Test
  public void testGetPropertiesOptionNotPresent() {
    Option option = new Argument();
    assertEquals(0, commandLine.getProperties(option).size());
  }

  // Test getProperties method with option present
  @Test
  public void testGetPropertiesOptionPresent() {
    Option option = new Argument();
    commandLine.addProperty(option, "property1", "value1");
    assertEquals(1, commandLine.getProperties(option).size());
  }

  // Test looksLikeOption method with trigger that starts with prefix
  @Test
  public void testLooksLikeOptionStartsWithPrefix(){
    String trigger = "-a";
    assertTrue(commandLine.looksLikeOption(trigger));
  }

  // Test looksLikeOption method with trigger that does not start with prefix
  @Test
  public void testLooksLikeOptionNotStartsWithPrefix(){
    String trigger = "arg1";
    assertFalse(commandLine.looksLikeOption(trigger));
  }

  // Test toString method
  @Test
  public void testToString() {
    String expectedString = "arg1 arg2";
    assertEquals(expectedString, commandLine.toString());
  }

  // Test getOptions method
  @Test
  public void testGetOptions() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptions().size());
  }

  // Test getOptionTriggers method
  @Test
  public void testGetOptionTriggers() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptionTriggers().size());
  }

  // Test setDefaultValues method
  @Test
  public void testSetDefaultValues() {
    Option option = new Argument();
    List<String> defaults = new ArrayList<>();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(defaults, commandLine.getValues(option, null));
  }

  // Test setDefaultSwitch method
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Argument();
    boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
  }

  // Test getNormalised method
  @Test
  public void testGetNormalised() {
    List<String> arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    assertEquals(arguments, commandLine.getNormalised());
  }
}