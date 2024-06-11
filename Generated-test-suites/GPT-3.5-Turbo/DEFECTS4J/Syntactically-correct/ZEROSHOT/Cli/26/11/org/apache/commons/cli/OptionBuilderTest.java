import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    assertEquals("test", OptionBuilder.create().getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testHasArgBoolean() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.create().getArgs());
    
    OptionBuilder.hasArg(false);
    assertEquals(-1, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    assertEquals("test", OptionBuilder.create().getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.create().isRequired());
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    assertEquals(':', OptionBuilder.create().getValueSeparator());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.create().getValueSeparator());
  }
  
  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.create().isRequired());
    
    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.create().isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    assertEquals(-2, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testHasArgsInt() {
    OptionBuilder.hasArgs(2);
    assertEquals(2, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    assertEquals(1, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertEquals(-2, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder.hasOptionalArgs(2);
    assertEquals(2, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Object.class);
    assertEquals(Object.class, OptionBuilder.create().getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    assertEquals("test", OptionBuilder.create().getDescription());
  }
  
  @Test
  public void testCreateChar() {
    assertEquals("a", OptionBuilder.create('a').getOpt());
  }
  
  @Test
  public void testCreate() {
    try {
      OptionBuilder.create();
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }
  
  @Test
  public void testCreateString() {
    assertEquals("test", OptionBuilder.create("test").getOpt());
  }
  
}