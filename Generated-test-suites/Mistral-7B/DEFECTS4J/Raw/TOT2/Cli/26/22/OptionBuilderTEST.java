import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create();
 Assert.assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create();
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 Assert.assertEquals(1, option.getArgs().length);
 Assert.assertTrue(option.isOptionalArg());
 }

 @Test
 public void testOptionBuilderValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create();
 Assert.assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithNum() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option option = optionBuilder.create();
 Assert.assertEquals(3, option.getArgs().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderInvalidHasArgsNum() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
 // create option will throw IllegalArgumentException
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = optionBuilder.create();
 Assert.assertTrue(option.getType() instanceof Class);
 Assert.assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
 Option option = optionBuilder.create();
 Assert.assertEquals("Description", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderUndefinedLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 optionBuilder.reset();
 Assert.assertNull(option.getLongOpt());
 optionBuilder.withLongOpt("longOpt");
 option = optionBuilder.create();
 Assert.assertEquals("longOpt", option.getLongOpt());
 optionBuilder.reset();
 option = optionBuilder.create();
 Assert.assertNull(option.getLongOpt());
 }
}