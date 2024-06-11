import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
  
  private Option option;

  @Before
  public void setUp() {
    option = new Option("opt", "description");
  }

  @Test
  public void testGetOpt() {
    assertEquals("opt", option.getOpt());
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
  }

  @Test
  public void testHasArg() {
    assertFalse(option.hasArg());
  }

  @Test
  public void testGetDescription() {
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testSetDescription() {
    option.setDescription("newDescription");
    assertEquals("newDescription", option.getDescription());
  }

  @Test
  public void testIsRequired() {
    assertFalse(option.isRequired());
  }

  @Test
  public void testSetRequired() {
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
  }

  @Test
  public void testHasArgs() {
    assertFalse(option.hasArgs());
  }

  @Test
  public void testSetArgs() {
    option.setArgs(2);
    assertTrue(option.hasArgs());
    assertEquals(2, option.getArgs());
  }

  @Test
  public void testSetValueSeparator() {
    option.setValueSeparator(',');
    assertTrue(option.hasValueSeparator());
    assertEquals(',', option.getValueSeparator());
  }

  @Test
  public void testHasValueSeparator() {
    assertFalse(option.hasValueSeparator());
  }

  @Test
  public void testGetArgs() {
    assertEquals(-1, option.getArgs());
  }

  @Test(expected = RuntimeException.class)
  public void testAddValueForProcessingThrowsException() {
    option.addValueForProcessing("value");
  }

  @Test
  public void testAdd() {
    option.setArgs(2);
    option.add("value1");
    option.add("value2");
    assertArrayEquals(new String[] {"value1", "value2"}, option.getValues());
  }

  @Test
  public void testGetValue() {
    assertNull(option.getValue());
  }

  @Test
  public void testGetValueWithIndex() {
    assertNull(option.getValue(1));
  }

  @Test
  public void testGetValueWithDefault() {
    assertEquals("default", option.getValue("default"));
  }

  @Test
  public void testGetValues() {
    assertNull(option.getValues());
  }

  @Test
  public void testGetValuesList() {
    assertNotNull(option.getValuesList());
    assertTrue(option.getValuesList().isEmpty());
  }

  @Test
  public void testToString() {
    assertEquals("[ option: opt  [ARG] :: description ]", option.toString());
  }

  @Test
  public void testEquals() {
    assertTrue(option.equals(option));
    assertFalse(option.equals(null));

    Option other = new Option("opt", "description");
    assertTrue(option.equals(other));

    Option differentOpt = new Option("opt2", "description");
    assertFalse(option.equals(differentOpt));

    Option differentLongOpt = new Option("opt", "description");
    differentLongOpt.setLongOpt("longOpt");
    assertFalse(option.equals(differentLongOpt));
  }

  @Test
  public void testHashCode() {
    Option other = new Option("opt", "description");
    assertEquals(option.hashCode(), other.hashCode());
  }
  
  @Test
  public void testClone() {
    Option cloned = (Option) option.clone();
    assertEquals(option.getOpt(), cloned.getOpt());
    assertEquals(option.getLongOpt(), cloned.getLongOpt());
    assertEquals(option.getDescription(), cloned.getDescription());
    assertEquals(option.isRequired(), cloned.isRequired());
    assertEquals(option.getArgName(), cloned.getArgName());
    assertEquals(option.getArgs(), cloned.getArgs());
    assertEquals(option.getValueSeparator(), cloned.getValueSeparator());
  }

  @Test
  public void testClearValues() {
    option.setArgs(2);
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
    option.clearValues();
    assertNull(option.getValue());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddValueThrowsException() {
    option.addValue("value");
  }

}