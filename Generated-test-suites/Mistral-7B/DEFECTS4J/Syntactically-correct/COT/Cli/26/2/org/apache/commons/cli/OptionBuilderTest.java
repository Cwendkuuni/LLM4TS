import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Option expectedOption = new Option("optionShortName", "optionLongName", false, "Optional description");
 Assert.assertEquals(expectedOption.getLongOpt(), optionBuilder.getOption().getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option("argName", "Optional description", true, "Required argument");
 Assert.assertEquals(expectedOption.getArgs(), Option.ONE_VALUE);
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgWithArgName("newArgName");
 Option expectedOption = new Option("argName", "Optional description", true, "Required argument with newArgName");
 Assert.assertEquals(expectedOption.getArgName(), optionBuilder.getOption().getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option("argName", "Optional description", true, "Required argument");
 Assert.assertTrue(expectedOption.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option("argName", "Optional description", true, "Required argument with ':' as separator");
 Assert.assertEquals(expectedOption.getValueSeparator(), optionBuilder.getOption().getValueSeparator());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = Option.builder("argName").hasArgs(Option.UNLIMITED_VALUES).build();
 Assert.assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option("argName", "Optional description", true, null);
 Assert.assertEquals(expectedOption.getArgs(), Option.ONE_VALUE);
 Assert.assertTrue(expectedOption.isOptional());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option expectedOption = Option.builder("argName").hasArgs(Option.UNLIMITED_VALUES).isOptional().build();
 Assert.assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
 Assert.assertTrue(expectedOption.isOptional());
 }

 @Test
 public void testHasOptionalArgsWithNumArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
 Option expectedOption = Option.builder("argName").hasArgs(3).isOptional().build();
 Assert.assertEquals(expectedOption.getArgs(), 3);
 Assert.assertTrue(expectedOption.isOptional());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option expectedOption = new Option("argName", "Optional description", true, null).getType(String.class);
 Assert.assertEquals(expectedOption.getType(), String.class);
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("updated description");
 Option expectedOption = new Option("argName", "updated description", true, "Updated description");
 Assert.assertEquals(expectedOption.getDescription(), optionBuilder.getOption().getDescription());
 }

 @Test
 public void testCreateWithInvalidLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("invalidLongOpt");
 Assert.assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
 }

 @Test
 public void testCreateWithInvalidCharOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Assert.assertThrows(IllegalArgumentException.class, () -> optionBuilder.create((char) 0));
 }
}