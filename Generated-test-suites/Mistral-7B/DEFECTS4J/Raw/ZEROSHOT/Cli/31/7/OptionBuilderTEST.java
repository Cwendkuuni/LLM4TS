import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongOpt");
 Option option = optionBuilder.create('o');

 Assert.assertEquals("optionLongOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = OptionBuilder.create();

 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = OptionBuilder.create('a');

 Assert.assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create('r');

 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = OptionBuilder.create('v');

 Assert.assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = OptionBuilder.create('h');

 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('o');

 Assert.assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = OptionBuilder.create('i');

 Assert.assertEquals(Integer.class, option.getType());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOptionBuilderWithoutLongOpt() {
 OptionBuilder.create();
 }
}