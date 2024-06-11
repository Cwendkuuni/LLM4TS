import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  public void setUp() {
    Option rootOption = new Option("root");
    List arguments = new ArrayList();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);

    assertTrue(commandLine.getOptions().contains(option));
    assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("testOption");
    commandLine.addValue(option, "value");

    assertTrue(commandLine.getValues(option, null).contains("value"));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testOption");
    commandLine.addSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);

    assertEquals(option, commandLine.getOption("testOption"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("testOption");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");

    assertEquals(values, commandLine.getValues(option, null));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("testOption");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");

    assertEquals(values, commandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testOption");
    commandLine.addSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    Option option = new Option("testOption");
    commandLine.addProperty(option, "property1", "value1");

    assertEquals("value1", commandLine.getProperty(option, "property1", null));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("testOption");
    commandLine.addProperty(option, "property1", "value1");

    Set<String> properties = commandLine.getProperties(option);
    assertTrue(properties.contains("property1"));
  }

  @Test
  public void testGetPropertyWithDefault() {
    Option option = new Option("testOption");
    commandLine.addProperty(option, "property1", "value1");

    assertEquals("defaultValue", commandLine.getProperty(option, "nonExistentProperty", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    Option option = new Option("testOption");
    commandLine.addProperty(option, "property1", "value1");

    Set<String> properties = commandLine.getProperties(option);
    assertTrue(properties.contains("property1"));
  }

  @Test
  public void testGetPropertiesWithDefault() {
    Option option = new Option("testOption");
    commandLine.addProperty(option, "property1", "value1");

    Set<String> properties = commandLine.getProperties(option);
    assertTrue(properties.isEmpty());
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-testOption"));
    assertTrue(commandLine.looksLikeOption("--testOption"));
    assertFalse(commandLine.looksLikeOption("testArgument"));
  }

  @Test
  public void testToString() {
    Option option = new Option("testOption");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    assertEquals("value1 value2", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("testOption");
    commandLine.addOption(option);

    List<Option> options = new ArrayList<>();
    options.add(option);

    assertEquals(options, commandLine.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("testOption");
    commandLine.addOption(option);

    Set<String> triggers = commandLine.getOptionTriggers();
    assertTrue(triggers.contains("testOption"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("testOption");
    List<String> defaults = new ArrayList<>();
    defaults.add("defaultValue");

    commandLine.setDefaultValues(option, defaults);

    assertEquals(defaults, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultValuesWithNull() {
    Option option = new Option("testOption");
    List<String> defaults = new ArrayList<>();
    defaults.add("defaultValue");

    commandLine.setDefaultValues(option, defaults);
    commandLine.setDefaultValues(option, null);

    List<String> emptyList = new ArrayList<>();

    assertEquals(emptyList, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testOption");
    commandLine.setDefaultSwitch(option, true);

    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testSetDefaultSwitchWithNull() {
    Option option = new Option("testOption");
    commandLine.setDefaultSwitch(option, true);
    commandLine.setDefaultSwitch(option, null);

    assertNull(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    List<String> arguments = new ArrayList<>();
    arguments.add("argument1");
    arguments.add("argument2");

    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), arguments);

    assertEquals(arguments, commandLine.getNormalised());
  }
}