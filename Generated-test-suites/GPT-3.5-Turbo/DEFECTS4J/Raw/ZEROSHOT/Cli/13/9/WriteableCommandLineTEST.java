import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
  
  private WriteableCommandLine commandLine;
  
  @Before
  public void setup() {
    commandLine = new WriteableCommandLineImpl();
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("test");
    commandLine.addOption(option);
    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new Option("test");
    Integer value = 10;
    commandLine.addValue(option, value);
    List<Object> values = commandLine.getOptionValues(option);
    assertTrue(values.contains(value));
  }
  
  @Test
  public void testGetArgumentValues() {
    Option option = new Option("test");
    List<Object> values = commandLine.getArgumentValues(option);
    assertEquals(0, values.size());
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new Option("test");
    List<Object> defaultValues = Arrays.asList("default1", "default2");
    commandLine.setDefaultValues(option, defaultValues);
    List<Object> values = commandLine.getOptionValues(option);
    assertEquals(defaultValues, values);
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("test");
    commandLine.addSwitch(option, true);
    Boolean switchValue = commandLine.getSwitchValue(option);
    assertTrue(switchValue);
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("test");
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    Boolean switchValue = commandLine.getSwitchValue(option);
    assertEquals(defaultSwitch, switchValue);
  }
  
  @Test
  public void testAddProperty() {
    String property = "test";
    String value = "value";
    commandLine.addProperty(property, value);
    String propertyValue = commandLine.getPropertyValue(property);
    assertEquals(value, propertyValue);
  }
  
  @Test
  public void testLooksLikeOption() {
    String argument = "-test";
    boolean result = commandLine.looksLikeOption(argument);
    assertTrue(result);
  }
}