import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgWithArgument() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgWithoutArgument() {
    OptionBuilder.hasArg(false);
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithDefaultSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithArgument() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testIsRequiredWithoutArgument() {
    OptionBuilder.isRequired(false);
    Option option = OptionBuilder.create();
    assertFalse(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithArgument() {
    OptionBuilder.hasArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithArgument() {
    OptionBuilder.hasOptionalArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
    Option option = OptionBuilder.create();
    assertEquals(String.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getDescription());
  }
  
  @Test
  public void testCreateWithCharOpt() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }
  
  @Test
  public void testCreateWithoutLongOpt() {
    try {
      OptionBuilder.create();
      fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }
  
  @Test
  public void testCreateWithOpt() {
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }
}