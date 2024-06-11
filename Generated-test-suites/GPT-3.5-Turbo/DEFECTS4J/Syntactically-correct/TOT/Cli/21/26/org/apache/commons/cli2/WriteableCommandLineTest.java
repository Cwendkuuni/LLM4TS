import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {

  private WriteableCommandLine commandLine;

  @Before
  public void setup() {
    commandLine = new WriteableCommandLineImpl();
  }

  @Test
  public void testAddOption() {
    Option option = new Option("f", "file", false, "A file option");
    commandLine.addOption(option);
    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("v", "value", true, "A value option");
    Object value = "testValue";
    commandLine.addValue(option, value);
    List<Object> values = commandLine.getValues(option);
    assertTrue(values.contains(value));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("d", "default", true, "A default option");
    List<Object> defaultValues = List.of("defaultValue1", "defaultValue2");
    commandLine.setDefaultValues(option, defaultValues);
    List<Object> values = commandLine.getUndefaultedValues(option);
    assertEquals(0, values.size());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("v", "value", true, "A value option");
    List<Object> defaultValues = List.of("defaultValue1", "defaultValue2");
    commandLine.setDefaultValues(option, defaultValues);
    List<Object> values = commandLine.getValues(option);
    assertEquals(defaultValues, values);
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("s", "switch", false, "A switch option");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.hasSwitch(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("s", "switch", false, "A switch option");
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getDefaultSwitch(option));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("p", "property", true, "A property option");
    String propertyName = "testProperty";
    String propertyValue = "testValue";
    commandLine.addProperty(option, propertyName, propertyValue);
    String retrievedValue = commandLine.getProperty(option, propertyName);
    assertEquals(propertyValue, retrievedValue);
  }

  @Test
  public void testAddPropertyToDefaultSet() {
    String propertyName = "testProperty";
    String propertyValue = "testValue";
    commandLine.addProperty(propertyName, propertyValue);
    String retrievedValue = commandLine.getProperty(propertyName);
    assertEquals(propertyValue, retrievedValue);
  }

  @Test
  public void testLooksLikeOption() {
    String argument = "-f";
    assertTrue(commandLine.looksLikeOption(argument));
  }

  @Test
  public void testSetCurrentOption() {
    Option option = new Option("o", "option", false, "An option");
    commandLine.setCurrentOption(option);
    assertEquals(option, commandLine.getCurrentOption());
  }
}