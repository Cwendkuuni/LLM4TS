import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_REQUIRED_OPTION = new String[]{"--input", "inputFile.txt", "--output", "outputFile.txt"};
 private static final String[] ARGS_WITH_INVALID_OPTION = new String[]{"--invalidOption", "--input", "inputFile.txt", "--output", "outputFile.txt"};
 private static final String[] ARGS_WITH_MISSING_REQUIRED_OPTION = new String[]{"--output", "outputFile.txt"};
 private static final String[] ARGS_WITH_MISSING_OPTION_AND_ARG = new String[]{"--input", "inputFile.txt"};
 private static final String[] ARGS_WITH_MISSING_OPTION = new String[]{"--output"};
 private static final String[] ARGS_WITH_NO_OPTION = new String[]{"file1.txt", "file2.txt"};

 @Test
 public void testParsingWithValidOptionsAndArgs() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file")
 .requiredOpt(true)
 .argName("file");
 options.addRequiredOption("output", true, "o")
 .longOpt("output")
 .desc("Output file")
 .requiredOpt(true)
 .argName("file");

 parser.parse(options, ARGS_WITH_REQUIRED_OPTION);
 CommandLine commandLine = parser.getParsedCommandLine();
 assertTrue(commandLine.hasOption("input"));
 assertTrue(commandLine.hasOption("output"));
 assertEquals("inputFile.txt", commandLine.getOptionValue("input"));
 assertEquals("outputFile.txt", commandLine.getOptionValue("output"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParsingWithMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file")
 .requiredOpt(true)
 .argName("file");
 options.addRequiredOption("output", true, "o")
 .longOpt("output")
 .desc("Output file")
 .requiredOpt(true)
 .argName("file");

 parser.parse(options, ARGS_WITH_MISSING_REQUIRED_OPTION);
 }

 @Test(expected = MissingArgumentException.class)
 public void testParsingWithMissingOptionAndArg() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file")
 .requiredOpt(true)
 .argName("file");

 parser.parse(options, ARGS_WITH_MISSING_OPTION_AND_ARG);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParsingWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file")
 .requiredOpt(true)
 .argName("file");
 options.addRequiredOption("output", true, "o")
 .longOpt("output")
 .desc("Output file")
 .requiredOpt(true)
 .argName("file");

 parser.parse(options, ARGS_WITH_INVALID_OPTION);
 }

 @Test
 public void testParsingWithNoOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file")
 .requiredOpt(true)
 .argName("file");
 options.addRequiredOption("output", true, "o")
 .longOpt("output")
 .desc("Output file")
 .requiredOpt(true)
 .argName("file");

 parser.parse(options, ARGS_WITH_NO_OPTION);
 assertNull(parser.getParsedCommandLine());
 }

}