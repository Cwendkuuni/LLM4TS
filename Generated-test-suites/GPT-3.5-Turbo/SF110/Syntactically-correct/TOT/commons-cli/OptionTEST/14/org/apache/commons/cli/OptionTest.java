import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
  
  private Option option;
  
  @Before
  public void setUp() {
    option = new Option("-o", "Output option");
  }
  
  @Test
  public void testGetOpt() {
    assertEquals("-o", option.getOpt());
  }
  
  @Test
  public void testGetLongOpt() {
    assertNull(option.getLongOpt());
  }
  
  @Test
  public void testSetLongOpt() {
    option.setLongOpt("output");
    assertEquals("output", option.getLongOpt());
  }
  
  @Test
  public void testSetOptionalArg() {
    assertFalse(option.hasOptionalArg());
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasLongOpt() {
    assertFalse(option.hasLongOpt());
    option.setLongOpt("output");
    assertTrue(option.hasLongOpt());
  }
  
  @Test
  public void testHasArg() {
    assertFalse(option.hasArg());
    option.setArgs(1);
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testGetDescription() {
    assertEquals("Output option", option.getDescription());
  }
  
  @Test
  public void testSetDescription() {
    option.setDescription("Specifies the output file");
    assertEquals("Specifies the output file", option.getDescription());
  }
  
  @Test
  public void testIsRequired() {
    assertFalse(option.isRequired());
    option.setRequired(true);
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testSetArgName() {
    assertEquals("arg", option.getArgName());
    option.setArgName("filename");
    assertEquals("filename", option.getArgName());
  }
  
  @Test
  public void testHasArgs() {
    assertFalse(option.hasArgs());
    option.setArgs(2);
    assertTrue(option.hasArgs());
  }
  
  @Test
  public void testSetArgs() {
    assertEquals(-1, option.getArgs());
    option.setArgs(3);
    assertEquals(3, option.getArgs());
  }
  
  @Test
  public void testHasValueSeparator() {
    assertFalse(option.hasValueSeparator());
    option.setValueSeparator(',');
    assertTrue(option.hasValueSeparator());
    assertEquals(',', option.getValueSeparator());
  }
  
  @Test
  public void testGetValues() {
    assertNull(option.getValues());
    option.addValueForProcessing("value1");
    option.addValueForProcessing("value2");
    String[] values = option.getValues();
    assertNotNull(values);
    assertEquals(2, values.length);
    assertEquals("value1", values[0]);
    assertEquals("value2", values[1]);
  }
  
  @Test
  public void testToString() {
    assertEquals("[ option: -o :: Output option ]", option.toString());
  }
  
  @Test
  public void testEquals() {
    Option option2 = new Option("-o", "Output option");
    assertEquals(option, option2);
    
    option2.setLongOpt("output");
    assertNotEquals(option, option2);
    option.setLongOpt("output");
    assertEquals(option, option2);
  }
  
  @Test
  public void testHashCode() {
    Option option2 = new Option("-o", "Output option");
    assertEquals(option.hashCode(), option2.hashCode());
    
    option2.setLongOpt("output");
    assertNotEquals(option.hashCode(), option2.hashCode());
    option.setLongOpt("output");
    assertEquals(option.hashCode(), option2.hashCode());
  }
  
  @Test
  public void testClone() {
    Option clonedOption = (Option) option.clone();
    assertEquals(option, clonedOption);
    assertNotSame(option, clonedOption);
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
}