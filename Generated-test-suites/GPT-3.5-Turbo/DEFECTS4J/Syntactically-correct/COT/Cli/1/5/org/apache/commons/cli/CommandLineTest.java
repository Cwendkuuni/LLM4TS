import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {
  
  private CommandLine commandLine;
  
  @Before
  public void setup() {
    commandLine = new CommandLine();
  }
  
  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption("opt"));
    assertFalse(commandLine.hasOption('o'));
  }
  
  @Test
  public void testHasOptionWithExistingOption() {
    Option option = new Option("opt", false);
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption("opt"));
  }
  
  @Test
  public void testHasOptionWithExistingOptionChar() {
    Option option = new Option("opt", false);
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption('o'));
  }
  
  @Test
  public void testHasOptionWithNonExistingOption() {
    assertFalse(commandLine.hasOption("nonExistingOpt"));
  }
  
  @Test
  public void testHasOptionWithNonExistingOptionChar() {
    assertFalse(commandLine.hasOption('n'));
  }
  
  @Test
  public void testGetOptionObject() {
    assertNull(commandLine.getOptionObject("opt"));
  }
  
  @Test
  public void testGetOptionObjectWithExistingOption() {
    Option option = new Option("opt", "description");
    option.setType(Integer.class);
    option.setValue("20");
    commandLine.addOption(option);
    
    assertEquals(Integer.valueOf(20), commandLine.getOptionObject("opt"));
  }
  
  @Test
  public void testGetOptionObjectWithExistingOptionChar() {
    Option option = new Option("opt", "description");
    option.setType(Double.class);
    option.setValue(String.valueOf(3.14));
    commandLine.addOption(option);
    
    assertEquals(Double.valueOf(3.14), commandLine.getOptionObject('o'));
  }
  
  @Test
  public void testGetOptionObjectWithNonExistingOption() {
    assertNull(commandLine.getOptionObject("nonExistingOpt"));
  }
  
  @Test
  public void testGetOptionObjectWithNonExistingOptionChar() {
    assertNull(commandLine.getOptionObject('n'));
  }
  
  @Test
  public void testGetOptionValue() {
    assertNull(commandLine.getOptionValue("opt"));
  }
  
  @Test
  public void testGetOptionValueWithExistingOption() {
    Option option = new Option("opt", "description");
    option.setArgs(1);
    option.addValue("value");
    commandLine.addOption(option);
    
    assertEquals("value", commandLine.getOptionValue("opt"));
  }
  
  @Test
  public void testGetOptionValueWithExistingOptionChar() {
    Option option = new Option("opt", "description");
    option.setArgs(1);
    option.addValue("value");
    commandLine.addOption(option);
    
    assertEquals("value", commandLine.getOptionValue('o'));
  }
  
  @Test
  public void testGetOptionValueWithNonExistingOption() {
    assertNull(commandLine.getOptionValue("nonExistingOpt"));
  }
  
  @Test
  public void testGetOptionValueWithNonExistingOptionChar() {
    assertNull(commandLine.getOptionValue('n'));
  }
  
  @Test
  public void testGetOptionValues() {
    assertNull(commandLine.getOptionValues("opt"));
  }
  
  @Test
  public void testGetOptionValuesWithExistingOption() {
    Option option = new Option("opt", "description");
    option.setArgs(2);
    option.addValue("value1");
    option.addValue("value2");
    commandLine.addOption(option);
    
    assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("opt"));
  }
  
  @Test
  public void testGetOptionValuesWithExistingOptionChar() {
    Option option = new Option("opt", "description");
    option.setArgs(2);
    option.addValue("value1");
    option.addValue("value2");
    commandLine.addOption(option);
    
    assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('o'));
  }
  
  @Test
  public void testGetOptionValuesWithNonExistingOption() {
    assertNull(commandLine.getOptionValues("nonExistingOpt"));
  }
  
  @Test
  public void testGetOptionValuesWithNonExistingOptionChar() {
    assertNull(commandLine.getOptionValues('n'));
  }

  @Test
  public void testGetOptionValueWithDefaultValue() {
    assertEquals("defaultValue", commandLine.getOptionValue("opt", "defaultValue"));
  }

  @Test
  public void testGetOptionValueWithDefaultValueChar() {
    assertEquals("defaultValue", commandLine.getOptionValue('o', "defaultValue"));
  }

  @Test
  public void testGetArgs() {
    assertArrayEquals(new String[]{}, commandLine.getArgs());
  }

  @Test
  public void testGetArgList() {
    assertTrue(commandLine.getArgList().isEmpty());
  }

  @Test
  public void testAddArg() {
    commandLine.addArg("arg");
    assertEquals(1, commandLine.getArgs().length);
    assertEquals("arg", commandLine.getArgs()[0]);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("opt", "description");
    commandLine.addOption(option);

    assertEquals(option, commandLine.getOptions()[0]);
  }
  
  @Test
  public void testIterator() {
    Option option1 = new Option("opt1", "description1");
    Option option2 = new Option("opt2", "description2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    assertEquals(option1, commandLine.iterator().next());
  }

}