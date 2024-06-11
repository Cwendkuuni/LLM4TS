import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
    assertNotNull(optionBuilder);
    optionBuilder = OptionBuilder.hasArg(false);
    assertNotNull(optionBuilder);
  }

  // Additional test case for negative argument value
  @Test
  public void testHasArgWithNegative() {
    try {
      OptionBuilder optionBuilder = OptionBuilder.hasArg(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of arguments must be non-negative.", e.getMessage());
    }
  }

  @Test
  public void testWithArgName() {
    OptionBuilder optionBuilder = OptionBuilder.withArgName("arg");
    assertNotNull(optionBuilder);
  }

  @Test
  public void testIsRequired() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
    assertNotNull(optionBuilder);
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
    assertNotNull(optionBuilder);
    optionBuilder = OptionBuilder.isRequired(false);
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasArgsWithNumber() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
    assertNotNull(optionBuilder);
  }

  // Additional test cases for negative argument value
  @Test
  public void testHasArgsWithNegative() {
    try {
      OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of arguments must be non-negative.", e.getMessage());
    }
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
    assertNotNull(optionBuilder);
  }

  @Test
  public void testHasOptionalArgsWithNumber() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
    assertNotNull(optionBuilder);
  }

  // Additional test cases for negative argument value
  @Test
  public void testHasOptionalArgsWithNegative() {
    try {
      OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of arguments must be non-negative.", e.getMessage());
    }
  }

  @Test
  public void testWithType() {
    OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
    assertNotNull(optionBuilder);
  }

  @Test
  public void testWithDescription() {
    OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
    assertNotNull(optionBuilder);
  }

  @Test
  public void testCreateChar() {
  	try {
      Option option = OptionBuilder.create('t');
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }
  
  @Test
  public void testCreate() {
  	try {
      Option option = OptionBuilder.create();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }

  @Test
  public void testCreateString() {
  	try {
      Option option = OptionBuilder.create("test");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("must specify longopt", e.getMessage());
    }
  }
}