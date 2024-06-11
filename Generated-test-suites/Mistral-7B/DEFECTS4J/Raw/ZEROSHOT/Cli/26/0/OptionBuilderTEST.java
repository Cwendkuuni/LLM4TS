import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaultValues() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('optionName');

 Assert.assertNull(option.getLongOpt());
 Assert.assertEquals("arg", option.getArgName());
 Assert.assertFalse(option.isRequired());
 Assert.assertTrue(option.hasArg());
 Assert.assertEquals(1, option.getNumberOfArgs());
 Assert.assertNull(option.getType());
 Assert.assertEquals(0, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithCustomValues() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("--long-option");
 optionBuilder.hasArg();
 optionBuilder.withArgName("argName");
 optionBuilder.isRequired();
 optionBuilder.withValueSeparator(':');
 optionBuilder.hasType(Long.TYPE);

 Option option = optionBuilder.create('o');

 Assert.assertEquals("--long-option", option.getLongOpt());
 Assert.assertEquals("argName", option.getArgName());
 Assert.assertTrue(option.isRequired());
 Assert.assertTrue(option.hasArg());
 Assert.assertEquals(':', option.getValueSeparator());
 Assert.assertEquals(Long.TYPE, option.getType());
 }

 @Test
 public void testOptionBuilderWithOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArg();

 Option option = optionBuilder.create('o');

 Assert.assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithUnlimitedArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs();

 Option option = optionBuilder.create('o');

 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
 }

 @Test
 public void testOptionBuilderWithSpecifiedNumberOfArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs(3);

 Option option = optionBuilder.create('o');

 Assert.assertEquals(3, option.getNumberOfArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('o');
 }
}