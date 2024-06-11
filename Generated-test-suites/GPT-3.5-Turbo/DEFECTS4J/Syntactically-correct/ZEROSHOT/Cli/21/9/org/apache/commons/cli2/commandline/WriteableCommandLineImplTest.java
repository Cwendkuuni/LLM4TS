import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList());
  }

  @Test
  public void testAddOption() {
    Option option = new Option("option");
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Argument argument = new Argument("argument");
    String value = "value";
    commandLine.addValue(argument, value);
    
    assertEquals(value, commandLine.getValues(argument, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("option");
    boolean value = true;
    commandLine.addSwitch(option, value);
    
    assertEquals(value, commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("option");
    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("option");
    commandLine.addOption(option);

    assertEquals(option, commandLine.getOption("option"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("option");
    String value = "value";
    commandLine.addValue(option, value);

    assertEquals(value, commandLine.getValues(option, null).get(0));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("option");
    String value = "value";
    commandLine.addValue(option, value);

    assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("option");
    boolean value = true;
    commandLine.addSwitch(option, value);

    assertEquals(value, commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(property, value);

    assertEquals(value, commandLine.getProperty(property));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("option");
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);

    assertEquals(value, commandLine.getProperty(option, property, null));
  }

  @Test
  public void testGetProperties() {
    Option option = new Option("option");
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);

    assertTrue(commandLine.getProperties(option).contains(property));
  }

  @Test
  public void testGetPropertiesWithDefaultOption() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(property, value);

    assertTrue(commandLine.getProperties().contains(property));
  }

  @Test
  public void testLooksLikeOption() {
    assertFalse(commandLine.looksLikeOption("test"));

    Option option = new Option("option");
    commandLine.addOption(option);

    assertTrue(commandLine.looksLikeOption("option"));
  }

  @Test
  public void testToString() {
    String argument = "argument";
    commandLine.addValue(new Argument(argument), "value");
    
    assertEquals(argument, commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("option");
    commandLine.addOption(option);
    
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("option");
    commandLine.addOption(option);
    
    assertTrue(commandLine.getOptionTriggers().contains("option"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("option");
    List<String> defaults = Arrays.asList("default1", "default2");
    commandLine.setDefaultValues(option, defaults);

    assertEquals(defaults, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("option");
    boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    Option option = new Option("option");
    String argument = "argument";
    List<String> normalised = Arrays.asList(argument);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, normalised);
    
    assertEquals(normalised, commandLine.getNormalised());
  }

}