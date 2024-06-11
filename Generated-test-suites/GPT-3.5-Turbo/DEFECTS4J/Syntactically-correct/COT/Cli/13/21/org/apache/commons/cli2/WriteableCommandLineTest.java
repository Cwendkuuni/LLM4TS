
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {
  
  private WriteableCommandLine writeableCommandLine;

  @Before
  public void setUp() {
    writeableCommandLine = new WriteableCommandLineImpl();
  }
  
  @Test
  public void testAddOption() {
    Option option = new OptionImpl("testOption");
    writeableCommandLine.addOption(option);
    
    List<Option> options = writeableCommandLine.getOptions();
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new OptionImpl("testOption");
    Object value = "testValue";
    writeableCommandLine.addValue(option, value);
    
    Object retrievedValue = writeableCommandLine.getValue(option);
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testGetValues() {
    Option option = new OptionImpl("testOption");
    Object value1 = "testValue1";
    Object value2 = "testValue2";
    
    writeableCommandLine.addValue(option, value1);
    writeableCommandLine.addValue(option, value2);
    
    List<Object> expectedValues = new ArrayList<>();
    expectedValues.add(value1);
    expectedValues.add(value2);
    
    List<Object> retrievedValues = writeableCommandLine.getValues(option);
    assertEquals(expectedValues, retrievedValues);
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new OptionImpl("testOption");
    List<Object> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    writeableCommandLine.setDefaultValues(option, defaultValues);
    
    List<Object> retrievedDefaultValues = writeableCommandLine.getDefaultValues(option);
    assertEquals(defaultValues, retrievedDefaultValues);
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new OptionImpl("testOption");
    boolean value = true;
    writeableCommandLine.addSwitch(option, value);
    
    boolean retrievedValue = writeableCommandLine.getSwitch(option);
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new OptionImpl("testOption");
    Boolean defaultSwitch = true;
    writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
    
    Boolean retrievedDefaultSwitch = writeableCommandLine.getDefaultSwitch(option);
    assertEquals(defaultSwitch, retrievedDefaultSwitch);
  }
  
  @Test
  public void testAddProperty() {
    String property = "testProperty";
    String value = "testValue";
    writeableCommandLine.addProperty(property, value);
    
    String retrievedValue = writeableCommandLine.getProperty(property);
    assertEquals(value, retrievedValue);
  }
  
  @Test
  public void testLooksLikeOption() {
    String argument = "-testOption";
    assertTrue(writeableCommandLine.looksLikeOption(argument));
  }
  
}
