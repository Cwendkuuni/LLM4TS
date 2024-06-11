import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withLongOpt("longOptValue").create();
 assertEquals("longOptValue", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArg().create();
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.withArgName("argName");
 Option option = optionBuilder.hasArg().create();
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.isRequired().create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withValueSeparator(':').create();
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testWithValueSeparatorEmpty() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withValueSeparator().create();
 assertEquals('=', option.getValueSeparator());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArgs().create();
 assertNull(option.getArgs());
 }

 @Test
 public void testHasArgsWithArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArgs(2).create();
 assertEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasOptionalArg().create();
 assertTrue(option.isOptionalArg());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasOptionalArgs().create();
 assertArrayEquals(new String[Option.UNLIMITED_VALUES], option.getArgs());
 }

 @Test
 public void testHasOptionalArgsWithArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasOptionalArgs(3).create();
 assertArrayEquals(new String[3], option.getArgs());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withType(Integer.TYPE).create();
 assertEquals(Integer.TYPE, option.getType());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withDescription("description").create();
 assertEquals("description", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.reset();
 try {
 Option option = optionBuilder.create();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}