import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private String[] args;

 @Before
 public void setup() {
 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 parser = new DefaultParser();
 parser.setOptions(options);
 args = new String[0];
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 args = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("input"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertTrue(cmd.hasOption("output"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 args = new String[]{"-o", "/path/to/output.txt"};
 parser.parse(options, args);
 fail("MissingRequiredOptionException expected but not thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 args = new String[]{"-x", "/path/to/output.txt"};
 parser.parse(options, args);
 fail("UnrecognizedOptionException expected but not thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArgumentValue() throws ParseException {
 options.addOption("d", "debug", false);
 args = new String[]{"-d", "false", "-i", "/path/to/input.txt", "-o", "output"};
 parser.parse(options, args);
 fail("MissingArgumentException expected but not thrown");
 }

 @Test
 public void testParseWithMixedLongAndShortOptions() throws ParseException {
 options.addOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 options.addOption("h", "help", false, "Print usage information");
 args = new String[]{"-h", "--help", "-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("-h"));
 assertTrue(cmd.hasOption("help"));
 assertTrue(cmd.hasOption("i"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertTrue(cmd.hasOption("o"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.put("input", "/path/to/input.txt");
 properties.put("output", "/path/to/output.txt");
 CommandLine cmd = parser.parse(options, (String[]) null, properties);
 assertTrue(cmd.hasOption("input"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertTrue(cmd.hasOption("output"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 parser = new PosixParser();
 parser.setOptions(options);
 args = new String[]{"--", "-i", "/path/to/input.txt", "-o", "/path/to/output.txt", "--ignoreThis"};
 CommandLine cmd = parser.parse(options, args, true);
 assertTrue(cmd.hasOption("input"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertTrue(cmd.hasOption("output"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 assertFalse(cmd.hasOption("ignoreThis"));
 }

}