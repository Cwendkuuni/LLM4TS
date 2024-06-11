import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setUp() {
    Option rootOption = new Option("root", "Root Option", true);
    Argument argument1 = new Argument("arg1", "Argument 1", true);
    Argument argument2 = new Argument("arg2", "Argument 2", true);
    List<Argument> arguments = new ArrayList<>();
    arguments.add(argument1);
    arguments.add(argument2);
    
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }
  
  // test addOption method
  @Test
  public void testAddOption() {
    Option option = new Option("opt1", "Option 1", true);
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  // test addValue method
  @Test
  public void testAddValue() {
    Option option = new Option("opt1", "Option 1", true);
    Object value = "value1";
    commandLine.addValue(option, value);
    
    List<Object> values = commandLine.getValues(option, null);
    assertEquals(1, values.size());
    assertEquals(value, values.get(0));
  }
  
  // test addSwitch method
  @Test
  public void testAddSwitch() {
    Option option = new Option("opt1", "Option 1", true);
    boolean switchValue = true;
    commandLine.addSwitch(option, switchValue);
    
    Boolean switchResult = commandLine.getSwitch(option, null);
    assertEquals(true, switchResult);
  }
  
  // test hasOption method
  @Test
  public void testHasOption() {
    Option option = new Option("opt1", "Option 1", true);
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  // test getOption method
  @Test
  public void testGetOption() {
    Option option = new Option("opt1", "Option 1", true);
    commandLine.addOption(option);
    
    Option result = commandLine.getOption(option.getPreferredName());
    assertEquals(option, result);
  }
  
  // test getValues method
  @Test
  public void testGetValues() {
    Option option = new Option("opt1", "Option 1", true);
    Object value1 = "value1";
    Object value2 = "value2";
    commandLine.addValue(option, value1);
    commandLine.addValue(option, value2);
    
    List<Object> result = commandLine.getValues(option, null);
    assertEquals(2, result.size());
    assertEquals(value1, result.get(0));
    assertEquals(value2, result.get(1));
  }
  
  // test getUndefaultedValues method
  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("opt1", "Option 1", true);
    Object value1 = "value1";
    Object value2 = "value2";
    commandLine.addValue(option, value1);
    commandLine.addValue(option, value2);
    
    List<Object> result = commandLine.getUndefaultedValues(option);
    assertEquals(2, result.size());
    assertEquals(value1, result.get(0));
    assertEquals(value2, result.get(1));
  }
  
  // test getSwitch method
  @Test
  public void testGetSwitch() {
    Option option = new Option("opt1", "Option 1", true);
    boolean switchValue = true;
    commandLine.addSwitch(option, switchValue);
    
    Boolean switchResult = commandLine.getSwitch(option, null);
    assertEquals(true, switchResult);
  }
  
  // test getProperty method
  @Test
  public void testGetProperty() {
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(property, value);
    
    String propertyResult = commandLine.getProperty(property);
    assertEquals(value, propertyResult);
  }
  
  // test addProperty method
  @Test
  public void testAddProperty() {
    Option option = new Option("opt1", "Option 1", true);
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(option, property, value);
    
    String propertyResult = commandLine.getProperty(option, property, null);
    assertEquals(value, propertyResult);
  }
  
  // test getProperty method when property does not exist
  @Test
  public void testGetPropertyWhenNotExist() {
    String propertyResult = commandLine.getProperty("nonexistent");
    assertNull(propertyResult);
  }
  
  // test getProperties method
  @Test
  public void testGetProperties() {
    Option option = new Option("opt1", "Option 1", true);
    String property1 = "property1";
    String property2 = "property2";
    commandLine.addProperty(option, property1, "value1");
    commandLine.addProperty(option, property2, "value2");
    
    Set<String> properties = commandLine.getProperties(option);
    assertEquals(2, properties.size());
    assertTrue(properties.contains(property1));
    assertTrue(properties.contains(property2));
  }
  
  // test getProperties method when no properties
  @Test
  public void testGetPropertiesWhenEmpty() {
    Set<String> properties = commandLine.getProperties();
    assertEquals(0, properties.size());
  }
  
  // test looksLikeOption method
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-opt1"));
    assertFalse(commandLine.looksLikeOption("value"));
  }
  
  // test toString method
  @Test
  public void testToString() {
    commandLine.addValue(commandLine.getRootOption(), "value");
    commandLine.addValue(commandLine.getRootOption(), "value2");
    
    String result = commandLine.toString();
    assertEquals("value value2", result);
  }
  
  // test getOptions method
  @Test
  public void testGetOptions() {
    Option option = new Option("opt1", "Option 1", true);
    commandLine.addOption(option);
    
    List<Option> options = commandLine.getOptions();
    assertEquals(1, options.size());
    assertEquals(option, options.get(0));
  }
  
  // test getOptionTriggers method
  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("opt1", "Option 1", true);
    commandLine.addOption(option);
    
    Set<String> triggers = commandLine.getOptionTriggers();
    assertEquals(1, triggers.size());
    assertTrue(triggers.contains(option.getPreferredName()));
  }
  
  // test setDefaultValues method
  @Test
  public void testSetDefaultValues() {
    Option option = new Option("opt1", "Option 1", true);
    List<Object> defaults = Arrays.asList("value1", "value2");
    commandLine.setDefaultValues(option, defaults);
    
    List<Object> result = commandLine.getValues(option, null);
    assertEquals(2, result.size());
    assertEquals("value1", result.get(0));
    assertEquals("value2", result.get(1));
  }
  
  // test setDefaultValues method when defaults is null
  @Test
  public void testSetDefaultValuesWhenNull() {
    Option option = new Option("opt1", "Option 1", true);
    List<Object> defaults = Arrays.asList("value1", "value2");
    commandLine.setDefaultValues(option, defaults);
    
    commandLine.setDefaultValues(option, null);
    
    List<Object> result = commandLine.getValues(option, null);
    assertEquals(2, result.size());
    assertEquals("value1", result.get(0));
    assertEquals("value2", result.get(1));
  }
  
  // test setDefaultSwitch method
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("opt1", "Option 1", true);
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    Boolean result = commandLine.getSwitch(option, null);
    assertEquals(true, result);
  }
  
  // test setDefaultSwitch method when defaultSwitch is null
  @Test
  public void testSetDefaultSwitchWhenNull() {
    Option option = new Option("opt1", "Option 1", true);
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    commandLine.setDefaultSwitch(option, null);
    
    Boolean result = commandLine.getSwitch(option, null);
    assertEquals(true, result);
  }
  
  // test getNormalised method
  @Test
  public void testGetNormalised() {
    List<String> arguments = Arrays.asList("value1", "value2");
    List<Option> options = new ArrayList<>();
    options.add(commandLine.getRootOption());
    
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(commandLine.getRootOption(), arguments, options);
    
    List<String> result = commandLine.getNormalised();
    assertEquals(2, result.size());
    assertEquals("value1", result.get(0));
    assertEquals("value2", result.get(1));
  }
}