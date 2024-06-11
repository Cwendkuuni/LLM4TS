import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "alert", true, "Display an alert message")
 .addOption("b", "batch", false, "Batch mode")
 .addOption("c", "count", 1, "Number of iterations")
 .addOptionGroup(new OptionGroup("g", "group").addOption("g1", true, "Group option 1"))
 .addOptionGroup(new OptionGroup("g2", "group2").addOption("g21", true, "Group option 2.1")
 .addOption("h", null, false, "Help"))
 .addOption("i", "input", true, "Input file path")
 .addOption("j", "json", false, "Use JSON format")
 .addOption("k", "key", true, "Key for the JSON object")
 .addOption("l", "limit", 10, "Maximum number of items to get")
 .addOption("m", "mode", true, "Operation mode")
 .addOption("o", "output", true, "Output file path")
 .addOption("p", "profile", false, "Profile mode")
 .addOption("q", "quiet", false, "Quiet mode")
 .addOption("r", "regex", true, "Regular expression for filtering")
 .addOption("s", "sort", false, "Sort the output")
 .addOption("t", "test", false, "Test mode")
 .addOption("u", "update", false, "Update mode")
 .addOption("v", "verbose", false, "Verbose mode")
 .addOption("w", "width", 80, "Console width")
 .addOption("x", "xml", false, "Use XML format")
 .addOption("y", "yes", false, "Assume yes for all prompts");
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();

 assertEquals("alert", options.getOption("a").getLongOpt());
 assertEquals(true, options.getOption("a").isRequired());
 assertEquals("Display an alert message", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup optionGroup = options.getOptionGroups().get(0);

 assertEquals("group", optionGroup.getName());
 assertEquals(true, optionGroup.isRequired());
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("--alert"));
 assertTrue(options.hasOption("--batch"));
 //...
 assertTrue(options.hasLongOption("--mode"));
 assertTrue(options.hasShortOption("m"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option option = options.getOption("a");

 assertEquals("Display an alert message", option.getDescription());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();

 List<String> optionsWithG = options.getMatchingOptions("g");
 assertEquals(2, optionsWithG.size());
 assertEquals("--group", optionsWithG.get(0));
 assertEquals("--group2", optionsWithG.get(1));
 }

 @Test
 public void testParsingOptions() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--alert", "true", "--batch", "--input", "input.txt", "--profile"};

 try {
 CommandLine cmd = parser.parse(options, args);

 assertFalse(cmd.hasOption("--c"));
 assertFalse(cmd.hasOption("--count"));
 assertEquals("input.txt", cmd.getOptionValue("--input"));
 assertEquals("true", cmd.getOptionValue("--alert"));
 assertTrue(cmd.hasOption("--batch"));
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--invalid"};

 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--count", "--output"};

 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidArgumentType() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--count", "asdf"};

 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 Options options = createOptions();

 String optionsString = options.toString();
 assertTrue(optionsString.contains("[ Options:"));
 assertTrue(optionsString.contains("[ short"));
 assertTrue(optionsString.contains("[ long"));
 }

}