import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
 Option option = OptionBuilder.create('o');
 Assert.assertEquals("optionLong", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = OptionBuilder.create();
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = OptionBuilder.create('c');
 Assert.assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = OptionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create();
 Assert.assertTrue(option.isOptional());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option option = OptionBuilder.create();
 Assert.assertNotNull(option.getType());
 Assert.assertEquals(String.class, option.getType().getClass());
 }

 @Test
 public void testCreateRequiredOptionWithArg() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired().hasArg();
 Option option = OptionBuilder.create('r');
 Assert.assertTrue(option.isRequired());
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testCreateOptionalOptionWithArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().hasArg();
 Option option = OptionBuilder.create('o');
 Assert.assertFalse(option.isRequired());
 Assert.assertEquals(1, option.getArgs().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidCharArg() {
 OptionBuilder optionBuilder = OptionBuilder.create('%');
 }

 @Test
 public void testCreateWithUninitializedNumberOfArgs() {
 OptionBuilder optionBuilder = OptionBuilder.create();
 Option option = OptionBuilder.create('u');
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 }

 @Test
 public void testCreateWithOptionalArgAndNoArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('o');
 Assert.assertNull(option.getValue());
 }

 @Test
 public void testCreateWithOptionalArgAndSingleArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('o');
 String[] args = {"test"};
 option.parseArgument(args);
 Assert.assertEquals("test", option.getValue());
 }

 @Test
 public void testCreateWithOptionalArgAndMultipleArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = OptionBuilder.create('o');
 String[] args = {"test1", "test2"};
 option.parseArgument(args);
 String[] values = option.getValue().split(OptionBuilder.valuesep);
 Assert.assertEquals(2, values.length);
 Assert.assertEquals("test1", values[0]);
 Assert.assertEquals("test2", values[1]);
 }

 @Test
 public void testCreateWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = OptionBuilder.create('d');
 String[] args = {"test1:test2"};
 option.parseArgument(args);
 String value = option.getValue();
 Assert.assertEquals("test1:test2", value);
 }
}