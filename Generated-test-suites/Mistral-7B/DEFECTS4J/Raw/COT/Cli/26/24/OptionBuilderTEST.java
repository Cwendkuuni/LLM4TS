import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptValue");
 Option expectedOption = new Option(null, "longOptValue", true, "longOpt description");
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option(null, null, true, "has arg option description");
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
 Option expectedOption = new Option("argName", null, true, "argName option description");
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option(null, null, true, "required option description");
 expectedOption.setRequired(true);
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option(null, null, true, "option with value separator : description");
 expectedOption.setValueSeparator(':');
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, "option with unlimited args description");
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgsWithNumArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option expectedOption = new Option(null, null, 3, "option with 3 args description");
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option(null, null, 1, "option with optional arg description");
 expectedOption.setOptionalArg(true);
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, "option with unlimited optional args description");
 expectedOption.setOptionalArg(true);
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArgsWithNumArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
 Option expectedOption = new Option(null, null, 2, "option with 2 optional args description");
 expectedOption.setOptionalArg(true);
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = new Option(null, null, true, "option with type Integer description");
 Assert.assertEquals(Integer.class, expectedOption.getType());
 Assert.assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("option with description description");
 Option expectedOption = new Option(null, null, true, "option with description description");
 Assert.assertEquals("option with description description", optionBuilder.create().getLongOpt());
 }

 @Test
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Assert.assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
 }
}