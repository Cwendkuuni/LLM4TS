import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 // Test for withLongOpt method
 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("optionA").create();
 assertEquals("optionA", option.getLongOpt());
 }

 // Test for hasArg method
 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg().create();
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 }

 // Test for hasArg with argument name
 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg().withArgName("argName").create();
 assertEquals("argName", option.getArgName());
 }

 // Test for isRequired method
 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired().create();
 assertTrue(option.isRequired());
 }

 // Test for withValueSeparator method
 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withValueSeparator(':').create();
 assertEquals(':', option.getValueSeparator());
 }

 // Test for hasArgs method
 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs().create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 // Test for hasArgs with specified number of arguments
 @Test
 public void testHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs(3).create();
 assertEquals(3, option.getArgs().length);
 }

 // Test for hasOptionalArg method
 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg().create();
 assertTrue(option.optionalArg);
 }

 // Test for hasOptionalArgs method
 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArgs(2).create();
 assertEquals(2, option.getMaxArgCount());
 }

 // Test for withType method
 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = new Option("testOption", "Test Option");
 Class<Integer> intType = Integer.class;
 OptionBuilder optionBuilderWithType = optionBuilder.withType(intType);
 assertEquals(intType, optionBuilderWithType.create("testOption").getType());
 }

 // Test for create method with required option
 @Test
 public void testCreateWithRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired().create();
 assertTrue(option.isRequired());
 }

 // Test for create method with optional arg
 @Test
 public void testCreateWithOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg().create();
 assertTrue(option.optionalArg);
 }

 // Test for create method with uninitialized arguments
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithUninitializedArguments() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 fail("create method should throw IllegalArgumentException when called without setting long opt");
 }

 // Test for create method with invalid long opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("test").create();
 fail("create method should throw IllegalArgumentException when invalid long opt is provided");
 }
}