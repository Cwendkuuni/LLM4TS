import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGS = {"--input", "input.txt", "--output", "output.txt"};

 @Test
 public void testParseWithValidOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file");
 CommandLine cmd = parser.parse(options, ARGS, false);
 Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, new String[]{"--unknown-option"}, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 CommandLine cmd = parser.parse(options, new String[0], false);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 CommandLine cmd = parser.parse(options, new String[]{"--input", "--output"}, false);
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 CommandLineParser parser = new DefaultParser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 if (stopAtNonOption) {
 return Arrays.copyOfRange(arguments, 0, Arrays.asList(arguments).indexOf("--"));
 }
 return super.flatten(opts, arguments, stopAtNonOption);
 }
 };
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 CommandLine cmd = parser.parse(options, ARGS, true);
 Assert.assertEquals(2, cmd.getArgs().size());
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addOption("output", true, "o", "Output file");
 CommandLine cmd = parser.parse(options, new String[]{"--input", "input.txt"}, null);
 Properties properties = new Properties();
 properties.setProperty("output", "output.txt");
 cmd = parser.parse(options, null, properties, true);
 Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
 }
}