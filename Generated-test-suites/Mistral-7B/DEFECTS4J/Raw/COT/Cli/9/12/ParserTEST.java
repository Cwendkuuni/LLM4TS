import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 parser = new DefaultParser();
 options = new Options();
 }

 @Test
 public void testSetOptions() throws ParseException {
 // Test setting options
 options.addOption("o", "option", true, "An option with a value");
 parser.setOptions(options);
 Assert.assertEquals(options, parser.getOptions());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOptionsNull() {
 // Test setting null options
 parser.setOptions(null);
 }

 @Test
 public void testGetOptions() {
 // Test getting options
 Assert.assertNotNull(parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 // Test getting required options
 options.addRequiredOption("r", "required", false, "A required option");
 parser.setOptions(options);
 List<Option> requiredOptions = parser.getRequiredOptions();
 Assert.assertEquals(1, requiredOptions.size());
 Assert.assertEquals("required", requiredOptions.get(0).getKey());
 }

 @Test
 public void testFlattenEmpty() throws ParseException {
 // Test flattening empty arguments
 String[] arguments = new String[0];
 String[] expected = new String[0];
 String[] actual = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlatten() throws ParseException {
 // Test flattening arguments
 options.addOption("o", "option", true, "An option with a value");
 options.addOption("a", "arg", false, "An option with no value");
 String[] arguments = {"--option", "value", "--arg", "-a", "argvalue"};
 String[] expected = {"--option", "value", "--arg", "-a", "argvalue"};
 String[] actual = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOptions() throws ParseException {
 // Test parsing with missing required options
 options.addRequiredOption("r", "required", false, "A required option");
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParse() throws ParseException {
 // Test parsing with valid arguments
 options.addOption("o", "option", true, "An option with a value");
 options.addOption("a", "arg", false, "An option with no value");
 String[] arguments = {"--option", "value", "--arg"};
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(2, cmd.getArgs().size());
 Assert.assertEquals("value", cmd.getOptionValue("option"));
 Assert.assertNull(cmd.getOptionValue("unknown"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingOption() throws ParseException {
 // Test parsing with missing option
 options.addOption("o", "option", true, "An option with a value");
 String[] arguments = {"--arg", "value"};
 try {
 parser.parse(options, arguments);
 fail("Expected MissingOptionException");
 } catch (MissingOptionException e) {
 // Expected
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 // Test parsing with unrecognized option
 options.addOption("o", "option", true, "An option with a value");
 String[] arguments = {"--unknown", "value"};
 try {
 parser.parse(options, arguments);
 fail("Expected UnrecognizedOptionException");
 } catch (UnrecognizedOptionException e) {
 // Expected
 }
 }

 @Test(expected = NoSuchOptionException.class)
 public void testParseNoSuchOption() throws ParseException {
 // Test parsing with no such option
 options.addOption("o", "option", true, "An option with a value");
 String[] arguments = {"--option", "value", "--noSuchOption", "value2"};
 try {
 parser.parse(options, arguments);
 fail("Expected NoSuchOptionException");
 } catch (NoSuchOptionException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithExtraArguments() throws ParseException {
 // Test parsing with extra arguments
 options.addOption("o", "option", true, "An option with a value");
 String[] arguments = {"--option", "value"};
 try {
 parser.parse(options, new String[]{"--option", "value", "extra"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 // Test parsing with properties
 options.addOption("o", "option", true, "An option with a value");
 Properties properties = new Properties();
 properties.setProperty("option", "value");
 CommandLine cmd = parser.parse(options, (String[])null, properties);
 Assert.assertEquals(1, cmd.getArgs().size());
 Assert.assertEquals("value", cmd.getOptionValue("option"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 // Test parsing with stopAtNonOption
 options.addOption("o", "option", true, "An option with a value");
 options.addOption("a", "arg", false, "An option with no value");
 String[] arguments = {"--option", "value", "-a", "argvalue"};
 CommandLine cmd = parser.parse(options, arguments, true);
 Assert.assertEquals(2, cmd.getOptions().size());
 Assert.assertEquals(1, cmd.getArgs().size());
 }

 @Test
 public void testProcessProperties() throws ParseException {
 // Test processing properties
 Properties properties = new Properties();
 properties.setProperty("option", "value");
 parser.processProperties(properties);
 Option option = new Option("option", "option", true, "An option with a value");
 Option expectedOption = cmd.getOption("option");
 Assert.assertEquals(option, expectedOption);
 }
}