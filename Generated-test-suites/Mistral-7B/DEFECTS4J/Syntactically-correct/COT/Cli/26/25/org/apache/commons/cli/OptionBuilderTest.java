import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
 Option option = OptionBuilder.create();
 Assert.assertEquals("optionLong", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = OptionBuilder.create();
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasArg_boolean() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
 Option option = OptionBuilder.create();
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 }

 @Test
 public void testHasArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = OptionBuilder.create();
 Assert.assertEquals("argName", option.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = OptionBuilder.create();
 Assert.assertArrayEquals(new String[]{}, option.getArgs());
 }

 @Test
 public void testHasArgs_int() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option option = OptionBuilder.create();
 Assert.assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test
 public void testIsRequired_boolean() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option option = OptionBuilder.create();
 Assert.assertEquals(String.class, option.getType());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Description");
 Option option = OptionBuilder.create();
 Assert.assertEquals("Test Description", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_NoLongOptSet() {
 OptionBuilder.create();
 }

 @Test
 public void testCreate_OptionalArgAndArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().hasArgs(2);
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test
 public void testCreate_OptionalArgAndNoArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertArrayEquals(new String[]{}, option.getArgs());
 }

 @Test
 public void testCreate_RequiredArgAndArgs() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired().hasArgs(2);
 Option option = OptionBuilder.create();
 Assert.assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_RequiredArgAndNoArgs() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create();
 Assert.assertArrayEquals(new String[]{}, option.getArgs());
 }
}