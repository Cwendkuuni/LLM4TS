import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
 Option option = OptionBuilder.create();
 Assert.assertEquals("optLong", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = OptionBuilder.create();
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasArgWithName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
 Option option = OptionBuilder.create();
 Assert.assertEquals("argName", option.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = OptionBuilder.create();
 Assert.assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.optionalArg);
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = OptionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArgsWithNumArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
 Option option = OptionBuilder.create();
 Assert.assertEquals(5, option.getArgs().length);
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = OptionBuilder.create();
 Assert.assertNotNull(option.getType());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
 Option option = OptionBuilder.create();
 Assert.assertEquals("Option description", option.getLongOpt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("@");
 OptionBuilder.create();
 }

 @Test
 public void testCreateWithRequiredArgAndNoArg() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired().hasArg();
 Option option = OptionBuilder.create();
 Assert.assertFalse(option.isOptional());
 Assert.assertFalse(option.hasArg());
 }

 @Test
 public void testCreateWithOptionalArgAndNoArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().hasArg();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertTrue(option.hasArg());
 }

 @Test
 public void testCreateWithRequiredArgAndArg() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired().hasArg();
 Option option = OptionBuilder.create();
 Assert.assertFalse(option.isOptional());
 Assert.assertTrue(option.hasArg());
 }

 @Test
 public void testCreateWithOptionalArgAndArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().hasArgs();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertTrue(option.hasArgs());
 }

}