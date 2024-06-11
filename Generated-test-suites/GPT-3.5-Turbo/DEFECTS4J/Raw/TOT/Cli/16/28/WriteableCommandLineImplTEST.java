import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setUp() {
    Option rootOption = new Option("rootOption");
    List arguments = new ArrayList();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new Option("option");
    String value = "value";
    commandLine.addValue(option, value);
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains(value));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("option");
    boolean value = true;
    commandLine.addSwitch(option, value);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertEquals(Boolean.valueOf(value), switchValue);
  }
  
  @Test
  public void testHasOption() {
    Option option = new Option("option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = new Option("option");
    commandLine.addOption(option);
    Option retrievedOption = commandLine.getOption("option");
    assertEquals(option, retrievedOption);
  }
  
  @Test
  public void testGetValuesWithDefaultValues() {
    Option option = new Option("option");
    String defaultValue = "default";
    commandLine.setDefaultValues(option, List.of(defaultValue));
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains(defaultValue));
  }
  
  @Test
  public void testGetValuesWithoutDefaultValues() {
    Option option = new Option("option");
    List<String> values = commandLine.getValues(option, null);
    assertEquals(0, values.size());
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("option");
    String value = "value";
    commandLine.addValue(option, value);
    List<String> values = commandLine.getUndefaultedValues(option);
    assertTrue(values.contains(value));
  }
  
  @Test
  public void testGetSwitchWithDefaultValue() {
    Option option = new Option("option");
    boolean defaultValue = true;
    commandLine.setDefaultSwitch(option, defaultValue);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertEquals(Boolean.valueOf(defaultValue), switchValue);
  }
  
  @Test
  public void testGetSwitchWithoutDefaultValue() {
    Option option = new Option("option");
    Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
    assertEquals(Boolean.FALSE, switchValue);
  }
  
  @Test
  public void testGetProperty() {
    String property = "property";
    String value = "value";
    Properties properties = new Properties();
    properties.setProperty(property, value);
    Option option = new PropertyOption();
    commandLine.addProperty(option, property, value);
    String retrievedValue = commandLine.getProperty(option, property, null);
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testGetProperties() {
    String property = "property";
    String value = "value";
    Properties properties = new Properties();
    properties.setProperty(property, value);
    Option option = new PropertyOption();
    commandLine.addProperty(option, property, value);
    Set<String> retrievedProperties = commandLine.getProperties(option);
    assertEquals(properties.keySet(), retrievedProperties);
  }
  
  @Test
  public void testLooksLikeOption() {
    String trigger = "-trigger";
    boolean looksLikeOption = commandLine.looksLikeOption(trigger);
    assertTrue(looksLikeOption);
  }
  
  @Test
  public void testToString() {
    String argument1 = "argument1";
    String argument2 = "argument2";
    List<String> arguments = List.of(argument1, argument2);
    String expectedString = argument1 + " " + argument2;
    commandLine = new WriteableCommandLineImpl(null, arguments);
    assertEquals(expectedString, commandLine.toString());
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("option1");
    Option option2 = new Option("option2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }
  
  @Test
  public void testGetOptionTriggers() {
    Option option1 = new Option("option1");
    Option option2 = new Option("option2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    Set<String> triggers = commandLine.getOptionTriggers();
    assertTrue(triggers.contains(option1.getPreferredName()));
    assertTrue(triggers.contains(option2.getPreferredName()));
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new Option("option");
    String defaultValue = "default";
    commandLine.setDefaultValues(option, List.of(defaultValue));
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains(defaultValue));
    commandLine.setDefaultValues(option, null);
    values = commandLine.getValues(option, null);
    assertEquals(0, values.size());
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("option");
    boolean defaultValue = true;
    commandLine.setDefaultSwitch(option, defaultValue);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertEquals(Boolean.valueOf(defaultValue), switchValue);
    commandLine.setDefaultSwitch(option, null);
    switchValue = commandLine.getSwitch(option, null);
    assertNull(switchValue);
  }
  
  @Test
  public void testGetNormalised() {
    Option rootOption = new Option("rootOption");
    List<String> arguments = List.of("argument1", "argument2");
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    List<String> normalised = commandLine.getNormalised();
    assertEquals(arguments, normalised);
  }
}