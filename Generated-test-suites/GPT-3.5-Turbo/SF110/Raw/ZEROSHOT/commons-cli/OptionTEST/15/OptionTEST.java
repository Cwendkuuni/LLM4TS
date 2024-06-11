import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {
  
  // Test variables
  private Option option;
  
  @Before
  public void setUp() {
    option = new Option("test", "Test Option");
  }
  
  @Test
  public void testGetOpt() {
    assertEquals("test", option.getOpt());
  }
  
  @Test
  public void testGetType() {
    assertNull(option.getType());
  }
  
  @Test
  public void testSetType() {
    option.setType("type");
    assertEquals("type", option.getType());
  }
  
  @Test
  public void testGetLongOpt() {
    assertNull(option.getLongOpt());
  }
  
  @Test
  public void testSetLongOpt() {
    option.setLongOpt("longOpt");
    assertEquals("longOpt", option.getLongOpt());
  }
  
  @Test
  public void testSetOptionalArg() {
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasLongOpt() {
    assertFalse(option.hasLongOpt());
    option.setLongOpt("longOpt");
    assertTrue(option.hasLongOpt());
  }
  
  @Test
  public void testHasArg() {
    assertFalse(option.hasArg());
    option.setArgs(1);
    assertTrue(option.hasArg());
    option.setArgs(Option.UNLIMITED_VALUES);
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testGetDescription() {
    assertEquals("Test Option", option.getDescription());
  }
  
  @Test
  public void testSetDescription() {
    option.setDescription("New Description");
    assertEquals("New Description", option.getDescription());
  }
  
  @Test
  public void testIsRequired() {
    assertFalse(option.isRequired());
    option.setRequired(true);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testSetArgName() {
    option.setArgName("argName");
    assertEquals("argName", option.getArgName());
  }
  
  @Test
  public void testHasArgName() {
    assertFalse(option.hasArgName());
    option.setArgName("argName");
    assertTrue(option.hasArgName());
  }
  
  @Test
  public void testHasArgs() {
    assertFalse(option.hasArgs());
    option.setArgs(2);
    assertTrue(option.hasArgs());
    option.setArgs(Option.UNLIMITED_VALUES);
    assertTrue(option.hasArgs());
  }
  
  @Test
  public void testSetValueSeparator() {
    option.setValueSeparator(',');
    assertEquals(',', option.getValueSeparator());
  }
  
  @Test
  public void testHasValueSeparator() {
    assertFalse(option.hasValueSeparator());
    option.setValueSeparator(',');
    assertTrue(option.hasValueSeparator());
  }
  
  @Test
  public void testGetArgs() {
    assertEquals(-1, option.getArgs());
    option.setArgs(2);
    assertEquals(2, option.getArgs());
  }
  
  @Test (expected = RuntimeException.class)
  public void testAddValueForProcessing_Exception() {
    option.addValueForProcessing("value");
  }
  
  @Test
  public void testProcessValue() {
    option.setArgs(2);
    option.addValueForProcessing("value1,value2");
    assertEquals("value1", option.getValue(0));
    assertEquals("value2", option.getValue(1));
  }
  
  @Test (expected = RuntimeException.class)
  public void testAdd_Exception() {
    option.setArgs(1);
    option.addValue("value1");
    option.addValue("value2");
  }
  
  @Test
  public void testGetValue() {
    assertNull(option.getValue());
    option.addValue("value");
    assertEquals("value", option.getValue());
  }
  
  @Test
  public void testGetValue_Index() {
    assertNull(option.getValue(0));
    option.addValue("value");
    assertEquals("value", option.getValue(0));
  }
  
  @Test
  public void testGetValue_DefaultValue() {
    assertNull(option.getValue("default"));
    option.addValue("value");
    assertEquals("value", option.getValue("default"));
  }
  
  @Test
  public void testGetValues() {
    assertNull(option.getValues());
    option.addValue("value1");
    option.addValue("value2");
    assertArrayEquals(new String[] {"value1", "value2"}, option.getValues());
  }
  
  @Test
  public void testGetValuesList() {
    assertEquals(new ArrayList<String>(), option.getValuesList());
    option.addValue("value1");
    option.addValue("value2");
    List<String> valuesList = new ArrayList<String>();
    valuesList.add("value1");
    valuesList.add("value2");
    assertEquals(valuesList, option.getValuesList());
  }
  
  @Test
  public void testToString() {
    assertEquals("[ option: test :: Test Option ]", option.toString());
    option.setLongOpt("longOpt");
    option.setArgs(2);
    assertEquals("[ option: test longOpt [ARG...] :: Test Option ]", option.toString());
  }
  
  @Test
  public void testEquals_True() {
    Option option2 = new Option("test", "Test Option");
    assertTrue(option.equals(option2));
    option.setLongOpt("longOpt");
    option2.setLongOpt("longOpt");
    assertTrue(option.equals(option2));
  }
  
  @Test
  public void testEquals_False() {
    Option option2 = new Option("test2", "Test Option2");
    assertFalse(option.equals(option2));
    option.setLongOpt("longOpt");
    assertFalse(option.equals(option2));
    option2.setLongOpt("longOpt2");
    assertFalse(option.equals(option2));
  }
  
  @Test
  public void testHashCode() {
    Option option2 = new Option("test", "Test Option");
    assertEquals(option.hashCode(), option2.hashCode());
    option2.setLongOpt("longOpt");
    option.setLongOpt("longOpt");
    assertEquals(option.hashCode(), option2.hashCode());
  }
  
  @Test
  public void testClone() {
    Option clonedOption = (Option)option.clone();
    assertEquals(option.getOpt(), clonedOption.getOpt());
    assertEquals(option.getDescription(), clonedOption.getDescription());
    assertEquals(option.getType(), clonedOption.getType());
    assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
    assertEquals(option.isRequired(), clonedOption.isRequired());
    assertEquals(option.getArgName(), clonedOption.getArgName());
    assertEquals(option.getArgs(), clonedOption.getArgs());
    assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
  }
  
  @Test
  public void testClearValues() {
    option.addValue("value1");
    option.addValue("value2");
    option.clearValues();
    assertEquals(new ArrayList<String>(), option.getValuesList());
  }
  
  @Test (expected = UnsupportedOperationException.class)
  public void testAddValue_Exception() {
    option.addValue("value");
  }
}