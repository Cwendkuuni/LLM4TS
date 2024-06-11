import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
    assertEquals("testLongOpt", OptionBuilder.getLongopt());
    OptionBuilder.reset();
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    OptionBuilder.reset();
  }
  
  @Test
  public void testHasArgWithBooleanArg() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.getNumberOfArgs());
    OptionBuilder.hasArg(false);
    assertEquals(-1, OptionBuilder.getNumberOfArgs());
    OptionBuilder.reset();
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testArgName");
    assertEquals("testArgName", OptionBuilder.getArgName());
    OptionBuilder.reset();
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.isRequired());
    OptionBuilder.reset();
  }
  
  @Test
  public void testWithSeparator() {
    OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.getValuesep());
    OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.getValuesep());
    OptionBuilder.reset();
  }
  
  @Test
  public void testIsRequiredWithBooleanArg() {
    OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.isRequired());
    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.isRequired());
    OptionBuilder.reset();
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    assertEquals(-2, OptionBuilder.getNumberOfArgs());
    OptionBuilder.reset();
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertEquals(-2, OptionBuilder.getNumberOfArgs());
    OptionBuilder.reset();
  }
  
  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder.hasOptionalArgs(3);
    assertEquals(3, OptionBuilder.getNumberOfArgs());
    OptionBuilder.reset();
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Object.class);
    assertEquals(Object.class, OptionBuilder.getType());
    OptionBuilder.reset();
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("testDescription");
    assertEquals("testDescription", OptionBuilder.getDescription());
    OptionBuilder.reset();
  }
  
  @Test
  public void testCreateWithCharOpt() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());
    OptionBuilder.reset();
  }
  
  @Test
  public void testCreate() {
    try {
      OptionBuilder.create();
      fail("IllegalArgumentException not thrown");
    } catch(IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
    OptionBuilder.withLongOpt("testLongOpt");
    Option option = OptionBuilder.create();
    assertEquals("testLongOpt", option.getLongOpt());
    OptionBuilder.reset();
  }
  
  @Test
  public void testCreateWithStringOpt() {
    Option option = OptionBuilder.create("testOpt");
    assertEquals("testOpt", option.getOpt());
    assertNull(option.getLongOpt());
    OptionBuilder.reset();
  }

}