import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder option = OptionBuilder.withLongOpt("testLongOpt");
    assertEquals("testLongOpt", option.getLongopt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder option = OptionBuilder.hasArg();
    assertEquals(1, option.getNumberOfArgs());
  }

  @Test
  public void testHasArgWithFalse() {
    OptionBuilder option = OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder option = OptionBuilder.withArgName("testArgName");
    assertEquals("testArgName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder option = OptionBuilder.isRequired();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder option = OptionBuilder.withDescription("testDescription");
    assertEquals("testDescription", option.getDescription());
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals('t', option.getOpt());
  }

  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("testLongOpt");
    Option option = OptionBuilder.create();
    assertEquals("testLongOpt", option.getLongOpt());
  }

  @Test
  public void testCreateString() {
    Option option = OptionBuilder.create("testOption");
    assertEquals("testOption", option.getOpt());
  }

  // Add more test cases for other methods as needed

  // Test edge cases and exception handling scenarios

  @Test
  public void testDefaultValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create('t');
    assertEquals('=', option.getValueSeparator());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }

}