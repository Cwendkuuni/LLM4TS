import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_OPTIONS = {"-o", "output.txt", "--input", "input.csv"};
 private static final String[] ARGS_WITH_LONG_OPTIONS = {"--output", "output.txt", "--input", "input.csv"};
 private static final String[] ARGS_MISSING_OPTION = {"--missing"};
 private static final String[] ARGS_MISSING_VALUE = {"-o", "--output"};
 private static final String[] ARGS_INVALID_VALUE = {"-o", "--output=invalid"};
 private static final String[] ARGS_MIXED_OPTIONS_AND_ARGUMENTS = {"-o", "output.txt", "-i", "input.csv", "--foo", "bar"};
 private static final String[] ARGS_INVALID_LONG_OPTION = {"--invalidOption"};
 private static final String[] ARGS_MIXED_LONG_AND_SHORT_OPTIONS = {"--output", "-o", "output.txt"};

 @Test
 public void testParseWithOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 options.addOption("i", "input", true, "input file");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_OPTIONS, false);
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertEquals("input.csv", cmd.getOptionValue("input"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 parser.parse(options, ARGS_MISSING_OPTION, false);
 fail("MissingOptionException expected");
 }

 @Test(expected = ParseException.class)
 public void testParseMissingValue() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 parser.parse(options, ARGS_MISSING_VALUE, false);
 fail("MissingArgumentException expected");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidValue() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 parser.parse(options, ARGS_INVALID_VALUE, false);
 fail("InvalidCombinationException expected");
 }

 @Test
 public void testParseMixedOptionsAndArguments() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 options.addOption("i", "input", true, "input file");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_MIXED_OPTIONS_AND_ARGUMENTS, false);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertEquals("input.csv", cmd.getOptionValue("input"));
 assertEquals("bar", cmd.getArgs().get(0));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidLongOption() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.parse(options, ARGS_INVALID_LONG_OPTION, false);
 fail("UnrecognizedOptionException expected");
 }

 @Test
 public void testParseMixedLongAndShortOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_MIXED_LONG_AND_SHORT_OPTIONS, false);
 assertEquals("output.txt", cmd.getOptionValue("output"));
 }

}