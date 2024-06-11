import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  private Option option;
  private List arguments;
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setup() {
    option = new Option("testOption");
    arguments = new ArrayList();
    commandLine = new WriteableCommandLineImpl(option, arguments);
  }
  
  @Test
  public void testAddOption() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Object value = "testValue";
    commandLine.addValue(option, value);
    List values = commandLine.getValues(option, new ArrayList());
    assertEquals(1, values.size());
    assertEquals(value, values.get(0));
  }
  
  @Test
  public void testAddSwitch() {
    boolean value = true;
    commandLine.addSwitch(option, value);
    Boolean switchValue = commandLine.getSwitch(option, false);
    assertTrue(switchValue);
  }
  
  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(option));
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option testOption = commandLine.getOption("testOption");
    assertEquals(option, testOption);
  }
  
  @Test
  public void testGetValues() {
    Object value1 = "value1";
    Object value2 = "value2";
    commandLine.addValue(option, value1);
    commandLine.addValue(option, value2);
    List values = commandLine.getValues(option, new ArrayList());
    assertEquals(2, values.size());
    assertEquals(value1, values.get(0));
    assertEquals(value2, values.get(1));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Object value = "testValue";
    commandLine.addValue(option, value);
    List undefaultedValues = commandLine.getUndefaultedValues(option);
    assertEquals(1, undefaultedValues.size());
    assertEquals(value, undefaultedValues.get(0));
  }
  
  @Test
  public void testGetSwitch() {
    assertFalse(commandLine.getSwitch(option, false));
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false)); 
  }
  
  @Test
  public void testGetProperty() {
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(option, property, value);
    String retrievedValue = commandLine.getProperty(option, property, "");
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testAddProperty() {
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(option, property, value);
    String retrievedValue = commandLine.getProperty(option, property, "");
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testGetPropertyWithDefault() {
    String property = "testProperty";
    String defaultValue = "defaultValue";
    String retrievedValue = commandLine.getProperty(option, property, defaultValue);
    assertEquals(defaultValue, retrievedValue);
  }
  
  @Test
  public void testGetProperties() {
    String property1 = "property1";
    String value1 = "value1";
    String property2 = "property2";
    String value2 = "value2";
    commandLine.addProperty(option, property1, value1);
    commandLine.addProperty(option, property2, value2);
    Set properties = commandLine.getProperties(option);
    assertEquals(2, properties.size());
    assertTrue(properties.contains(property1));
    assertTrue(properties.contains(property2));
  }
  
  @Test
  public void testGetPropertiesWithDefault() {
    String property = "property";
    Set properties = commandLine.getProperties(option);
    assertEquals(0, properties.size());
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-t"));
    assertFalse(commandLine.looksLikeOption("t"));
  }
  
  @Test
  public void testToString() {
    arguments.add("arg1");
    arguments.add("arg2");
    String expectedToString = "arg1 arg2";
    String toString = commandLine.toString();
    assertEquals(expectedToString, toString);
  }
  
  @Test
  public void testGetOptions() {
    List options = commandLine.getOptions();
    assertEquals(1, options.size());
    assertEquals(option, options.get(0));
  }
  
  @Test
  public void testGetOptionTriggers() {
    Set triggers = commandLine.getOptionTriggers();
    assertEquals(1, triggers.size());
    assertTrue(triggers.contains("testOption"));
  }
  
  @Test
  public void testSetDefaultValues() {
    List defaults = new ArrayList();
    defaults.add("defaultValue");
    commandLine.setDefaultValues(option, defaults);
    List defaultValues = commandLine.getValues(option, new ArrayList());
    assertEquals(defaults, defaultValues);
  }
  
  @Test
  public void testSetDefaultValuesWithNull() {
    List defaults = new ArrayList();
    defaults.add("defaultValue");
    commandLine.setDefaultValues(option, defaults);
    List defaultValues = commandLine.getValues(option, new ArrayList());
    assertEquals(defaults, defaultValues);
    commandLine.setDefaultValues(option, null);
    defaultValues = commandLine.getValues(option, new ArrayList());
    assertEquals(0, defaultValues.size());
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    Boolean switchValue = commandLine.getSwitch(option, false);
    assertEquals(defaultSwitch, switchValue);
  }
  
  @Test
  public void testSetDefaultSwitchWithNull() {
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    Boolean switchValue = commandLine.getSwitch(option, false);
    assertEquals(defaultSwitch, switchValue);
    commandLine.setDefaultSwitch(option, null);
    switchValue = commandLine.getSwitch(option, false);
    assertFalse(switchValue);
  }
  
  @Test
  public void testGetNormalised() {
    List normalised = commandLine.getNormalised();
    assertEquals(0, normalised.size());
  }
  
}