import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {
  
  private CommandLine commandLine;
  
  @Before
  public void setUp() {
    commandLine = new CommandLine();
  }
  
  @Test
  public void testHasOptionWithExistingOption() {
    commandLine.addOption(new Option("f", "file", true, "File option"));
    
    assertTrue(commandLine.hasOption("f"));
    assertTrue(commandLine.hasOption('f'));
    assertTrue(commandLine.hasOption("file"));
  }
  
  @Test
  public void testHasOptionWithNonExistingOption() {
    assertFalse(commandLine.hasOption("f"));
    assertFalse(commandLine.hasOption('f'));
    assertFalse(commandLine.hasOption("file"));
  }
  
  @Test
  public void testGetOptionObjectWithExistingOption() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    commandLine.addOptionValue("f", "example.txt");
    
    assertEquals(option, commandLine.getOptionObject("f"));
  }
  
  @Test
  public void testGetOptionObjectWithNonExistingOption() {
    assertNull(commandLine.getOptionObject("f"));
  }
  
  @Test
  public void testGetOptionValueWithExistingOption() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    commandLine.addOptionValue("f", "example.txt");
    
    assertEquals("example.txt", commandLine.getOptionValue("f"));
  }
  
  @Test
  public void testGetOptionValueWithNonExistingOption() {
    assertNull(commandLine.getOptionValue("f"));
  }
  
  @Test
  public void testGetOptionValuesWithExistingOption() {
    Option option = new Option("c", "config", true, "Config option");
    commandLine.addOption(option);
    
    commandLine.addOptionValue("c", "config1");
    commandLine.addOptionValue("c", "config2");
    commandLine.addOptionValue("c", "config3");
    
    assertArrayEquals(new String[]{"config1", "config2", "config3"}, commandLine.getOptionValues("c"));
  }
  
  @Test
  public void testGetOptionValuesWithNonExistingOption() {
    assertNull(commandLine.getOptionValues("c"));
  }
  
  @Test
  public void testGetOptionValueWithDefaultValue() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    assertEquals("default.txt", commandLine.getOptionValue("f", "default.txt"));
  }
  
  @Test
  public void testGetOptionValueWithNullDefaultValue() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    assertNull(commandLine.getOptionValue("f", null));
  }
  
  @Test
  public void testGetOptionValuesWithDefaultValue() {
    Option option = new Option("c", "config", true, "Config option");
    commandLine.addOption(option);
    
    assertArrayEquals(new String[]{"default"}, commandLine.getOptionValues("c", new String[]{"default"}));
  }
  
  @Test
  public void testGetOptionValuesWithNullDefaultValue() {
    Option option = new Option("c", "config", true, "Config option");
    commandLine.addOption(option);
    
    assertNull(commandLine.getOptionValues("c", null));
  }
  
  @Test
  public void testGetArgs() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    commandLine.addArg("arg1");
    commandLine.addArg("arg2");
    commandLine.addArg("arg3");
    
    assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, commandLine.getArgs());
  }
  
  @Test
  public void testGetArgList() {
    Option option = new Option("f", "file", true, "File option");
    commandLine.addOption(option);
    
    commandLine.addArg("arg1");
    commandLine.addArg("arg2");
    commandLine.addArg("arg3");
    
    assertEquals(3, commandLine.getArgList().size());
    assertTrue(commandLine.getArgList().contains("arg1"));
    assertTrue(commandLine.getArgList().contains("arg2"));
    assertTrue(commandLine.getArgList().contains("arg3"));
  }
  
  @Test
  public void testIterator() {
    Option option1 = new Option("f", "file", true, "File option");
    commandLine.addOption(option1);
    
    Option option2 = new Option("v", "verbose", false, "Verbose option");
    commandLine.addOption(option2);
    
    Option option3 = new Option("c", "config", true, "Config option");
    commandLine.addOption(option3);
    
    Iterator<Option> iterator = commandLine.iterator();
    
    assertTrue(iterator.hasNext());
    assertEquals(option1, iterator.next());
    
    assertTrue(iterator.hasNext());
    assertEquals(option2, iterator.next());
    
    assertTrue(iterator.hasNext());
    assertEquals(option3, iterator.next());
    
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("f", "file", true, "File option");
    commandLine.addOption(option1);
    
    Option option2 = new Option("v", "verbose", false, "Verbose option");
    commandLine.addOption(option2);
    
    Option option3 = new Option("c", "config", true, "Config option");
    commandLine.addOption(option3);
    
    Option[] options = commandLine.getOptions();
    
    assertEquals(3, options.length);
    assertArrayEquals(new Option[]{option1, option2, option3}, options);
  }
}