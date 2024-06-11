import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 // Test case for method withLongOpt
 @Test
 public void testWithLongOpt() {
 Options options = new Options();
 OptionBuilder.withLongOpt("optionA");
 Option optionA = OptionBuilder.create('A');
 options.addOption(optionA);

 Assert.assertEquals("optionA", optionA.getLongOpt());
 }

 // Test case for method hasArg
 @Test
 public void testHasArg() {
 Options options = new Options();
 OptionBuilder.hasArg();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals(1, option.getArgCount());
 }

 // Test case for method hasArg with argName
 @Test
 public void testHasArgWithArgName() {
 Options options = new Options();
 OptionBuilder.hasArg().withArgName("param");
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals("param", option.getArgName());
 }

 // Test case for method isRequired with true
 @Test
 public void testIsRequiredTrue() {
 Options options = new Options();
 OptionBuilder.isRequired();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertTrue(option.isRequired());
 }

 // Test case for method isRequired with false
 @Test
 public void testIsRequiredFalse() {
 Options options = new Options();
 OptionBuilder.isRequired(false);
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertFalse(option.isRequired());
 }

 // Test case for method hasArgs
 @Test
 public void testHasArgs() {
 Options options = new Options();
 OptionBuilder.hasArgs();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
 }

 // Test case for method hasArgs with specific number of arguments
 @Test
 public void testHasArgsWithSpecificNumber() {
 Options options = new Options();
 OptionBuilder.hasArgs(5);
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals(5, option.getArgCount());
 }

 // Test case for method hasOptionalArg
 @Test
 public void testHasOptionalArg() {
 Options options = new Options();
 OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertTrue(option.isOptional());
 }

 // Test case for method hasOptionalArgs
 @Test
 public void testHasOptionalArgs() {
 Options options = new Options();
 OptionBuilder.hasOptionalArgs();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertTrue(option.isOptional());
 }

 // Test case for method hasOptionalArgs with specific number of optional arguments
 @Test
 public void testHasOptionalArgsWithSpecificNumber() {
 Options options = new Options();
 OptionBuilder.hasOptionalArgs(3);
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals(3, option.getArgCount());
 Assert.assertTrue(option.isOptional());
 }

 // Test case for method withType
 @Test
 public void testWithType() {
 Options options = new Options();
 OptionBuilder.withType(Integer.class);
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals(Integer.class, option.getType());
 }

 // Test case for method withDescription
 @Test
 public void testWithDescription() {
 Options options = new Options();
 OptionBuilder.withDescription("option description");
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals("option description", option.getDescription());
 }

 // Test case for method create with valid option
 @Test
 public void testCreateValidOption() {
 Options options = new Options();
 Option option = OptionBuilder.create('A');
 options.addOption(option);

 Assert.assertEquals('A', option.getOpt());
 }

 // Test case for method create with invalid option
 @Test(expected = IllegalArgumentException.class)
 public void testCreateInvalidOption() {
 Options options = new Options();
 OptionBuilder.create(' ');
 }

 // Test case for method create with empty long opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateEmptyLongOpt() {
 Options options = new Options();
 OptionBuilder.create(null);
 }

 // Test case for method create with null long opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateNullLongOpt() {
 Options options = new Options();
 OptionBuilder.create("");
 }
}