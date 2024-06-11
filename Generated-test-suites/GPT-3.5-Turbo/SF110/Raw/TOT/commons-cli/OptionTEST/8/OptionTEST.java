import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

  Option option;

  @Before
  public void setUp() {
    option = new Option("o", "option", true, "This is an option");
  }

  @Test
  public void testGetOpt() {
    assertEquals("o", option.getOpt());
  }

  @Test
  public void testGetType() {
    assertNull(option.getType());
  }

  @Test
  public void testSetType() {
    option.setType(String.class);
    assertEquals(String.class, option.getType());
  }

  @Test
  public void testGetLongOpt() {
    assertEquals("option", option.getLongOpt());
  }

  @Test
  public void testSetLongOpt() {
    option.setLongOpt("newoption");
    assertEquals("newoption", option.getLongOpt());
  }

  @Test
  public void testSetOptionalArg() {
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasLongOpt() {
    assertTrue(option.hasLongOpt());
  }

  @Test
  public void testHasArg() {
    assertTrue(option.hasArg());
  }

  @Test
  public void testGetDescription() {
    assertEquals("This is an option", option.getDescription());
  }

  @Test
  public void testSetDescription() {
    option.setDescription("This is a new option");
    assertEquals("This is a new option", option.getDescription());
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
    option.setArgName("arg");
    assertEquals("arg", option.getArgName());
  }

  @Test
  public void testHasArgName() {
    option.setArgName("arg");
    assertTrue(option.hasArgName());
  }

  @Test
  public void testHasArgs() {
    option.setArgs(2);
    assertTrue(option.hasArgs());
  }

  @Test
  public void testSetArgs() {
    option.setArgs(2);
    assertEquals(2, option.getArgs());
  }

  @Test
  public void testSetValueSeparator() {
    option.setValueSeparator(',');
    assertEquals(',', option.getValueSeparator());
  }

  @Test
  public void testHasValueSeparator() {
    option.setValueSeparator(':');
    assertTrue(option.hasValueSeparator());
  }

  @Test
  public void testAddValueForProcessing() {
    try {
      option.addValueForProcessing("value1");
      option.addValueForProcessing("value2");
      assertEquals("value1", option.getValue(0));
      assertEquals("value2", option.getValue(1));
    } catch (Exception e) {
      fail("Should not throw exception");
    }
  }

  @Test
  public void testAddValueForProcessing_NoArgsAllowed() {
    option.setArgs(Option.UNINITIALIZED);
    try {
      option.addValueForProcessing("value1");
      fail("Should throw exception");
    } catch (RuntimeException e) {
      assertEquals("NO_ARGS_ALLOWED", e.getMessage());
    }
  }

  @Test
  public void testGetValues() {
    try {
      option.addValueForProcessing("value1");
      option.addValueForProcessing("value2");
      String[] values = option.getValues();
      assertEquals(2, values.length);
      assertEquals("value1", values[0]);
      assertEquals("value2", values[1]);
    } catch (Exception e) {
      fail("Should not throw exception");
    }
  }

  @Test
  public void testGetValuesList() {
    try {
      option.addValueForProcessing("value1");
      option.addValueForProcessing("value2");
      List<String> valuesList = option.getValuesList();
      assertEquals(2, valuesList.size());
      assertEquals("value1", valuesList.get(0));
      assertEquals("value2", valuesList.get(1));
    } catch (Exception e) {
      fail("Should not throw exception");
    }
  }

  @Test
  public void testToString() {
    String expected = "[ option: o option [ARG] :: This is an option ]";
    assertEquals(expected, option.toString());
  }

  @Test
  public void testEquals() {
    Option sameOption = new Option("o", "option", true, "This is an option");
    Option differentOption = new Option("d", "different", true, "This is a different option");
    
    assertTrue(option.equals(option));
    assertTrue(option.equals(sameOption));
    assertFalse(option.equals(differentOption));
    assertFalse(option.equals(null));
    assertFalse(option.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    Option sameOption = new Option("o", "option", true, "This is an option");
    
    assertEquals(option.hashCode(), sameOption.hashCode());
  }

  @Test
  public void testClone() {
    Option cloneOption = (Option) option.clone();
    
    assertEquals(option.getOpt(), cloneOption.getOpt());
    assertEquals(option.getType(), cloneOption.getType());
    assertEquals(option.getLongOpt(), cloneOption.getLongOpt());
    assertEquals(option.getDescription(), cloneOption.getDescription());
    assertEquals(option.isRequired(), cloneOption.isRequired());
    assertEquals(option.getArgName(), cloneOption.getArgName());
    assertEquals(option.hasArgs(), cloneOption.hasArgs());
    assertEquals(option.getArgs(), cloneOption.getArgs());
    assertEquals(option.getValueSeparator(), cloneOption.getValueSeparator());
  }

  @Test
  public void testClearValues() {
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
    option.clearValues();
    
    assertEquals(0, option.getValuesList().size());
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testAddValue() {
    option.addValue("value");
  }

  @After
  public void tearDown() {
    option = null;
  }

}