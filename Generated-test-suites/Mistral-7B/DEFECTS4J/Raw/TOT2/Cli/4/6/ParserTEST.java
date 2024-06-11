import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

 private CommandLineParser parser;
 private Options options;

 @Before
 public void setUp() {
 parser = new DefaultParser();

 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addRequiredOption("o", "output", true, "Output file path");
 options.addOption("h", "help", false, "Print help message");
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = parser.parse(options, arguments, false);

 Assert.assertNotNull(cmd.getOptionValue("input"));
 Assert.assertNotNull(cmd.getOptionValue("output"));
 Assert.assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 String[] arguments = new String[]{"-i", "/path/to/input.txt"};
 CommandLine cmd = parser.parse(options, arguments, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 String[] arguments = new String[]{"-o", "/path/to/output.txt"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() throws ParseException {
 String[] arguments = new String[]{"--invalidOption"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 String[] arguments = new String[]{"--help"};
 parser.parse(options, arguments, false);
 }

 @Test(expected = NoSuchOptionException.class)
 public void testParseOptionWithNoValue() throws ParseException {
 String[] arguments = new String[]{"-h"};
 CommandLine cmd = parser.parse(options, arguments, false);
 Assert.assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = ParseException.class)
 public void testParseMultipleValuesForOption() throws ParseException {
 options.getOption("i").setArgName("inputFile");
 options.getOption("o").setArgName("outputFile");
 options.getOption("i").setValueSeparator(';');

 String[] arguments = new String[]{"-i", "/path/to/input1.txt;input2.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = parser.parse(options, arguments, false);

 Assert.assertEquals(2, cmd.getOptionValues("input").length);
 Assert.assertEquals("/path/to/input1.txt", cmd.getOptionValue("input")[0]);
 Assert.assertEquals("/path/to/input2.txt", cmd.getOptionValue("input")[1]);
 }
}