import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGS = {"--input", "inputFile.txt", "-o", "outputFile.txt", "--help"};

 @Test
 public void testParseWithValidArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 CommandLine cmd = parser.parse(options, ARGS, null, false);
 Assert.assertEquals(2, cmd.getArgs().size());
 Assert.assertEquals("inputFile.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("outputFile.txt", cmd.getOptionValue("o"));
 Assert.assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 parser.parse(options, new String[0], null, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 parser.parse(options, ARGS, null, true);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 parser.parse(options, ARGS, null, false);
 parser.parse(options, new String[]{"--unknown"}, null, false);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 options.addProperties("properties.properties");
 parser.parse(options, ARGS, null, false);
 CommandLine cmd = parser.parse(options, ARGS, null, false);
 Assert.assertEquals(2, cmd.getArgs().size());
 Assert.assertEquals("inputFile.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("outputFile.txt", cmd.getOptionValue("o"));
 Assert.assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 parser.parse(options, ARGS, null, false);
 parser.parse(options, new String[]{"--input", "--o"}, null, false);
 }

 @Test
 public void testParseWithHelpOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addRequiredOption("o", true, "o", "output file");
 options.addOption("help", false, null, "Prints this help message.");
 parser.parse(options, ARGS, false);
 parser.parse(options, ARGS, null, false);
 parser.parse(options, new String[]{"--help"}, null, false);
 }

}