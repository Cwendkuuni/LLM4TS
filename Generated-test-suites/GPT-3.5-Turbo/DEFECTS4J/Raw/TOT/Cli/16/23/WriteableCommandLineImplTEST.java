import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private WriteableCommandLine commandLine;
  
  @Before
  public void setUp() {
    Option option = new Option("name", "n", false, "Name option");
    Argument argument = new Argument();
    commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("age", "a", false, "Age option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Argument argument = new Argument();
    commandLine.addOption(argument);
    commandLine.addValue(argument, "John");
    List<String> values = commandLine.getValues(argument, null);
    assertEquals("John", values.get(0));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("flag", "f", true, "Flag option");
    commandLine.addSwitch(option, true);
    Boolean flag = commandLine.getSwitch(option, false);
    assertTrue(flag);
  }
  
  @Test
  public void testHasOption() {
    Option option = new Option("age", "a", false, "Age option");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = commandLine.getOption("n");
    assertEquals("name", option.getPreferredName());
  }
  
  @Test
  public void testGetValues() {
    Argument argument = new Argument();
    commandLine.addOption(argument);
    commandLine.addValue(argument, "John");
    List<String> values = commandLine.getValues(argument, null);
    assertEquals("John", values.get(0));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Argument argument = new Argument();
    commandLine.addOption(argument);
    List<String> values = commandLine.getUndefaultedValues(argument);
    assertTrue(values.isEmpty());
  }
  
  @Test
  public void testGetSwitch() {
    Option option = new Option("flag", "f", true, "Flag option");
    commandLine.addSwitch(option, true);
    Boolean flag = commandLine.getSwitch(option, false);
    assertTrue(flag);
  }
  
  @Test
  public void testGetProperty() {
    String property = commandLine.getProperty("property");
    assertNull(property);
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty("property", "value");
    String property = commandLine.getProperty("property");
    assertEquals("value", property);
  }
  
  @Test
  public void testGetPropertyWithDefault() {
    String property = commandLine.getProperty(new Option(), "property", "default");
    assertEquals("default", property);
  }
  
  @Test
  public void testGetProperties() {
    Set<String> properties = commandLine.getProperties();
    assertTrue(properties.isEmpty());
  }
  
  @Test
  public void testLooksLikeOption() {
    boolean looksLikeOption = commandLine.looksLikeOption("-name");
    assertTrue(looksLikeOption);
  }
  
  @Test
  public void testToString() {
    String commandLineString = commandLine.toString();
    assertEquals("", commandLineString);
  }
  
  @Test
  public void testGetOptions() {
    List<Option> options = commandLine.getOptions();
    assertTrue(options.isEmpty());
  }
  
  @Test
  public void testGetOptionTriggers() {
    Set<String> optionTriggers = commandLine.getOptionTriggers();
    assertTrue(optionTriggers.isEmpty());
  }
  
  @Test
  public void testSetDefaultValues() {
    Argument argument = new Argument();
    List<String> defaults = new ArrayList<String>();
    defaults.add("default1");
    commandLine.setDefaultValues(argument, defaults);
    List<String> values = commandLine.getValues(argument, null);
    assertEquals("default1", values.get(0));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("flag", "f", true, "Flag option");
    commandLine.setDefaultSwitch(option, true);
    Boolean flag = commandLine.getSwitch(option, false);
    assertTrue(flag);
  }
  
  @Test
  public void testGetNormalised() {
    List<String> normalised = commandLine.getNormalised();
    assertTrue(normalised.isEmpty());
  }
  
}