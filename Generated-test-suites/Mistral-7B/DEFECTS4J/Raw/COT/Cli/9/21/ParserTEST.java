import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List<Option> requiredOptions = options.getRequiredOptions();
 parser.setOptions(options);
 assertEquals(requiredOptions, parser.getRequiredOptions());
 }

 @Test
 public void testFlattenAbstractMethod() {
 throw new UnsupportedOperationException("flatten is an abstract method");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseWithNullOptions() {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 try {
 parser.parse(null, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 fail("ParseException should be thrown");
 }

 @Test
 public void testParseWithEmptyArguments() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertTrue(parser.cmd.getArgs().isEmpty());
 }

 @Test
 public void testParseWithSingleArgument() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"arg"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertEquals(1, parser.cmd.getArgs().size());
 assertEquals("arg", parser.cmd.getArgs().get(0));
 }

 @Test
 public void testParseWithSingleOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option Value");
 options.addOption(option);
 String[] arguments = new String[]{"-o"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertTrue(parser.cmd.getOptions().size() > 0);
 Option parsedOption = parser.cmd.getOption("o");
 assertEquals("Option Value", parsedOption.getValue());
 }

 @Test
 public void testParseWithSingleOptionValue() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("o", "option");
 options.addOption(option);
 String[] arguments = new String[]{"-o", "value"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertTrue(parser.cmd.getOptions().size() > 0);
 Option parsedOption = parser.cmd.getOption("o");
 assertNull(parsedOption.getValue());
 }

 @Test
 public void testParseWithMultipleOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option1 = new Option("o1", "option1");
 Option option2 = new Option("o2", "option2");
 options.addOption(option1);
 options.addOption(option2);
 String[] arguments = new String[]{"-o1", "-o2"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertTrue(parser.cmd.getOptions().size() > 1);
 Option parsedOption1 = parser.cmd.getOption("o1");
 Option parsedOption2 = parser.cmd.getOption("o2");
 assertNotNull(parsedOption1);
 assertNotNull(parsedOption2);
 }

 @Test
 public void testParseWithOptionalArgument() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("o", "option");
 option.hasArg(true);
 options.addOption(option);
 String[] arguments = new String[]{"-o", "value"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertNotNull(parser.cmd);
 assertTrue(parser.cmd.getOptions().size() > 0);
 Option parsedOption = parser.cmd.getOption("o");
 assertEquals("value", parsedOption.getValue());
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArgumentForOptionalOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("o", "option");
 option.hasArg(true);
 options.addOption(option);
 String[] arguments = new String[]{"-o"};
 try {
 parser.parse(options, arguments);
 } catch (MissingArgumentException e) {
 // Expected
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 fail("MissingArgumentException should be thrown");
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArgumentForRequiredOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("o", "option");
 option.isRequired(true);
 options.addOption(option);
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 } catch (MissingOptionException e) {
 // Expected
 } catch (MissingArgumentException e) {
 fail("MissingArgumentException should not be thrown");
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 fail("MissingOptionException or MissingArgumentException should be thrown");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"-x"};
 try {
 parser.parse(options, arguments);
 } catch (UnrecognizedOptionException e) {
 // Expected
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 fail("UnrecognizedOptionException should be thrown");
 }

 @Test
 public void testParseWithProperties() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Properties properties = new Properties();
 properties.setProperty("option.key", "option.value");
 try {
 parser.parse(options, new String[0], properties);
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 assertTrue(parser.cmd.getOptions().size() > 0);
 Option option = parser.cmd.getOption("option.key");
 assertEquals("option.value", option.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullProperties() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Properties properties = null;
 try {
 parser.parse(options, new String[0], properties);
 } catch (NullPointerException e) {
 // Expected
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 }
}