import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
  
  private WriteableCommandLine commandLine;
  private Option option;
  
  @Before
  public void setup() {
    commandLine = new WriteableCommandLineImpl();
    option = new OptionImpl("test");
  }
  
  @Test
  public void testAddOption() {
    assertTrue(commandLine.getOptions().isEmpty());
    
    commandLine.addOption(option);
    
    assertEquals(1, commandLine.getOptions().size());
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testAddValue() {
    assertNull(commandLine.getValue(option));
    
    String value = "value";
    commandLine.addValue(option, value);
    
    assertEquals(value, commandLine.getValue(option));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
    
    String value1 = "value1";
    String value2 = "value2";
    commandLine.addValue(option, value1);
    commandLine.addValue(option, value2);
    
    List<String> values = commandLine.getUndefaultedValues(option);
    
    assertEquals(2, values.size());
    assertTrue(values.contains(value1));
    assertTrue(values.contains(value2));
  }
  
  @Test
  public void testSetDefaultValues() {
    assertTrue(commandLine.getDefaultValues(option).isEmpty());
    
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("default1");
    defaultValues.add("default2");
    
    commandLine.setDefaultValues(option, defaultValues);
    
    assertEquals(defaultValues, commandLine.getDefaultValues(option));
  }
  
  @Test
  public void testAddSwitch() {
    assertTrue(commandLine.getSwitches().isEmpty());
    
    commandLine.addSwitch(option, true);
    
    assertEquals(1, commandLine.getSwitches().size());
    assertTrue(commandLine.getSwitches().contains(option));
    
    assertTrue(commandLine.getSwitchValue(option));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testAddDuplicateSwitch() {
    commandLine.addSwitch(option, true);
    commandLine.addSwitch(option, false);
  }
  
  @Test
  public void testSetDefaultSwitch() {
    assertNull(commandLine.getDefaultSwitch(option));
    
    commandLine.setDefaultSwitch(option, true);
    
    assertTrue(commandLine.getDefaultSwitch(option));
  }
  
  @Test
  public void testAddProperty() {
    assertTrue(commandLine.getProperties().isEmpty());
    
    String property = "property";
    String value = "value";
    
    commandLine.addProperty(option, property, value);
    
    assertEquals(value, commandLine.getProperty(option, property));
  }
  
  @Test
  public void testAddDefaultProperty() {
    assertTrue(commandLine.getProperties().isEmpty());
    
    String property = "property";
    String value = "value";
    
    commandLine.addProperty(property, value);
    
    assertEquals(value, commandLine.getProperty(null, property));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option"));
    assertTrue(commandLine.looksLikeOption("--option"));
    assertFalse(commandLine.looksLikeOption("value"));
  }
  
  @Test
  public void testGetCurrentOption() {
    assertNull(commandLine.getCurrentOption());
    
    commandLine.setCurrentOption(option);
    
    assertNotNull(commandLine.getCurrentOption());
    assertEquals(option, commandLine.getCurrentOption());
  }
  
  @Test
  public void testSetCurrentOption() {
    assertNull(commandLine.getCurrentOption());
    
    commandLine.setCurrentOption(option);
    
    assertNotNull(commandLine.getCurrentOption());
    assertEquals(option, commandLine.getCurrentOption());
  }
}