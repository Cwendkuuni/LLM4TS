import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testLongOpt() {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .create();
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 Option option = OptionBuilder.hasArg()
 .create();
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArg() {
 Option option = OptionBuilder.hasOptionalArg()
 .create();
 assertEquals(1, option.getArgs().length);
 assertTrue(option.isOptionalArg());
 }

 @Test
 public void testHasArgs() {
 Option option = OptionBuilder.hasArgs()
 .create();
 assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test
 public void testHasArgsWithSpecifiedSize() {
 Option option = OptionBuilder.hasArgs(2)
 .create();
 assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 Option option = OptionBuilder.create();
 fail("must specify long opt");
 }

 @Test
 public void testIsRequired() {
 Option option = OptionBuilder.isRequired()
 .create();
 assertFalse(option.isRequired());
 }

 @Test
 public void testIsRequiredWithSpecifiedValue() {
 Option option = OptionBuilder.isRequired(true)
 .create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 Option option = OptionBuilder.withValueSeparator(':')
 .create();
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testWithDescription() {
 Option option = OptionBuilder.withDescription("Test Option")
 .create();
 assertEquals("Test Option", option.getDescription());
 }

 @Test
 public void testWithType() {
 Option option = OptionBuilder.withType(Integer.TYPE)
 .create();
 assertEquals(Integer.TYPE, option.getType());
 }
}