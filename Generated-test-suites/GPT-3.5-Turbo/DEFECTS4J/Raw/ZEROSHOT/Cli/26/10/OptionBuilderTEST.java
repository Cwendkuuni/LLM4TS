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
    assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithFalse() {
    OptionBuilder.hasArg(false);
    Option option = OptionBuilder.create();
    assertEquals(Option.UNINITIALIZED, option.getArgs());
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
  public void testWithDefaultValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
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
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsWithNum() {
    OptionBuilder.hasArgs(5);
    Option option = OptionBuilder.create();
    assertEquals(5, option.getArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    assertEquals(1, option.getArgs());
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
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder.hasOptionalArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
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
    OptionBuilder.withDescription("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getDescription());
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('c');
    assertEquals("c", option.getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNull() {
    OptionBuilder.create(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNoLongOpt() {
    OptionBuilder.create();
  }

  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }
}