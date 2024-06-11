import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

  @Test
  public void testHasOption_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a"));
    
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("b"));
  }

  @Test
  public void testHasOption_char() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a"));
    
    assertTrue(commandLine.hasOption('a'));
    assertFalse(commandLine.hasOption('b'));
  }

  @Test
  public void testGetOptionObject_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionObject("a"));
    assertNull(commandLine.getOptionObject("b"));
  }

  @Test
  public void testGetOptionObject_char() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionObject('a'));
    assertNull(commandLine.getOptionObject('b'));
  }

  @Test
  public void testGetOptionValue_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionValue("a"));
    assertNull(commandLine.getOptionValue("b"));
  }

  @Test
  public void testGetOptionValue_char() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionValue('a'));
    assertNull(commandLine.getOptionValue('b'));
  }

  @Test
  public void testGetOptionValues_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
    assertNull(commandLine.getOptionValues("b"));
  }

  @Test
  public void testGetOptionValues_char() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('a'));
    assertNull(commandLine.getOptionValues('b'));
  }

  @Test
  public void testGetOptionValue_String_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionValue("a", "default"));
    assertEquals("default", commandLine.getOptionValue("b", "default"));
  }

  @Test
  public void testGetOptionValue_char_String() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addOption(new Option("a", true, "option a"));
    
    assertEquals("value", commandLine.getOptionValue('a', "default"));
    assertEquals("default", commandLine.getOptionValue('b', "default"));
  }

  @Test
  public void testGetArgs() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addArg("arg1");
    commandLine.addArg("arg2");
    
    assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
  }

  @Test
  public void testGetArgList() {
    CommandLine commandLine = new CommandLine();
    
    commandLine.addArg("arg1");
    commandLine.addArg("arg2");
    
    assertEquals(2, commandLine.getArgList().size());
    assertTrue(commandLine.getArgList().contains("arg1"));
    assertTrue(commandLine.getArgList().contains("arg2"));
  }
  
}