import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 parser = new DefaultParser();
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] args = new String[]{"-i", "/path/to/input/file.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("/path/to/input/file.txt", cmd.getOptionValue("input"));
 }

 @Test
 public void testParseWithOptionalArguments() throws ParseException {
 String[] args = new String[]{"-i", "/path/to/input/file.txt", "--help"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("/path/to/input/file.txt", cmd.getOptionValue("input"));
 assertTrue(cmd.hasOption("help"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredArgument() throws ParseException {
 String[] args = new String[]{"-i"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("The following required options do not have values:", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 String[] args = new String[]{"--invalid"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("Unrecognized option: invalid", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithTwoDashOption() throws ParseException {
 String[] args = new String[]{"--", "--help"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("An unrecognized option '--' was encountered", e.getMessage());
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOptions() throws ParseException {
 String[] args = new String[]{"--input"};
 try {
 parser.parse(options, args);
 fail();
 } catch (MissingOptionException e) {
 assertEquals(Arrays.asList("input"), e.getMissingOptions());
 }
 }

 @Test
 public void testParseWithHelpOption() throws ParseException {
 String[] args = new String[]{"--help"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("help"));
 }

 @Test
 public void testParseWithLongOptions() throws ParseException {
 options.addOption("longInput", "long_input", true, "Long Input file path");
 String[] args = new String[]{"--long-input", "/path/to/long/input/file.txt"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("/path/to/long/input/file.txt", cmd.getOptionValue("longInput"));
 }

 @Test
 public void testParseWithGroupOptions() throws ParseException {
 OptionGroup group = new OptionGroup("mygroup");
 group.addOption(new Option("g", "group", false, "Group option"));
 group.addOption(new Option("h", "help", true, "Help option"));
 options.addOptionGroup(group);
 String[] args = new String[]{"-g", "--help"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("group"));
 assertTrue(cmd.hasOption("help"));
 }
}