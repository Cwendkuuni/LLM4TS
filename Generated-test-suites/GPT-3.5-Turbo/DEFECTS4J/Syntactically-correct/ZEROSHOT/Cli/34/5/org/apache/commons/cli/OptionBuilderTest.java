import org.junit.Test;
import static org.junit.Assert.*;

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
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
    OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
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
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithDefaultValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
    OptionBuilder.isRequired(false);
    option = OptionBuilder.create();
    assertFalse(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsWithNum() {
    OptionBuilder.hasArgs(3);
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
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
    assertFalse(option.hasArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder.hasOptionalArgs(4);
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
    assertEquals(4, option.getArgs());
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

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals('t', option.getOpt());
  }

  @Test
  public void testCreateWithNull() {
    try {
      OptionBuilder.create(null);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }

  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }
}