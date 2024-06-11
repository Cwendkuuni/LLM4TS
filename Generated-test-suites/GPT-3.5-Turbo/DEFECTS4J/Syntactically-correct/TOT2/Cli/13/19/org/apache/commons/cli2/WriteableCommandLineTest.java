import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

  @Test
  public void testAddOption() {
    // Test adding an option to the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    // Test adding a value to an option in the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    Object value = "value";
    commandLine.addValue(option, value);
    
    assertTrue(commandLine.hasValue(option, value));
  }

  @Test
  public void testGetArgumentValues() {
    // Test retrieving argument values for an option in the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    List argumentValues = commandLine.getArgumentValues(option);
    
    assertNotNull(argumentValues);
    assertEquals(0, argumentValues.size());
  }

  @Test
  public void testSetDefaultValues() {
    // Test setting default values for an option in the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    List defaultValues = new ArrayList();
    defaultValues.add("default");
    commandLine.setDefaultValues(option, defaultValues);
    
    assertEquals(defaultValues, commandLine.getDefaultValues(option));
  }

  @Test
  public void testAddSwitch() {
    // Test adding a switch value to an option in the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    boolean value = true;
    commandLine.addSwitch(option, value);
    
    assertTrue(commandLine.getSwitch(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    // Test setting default switch value for an option in the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("test");
    boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    assertTrue(commandLine.getDefaultSwitch(option));
  }

  @Test
  public void testAddProperty() {
    // Test adding a property value to the command line
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String property = "test";
    String value = "value";
    commandLine.addProperty(property, value);
    
    assertEquals(value, commandLine.getProperty(property));
  }

  @Test
  public void testLooksLikeOption() {
    // Test checking if an argument looks like an option trigger
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String argument = "-test";
    boolean result = commandLine.looksLikeOption(argument);
    
    assertTrue(result);
  }
}