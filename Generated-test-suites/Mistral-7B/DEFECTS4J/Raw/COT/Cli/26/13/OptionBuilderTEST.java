import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withLongOpt("optionName");
 Option expectedOption = new Option("optionName", null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithLongOptIfNotSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.create();
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.hasArg();
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getArgName(), actualOption.getArgName());
 assertEquals(expectedOption.getArgs(), Option.VALUE_ONE_ARG);
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.hasArg().withArgName("argName");
 Option expectedOption = Option.builder().required(false).argName("argName").build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getArgName(), actualOption.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.isRequired();
 Option expectedOption = Option.builder().required(true).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withValueSeparator(',');
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 expectedOption.setValueSeparator(',');
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withType(String.class);
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 expectedOption.setType(String.class);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getType(), actualOption.getType());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 expectedOption.setOptionalArg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.isOptionalArg(), actualOption.isOptionalArg());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.hasOptionalArgs();
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 expectedOption.setOptionalArgs(Option.UNLIMITED_VALUES);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
 }

 @Test
 public void testHasOptionalArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.hasOptionalArgs(2);
 Option expectedOption = Option.builder().required(false).argName("arg").build();
 expectedOption.setOptionalArgs(2);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getArgs(), 2);
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withDescription("option description");
 Option expectedOption = new Option("option", "option description");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.getDescription(), actualOption.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.create();
 }

 @Test
 public void testCreateWithLongOptAndDescription() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withLongOpt("optionName").withDescription("option description");
 Option expectedOption = new Option("optionName", "option description");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.getDescription(), actualOption.getDescription());
 }

 @Test
 public void testReset() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 OptionBuilder.reset();
 Option expectedOption = Option.UNINITIALIZED_OPTION;
 Option actualOption = optionBuilder.create();
 assertSame(expectedOption, actualOption);
 }

 @Test
 public void testInstance() {
 OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
 OptionBuilder actualOptionBuilder = OptionBuilder.instance();
 assertSame(expectedOptionBuilder, actualOptionBuilder);
 }
}