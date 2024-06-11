import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

  @Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    Object value = "testValue";
    commandLine.addValue(option, value);
    assertTrue(commandLine.getValues(option).contains(value));
  }

  @Test
  public void testGetArgumentValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    Object value1 = "testValue1";
    Object value2 = "testValue2";
    commandLine.addValue(option, value1);
    commandLine.addValue(option, value2);
    assertTrue(commandLine.getArgumentValues(option).contains(value1));
    assertTrue(commandLine.getArgumentValues(option).contains(value2));
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    List defaultValues = new ArrayList();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    commandLine.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, commandLine.getDefaultValues(option));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertEquals(value, commandLine.getSwitchValue(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    Option option = new Option("testOption");
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(property, value);
    assertEquals(value, commandLine.getPropertyValue(property));
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String argument = "-testOption";
    assertTrue(commandLine.looksLikeOption(argument));
  }
}