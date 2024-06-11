import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
    Option option = builder.create();

    assertEquals("testLongOpt", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder builder = OptionBuilder.hasArg();
    Option option = builder.create();

    assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithArg() {
    OptionBuilder builder = OptionBuilder.hasArg(true);
    Option option = builder.create();

    assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithoutArg() {
    OptionBuilder builder = OptionBuilder.hasArg(false);
    Option option = builder.create();

    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder builder = OptionBuilder.withArgName("testArgName");
    Option option = builder.create();

    assertEquals("testArgName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder builder = OptionBuilder.isRequired();
    Option option = builder.create();

    assertTrue(option.isRequired());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder builder = OptionBuilder.withValueSeparator(':');
    Option option = builder.create();

    assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder builder = OptionBuilder.withValueSeparator();
    Option option = builder.create();

    assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testIsRequiredTrue() {
    OptionBuilder builder = OptionBuilder.isRequired(true);
    Option option = builder.create();

    assertTrue(option.isRequired());
  }

  @Test
  public void testIsRequiredFalse() {
    OptionBuilder builder = OptionBuilder.isRequired(false);
    Option option = builder.create();

    assertFalse(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder builder = OptionBuilder.hasArgs();
    Option option = builder.create();

    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsWithArg() {
    OptionBuilder builder = OptionBuilder.hasArgs(2);
    Option option = builder.create();

    assertEquals(2, option.getArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder builder = OptionBuilder.hasOptionalArg();
    Option option = builder.create();

    assertEquals(1, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs();
    Option option = builder.create();

    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithArg() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
    Option option = builder.create();

    assertEquals(3, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    OptionBuilder builder = OptionBuilder.withType(Integer.class);
    Option option = builder.create();

    assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder builder = OptionBuilder.withDescription("testDescription");
    Option option = builder.create();

    assertEquals("testDescription", option.getDescription());
  }

  @Test
  public void testCreateChar() {
    OptionBuilder builder = OptionBuilder.create('t');
    Option option = builder.create();

    assertEquals("t", option.getOpt());
  }

  @Test
  public void testCreate() {
    OptionBuilder builder = OptionBuilder.create();
    Option option = builder.create();

    assertNull(option.getOpt());
    assertNotNull(option.getLongOpt());
  }

  @Test
  public void testCreateString() {
    OptionBuilder builder = OptionBuilder.create("test");
    Option option = builder.create();

    assertEquals("test", option.getOpt());
  }
}