import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  @Test
  public void testAddOption() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addOption(option);
    
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    
    assertEquals(2, commandLine.getValues(option, new ArrayList()).size());
    assertTrue(commandLine.getValues(option, new ArrayList()).contains("value1"));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addSwitch(option, true);
    
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addOption(option);
    
    assertEquals(option, commandLine.getOption("test"));
  }
  
  @Test
  public void testGetValues() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    
    assertEquals(2, commandLine.getValues(option, new ArrayList()).size());
    assertTrue(commandLine.getValues(option, new ArrayList()).contains("value1"));
  }
  
  @Test
  public void testGetSwitch() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    commandLine.addSwitch(option, true);
    
    assertTrue(commandLine.getSwitch(option, false));
  }
  
  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test", "Test option"), new ArrayList());
    
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    
    assertEquals("value1", commandLine.getProperty("property1", ""));
    assertEquals("value2", commandLine.getProperty("property2", ""));
  }
  
  @Test
  public void testLooksLikeOption() {
    Option option = new Option("test", "Test option");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    
    assertTrue(commandLine.looksLikeOption("-t"));
  }
  
  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test", "Test option"), new ArrayList());
    commandLine.addOption(new Option("option1", "Option 1"));
    commandLine.addOption(new Option("option2", "Option 2"));
    commandLine.addValue(new Argument("argument1", "Argument 1"), "value1");
    commandLine.addValue(new Argument("argument2", "Argument 2"), "value2");
    
    assertEquals("option1 value1 option2 value2", commandLine.toString());
  }
}