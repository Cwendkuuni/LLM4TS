import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

 @Test
 public void testLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptValue");
 Option option = optionBuilder.create('L');
 assertEquals("longOptValue", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('A');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('O');
 assertTrue(option.isOptionalArg());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option option = optionBuilder.create('H');
 assertEquals(2, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create('P');
 assertTrue(option.isUnbounded());
 }

 @Test
 public void testHasOptionalArgsWithLimitedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
 Option option = optionBuilder.create('Q');
 assertEquals(3, option.getArgs().length);
 }

 @Test
 public void testWithType() {
 Object type = Integer.class;
 OptionBuilder optionBuilder = OptionBuilder.withType(type);
 Option option = optionBuilder.create('T');
 assertEquals(type, option.getType());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option Description");
 Option option = optionBuilder.create('Z');
 assertEquals("Test Option Description", option.getDescription());
 }

 @Test
 public void testCreateWithEmptyLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
 Option option = optionBuilder.create('E');
 assertNull(option.getLongOpt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('F');
 }

 @Test
 public void testCreateWithEmptyDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("");
 Option option = optionBuilder.create('G');
 assertNull(option.getDescription());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('R');
 assertTrue(option.isRequired());
 }

 @Test
 public void testIsNotRequired() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('S');
 assertFalse(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('V');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testWithValueSeparatorEmpty() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
 Option option = optionBuilder.create('W');
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
 Option option = optionBuilder.create('X');
 assertEquals("testArgName", option.getArgName());
 }
}