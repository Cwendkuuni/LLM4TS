import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl cmd;
  
  @Before
  public void setUp() {
    Option rootOption = new Option.Builder("r")
        .withPreferredName("root")
        .withDescription("Root option")
        .create();
    List arguments = new ArrayList();
    cmd = new WriteableCommandLineImpl(rootOption, arguments);
  }

  // Test case for addOption method
  @Test
  public void testAddOption() {
    Option option = new Option.Builder("t")
        .withPreferredName("test")
        .withDescription("Test option")
        .create();
    cmd.addOption(option);
    
    assertTrue(cmd.hasOption(option));
    assertEquals(option, cmd.getOption("t"));
    assertEquals(option, cmd.getOption("test"));
  }
  
  // Test case for addValue method with Argument option
  @Test
  public void testAddValueWithArgumentOption() {
    Argument option = new Argument.Builder("a")
        .withDescription("Argument option")
        .create();
    cmd.addValue(option, "value1");
    
    assertTrue(cmd.hasOption(option));
    assertEquals("value1", cmd.getValues(option, null).get(0));
  }
  
  // Test case for addValue method with non-Argument option
  @Test
  public void testAddValueWithNonArgumentOption() {
    Option option = new Option.Builder("o")
        .withPreferredName("other")
        .withDescription("Other option")
        .create();
    cmd.addValue(option, "value2");
    
    assertTrue(cmd.hasOption(option));
    assertEquals("value2", cmd.getValues(option, null).get(0));
  }
  
  // Test case for addSwitch method
  @Test
  public void testAddSwitch() {
    Option option1 = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    Option option2 = new Option.Builder("v")
        .withPreferredName("verbose")
        .withDescription("Verbose option")
        .create();
    
    cmd.addSwitch(option1, true);
    cmd.addSwitch(option2, false);
    
    assertTrue(cmd.hasOption(option1));
    assertTrue(cmd.getSwitch(option1, false));
    
    assertTrue(cmd.hasOption(option2));
    assertFalse(cmd.getSwitch(option2, true));
  }
  
  // Test case for getProperty method
  @Test
  public void testGetProperty() {
    cmd.addProperty("key1", "value1");
    
    assertEquals("value1", cmd.getProperty("key1"));
    assertNull(cmd.getProperty("key2"));
  }
  
  // Test case for addProperty method
  @Test
  public void testAddProperty() {
    Option option = new Option.Builder("p")
        .withPreferredName("prop")
        .withDescription("Property option")
        .create();
    
    cmd.addProperty(option, "key1", "value1");
    
    assertEquals("value1", cmd.getProperty(option, "key1", ""));
    assertEquals("", cmd.getProperty(option, "key2", ""));
  }
  
  // Test case for getValues method with default values
  @Test
  public void testGetValuesWithDefaultValues() {
    Option option = new Option.Builder("l")
        .withPreferredName("list")
        .withDescription("List option")
        .create();
    List defaults = new ArrayList();
    defaults.add("default1");
    defaults.add("default2");
    cmd.setDefaultValues(option, defaults);
    
    List values = new ArrayList();
    values.add("value1");
    cmd.addValue(option, "value1");
    
    assertEquals(values, cmd.getValues(option, null));
  }
  
  // Test case for getValues method without default values
  @Test
  public void testGetValuesWithoutDefaultValues() {
    Option option = new Option.Builder("l")
        .withPreferredName("list")
        .withDescription("List option")
        .create();
    
    List values = new ArrayList();
    values.add("value1");
    cmd.addValue(option, "value1");
    
    assertEquals(values, cmd.getValues(option, null));
  }
  
  // Test case for getUndefaultedValues method
  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option.Builder("l")
        .withPreferredName("list")
        .withDescription("List option")
        .create();
    
    List values = new ArrayList();
    values.add("value1");
    cmd.addValue(option, "value1");
    
    assertEquals(values, cmd.getUndefaultedValues(option));
  }
  
  // Test case for getSwitch method with default switch
  @Test
  public void testGetSwitchWithDefaultSwitch() {
    Option option = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    cmd.setDefaultSwitch(option, true);
    
    assertTrue(cmd.getSwitch(option, null));
  }
  
  // Test case for getSwitch method without default switch
  @Test
  public void testGetSwitchWithoutDefaultSwitch() {
    Option option = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    cmd.addSwitch(option, false);
    
    assertFalse(cmd.getSwitch(option, null));
  }
  
  // Test case for getProperty method for default option
  @Test
  public void testGetPropertyForDefaultOption() {
    Option option = new Option.Builder("p")
        .withPreferredName("prop")
        .withDescription("Property option")
        .create();
    
    cmd.addProperty(option, "key1", "value1");
    
    assertEquals("value1", cmd.getProperty(option, "key1", ""));
    assertEquals("", cmd.getProperty(option, "key2", ""));
  }
  
  // Test case for getProperties method for default option
  @Test
  public void testGetPropertiesForDefaultOption() {
    Option option = new Option.Builder("p")
        .withPreferredName("prop")
        .withDescription("Property option")
        .create();
    
    cmd.addProperty(option, "key1", "value1");
    cmd.addProperty(option, "key2", "value2");
    
    Set properties = new HashSet();
    properties.add("key1");
    properties.add("key2");
    
    assertEquals(properties, cmd.getProperties(option));
  }
  
  // Test case for looksLikeOption method with valid option
  @Test
  public void testLooksLikeOptionWithValidOption() {
    assertTrue(cmd.looksLikeOption("-r"));
    assertTrue(cmd.looksLikeOption("--root"));
  }
  
  // Test case for looksLikeOption method with invalid option
  @Test
  public void testLooksLikeOptionWithInvalidOption() {
    assertFalse(cmd.looksLikeOption("r"));
    assertFalse(cmd.looksLikeOption("-t"));
  }
  
  // Test case for toString method
  @Test
  public void testToString() {
    cmd.addValue(new Argument("arg1"), "value1");
    cmd.addValue(new Argument("arg2"), "value2");
    
    assertEquals("value1 value2", cmd.toString());
  }
  
  // Test case for getOptions method
  @Test
  public void testGetOptions() {
    Option option1 = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    Option option2 = new Option.Builder("v")
        .withPreferredName("verbose")
        .withDescription("Verbose option")
        .create();
    
    cmd.addOption(option1);
    cmd.addOption(option2);
    
    List options = new ArrayList();
    options.add(option1);
    options.add(option2);
    
    assertEquals(options, cmd.getOptions());
  }
  
  // Test case for getOptionTriggers method
  @Test
  public void testGetOptionTriggers() {
    Option option1 = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    Option option2 = new Option.Builder("v")
        .withPreferredName("verbose")
        .withDescription("Verbose option")
        .create();
    
    cmd.addOption(option1);
    cmd.addOption(option2);
    
    Set triggers = new HashSet();
    triggers.add("d");
    triggers.add("debug");
    triggers.add("v");
    triggers.add("verbose");
    
    assertEquals(triggers, cmd.getOptionTriggers());
  }
  
  // Test case for setDefaultValues method
  @Test
  public void testSetDefaultValues() {
    Option option = new Option.Builder("l")
        .withPreferredName("list")
        .withDescription("List option")
        .create();
    List defaults = new ArrayList();
    defaults.add("default1");
    defaults.add("default2");
    cmd.setDefaultValues(option, defaults);
    
    List values = new ArrayList();
    values.add("value1");
    cmd.addValue(option, "value1");
    
    assertEquals(values, cmd.getValues(option, null));
  }
  
  // Test case for setDefaultSwitch method
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option.Builder("d")
        .withPreferredName("debug")
        .withDescription("Debug option")
        .create();
    cmd.setDefaultSwitch(option, true);
    
    assertTrue(cmd.getSwitch(option, null));
  }
  
  // Test case for getNormalised method
  @Test
  public void testGetNormalised() {
    cmd.addValue(new Argument("arg1"), "value1");
    cmd.addSwitch(new Option.Builder("d")
                    .withPreferredName("debug")
                    .withDescription("Debug option")
                    .create(),
                  true);
    
    List normalised = new ArrayList();
    normalised.add("value1");
    assertTrue(cmd.getNormalised().containsAll(normalised));
  }
}