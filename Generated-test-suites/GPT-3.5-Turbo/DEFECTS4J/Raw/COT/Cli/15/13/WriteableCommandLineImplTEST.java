import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setup() {
    // Create an instance of the WriteableCommandLineImpl class with a root Option and arguments
    Option rootOption = new Option("test");
    List arguments = new ArrayList();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("option");
    
    // Add the option to the command line
    commandLine.addOption(option);
    
    // Check if the option is added correctly
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testAddValue() {
    Argument argument = new Argument("arg");
    String value = "value";
    
    // Add the value to the command line
    commandLine.addValue(argument, value);
    
    // Check if the value is added correctly
    assertTrue(commandLine.getValues(argument, null).contains(value));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("switch");
    boolean value = true;
    
    // Add the switch to the command line
    commandLine.addSwitch(option, value);
    
    // Check if the switch is added correctly
    assertEquals(value, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testHasOption() {
    Option option = new Option("option");
    
    // Add the option to the command line
    commandLine.addOption(option);
    
    // Check if the command line has the option
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = new Option("option");
    
    // Add the option to the command line
    commandLine.addOption(option);
    
    // Get the option from the command line
    Option result = commandLine.getOption("option");
    
    // Check if the retrieved option is correct
    assertEquals(option, result);
  }
  
  @Test
  public void testGetValues() {
    Argument argument = new Argument("arg");
    List defaultValues = new ArrayList();
    defaultValues.add("default");
    
    // Add a value to the command line
    commandLine.addValue(argument, "value");
    
    // Get the values for the argument from the command line
    List values = commandLine.getValues(argument, defaultValues);
    
    // Check if the retrieved values are correct
    assertEquals(1, values.size());
    assertEquals("value", values.get(0));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Argument argument = new Argument("arg");
    
    // Add a value to the command line
    commandLine.addValue(argument, "value");
    
    // Get the undefaulted values for the argument from the command line
    List values = commandLine.getUndefaultedValues(argument);
    
    // Check if the retrieved values are correct
    assertEquals(1, values.size());
    assertEquals("value", values.get(0));
  }
  
  @Test
  public void testGetSwitch() {
    Option option = new Option("switch");
    Boolean defaultValue = false;
    
    // Add a switch to the command line
    commandLine.addSwitch(option, true);
    
    // Get the switch value for the option from the command line
    Boolean switchValue = commandLine.getSwitch(option, defaultValue);
    
    // Check if the retrieved switch value is correct
    assertTrue(switchValue);
  }
  
  @Test
  public void testGetProperty() {
    String property = "property";
    
    // Add a property to the command line
    commandLine.addProperty(property, "value");
    
    // Get the property value from the command line
    String propertyValue = commandLine.getProperty(property);
    
    // Check if the retrieved property value is correct
    assertEquals("value", propertyValue);
  }
  
  @Test
  public void testAddProperty() {
    Option option = new Option("option");
    String property = "property";
    
    // Add a property to the command line
    commandLine.addProperty(option, property, "value");
    
    // Get the properties for the option from the command line
    Set properties = commandLine.getProperties(option);
    
    // Check if the property is added correctly
    assertTrue(properties.contains(property));
  }
  
  @Test
  public void testGetPropertyWithDefaultValue() {
    String property = "property";
    String defaultValue = "default";
    
    // Get the property value from the command line with a default value
    String propertyValue = commandLine.getProperty(property, defaultValue);
    
    // Check if the retrieved property value is the default value
    assertEquals(defaultValue, propertyValue);
  }
  
  @Test
  public void testGetProperties() {
    Option option = new Option("option");
    String property = "property";
    
    // Add a property to the command line
    commandLine.addProperty(option, property, "value");
    
    // Get the properties from the command line
    Set properties = commandLine.getProperties();
    
    // Check if the retrieved properties contain the property
    assertTrue(properties.contains(property));
  }
  
  @Test
  public void testLooksLikeOptionValidTrigger() {
    String trigger = "-option";
    
    // Check if the trigger looks like an option
    assertTrue(commandLine.looksLikeOption(trigger));
  }
    
  @Test
  public void testLooksLikeOptionInvalidTrigger() {
    String trigger = "option";
    
    // Check if the trigger looks like an option
    assertFalse(commandLine.looksLikeOption(trigger));
  }
}