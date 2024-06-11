import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 Options options = new Options();
 parser = new DefaultParser();
 parser.setOptions(options);
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments);
 assertNull(cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithOptions() throws ParseException {
 String[] arguments = {"-a", "--optionB=valueB"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueB", cmd.getOptionValue("optionB"));
 assertFalse(cmd.hasOption("notAnOption"));
 assertNull(cmd.getArgs());
 }

 @Test
 public void testParseWithArguments() throws ParseException {
 String[] arguments = {"arg1", "arg2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 String[] arguments = {"--optionA"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Missing required option exception expected");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 String[] arguments = {"--unrecognizedOption"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Unrecognized option exception expected");
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 Option option = new Option("o", "option", true, "Default value");
 option.setRequired(true);
 options.addOption(option);
 String[] arguments = {"-o"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Missing argument exception expected");
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("optionA", "valueA");
 properties.setProperty("optionB", "valueB");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 assertTrue(cmd.hasOption("optionA"));
 assertEquals("valueA", cmd.getOptionValue("optionA"));
 assertTrue(cmd.hasOption("optionB"));
 assertEquals("valueB", cmd.getOptionValue("optionB"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 String[] arguments = {"--", "-n", "arg1", "-o", "arg2"};
 CommandLine cmd = parser.parse(options, arguments, true);
 assertEquals(new String[]{"-n", "arg1", "-o", "arg2"}, cmd.getArgs());
 }

}