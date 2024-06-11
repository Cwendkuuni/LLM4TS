import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
 Option option = optionBuilder.create('o');
 Assert.assertEquals("optionLong", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('a');
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('o');
 Assert.assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('a');
 Assert.assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('r');
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('s');
 Assert.assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('h');
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option option = optionBuilder.create('x');
 Assert.assertEquals(3, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
 Option option = optionBuilder.create('i');
 Assert.assertEquals(Integer.TYPE, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
 Option option = optionBuilder.create('d');
 Assert.assertEquals("Option description", option.getDescription());
 }

 @Test
 public void testCreateOptionWithNoSettings() {
 Option option = OptionBuilder.create();
 Assert.assertNull(option.getLongOpt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.create();
 Option option = optionBuilder.create('x');
 }

 @Test
 public void testCreateOptionWithAllSettings() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
 .hasArg()
 .withArgName("argName")
 .isRequired()
 .hasOptionalArg()
 .withValueSeparator(':')
 .hasArgs(3)
 .withType(Integer.TYPE)
 .withDescription("Option description");
 Option option = optionBuilder.create('o');
 Assert.assertEquals("optionLong", option.getLongOpt());
 Assert.assertEquals(1, option.getArgs().length);
 Assert.assertEquals("argName", option.getArgName());
 Assert.assertTrue(option.isRequired());
 Assert.assertTrue(option.hasOptionalArg());
 Assert.assertEquals(':', option.getValueSeparator());
 Assert.assertEquals(3, option.getArgs().length);
 Assert.assertEquals(Integer.TYPE, option.getType());
 Assert.assertEquals("Option description", option.getDescription());
 }

 @Test
 public void testCreateOptionsWithMultipleOptions() {
 Options options = new Options();
 OptionBuilder optionBuilder1 = OptionBuilder.withLongOpt("option1Long")
 .hasArg()
 .create('o');
 OptionBuilder optionBuilder2 = OptionBuilder.withLongOpt("option2Long")
 .create('p');
 options.addOption(optionBuilder1.create('o'));
 options.addOption(optionBuilder2.create('p'));
 Assert.assertEquals(2, options.getOptions().length);
 }
}