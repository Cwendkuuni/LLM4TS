import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option expectedOption = new Option("longOpt", "description for long opt");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option(null, null, true, "arg");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option(null, null, true, "arg");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option expectedOption = new Option(null, null, false, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option(null, null, true, null);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option(null, null, false, null, ':', null);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option("args", "description for args", Option.UNLIMITED_VALUES);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option expectedOption = new Option("args", "description for args", 3);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option expectedOption = new Option("args", "description for args", Option.UNLIMITED_VALUES, null);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasOptionalArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
 Option expectedOption = new Option("args", "description for args", Option.UNLIMITED_VALUES, null);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderHasOptionalArgsWithNumberAndArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5, "argName");
 Option expectedOption = new Option("args", "description for args", Option.UNLIMITED_VALUES, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option expectedOption = new Option(null, null, false, null, null, "java.lang.String");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("description for option");
 Option expectedOption = new Option(null, "description for option", false, null, null, null);
 assertEquals(expectedOption, optionBuilder.create());
 }
}