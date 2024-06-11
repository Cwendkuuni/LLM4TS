import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.CommandLineBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() throws Exception {
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();
    Argument argument = ArgumentBuilder.withName("arg1").withDescription("argument 1").create();

    commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
  }

  @Test
  public void testAddOption() {
    // Test case for adding an option
    Option newOption = OptionBuilder.withLongName("opt2").withDescription("option 2").create();

    commandLine.addOption(newOption);

    assertTrue(commandLine.hasOption(newOption));
  }

  @Test
  public void testAddValue() {
    // Test case for adding a value to an argument option
    Option option = commandLine.getOption("arg1");
    assertNotNull(option);

    commandLine.addValue(option, "value1");

    assertEquals(Collections.singletonList("value1"), commandLine.getValues(option));
  }

  @Test
  public void testAddSwitch() {
    // Test case for adding a switch to an option
    Option option = commandLine.getOption("opt1");
    assertNotNull(option);

    commandLine.addSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    // Test case for checking if an option exists
    Option option = OptionBuilder.withLongName("opt2").withDescription("option 2").create();

    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    // Test case for getting an option by trigger
    Option option = OptionBuilder.withLongName("opt2").withDescription("option 2").create();

    assertNull(commandLine.getOption(option.getPreferredName()));
  }

  @Test
  public void testGetValues() {
    // Test case for getting values of an argument option
    Option option = commandLine.getOption("arg1");
    assertNotNull(option);

    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option));
  }

  @Test
  public void testGetUndefaultedValues() {
    // Test case for getting undefaulted values of an argument option
    Option option = commandLine.getOption("arg1");
    assertNotNull(option);

    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");

    assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    // Test case for getting a switch value of an option
    Option option = commandLine.getOption("opt1");
    assertNotNull(option);

    commandLine.addSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    // Test case for getting a property value of an option
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    assertNull(commandLine.getProperty(option, "prop1", "defaultValue"));
  }

  @Test
  public void testAddProperty() {
    // Test case for adding a property to an option
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    commandLine.addProperty(option, "prop1", "value1");

    assertEquals("value1", commandLine.getProperty(option, "prop1", null));
  }

  @Test
  public void testGetPropertyDefault() {
    // Test case for getting a property value of an option with default value
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    assertEquals("defaultValue", commandLine.getProperty(option, "prop1", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    // Test case for getting all properties of an option
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    commandLine.addProperty(option, "prop1", "value1");
    commandLine.addProperty(option, "prop2", "value2");

    Set<String> properties = commandLine.getProperties(option);
    assertEquals(2, properties.size());
    assertTrue(properties.contains("prop1"));
    assertTrue(properties.contains("prop2"));
  }

  @Test
  public void testGetAllProperties() {
    // Test case for getting all properties of an option and the default PropertyOption
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    commandLine.addProperty(option, "prop1", "value1");
    commandLine.addProperty("prop2", "value2");

    Set<String> properties = commandLine.getProperties();
    assertEquals(2, properties.size());
    assertTrue(properties.contains("prop1"));
    assertTrue(properties.contains("prop2"));
  }

  @Test
  public void testLooksLikeOption() {
    // Test case for checking if a string looks like an option
    assertTrue(commandLine.looksLikeOption("-opt1"));
    assertFalse(commandLine.looksLikeOption("value1"));
  }

  @Test
  public void testToString() {
    // Test case for converting command line to string
    commandLine.addValue(commandLine.getOption("arg1"), "value1");
    commandLine.addSwitch(commandLine.getOption("opt1"), true);

    assertEquals("value1 --opt1", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    // Test case for getting all options
    commandLine.addOption(OptionBuilder.withLongName("opt2").withDescription("option 2").create());

    assertEquals(2, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    // Test case for getting all option triggers
    commandLine.addOption(OptionBuilder.withLongName("opt2").withDescription("option 2").create());

    assertEquals(2, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    // Test case for setting default values for an option
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();
    List<String> defaults = Arrays.asList("default1", "default2");

    commandLine.setDefaultValues(option, defaults);

    assertEquals(defaults, commandLine.getValues(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    // Test case for setting default switch for an option
    Option option = OptionBuilder.withLongName("opt1").withDescription("option 1").create();

    commandLine.setDefaultSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    // Test case for getting all normalised arguments
    commandLine.addValue(commandLine.getOption("arg1"), "value1");
    commandLine.addSwitch(commandLine.getOption("opt1"), true);

    assertEquals(Collections.singletonList("value1 --opt1"), commandLine.getNormalised());
  }
}