import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

  private WriteableCommandLine writeableCommandLine;
  private Option option;
  
  @Before
  public void setUp() {
    writeableCommandLine = new WriteableCommandLineImpl();
    option = new OptionImpl("test", "t", false, "Test Option");
  }
  
  @Test
  public void testAddOption() {
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.hasOption("test"));
  }
  
  @Test
  public void testAddValue() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.addValue(option, "value1");
    writeableCommandLine.addValue(option, "value2");
    
    List expectedValues = new ArrayList();
    expectedValues.add("value1");
    expectedValues.add("value2");
    
    assertEquals(expectedValues, writeableCommandLine.getUndefaultedValues(option));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    option.setArgs(2);
    writeableCommandLine.addOption(option);
    writeableCommandLine.addValue(option, "value1");
    writeableCommandLine.addValue(option, "value2");
    
    List expectedValues = new ArrayList();
    expectedValues.add("value1");
    expectedValues.add("value2");
    
    assertEquals(expectedValues, writeableCommandLine.getUndefaultedValues(option));
  }
  
  @Test
  public void testSetDefaultValues() {
    option.setArgs(2);
    writeableCommandLine.addOption(option);
    
    List defaultValues = new ArrayList();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    
    writeableCommandLine.setDefaultValues(option, defaultValues);
    
    assertEquals(defaultValues, writeableCommandLine.getUndefaultedValues(option));
  }
  
  @Test
  public void testAddSwitch() {
    option.setArgs(0);
    writeableCommandLine.addOption(option);
    
    writeableCommandLine.addSwitch(option, true);
    
    assertTrue(writeableCommandLine.hasOption("test"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testAddSwitch_ThrowsException() {
    option.setArgs(0);
    writeableCommandLine.addOption(option);
    
    writeableCommandLine.addSwitch(option, true);
    writeableCommandLine.addSwitch(option, false);
  }
  
  @Test
  public void testSetDefaultSwitch() {
    option.setArgs(0);
    writeableCommandLine.addOption(option);
    
    writeableCommandLine.setDefaultSwitch(option, true);
    
    assertTrue(writeableCommandLine.hasOption("test"));
  }
  
  @Test
  public void testAddProperty() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.addProperty(option, "property1", "value1");
    writeableCommandLine.addProperty(option, "property2", "value2");
    
    assertEquals("value1", writeableCommandLine.getProperty(option, "property1"));
    assertEquals("value2", writeableCommandLine.getProperty(option, "property2"));
  }
  
  @Test
  public void testAddProperty_DefaultSet() {
    writeableCommandLine.addProperty("property1", "value1");
    writeableCommandLine.addProperty("property2", "value2");
    
    assertEquals("value1", writeableCommandLine.getProperty(option, "property1"));
    assertEquals("value2", writeableCommandLine.getProperty(option, "property2"));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(writeableCommandLine.looksLikeOption("-t"));
    assertTrue(writeableCommandLine.looksLikeOption("--test"));
    assertFalse(writeableCommandLine.looksLikeOption("value1"));
    assertFalse(writeableCommandLine.looksLikeOption("-value1"));
  }
  
}