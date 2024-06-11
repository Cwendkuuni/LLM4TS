import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
  
  @Test
  public void testConstructor1() {
    try {
      Option option = new Option("opt", "description");
      assertEquals("opt", option.getOpt());
      assertNull(option.getArgName());
      assertEquals(-1, option.getArgs());
      assertEquals("description", option.getDescription());
      assertFalse(option.isRequired());
      assertFalse(option.hasLongOpt());
      assertFalse(option.hasArg());
      assertFalse(option.hasOptionalArg());
      assertFalse(option.hasArgName());
      assertFalse(option.hasArgs());
      assertFalse(option.hasValueSeparator());
      assertEquals("[ option: opt  :: description ]", option.toString());
    } catch (IllegalArgumentException e) {
      fail("Unexpected IllegalArgumentException");
    }
  }
  
  @Test
  public void testConstructor2() {
    try {
      Option option = new Option("opt", true, "description");
      assertEquals("opt", option.getOpt());
      assertNull(option.getArgName());
      assertEquals(1, option.getArgs());
      assertEquals("description", option.getDescription());
      assertFalse(option.isRequired());
      assertFalse(option.hasLongOpt());
      assertTrue(option.hasArg());
      assertFalse(option.hasOptionalArg());
      assertFalse(option.hasArgName());
      assertFalse(option.hasArgs());
      assertFalse(option.hasValueSeparator());
      assertEquals("[ option: opt  [ARG] :: description ]", option.toString());
    } catch (IllegalArgumentException e) {
      fail("Unexpected IllegalArgumentException");
    }
  }
  
  @Test
  public void testConstructor3() {
    try {
      Option option = new Option("opt", "longOpt", true, "description");
      assertEquals("opt", option.getOpt());
      assertEquals("longOpt", option.getLongOpt());
      assertNull(option.getArgName());
      assertEquals(1, option.getArgs());
      assertEquals("description", option.getDescription());
      assertFalse(option.isRequired());
      assertTrue(option.hasLongOpt());
      assertTrue(option.hasArg());
      assertFalse(option.hasOptionalArg());
      assertFalse(option.hasArgName());
      assertFalse(option.hasArgs());
      assertFalse(option.hasValueSeparator());
      assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());
    } catch (IllegalArgumentException e) {
      fail("Unexpected IllegalArgumentException");
    }
  }
  
  @Test
  public void testSetType() {
    Option option = new Option("opt", "description");
    assertNull(option.getType());
    option.setType(String.class);
    assertEquals(String.class, option.getType());
  }
  
  @Test
  public void testSetLongOpt() {
    Option option = new Option("opt", "description");
    assertFalse(option.hasLongOpt());
    option.setLongOpt("longOpt");
    assertTrue(option.hasLongOpt());
    assertEquals("longOpt", option.getLongOpt());
  }
  
  @Test
  public void testSetOptionalArg() {
    Option option = new Option("opt", "description");
    assertFalse(option.hasOptionalArg());
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasLongOpt() {
    Option option1 = new Option("opt", "description");
    assertFalse(option1.hasLongOpt());
    Option option2 = new Option("opt", "longOpt", true, "description");
    assertTrue(option2.hasLongOpt());
  }
  
  @Test
  public void testHasArg() {
    Option option1 = new Option("opt", "description");
    assertFalse(option1.hasArg());
    Option option2 = new Option("opt", true, "description");
    assertTrue(option2.hasArg());
    Option option3 = new Option("opt", "longOpt", true, "description");
    assertTrue(option3.hasArg());
  }
  
  @Test
  public void testSetDescription() {
    Option option = new Option("opt", "description");
    assertEquals("description", option.getDescription());
    option.setDescription("new description");
    assertEquals("new description", option.getDescription());
  }
  
  @Test
  public void testSetRequired() {
    Option option = new Option("opt", "description");
    assertFalse(option.isRequired());
    option.setRequired(true);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testSetArgName() {
    Option option = new Option("opt", "description");
    assertFalse(option.hasArgName());
    option.setArgName("argName");
    assertTrue(option.hasArgName());
    assertEquals("argName", option.getArgName());
  }
  
  @Test
  public void testHasArgs() {
    Option option1 = new Option("opt", "description");
    assertFalse(option1.hasArgs());
    Option option2 = new Option("opt", true, "description");
    assertFalse(option2.hasArgs());
    Option option3 = new Option("opt", "longOpt", true, "description");
    assertFalse(option3.hasArgs());
    option3.setArgs(3);
    assertTrue(option3.hasArgs());
  }
  
  @Test
  public void testSetValueSeparator() {
    Option option = new Option("opt", "description");
    assertFalse(option.hasValueSeparator());
    option.setValueSeparator(':');
    assertTrue(option.hasValueSeparator());
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testClearValues() {
    Option option = new Option("opt", "description");
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
    assertEquals(2, option.getValues().length);
    option.clearValues();
    assertNull(option.getValues());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddValue() {
    Option option = new Option("opt", "description");
    option.addValue("value");
  }

}