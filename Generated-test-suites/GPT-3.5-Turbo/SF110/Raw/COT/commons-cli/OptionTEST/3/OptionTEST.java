import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

  @Test
  public void testOptionConstructor1() {
    Option option = new Option("test", "description");
    assertEquals("test", option.getOpt());
    assertNull(option.getLongOpt());
    assertFalse(option.hasArg());
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testOptionConstructor2() {
    Option option = new Option("test", true, "description");
    assertEquals("test", option.getOpt());
    assertNull(option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testOptionConstructor3() {
    Option option = new Option("test", "longOpt", true, "description");
    assertEquals("test", option.getOpt());
    assertEquals("longOpt", option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testGetId() {
    Option option = new Option("test", "description");
    assertEquals('t', option.getId());
  }

  @Test
  public void testGetOpt() {
    Option option = new Option("test", "description");
    assertEquals("test", option.getOpt());
  }

  @Test
  public void testGetType() {
    Option option = new Option("test", "description");
    assertNull(option.getType());
  }

  @Test
  public void testSetType() {
    Option option = new Option("test", "description");
    option.setType(Object.class);
    assertEquals(Object.class, option.getType());
  }

  @Test
  public void testGetLongOpt() {
    Option option = new Option("test", "description");
    assertNull(option.getLongOpt());
  }

  @Test
  public void testSetLongOpt() {
    Option option = new Option("test", "description");
    option.setLongOpt("longOpt");
    assertEquals("longOpt", option.getLongOpt());
  }

  @Test
  public void testSetOptionalArg() {
    Option option = new Option("test", "description");
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasLongOpt() {
    Option option = new Option("test", "description");
    assertFalse(option.hasLongOpt());
  }

  @Test
  public void testHasArg() {
    Option option1 = new Option("test", "description");
    assertFalse(option1.hasArg());

    Option option2 = new Option("test", true, "description");
    assertTrue(option2.hasArg());

    Option option3 = new Option("test", "longOpt", true, "description");
    assertTrue(option3.hasArg());
  }

  @Test
  public void testGetDescription() {
    Option option = new Option("test", "description");
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testSetDescription() {
    Option option = new Option("test", "description");
    option.setDescription("new description");
    assertEquals("new description", option.getDescription());
  }

  @Test
  public void testIsRequired() {
    Option option = new Option("test", "description");
    assertFalse(option.isRequired());
  }

  @Test
  public void testSetRequired() {
    Option option = new Option("test", "description");
    option.setRequired(true);
    assertTrue(option.isRequired());
  }

  @Test
  public void testSetArgName() {
    Option option = new Option("test", "description");
    option.setArgName("argName");
    assertEquals("argName", option.getArgName());
  }

  @Test
  public void testGetArgName() {
    Option option = new Option("test", "description");
    assertNull(option.getArgName());
  }

  @Test
  public void testHasArgName() {
    Option option = new Option("test", "description");
    assertFalse(option.hasArgName());
  }

  @Test
  public void testHasArgs() {
    Option option1 = new Option("test", "description");
    assertFalse(option1.hasArgs());

    Option option2 = new Option("test", true, "description");
    assertFalse(option2.hasArgs());

    Option option3 = new Option("test", 2, "description");
    assertTrue(option3.hasArgs());
  }

  @Test
  public void testSetArgs() {
    Option option = new Option("test", "description");
    option.setArgs(2);
    assertEquals(2, option.getArgs());
  }

  @Test
  public void testSetValueSeparator() {
    Option option = new Option("test", "description");
    option.setValueSeparator(',');
    assertEquals(',', option.getValueSeparator());
  }

  @Test
  public void testHasValueSeparator() {
    Option option = new Option("test", "description");
    assertFalse(option.hasValueSeparator());
  }

  @Test
  public void testGetArgs() {
    Option option = new Option("test", "description");
    assertEquals(-1, option.getArgs());
  }

  @Test(expected = RuntimeException.class)
  public void testAddValueForProcessingNoArgsAllowed() {
    Option option = new Option("test", "description");
    option.setArgs(Option.UNINITIALIZED);
    option.addValueForProcessing("value");
  }

  @Test(expected = RuntimeException.class)
  public void testAddValueForProcessingListFull() {
    Option option = new Option("test", 1, "description");
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
  }

  @Test
  public void testAddValueForProcessing() {
    Option option = new Option("test", 2, "description");
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
    assertEquals("value1", option.getValue(0));
    assertEquals("value2", option.getValue(1));
  }

  @Test
  public void testGetValue() {
    Option option = new Option("test", "description");
    assertNull(option.getValue());

    Option option2 = new Option("test", true, "description");
    option2.addValueForProcessing("value");
    assertEquals("value", option2.getValue());
  }

  @Test
  public void testGetValueIndexOutOfBounds() {
    Option option = new Option("test", "description");
    assertNull(option.getValue(0));
  }

  @Test
  public void testGetValueWithDefaultValue() {
    Option option = new Option("test", "description");
    assertNull(option.getValue("defaultValue"));

    Option option2 = new Option("test", true, "description");
    option2.addValueForProcessing("value");
    assertEquals("value", option2.getValue("defaultValue"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("test", "description");
    assertNull(option.getValues());

    Option option2 = new Option("test", 2, "description");
    option2.addValueForProcessing("value1");
    option2.addValueForProcessing("value2");
    assertArrayEquals(new String[]{"value1", "value2"}, option2.getValues());
  }

  @Test
  public void testGetValuesList() {
    Option option = new Option("test", "description");
    assertTrue(option.getValuesList().isEmpty());

    Option option2 = new Option("test", 2, "description");
    option2.addValueForProcessing("value1");
    option2.addValueForProcessing("value2");
    assertEquals(Arrays.asList("value1", "value2"), option2.getValuesList());
  }

  @Test
  public void testToString() {
    Option option1 = new Option("test", "description");
    assertEquals("[ option: test  :: description ]", option1.toString());

    Option option2 = new Option("test", "longOpt", true, "description");
    assertEquals("[ option: test longOpt [ARG] :: description ]", option2.toString());
  }

  @Test
  public void testEqualsSameObject() {
    Option option = new Option("test", "description");
    assertTrue(option.equals(option));
  }

  @Test
  public void testEqualsSameParameters() {
    Option option1 = new Option("test", "description");
    Option option2 = new Option("test", "description");
    assertTrue(option1.equals(option2));
  }

  @Test
  public void testEqualsDifferentOpt() {
    Option option1 = new Option("test1", "description");
    Option option2 = new Option("test2", "description");
    assertFalse(option1.equals(option2));
  }

  @Test
  public void testEqualsDifferentLongOpt() {
    Option option1 = new Option("test", "longOpt1", true, "description");
    Option option2 = new Option("test", "longOpt2", true, "description");
    assertFalse(option1.equals(option2));
  }

  @Test
  public void testEqualsDifferentClass() {
    Option option = new Option("test", "description");
    assertFalse(option.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    Option option1 = new Option("test", "description");
    Option option2 = new Option("test", "description");
    assertEquals(option1.hashCode(), option2.hashCode());
  }

  @Test
  public void testClone() {
    Option option1 = new Option("test", "description");
    option1.addValueForProcessing("value");
    Option option2 = (Option) option1.clone();
    assertEquals(option1, option2);
    assertEquals(option1.getValuesList(), option2.getValuesList());
    assertNotSame(option1.getValuesList(), option2.getValuesList());
  }

  @Test
  public void testClearValues() {
    Option option = new Option("test", "description");
    option.addValueForProcessing("value");
    option.clearValues();
    assertTrue(option.getValuesList().isEmpty());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddValue() {
    Option option = new Option("test", "description");
    option.addValue("value");
  }

}