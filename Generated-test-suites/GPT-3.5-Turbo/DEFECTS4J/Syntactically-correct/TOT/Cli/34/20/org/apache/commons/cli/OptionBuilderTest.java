import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  
  // Testing withLongOpt method
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test").create();
    // Verify that the longopt is set correctly
    assertEquals("test", OptionBuilder.longopt);
  }
  
  @Test
  
  // Testing hasArg method
  public void testHasArg() {
    OptionBuilder.hasArg().create();
    // Verify that numberOfArgs is set to 1
    assertEquals(1, OptionBuilder.numberOfArgs);
  }
  
  @Test
  
  // Testing hasArg(boolean) method with true parameter
  public void testHasArgTrue() {
    OptionBuilder.hasArg(true).create();
    // Verify that numberOfArgs is set to 1
    assertEquals(1, OptionBuilder.numberOfArgs);
  }
  
  @Test
  
  // Testing hasArg(boolean) method with false parameter
  public void testHasArgFalse() {
    OptionBuilder.hasArg(false).create();
    // Verify that numberOfArgs is set to UNINITIALIZED
    assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
  }
  
  @Test
  
  // Testing withArgName method
  public void testWithArgName() {
    OptionBuilder.withArgName("arg").create();
    // Verify that argName is set correctly
    assertEquals("arg", OptionBuilder.argName);
  }
  
  @Test
  
  // Testing isRequired method
  public void testIsRequired() {
    OptionBuilder.isRequired().create();
    // Verify that required is set to true
    assertEquals(true, OptionBuilder.required);
  }
  
  @Test
  
  // Testing withValueSeparator(char) method
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':').create();
    // Verify that valuesep is set to ':'
    assertEquals(':', OptionBuilder.valuesep);
  }
  
  @Test
  
  // Testing withValueSeparator() method
  public void testWithValueSeparatorDefault() {
    OptionBuilder.withValueSeparator().create();
    // Verify that valuesep is set to '='
    assertEquals('=', OptionBuilder.valuesep);
  }
  
  @Test
  
  // Testing isRequired(boolean) method with true parameter
  public void testIsRequiredTrue() {
    OptionBuilder.isRequired(true).create();
    // Verify that required is set to true
    assertEquals(true, OptionBuilder.required);
  }
  
  @Test
  
  // Testing isRequired(boolean) method with false parameter
  public void testIsRequiredFalse() {
    OptionBuilder.isRequired(false).create();
    // Verify that required is set to false
    assertEquals(false, OptionBuilder.required);
  }
  
  @Test
  
  // Testing hasArgs method
  public void testHasArgs() {
    OptionBuilder.hasArgs().create();
    // Verify that numberOfArgs is set to UNLIMITED_VALUES
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
  }
  
  @Test
  
  // Testing hasArgs(int) method
  public void testHasArgsNumber() {
    OptionBuilder.hasArgs(3).create();
    // Verify that numberOfArgs is set to 3
    assertEquals(3, OptionBuilder.numberOfArgs);
  }
  
  @Test
  
  // Testing hasOptionalArg method
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg().create();
    // Verify that numberOfArgs is set to 1
    assertEquals(1, OptionBuilder.numberOfArgs);
    // Verify that optionalArg is set to true
    assertEquals(true, OptionBuilder.optionalArg);
  }
  
  @Test
  
  // Testing hasOptionalArgs method
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs().create();
    // Verify that numberOfArgs is set to UNLIMITED_VALUES
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    // Verify that optionalArg is set to true
    assertEquals(true, OptionBuilder.optionalArg);
  }
  
  @Test
  
  // Testing hasOptionalArgs(int) method
  public void testHasOptionalArgsNumber() {
    OptionBuilder.hasOptionalArgs(2).create();
    // Verify that numberOfArgs is set to 2
    assertEquals(2, OptionBuilder.numberOfArgs);
    // Verify that optionalArg is set to true
    assertEquals(true, OptionBuilder.optionalArg);
  }
  
  @Test
  
  // Testing withType method
  public void testWithType() {
    OptionBuilder.withType(Integer.class).create();
    // Verify that type is set to Integer.class
    assertEquals(Integer.class, OptionBuilder.type);
  }
  
  @Test
  
  // Testing withDescription method
  public void testWithDescription() {
    OptionBuilder.withDescription("description").create();
    // Verify that description is set correctly
    assertEquals("description", OptionBuilder.description);
  }
  
  @Test(expected = IllegalArgumentException.class)
  
  // Testing create(char) method with invalid option character
  public void testCreateInvalidChar() {
    OptionBuilder.create('$');
  }
  
  @Test(expected = IllegalArgumentException.class)
  
  // Testing create() method without setting longopt
  public void testCreateInvalidLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  
  // Testing create(char) method
  public void testCreateChar() {
    Option option = OptionBuilder.create('t');
    // Verify that the Option is created correctly
    assertEquals('t', option.getOpt());
    assertEquals(OptionBuilder.longopt, option.getLongOpt());
    assertEquals(OptionBuilder.description, option.getDescription());
    assertEquals(OptionBuilder.required, option.isRequired());
    assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
    assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
    assertEquals(OptionBuilder.type, option.getType());
    assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
    assertEquals(OptionBuilder.argName, option.getArgName());
  }
  
  @Test
  
  // Testing create() method
  public void testCreate() {
    Option option = OptionBuilder.create("test");
    // Verify that the Option is created correctly
    assertEquals("test", option.getOpt());
    assertEquals(OptionBuilder.longopt, option.getLongOpt());
    assertEquals(OptionBuilder.description, option.getDescription());
    assertEquals(OptionBuilder.required, option.isRequired());
    assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
    assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
    assertEquals(OptionBuilder.type, option.getType());
    assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
    assertEquals(OptionBuilder.argName, option.getArgName());
  }
}