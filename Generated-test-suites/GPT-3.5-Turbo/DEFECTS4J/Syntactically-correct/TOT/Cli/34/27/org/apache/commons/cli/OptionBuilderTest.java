import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    // Typical use case: valid long option
    OptionBuilder.withLongOpt("longOptTest");
    Option option = OptionBuilder.create("a");
    assertEquals("longOptTest", option.getLongOpt());

    // Edge case: long option with special characters
    OptionBuilder.withLongOpt("longOpt#$%Test");
    Option option2 = OptionBuilder.create("b");
    assertEquals("longOpt#$%Test", option2.getLongOpt());

    // Error scenario: long option not specified
    try {
      OptionBuilder.create("c");
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testHasArg() {
    // Typical use case: hasArg is true
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create("a");
    assertTrue(option.hasArg());

    // Typical use case: hasArg is false
    OptionBuilder.hasArg(false);
    Option option2 = OptionBuilder.create("b");
    assertFalse(option2.hasArg());

    // Error scenario: invalid argument value for hasArg
    OptionBuilder.hasArg(true);
    Option option3 = OptionBuilder.create("c");
    assertTrue(option3.hasArg());

    try {
      OptionBuilder.hasArg(2);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testWithArgName() {
    // Typical use case: valid argument name
    OptionBuilder.withArgName("argNameTest");
    Option option = OptionBuilder.create("a");
    assertEquals("argNameTest", option.getArgName());

    // Edge case: empty argument name
    OptionBuilder.withArgName("");
    Option option2 = OptionBuilder.create("b");
    assertEquals("", option2.getArgName());

    // Error scenario: argument name not specified
    try {
      OptionBuilder.create("c");
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testIsRequired() {
    // Typical use case: option is required
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create("a");
    assertTrue(option.isRequired());

    // Typical use case: option is not required
    OptionBuilder.isRequired(false);
    Option option2 = OptionBuilder.create("b");
    assertFalse(option2.isRequired());

    // Error scenario: invalid argument value for isRequired
    OptionBuilder.isRequired(true);
    Option option3 = OptionBuilder.create("c");
    assertTrue(option3.isRequired());

    try {
      OptionBuilder.isRequired(2);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testWithValueSeparator() {
    // Typical use case: value separator specified
    OptionBuilder.withValueSeparator(';');
    Option option = OptionBuilder.create("a");
    assertEquals(';', option.getValueSeparator());

    // Typical use case: default value separator used
    OptionBuilder.withValueSeparator();
    Option option2 = OptionBuilder.create("b");
    assertEquals('=', option2.getValueSeparator());
  }

  @Test
  public void testHasArgs() {
    // Typical use case: unlimited number of arguments
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create("a");
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());

    // Typical use case: specific number of arguments
    OptionBuilder.hasArgs(3);
    Option option2 = OptionBuilder.create("b");
    assertEquals(3, option2.getArgs());
  }

  @Test
  public void testHasOptionalArg() {
    // Typical use case: option has an optional argument
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create("a");
    assertTrue(option.hasOptionalArg());

    // Error scenario: hasOptionalArg sets the number of arguments to 1
    OptionBuilder.hasOptionalArg();
    Option option2 = OptionBuilder.create("b");
    assertTrue(option2.hasOptionalArg());

    // Error scenario: hasOptionalArg sets the optional argument even when hasArg is false
    OptionBuilder.hasArg(false);
    OptionBuilder.hasOptionalArg();
    Option option3 = OptionBuilder.create("c");
    assertTrue(option3.hasOptionalArg());
  }

  @Test
  public void testWithOptionalArgs() {
    // Typical use case: option has unlimited number of optional arguments
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create("a");
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());

    // Typical use case: option has multiple optional arguments
    OptionBuilder.hasOptionalArgs(3);
    Option option2 = OptionBuilder.create("b");
    assertEquals(3, option2.getArgs());
    assertTrue(option2.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    // Typical use case: option value is of specified type
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create("a");
    assertEquals(Integer.class, option.getType());

    // Typical use case: option value has default type
    OptionBuilder.withType();
    Option option2 = OptionBuilder.create("b");
    assertNull(option2.getType());

    // Error scenario: invalid type specified
    try {
      OptionBuilder.withType(OptionBuilder.class);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testWithDescription() {
    // Typical use case: option has a description
    OptionBuilder.withDescription("Description Test");
    Option option = OptionBuilder.create("a");
    assertEquals("Description Test", option.getDescription());

    // Edge case: empty description
    OptionBuilder.withDescription("");
    Option option2 = OptionBuilder.create("b");
    assertEquals("", option2.getDescription());

    // Error scenario: description not specified
    try {
      OptionBuilder.create("c");
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
}