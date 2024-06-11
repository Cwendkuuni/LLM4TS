import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
    assertEquals("test", optionBuilder.longopt);
  }

  @Test
  public void testHasArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg();
    assertEquals(1, optionBuilder.numberOfArgs);
  }

  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
    assertEquals(1, optionBuilder.numberOfArgs);
    
    optionBuilder = OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
  }

  @Test
  public void testWithArgName() {
    OptionBuilder optionBuilder = OptionBuilder.withArgName("test");
    assertEquals("test", optionBuilder.argName);
  }

  @Test
  public void testIsRequired() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired();
    assertTrue(optionBuilder.required);
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
    assertEquals(':', optionBuilder.valuesep);
  }

  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
    assertEquals('=', optionBuilder.valuesep);
  }

  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
    assertTrue(optionBuilder.required);
    
    optionBuilder = OptionBuilder.isRequired(false);
    assertFalse(optionBuilder.required);
  }

  @Test
  public void testHasArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs();
    assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
  }

  @Test
  public void testHasArgsWithNum() {
    OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
    assertEquals(5, optionBuilder.numberOfArgs);
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
    assertEquals(1, optionBuilder.numberOfArgs);
    assertTrue(optionBuilder.optionalArg);
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
    assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    assertTrue(optionBuilder.optionalArg);
  }

  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
    assertEquals(3, optionBuilder.numberOfArgs);
    assertTrue(optionBuilder.optionalArg);
  }

  @Test
  public void testWithType() {
    Object type = new Object();
    OptionBuilder optionBuilder = OptionBuilder.withType(type);
    assertEquals(type, optionBuilder.type);
  }

  @Test
  public void testWithDescription() {
    OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
    assertEquals("test", optionBuilder.description);
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());
    assertEquals("t", option.getDescription());
  }

  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertNull(option.getOpt());
    assertEquals("test", option.getLongOpt());
    assertNull(option.getDescription());
  }

  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("t");
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());
    assertNull(option.getDescription());
  }
  
  // Add more tests for other methods
  
}