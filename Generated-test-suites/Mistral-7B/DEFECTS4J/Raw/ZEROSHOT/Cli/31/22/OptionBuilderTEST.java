import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder.withLongOpt("longOptKey");
 Option option = OptionBuilder.create('l');
 Assert.assertEquals("longOptKey", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder.hasArg();
 Option option = OptionBuilder.create();
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
 OptionBuilder.reset();
 OptionBuilder.hasArg(true);
 Option optionWithArg = OptionBuilder.create();
 Assert.assertEquals(1, optionWithArg.getArgs());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder.withArgName("argName");
 Option option = OptionBuilder.create('a');
 Assert.assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder.isRequired();
 Option option = OptionBuilder.create();
 Assert.assertFalse(option.isRequired());
 OptionBuilder.reset();
 OptionBuilder.isRequired(true);
 Option optionRequired = OptionBuilder.create();
 Assert.assertTrue(optionRequired.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder.withValueSeparator(':');
 Option option = OptionBuilder.create('v');
 Assert.assertEquals(':', option.getValueSeparator());
 OptionBuilder.reset();
 OptionBuilder.withValueSeparator();
 Option optionWithValueSeparator = OptionBuilder.create('v');
 Assert.assertEquals('=', optionWithValueSeparator.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder.hasArgs();
 Option option = OptionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create();
 Assert.assertFalse(option.hasOptionalArg());
 OptionBuilder.reset();
 OptionBuilder.hasOptionalArg(true);
 Option optionWithOptionalArg = OptionBuilder.create();
 Assert.assertTrue(optionWithOptionalArg.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder.withType(String.class);
 Option option = OptionBuilder.create('t');
 Assert.assertEquals(String.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder.withDescription("A test description");
 Option option = OptionBuilder.create('d');
 Assert.assertEquals("A test description", option.getDescription());
 }

 @Test
 public void testOptionBuilderCreate() {
 Options options = new Options();
 OptionBuilder.hasArg();
 OptionBuilder.withArgName("argName");
 OptionBuilder.create(options, 'a');
 Options parsedOptions = new Options();
 OptionCommandLineParser parser = new OptionCommandLineParser(options);
 String[] args = {"-a", "argValue"};
 try {
 parser.parse(args);
 } catch (ParseException e) {
 // JUnit does not support ParseException, so we cannot use Assert.fail(e);
 }
 Option option = parsedOptions.getOption("argName");
 Assert.assertEquals(1, option.getArgs().length);
 Assert.assertEquals("argValue", option.getArgs()[0]);
 }
}