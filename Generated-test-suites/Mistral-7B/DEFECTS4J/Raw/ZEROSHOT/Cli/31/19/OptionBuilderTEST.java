import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
 Option option = optionBuilder.create('o');
 assertEquals("optionLong", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('a');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('o');
 assertEquals(1, option.getArgs().length);
 assertEquals(true, option.isOptionalArg());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('n');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('r');
 assertEquals(true, option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('s');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('h');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithNum() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
 Option option = optionBuilder.create('g');
 assertEquals(5, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderCreate() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
 .hasArg()
 .withArgName("argName")
 .isRequired()
 .withValueSeparator(':')
 .hasArgs(5);
 Option option = optionBuilder.create('x');
 assertEquals("optionLong", option.getLongOpt());
 assertEquals(1, option.getArgs().length);
 assertEquals("argName", option.getArgName());
 assertEquals(true, option.isRequired());
 assertEquals(':', option.getValueSeparator());
 assertEquals(5, option.getArgs().length);
 }
}