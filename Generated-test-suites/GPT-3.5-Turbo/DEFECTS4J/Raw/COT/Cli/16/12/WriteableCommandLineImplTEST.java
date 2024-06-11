import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setUp() {
    Option rootOption = new Option("root");
    List<String> arguments = new ArrayList<String>();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new Option("testOption");
    Object value = "testValue";
    commandLine.addValue(option, value);
    
    List<Object> values = commandLine.getValues(option, null);
    assertEquals(1, values.size());
    assertEquals(value, values.get(0));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new Option("testOption");
    boolean value = true;
    commandLine.addSwitch(option, value);
    
    assertEquals(value, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetProperty() {
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(property, value);
    
    assertEquals(value, commandLine.getProperty(property));
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new Option("testOption");
    List<String> defaults = new Option();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    
    List<String> retrievedDefaults = commandLine.getValues(option, null);
    assertEquals(defaults.size(), retrievedDefaults.size());
    for (int i = 0; i < defaults.size(); i++) {
      assertEquals(defaults.get(i), retrievedDefaults.get(i));
    }
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testOption");
    boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("option1");
    Option option2 = new Option("option2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    List<Option> options = commandLine.getOptions();
    
    assertEquals(2, options.size());
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }
  
  @Test
  public void testGetOptionTriggers() {
    Option option1 = new Option("option1");
    Option option2 = new Option("option2");
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    Set<String> optionTriggers = commandLine.getOptionTriggers();
    
    assertEquals(2, optionTriggers.size());
    assertTrue(optionTriggers.contains("option1"));
    assertTrue(optionTriggers.contains("option2"));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option1"));
    assertTrue(commandLine.looksLikeOption("--option2"));
    assertFalse(commandLine.looksLikeOption("value"));
  }
  
  @Test
  public void testToString() {
    Option option = new Option("testOption");
    Object value = "testValue";
    commandLine.addValue(option, value);
    List<String> normalised = commandLine.getNormalised();
    
    StringBuilder expected = new StringBuilder();
    for (String argument : normalised) {
      expected.append(argument).append(" ");
    }
    
    assertEquals(expected.toString().trim(), commandLine.toString());
  }
}