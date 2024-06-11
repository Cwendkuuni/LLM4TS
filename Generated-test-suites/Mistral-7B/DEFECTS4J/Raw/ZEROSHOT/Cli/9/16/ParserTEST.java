import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("o", "output", true, "The output file.");
 options.addOption("i", "input", true, "The input file.");
 options.addOption("v", "verbose", false, "Verbose output.");
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = {"-o", "output.txt", "-i", "input.txt"};
 parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments);
 Assert.assertEquals(2, commandLine.getArgs().length);
 Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
 Assert.assertEquals("input.txt", commandLine.getOptionValue("input"));
 Assert.assertNull(commandLine.getOptionValue("verbose"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 String[] arguments = {"-i", "input.txt"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 String[] arguments = {"-x", "invalidOption"};
 parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test
 public void testParseWithOptionalValue() throws ParseException {
 String[] arguments = {"-v"};
 parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments);
 Assert.assertFalse(commandLine.hasOption("verbose"));

 arguments = new String[]{"-v"};
 commandLine = parser.parse(options, arguments);
 Assert.assertTrue(commandLine.hasOption("verbose"));
 }

 @Test
 public void testParseWithMultipleValues() throws ParseException {
 options.addOption("files", "f", true, "The input files.");
 String[] arguments = {"-f", "file1.txt", "-f", "file2.txt"};
 parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments);
 List<String> files = commandLine.getOptionValues("files");
 Assert.assertEquals(2, files.size());
 Assert.assertEquals("file1.txt", files.get(0));
 Assert.assertEquals("file2.txt", files.get(1));
 }

 @Test
 public void testParseWithGroup() throws ParseException {
 OptionGroup group = new OptionGroup("inputGroup", "Input options");
 group.addOption("i", "inputFile", true, "The input file.");
 group.addOption("f", "format", true, "The input format.");
 options.addOptionGroup(group);
 options.addRequiredOption("o", "output", true, "The output file.");

 String[] arguments = {"-i", "input.txt", "-f", "csv", "-o", "output.txt"};
 parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments);
 Assert.assertEquals(1, commandLine.getOptions().size());
 Option inputOption = commandLine.getOption("inputGroup");
 Assert.assertEquals("input.txt", inputOption.getValues()[0]);
 Assert.assertEquals("csv", inputOption.getValues()[1]);
 }
}