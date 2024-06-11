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
    assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasArgWithFalse() {
    OptionBuilder.hasArg(false);
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
    assertEquals(-1, option.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("name");
    Option option = OptionBuilder.create();
    assertEquals("name", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithFalse() {
    OptionBuilder.isRequired(false);
    Option option = OptionBuilder.create();
    assertFalse(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Integer.MAX_VALUE, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    OptionBuilder.hasArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertEquals(Integer.MAX_VALUE, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithNum() {
    OptionBuilder.hasOptionalArgs(2);
    Option option = OptionBuilder.create();
    assertEquals(2, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    assertEquals(Integer.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test description");
    Option option = OptionBuilder.create();
    assertEquals("test description", option.getDescription());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }
  
  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }
}