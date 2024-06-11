import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "input file path");
 parser = new DefaultParser();
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = {"-i", "test.txt"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals("test.txt", cmd.getOptionValue("input"));
 }

 @Test
 public void testParseWithMultipleOptions() throws ParseException {
 String[] arguments = {"-i", "test1.txt", "--verbose", "--help"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("input"));
 assertTrue(cmd.hasOption("verbose"));
 assertFalse(cmd.hasOption("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 String[] arguments = {"-i"};
 parser.parse(options, arguments);
 fail("MissingArgumentException should be thrown");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 String[] arguments = {"--unknown"};
 parser.parse(options, arguments);
 fail("UnrecognizedOptionException should be thrown");
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 String[] arguments = {"--help"};
 try {
 parser.parse(options, arguments);
 fail("MissingOptionException should be thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("input", "test.txt");
 CommandLine cmd = parser.parse(options, null, properties);
 assertEquals("test.txt", cmd.getOptionValue("input"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 options.getOptions().add(new Option("n", "no-options", false, "no options"));
 String[] arguments = {"--no-options", "--help"};
 CommandLine cmd = parser.parse(options, arguments, null, true);
 assertTrue(cmd.hasOption("no-options"));
 assertTrue(cmd.getArgs().length > 0);
 }

 @Test
 public void testParseWithLongOptions() throws ParseException {
 options.addOption("long-input-option", true, "Specifies a long input option");
 String[] arguments = {"--long-input-option=test.txt"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals("test.txt", cmd.getOptionValue("long-input-option"));
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 OptionGroup group = new OptionGroup("group", "Group of options");
 group.setRequired(false);
 options.addOptionGroup(group);
 group.addOption("g", "group-option", false, "An option in the group");
 String[] arguments = {"--group-option"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("group-option"));
 }
}