import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  @Test
  public void testAddOption() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    Object value = "value";
    commandLine.addValue(option, value);
    assertEquals(value, commandLine.getValues(option, null).get(0));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testHasOption() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    assertFalse(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    assertEquals(option, commandLine.getOption("arg"));
  }
  
  @Test
  public void testGetValues() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    Object value = "value";
    commandLine.addValue(option, value);
    assertEquals(value, commandLine.getValues(option, null).get(0));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    Object value = "value";
    commandLine.addValue(option, value);
    assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
  }
  
  @Test
  public void testGetSwitch() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetProperty() {
    Option option = new PropertyOption();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);
    assertEquals(value, commandLine.getProperty(option, property, null));
  }
  
  @Test
  public void testAddProperty() {
    Option option = new PropertyOption();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);
    assertEquals(value, commandLine.getProperty(option, property, null));
  }
  
  @Test
  public void testGetProperty() {
    Option option = new PropertyOption();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String property = "property";
    String defaultValue = "defaultValue";
    assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
  }
  
  @Test
  public void testGetProperties() {
    Option option = new PropertyOption();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String property = "property";
    commandLine.addProperty(option, property, "value");
    assertTrue(commandLine.getProperties(option).contains(property));
  }
  
  @Test
  public void testGetProperties() {
    Option option = new PropertyOption();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String property = "property";
    commandLine.addProperty(option, property, "value");
    assertTrue(commandLine.getProperties().contains(property));
  }
  
  @Test
  public void testLooksLikeOption() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    assertTrue(commandLine.looksLikeOption("-arg"));
  }
  
  @Test
  public void testToString() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    String arg = "arg value";
    commandLine.addValue(option, arg);
    assertEquals("\"arg value\"", commandLine.toString());
  }
  
  @Test
  public void testGetOptions() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testGetOptionTriggers() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    assertTrue(commandLine.getOptionTriggers().contains("arg"));
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    List defaults = new ArrayList();
    defaults.add("default");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(defaults, commandLine.getValues(option, null));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetNormalised() {
    Option option = new Argument("arg");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addValue(option, "arg value");
    commandLine.addValue(option, "arg2 value2");
    List normalised = new ArrayList();
    normalised.add("arg");
    normalised.add("arg2");
    assertEquals(normalised, commandLine.getNormalised());
  }
}