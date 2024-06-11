import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--input", "inputFile.txt", "--output", "outputFile.txt", "-a", "1", "-b", "2", "--help"};

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("input", true, "i", "Input file");
 options.addOption("output", true, "o", "Output file");
 options.addOption("a", true, "a", "Option A");
 options.addOption("b", true, "b", "Option B");
 options.addOption("help", false, "h", "Print this help message.");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGUMENTS);

 Assert.assertEquals("inputFile.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("outputFile.txt", cmd.getOptionValue("output"));
 Assert.assertEquals("1", cmd.getOptionValue("a"));
 Assert.assertEquals("2", cmd.getOptionValue("b"));
 Assert.assertNull(cmd.getOptionValue("unknown"));
 Assert.assertFalse(cmd.hasOption("unknown"));
 Assert.assertTrue(cmd.hasOption("help"));
 Assert.assertFalse(cmd.hasOption("help"));
 Assert.assertEquals(10, cmd.getArgs().length);
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("input", true, "i", "Input file");
 options.addOption("output", true, "o", "Output file");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("input", true, "i", "Input file");
 options.addOption("output", true, "o", "Output file");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"--unknown"});
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("input", true, "i", "Input file");
 options.addOption("output", true, "o", "Output file");
 options.addOption("a", true, "a", "Option A");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"--input", "--output"});
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("input", true, "i", "Input file", true);
 options.addOption("output", true, "o", "Output file", true);

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 group.setRequired(true);
 options.addOptionGroup(group);
 group.addOption(new Option("a", "a", true, "Option A"));
 group.addOption(new Option("b", "b", true, "Option B"));

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 }
}