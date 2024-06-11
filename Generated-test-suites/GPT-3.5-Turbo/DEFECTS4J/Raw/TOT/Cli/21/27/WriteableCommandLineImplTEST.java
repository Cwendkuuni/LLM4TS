import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setup() {
    Option argumentOption = new Argument();
    Option propertyOption = new PropertyOption();
    Option option1 = new OptionBuilder("o1").create();
    Option option2 = new OptionBuilder("o2").create();
    Option option3 = new OptionBuilder("o3").create();
    Option option4 = new OptionBuilder("o4").create();

    commandLine = new WriteableCommandLineImpl(argumentOption, new ArrayList<String>());
  }

  // Test case for addOption()
  @Test
  public void testAddOption() {
    commandLine.addOption(option1);

    assertTrue(commandLine.hasOption(option1));
    assertFalse(commandLine.hasOption(option2));
  }

  // Test case for addValue()
  @Test
  public void testAddValue() {
    String value1 = "value1";
    String value2 = "value2";

    // Testing with Argument option
    commandLine.addValue(argumentOption, value1);
    commandLine.addValue(argumentOption, value2);

    assertEquals(2, commandLine.getUndefaultedValues(argumentOption).size());
    assertTrue(commandLine.getUndefaultedValues(argumentOption).contains(value1));
    assertTrue(commandLine.getUndefaultedValues(argumentOption).contains(value2));

    // Testing with Option other than Argument
    commandLine.addValue(option1, value1);

    assertEquals(1, commandLine.getUndefaultedValues(option1).size());
    assertTrue(commandLine.getUndefaultedValues(option1).contains(value1));
  }

  // Test case for addSwitch()
  @Test
  public void testAddSwitch() {
    // Testing with true value
    commandLine.addSwitch(option1, true);

    assertTrue(commandLine.hasOption(option1));
    assertTrue(commandLine.getSwitch(option1, false));

    // Testing with false value
    commandLine.addSwitch(option2, false);

    assertTrue(commandLine.hasOption(option2));
    assertFalse(commandLine.getSwitch(option2, true));
  }

  // Test case for hasOption()
  @Test
  public void testHasOption() {
    commandLine.addOption(option1);

    assertTrue(commandLine.hasOption(option1));
    assertFalse(commandLine.hasOption(option2));
  }

  // Test case for getOption()
  @Test
  public void testGetOption() {
    commandLine.addOption(option1);

    assertEquals(option1, commandLine.getOption("o1"));
    assertNull(commandLine.getOption("o2"));
  }

  // Test case for getValues()
  @Test
  public void testGetValues() {
    String value1 = "value1";
    String value2 = "value2";
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");

    // Testing with Argument option
    commandLine.addValue(argumentOption, value1);
    commandLine.addValue(argumentOption, value2);

    assertEquals(2, commandLine.getValues(argumentOption, defaultValues).size());
    assertTrue(commandLine.getValues(argumentOption, defaultValues).contains(value1));
    assertTrue(commandLine.getValues(argumentOption, defaultValues).contains(value2));

    // Testing with Option other than Argument
    commandLine.addValue(option1, value1);

    assertEquals(1, commandLine.getValues(option1, defaultValues).size());
    assertTrue(commandLine.getValues(option1, defaultValues).contains(value1));

    // Testing with default values
    assertEquals(defaultValues, commandLine.getValues(option2, defaultValues));

    // Testing with no values and no default values
    assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option3, defaultValues));
  }

  // Test case for getUndefaultedValues()
  @Test
  public void testGetUndefaultedValues() {
    String value1 = "value1";
    String value2 = "value2";

    // Testing with Argument option
    commandLine.addValue(argumentOption, value1);
    commandLine.addValue(argumentOption, value2);

    assertEquals(2, commandLine.getUndefaultedValues(argumentOption).size());
    assertTrue(commandLine.getUndefaultedValues(argumentOption).contains(value1));
    assertTrue(commandLine.getUndefaultedValues(argumentOption).contains(value2));

    // Testing with Option other than Argument
    commandLine.addValue(option1, value1);

    assertEquals(1, commandLine.getUndefaultedValues(option1).size());
    assertTrue(commandLine.getUndefaultedValues(option1).contains(value1));

    // Testing with no values
    assertEquals(Collections.EMPTY_LIST, commandLine.getUndefaultedValues(option2));
  }

  // Test case for getSwitch()
  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option1, true);

    assertTrue(commandLine.hasOption(option1));
    assertTrue(commandLine.getSwitch(option1, false));
    assertNull(commandLine.getSwitch(option2, true));
  }

  // Test case for getProperty()
  @Test
  public void testGetProperty() {
    String property = "property";
    String value = "value";

    commandLine.addProperty(option1, property, value);

    assertEquals(value, commandLine.getProperty(option1, property, null));
    assertNull(commandLine.getProperty(option2, property, null));
    assertNull(commandLine.getProperty(option1, "unknownProperty", null));
  }

  // Test case for addProperty()
  @Test
  public void testAddProperty() {
    String property = "property";
    String value = "value";

    commandLine.addProperty(option1, property, value);

    assertEquals(value, commandLine.getProperty(option1, property, null));
    assertNull(commandLine.getProperty(option2, property, null));
  }

  // Test case for getProperties()
  @Test
  public void testGetProperties() {
    String property1 = "property1";
    String value1 = "value1";
    String property2 = "property2";
    String value2 = "value2";

    commandLine.addProperty(option1, property1, value1);
    commandLine.addProperty(option1, property2, value2);

    assertTrue(commandLine.getProperties(option1).contains(property1));
    assertTrue(commandLine.getProperties(option1).contains(property2));
    assertFalse(commandLine.getProperties(option2).contains(property1));
    assertFalse(commandLine.getProperties(option2).contains(property2));
  }

  // Test case for getProperties() with default option
  @Test
  public void testGetPropertiesWithDefaultOption() {
    String property1 = "property1";
    String value1 = "value1";
    String property2 = "property2";
    String value2 = "value2";

    commandLine.addProperty(propertyOption, property1, value1);
    commandLine.addProperty(propertyOption, property2, value2);

    assertTrue(commandLine.getProperties().contains(property1));
    assertTrue(commandLine.getProperties().contains(property2));
  }

  // Test case for looksLikeOption()
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-o1"));
    assertTrue(commandLine.looksLikeOption("--o1"));
    assertFalse(commandLine.looksLikeOption("o1"));
  }

  // Test case for toString()
  @Test
  public void testToString() {
    commandLine.addOption(option1);
    commandLine.addSwitch(option2, true);
    commandLine.addValue(argumentOption, "value1");
    commandLine.addValue(argumentOption, "value2");

    assertEquals("-o1 value1 value2 --o2", commandLine.toString());
    commandLine.getNormalised().add("-o3 value3");

    assertEquals("-o1 value1 value2 --o2 -o3 value3", commandLine.toString());
  }

  // Test case for getOptions()
  @Test
  public void testGetOptions() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    commandLine.addOption(option3);

    assertEquals(3, commandLine.getOptions().size());
    assertTrue(commandLine.getOptions().contains(option1));
    assertTrue(commandLine.getOptions().contains(option2));
    assertTrue(commandLine.getOptions().contains(option3));
  }

  // Test case for getOptionTriggers()
  @Test
  public void testGetOptionTriggers() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    commandLine.addOption(option3);

    assertEquals(3, commandLine.getOptionTriggers().size());
    assertTrue(commandLine.getOptionTriggers().contains("o1"));
    assertTrue(commandLine.getOptionTriggers().contains("o2"));
    assertTrue(commandLine.getOptionTriggers().contains("o3"));
  }

  // Test case for setDefaultValues()
  @Test
  public void testSetDefaultValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");

    commandLine.setDefaultValues(option1, defaultValues);

    assertEquals(defaultValues, commandLine.getValues(option1, null));

    commandLine.setDefaultValues(option2, null);
    assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option2, null));
  }

  // Test case for setDefaultSwitch()
  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option1, true);

    assertTrue(commandLine.getSwitch(option1, false));

    commandLine.setDefaultSwitch(option2, null);
    assertNull(commandLine.getSwitch(option2, true));
  }

  // Test case for getNormalised()
  @Test
  public void testGetNormalised() {
    commandLine.getNormalised().add("-o1 value1");
    commandLine.getNormalised().add("--o2 value2");

    assertTrue(commandLine.getNormalised().contains("-o1 value1"));
    assertTrue(commandLine.getNormalised().contains("--o2 value2"));
  }
}