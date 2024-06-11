import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 parser = new DefaultParser();
 parser.setOptions(options);
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, arguments);
 assertNotNull(cmd);
 assertEquals("-i", cmd.getOptionValue("input"));
 assertEquals("-o", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithMissingRequiredOption() throws ParseException {
 String[] arguments = new String[]{"-i", "input.txt"};
 try {
 parser.parse(options, arguments);
 fail("MissingRequiredOptionException should have been thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testParseWithInvalidOption() throws ParseException {
 String[] arguments = new String[]{"--invalidOption"};
 try {
 parser.parse(options, arguments);
 fail("UnrecognizedOptionException should have been thrown");
 } catch (UnrecognizedOptionException e) {
 // expected
 }
 }

 @Test
 public void testParseWithUnspecifiedOptionValue() throws ParseException {
 String[] arguments = new String[]{"-i", "", "-o"};
 CommandLine cmd = parser.parse(options, arguments);
 assertNull(cmd.getOptionValue("input"));
 assertEquals("-o", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithGroupedOptions() throws ParseException {
 OptionGroup group = new OptionGroup("g", "Group");
 group.setRequired(true);
 group.addOption("a", "OptionA");
 group.addOption("b", "OptionB");
 options.addOptionGroup(group);
 String[] arguments = new String[]{"-g", "OptionA"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 }

 @Test
 public void testParseWithMultipleValuesForOption() throws ParseException {
 options.addOption("f", "files", true, "Input files");
 String[] arguments = new String[]{"-f", "file1.txt", "-f", "file2.txt"};
 CommandLine cmd = parser.parse(options, arguments);
 assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, cmd.getOptionValues("files"));
 }

 @Test
 public void testParseWithMixedLongAndShortOptions() throws ParseException {
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("h", "help", true, "Print help on a specific topic.");
 String[] arguments = new String[]{"-h", "--help"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(2, cmd.getOptions().size());
 assertTrue(cmd.hasOption("help"));
 assertTrue(cmd.hasOption("help"));
 }

}