import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

 @Test
 public void testDefaultOption() throws Exception {
 Option option = OptionBuilder.create();
 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals(Option.builder(), option.getOptparser().getOptionBuilder());
 }

 @Test
 public void testOptionWithLongOpt() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .create();

 assertEquals("option", option.getLongOpt());
 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals("option", option.getOptparser().getOptionByName("option").getOpt());
 }

 @Test
 public void testOptionWithDescription() throws Exception {
 Option option = OptionBuilder.withDescription("Description")
 .create();

 assertNull(OptionBuilder.longopt);
 assertEquals("Description", option.getDescription());
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals("Description", option.getDescription());
 }

 @Test
 public void testOptionWithArgName() throws Exception {
 Option option = OptionBuilder.withArgName("argName")
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertSame("argName", option.getArgName());
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionIsRequired() throws Exception {
 Option option = OptionBuilder.isRequired()
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertTrue(option.isRequired());
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionHasArg() throws Exception {
 Option option = OptionBuilder.hasArg()
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(1, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertNull(option.getArgName());
 assertTrue(option.hasArg());
 }

 @Test
 public void testOptionHasOptionalArg() throws Exception {
 Option option = OptionBuilder.hasOptionalArg()
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(1, OptionBuilder.numberOfArgs);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertSame(Option.UNINITIALIZED, option.getNumberOfArgs());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionWithValueSeparator() throws Exception {
 Option option = OptionBuilder.withValueSeparator(':')
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame(':', OptionBuilder.valuesep);

 assertNull(option.getArgName());
 assertNull(option.getValueSeparator());
 assertTrue(option.hasValueSeparator());
 }

 @Test
 public void testOptionWithArg() throws Exception {
 Option option = OptionBuilder.hasArg("arg")
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertSame("arg", option.getArgName());
 assertFalse(OptionBuilder.required);
 assertSame(1, OptionBuilder.numberOfArgs);
 assertNull(OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertSame("arg", option.getArgName());
 }

 @Test
 public void testOptionWithArgs() throws Exception {
 Option option = OptionBuilder.hasArgs()
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertSame(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
 }

 @Test
 public void testOptionWithOptionalArgs() throws Exception {
 Option option = OptionBuilder.hasOptionalArgs()
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
 assertTrue(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertSame(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionWithOptionalArgsNumber() throws Exception {
 Option option = OptionBuilder.hasOptionalArgs(2)
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(2, OptionBuilder.numberOfArgs);
 assertTrue(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals(2, option.getNumberOfArgs());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionWithType() throws Exception {
 Option option = OptionBuilder.withType(String.class)
 .create();

 assertNull(OptionBuilder.longopt);
 assertNull(OptionBuilder.description);
 assertNull(OptionBuilder.argName);
 assertFalse(OptionBuilder.required);
 assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
 assertSame(String.class, OptionBuilder.type);
 assertFalse(OptionBuilder.optionalArg);
 assertSame((char) 0, OptionBuilder.valuesep);

 assertEquals(String.class, option.getType());
 }

 @Test
 public void testOptionWithLongOptAndDescription() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 }

 @Test
 public void testOptionWithLongOptAndArgName() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withArgName("argName")
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgName() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndHasArg() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .hasArg()
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertTrue(option.hasArg());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndIsRequired() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .isRequired()
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndOptionalArg() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .hasOptionalArg()
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndOptionalArgs() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .hasOptionalArgs()
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertTrue(option.hasOptionalArgs());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndOptionalArgsNumber() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .hasOptionalArgs(2)
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertEquals(2, option.getNumberOfArgs());
 assertTrue(option.hasOptionalArgs());
 }

 @Test
 public void testOptionWithLongOptAndDescriptionAndArgNameAndType() throws Exception {
 Option option = OptionBuilder.withLongOpt("longOpt")
 .withDescription("Description")
 .withArgName("argName")
 .withType(Integer.class)
 .create();

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("Description", option.getDescription());
 assertEquals("argName", option.getArgName());
 assertEquals(Integer.class, option.getType());
 }

}