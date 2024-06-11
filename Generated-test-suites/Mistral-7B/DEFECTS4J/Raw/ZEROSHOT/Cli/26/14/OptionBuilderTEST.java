import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
 Option option = OptionBuilder.create('L');
 assertEquals("longOption", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = OptionBuilder.create('H');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = OptionBuilder.create('P');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create('R');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('O');
 assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = OptionBuilder.create('I');
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
 Option option = OptionBuilder.create('D');
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = OptionBuilder.create('V');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderReset() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
 Option option = OptionBuilder.create('L');
 OptionBuilder.reset();
 assertNull(OptionBuilder.longopt);
 }
}