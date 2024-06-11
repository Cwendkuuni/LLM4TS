import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.withLongOpt("longOpt");

 assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt("longOpt"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.create();
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.hasArg();

 assertEquals(expectedOptionBuilder, optionBuilder.hasArg());
 }

 @Test
 public void testHasArgWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.hasArg();
 expectedOptionBuilder.withArgName("argName");

 assertEquals(expectedOptionBuilder, optionBuilder.hasArg().withArgName("argName"));
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.isRequired();

 assertEquals(expectedOptionBuilder, optionBuilder.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.withValueSeparator(':');

 assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator(':'));
 }

 @Test
 public void testWithValueSeparatorEmpty() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder expectedOptionBuilder = new OptionBuilder();
 expectedOptionBuilder.withValueSeparator();

 assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator());
 }

 @Test
 public void testCreateOptionWithMandatoryArguments() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("longOpt")
 .isRequired()
 .hasArg()
 .create('o');

 assertEquals("longOpt", option.getLongOpt());
 assertFalse(option.isRequired());
 assertTrue(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testCreateOptionWithOptionalArguments() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("longOpt")
 .hasOptionalArgs(2)
 .create('o');

 assertEquals("longOpt", option.getLongOpt());
 assertFalse(option.isRequired());
 assertTrue(option.hasOptionalArgs());
 assertNull(option.getValue());
 }

 @Test
 public void testCreateOptionWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("longOpt")
 .hasArg()
 .withValueSeparator(':')
 .create('o', "value");

 assertEquals("longOpt", option.getLongOpt());
 assertFalse(option.isRequired());
 assertTrue(option.hasArg());
 assertEquals("value", option.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidNumberOfArguments() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("longOpt")
 .hasArgs(0)
 .create('o');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidOptionalArguments() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("longOpt")
 .hasOptionalArgs(-1)
 .create('o');
 }
}