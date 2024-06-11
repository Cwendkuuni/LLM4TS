
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  private Option option1;
  private Option option2;
  private Option option3;
  
  @Before
  public void setUp() {
    option1 = new Option("o1");
    option2 = new Option("o2");
    option3 = new Option("o3");
    
    commandLine = new WriteableCommandLineImpl(option1, null);
  }
  
  @Test
  public void testAddOption() {
    commandLine.addOption(option2);
    commandLine.addOption(option3);
    
    assertTrue(commandLine.hasOption(option2));
    assertTrue(commandLine.hasOption(option3));
  }
  
  @Test
  public void testAddValue() {
    Argument argument = new Argument("arg1");

    commandLine.addValue(argument, "value1");

    assertTrue(commandLine.getValues(argument, null).contains("value1"));
  }
  
  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option1, true);

    assertTrue(commandLine.getSwitch(option1, null));
  }
  
  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(option2));

    commandLine.addOption(option2);
    
    assertTrue(commandLine.hasOption(option2));
  }
  
  @Test
  public void testGetOption() {
    assertNull(commandLine.getOption("unknownOption"));

    commandLine.addOption(option2);
    
    assertEquals(option2, commandLine.getOption("o2"));
  }
  
  @Test
  public void testGetValues() {
    Argument argument1 = new Argument("arg1");
    Argument argument2 = new Argument("arg2");
    List<String> defaultValues1 = new ArrayList<String>(Arrays.asList("value1", "value2"));
    List<String> defaultValues2 = new ArrayList<String>(Arrays.asList("value3", "value4"));

    commandLine.setDefaults(argument1, defaultValues1);
    
    assertEquals(defaultValues1, commandLine.getValues(argument1, null));
    assertEquals(defaultValues1, commandLine.getValues(argument1, defaultValues2));
    
    assertEquals(Collections.emptyList(), commandLine.getValues(argument2, null));
    assertEquals(defaultValues2, commandLine.getValues(argument2, defaultValues2));
  }
  
  @Test
  public void testGetSwitch() {
    commandLine.setDefaults(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, false));
    assertFalse(commandLine.getSwitch(option2, false));
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    
    assertEquals("value1", commandLine.getProperty("property1", null));
    assertEquals("value2", commandLine.getProperty("property2", null));
    assertEquals("default", commandLine.getProperty("property3", "default"));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-o1"));
    assertFalse(commandLine.looksLikeOption("argument1"));
  }
  
  @Test
  
  public void testToString() {
    Argument argument1 = new Argument("arg1");
    Argument argument2 = new Argument("arg2");
    
    commandLine.addOption(option1);
    commandLine.addValue(argument1, "value1");
    commandLine.addSwitch(option2, true);
    commandLine.addValue(argument2, "value2");
    
    assertEquals("o1 arg1 value1 --o2 arg2 value2", commandLine.toString());
  }
  
  @Test
  public void testGetOptions() {
    commandLine.addOption(option2);
    commandLine.addOption(option3);
    
    List<Option> options = commandLine.getOptions();
    
    assertTrue(options.contains(option2));
    assertTrue(options.contains(option3));
  }
  
  @Test
  public void testGetOptionTriggers() {
    commandLine.addOption(option2);
    commandLine.addOption(option3);
    
    Set<String> triggers = commandLine.getOptionTriggers();
    
    assertTrue(triggers.contains("o2"));
    assertTrue(triggers.contains("o3"));
  }
  
  @Test
  public void testSetDefaultValues() {
    Argument argument1 = new Argument("arg1");
    Argument argument2 = new Argument("arg2");
    
    List<String> defaultValues1 = new ArrayList<String>(Arrays.asList("value1", "value2"));
    List<String> defaultValues2 = new ArrayList<String>(Arrays.asList("value3", "value4"));
    
    commandLine.addOption(argument1);
    commandLine.addOption(argument2);
    
    commandLine.setDefaultValues(argument1, defaultValues1);
    
    assertEquals(defaultValues1, commandLine.getValues(argument1, null));
    assertEquals(defaultValues2, commandLine.getValues(argument2, defaultValues2));
    
    commandLine.setDefaultValues(argument1, defaultValues2);
    
    assertEquals(defaultValues2, commandLine.getValues(argument1, null));  
    assertEquals(defaultValues2, commandLine.getValues(argument2, defaultValues1));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    commandLine.setDefaultSwitch(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, false));
    assertFalse(commandLine.getSwitch(option2, false));
    
    commandLine.setDefaultSwitch(option1, false);
    
    assertFalse(commandLine.getSwitch(option1, true));
  }
  
  @Test
  public void testGetNormalised() {
    Argument argument1 = new Argument("arg1");
    Argument argument2 = new Argument("arg2");
    
    List<String> arguments = new ArrayList<String>(Arrays.asList("value1", "value2"));
    
    commandLine.addValue(argument1, "value1");
    commandLine.addValue(argument2, "value2");
    
    assertEquals(arguments, commandLine.getNormalised());
  }  
}
