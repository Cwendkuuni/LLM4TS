import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setup() {
    Option argOption = new Argument();
    Option switchOption = new Switch();
    
    List<String> argumentList = new ArrayList<String>();
    argumentList.add("arg1");
    argumentList.add("arg2");
    
    commandLine = new WriteableCommandLineImpl(argOption, argumentList);
    commandLine.addOption(switchOption);
    
    List<String> defaults = new ArrayList<String>();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(argOption, defaults);
  }
  
  @Test
  public void testAddOption() {
    Option option = new Argument();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testAddValue() {
    Option argOption = new Argument();
    String value = "value1";
    commandLine.addValue(argOption, value);
    
    List<String> valueList = commandLine.getValues(argOption, null);
    assertTrue(valueList.contains(value));
  }
  
  @Test
  public void testAddSwitch() {
    Option switchOption = new Switch();
    boolean value = true;
    commandLine.addSwitch(switchOption, value);
    
    Boolean switchValue = commandLine.getSwitch(switchOption, null);
    assertEquals(value, switchValue.booleanValue());
  }
  
  @Test
  public void testHasOption() {
    Option option = new Argument();
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = commandLine.getOption("arg1");
    assertNotNull(option);
  }
  
  @Test
  public void testGetValues() {
    Option argOption = new Argument();
    List<String> defaultValues = new ArrayList<String>();
    defaultValues.add("default1");
    defaultValues.add("default2");
    
    List<String> values = commandLine.getValues(argOption, defaultValues);
    assertTrue(values.contains("arg1"));
    assertTrue(values.contains("arg2"));
    assertTrue(values.contains("default1"));
    assertTrue(values.contains("default2"));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Option argOption = new Argument();
    List<String> values = commandLine.getUndefaultedValues(argOption);
    assertTrue(values.contains("arg1"));
    assertTrue(values.contains("arg2"));
  }
  
  @Test
  public void testGetSwitch() {
    Option switchOption = new Switch();
    commandLine.addSwitch(switchOption, true);
    
    Boolean switchValue = commandLine.getSwitch(switchOption, false);
    assertTrue(switchValue.booleanValue());
  }
  
  @Test
  public void testGetProperty() {
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(property, value);
    
    String retrievedValue = commandLine.getProperty(property);
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testAddProperty() {
    Option propertyOption = new PropertyOption();
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(propertyOption, property, value);
    
    Set<String> properties = commandLine.getProperties(propertyOption);
    assertTrue(properties.contains(property));
  }
  
  @Test
  public void testGetPropertyWithDefault() {
    Option propertyOption = new PropertyOption();
    String property = "property1";
    String value = "value1";
    commandLine.addProperty(propertyOption, property, value);
    
    String retrievedValue = commandLine.getProperty(propertyOption, property, "default");
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testGetPropertyWithDefaultWhenNull() {
    Option propertyOption = new PropertyOption();
    String property = "property1";
    commandLine.addProperty(propertyOption, property, null);
    
    String retrievedValue = commandLine.getProperty(propertyOption, property, "default");
    assertEquals("default", retrievedValue);
  }
  
  @Test
  public void testGetProperties() {
    Option propertyOption = new PropertyOption();
    String property1 = "property1";
    String property2 = "property2";
    String value = "value1";
    commandLine.addProperty(propertyOption, property1, value);
    commandLine.addProperty(propertyOption, property2, value);
    
    Set<String> properties = commandLine.getProperties(propertyOption);
    assertTrue(properties.contains(property1));
    assertTrue(properties.contains(property2));
  }
  
  @Test
  public void testLooksLikeOption() {
    String trigger = "-o";
    assertTrue(commandLine.looksLikeOption(trigger));
  }
  
  @Test
  public void testToString() {
    String expected = "arg1 arg2";
    String actual = commandLine.toString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetOptions() {
    List<Option> options = commandLine.getOptions();
    assertEquals(2, options.size());
  }
  
  @Test
  public void testGetOptionTriggers() {
    Set<String> triggers = commandLine.getOptionTriggers();
    assertEquals(2, triggers.size());
  }
  
  @Test
  public void testSetDefaultValues() {
    Option argOption = new Argument();
    List<String> defaults = new ArrayList<String>();
    defaults.add("default1");
    commandLine.setDefaultValues(argOption, defaults);
    
    List<String> values = commandLine.getValues(argOption, null);
    assertTrue(values.contains("arg1"));
    assertTrue(values.contains("arg2"));
    assertTrue(values.contains("default1"));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option switchOption = new Switch();
    commandLine.setDefaultSwitch(switchOption, true);
    
    Boolean switchValue = commandLine.getSwitch(switchOption, null);
    assertTrue(switchValue.booleanValue());
  }
  
  @Test
  public void testGetNormalised() {
    List<String> normalised = commandLine.getNormalised();
    assertEquals(2, normalised.size());
    assertTrue(normalised.contains("arg1"));
    assertTrue(normalised.contains("arg2"));
  }
  
}