import org.junit.*;
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
  }

  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());

    OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
    assertFalse(option.hasArg());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("argument");
    Option option = OptionBuilder.create();
    assertEquals("argument", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithRequiredBoolean() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());

    OptionBuilder.isRequired(false);
    option = OptionBuilder.create();
    assertFalse(option.isRequired());
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsWithNumber() {
    OptionBuilder.hasArgs(2);
    Option option = OptionBuilder.create();
    assertEquals(2, option.getArgs());
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
  public void testHasOptionalArgsWithNumber() {
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
    OptionBuilder.withDescription("This is a test option");
    Option option = OptionBuilder.create();
    assertEquals("This is a test option", option.getDescription());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithCharacter() {
    OptionBuilder.create(' t');
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
}