import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Before
  public void setUp() {
    OptionBuilder.reset();
  }

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("option1");
    Option option = OptionBuilder.create();
    assertEquals("option1", option.getLongOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }

  @Test
  public void testHasArg() {
    OptionBuilder.withLongOpt("option1").hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }

  @Test
  public void testHasArgCustom() {
    OptionBuilder.withLongOpt("option1").hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }

  @Test
  public void testHasArgCustomFalse() {
    OptionBuilder.withLongOpt("option1").hasArg(false);
    Option option = OptionBuilder.create();
    assertFalse(option.hasArg());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withLongOpt("option1").withArgName("argument");
    Option option = OptionBuilder.create();
    assertEquals("argument", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.withLongOpt("option1").isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testIsRequiredCustom() {
    OptionBuilder.withLongOpt("option1").isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    OptionBuilder.withLongOpt("option1").hasOptionalArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithOptionalArgs() {
    OptionBuilder.withLongOpt("option1").hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testWithOptionalArgsCustom() {
    int numArgs = 3;
    OptionBuilder.withLongOpt("option1").hasOptionalArgs(numArgs);
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
    assertEquals(numArgs, option.getArgs());
  }

  @Test
  public void testWithNoValueType() {
    OptionBuilder.withLongOpt("option1");
    Option option = OptionBuilder.create();
    assertNull(option.getType());
  }

  @Test
  public void testWithType() {
    OptionBuilder.withLongOpt("option1").withType(Integer.class);
    Option option = OptionBuilder.create();
    assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    String description = "This is an option";
    OptionBuilder.withLongOpt("option1").withDescription(description);
    Option option = OptionBuilder.create();
    assertEquals(description, option.getDescription());
  }

  @Test
  public void testWithValueSeparatorCustom() {
    char separator = ':';
    OptionBuilder.withLongOpt("option1").withValueSeparator(separator);
    Option option = OptionBuilder.create();
    assertEquals(separator, option.getValueSeparator());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withLongOpt("option1").withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }

  // Additional test cases for complete code coverage

  @Test
  public void testHasArgs() {
    OptionBuilder.withLongOpt("option1").hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsCustom() {
    int numArgs = 2;
    OptionBuilder.withLongOpt("option1").hasArgs(numArgs);
    Option option = OptionBuilder.create();
    assertEquals(numArgs, option.getArgs());
  }

  @Test
  public void testCreateChar() {
    char opt = 'o';
    OptionBuilder.create(opt);
    Option option = OptionBuilder.create();
    assertEquals(String.valueOf(opt), option.getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateString() {
    String opt = "option1";
    OptionBuilder.create(opt);
  }

}