import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();
 options.addOption("a", "alpha", false, "Option A");
 options.addOption("b", "beta", true, "Option B requires an argument");
 options.addOptionGroup(new OptionGroup("g", "group")
 .addOption(new Option("g1", "group1", true, "Group option 1 requires an argument"))
 .addOption(new Option("g2", "group2", false, "Group option 2 doesn't require an argument")));
 return options;
 }

 @Test
 public void testNoArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createTestOptions();
 String[] args = {};
 CommandLine cmd = parser.parse(options, args);

 assertEquals(0, cmd.getArgs().length);
 assertEquals(false, cmd.hasOption("a"));
 assertEquals(false, cmd.hasOption("b"));
 assertEquals(Collections.emptyList(), options.getOptionGroups());
 }

 @Test
 public void testSingleArgument() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createTestOptions();
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals(0, cmd.getArgs().length);
 assertEquals(true, cmd.hasOption("a"));
 assertEquals(false, cmd.hasOption("b"));
 assertEquals(Collections.emptyList(), options.getOptionGroups());
 }

 @Test
 public void testTwoArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createTestOptions();
 String[] args = {"-a", "valueA", "-b", "valueB"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals(2, cmd.getArgs().length);
 assertEquals(true, cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertEquals(true, cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertEquals(Collections.singletonList(options.getOptionGroup("g")), options.getOptionGroups());
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createTestOptions();
 String[] args = {"-z"};
 CommandLine cmd = parser.parse(options, args);
 }

 @Test(expected = ParseException.class)
 public void testMissingArgument() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createTestOptions();
 String[] args = {"-b"};
 CommandLine cmd = parser.parse(options, args);
 }

 @Test
 public void testGroupRequired() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options")
 .required(true)
 .addOption(new Option("g1", "group1", true, "Group option 1 requires an argument"));
 options.addOptionGroup(group);
 String[] args = {};
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, args);
 fail("No exception thrown for missing required option");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testGroupOptional() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options")
 .required(false)
 .addOption(new Option("g1", "group1", true, "Group option 1 requires an argument"));
 options.addOptionGroup(group);
 String[] args = {};
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("g"));
 assertFalse(cmd.hasOption("g1"));
 }

 @Test
 public void testHelp() {
 HelpFormatter formatter = new HelpFormatter();
 Options options = createTestOptions();
 String help = formatter.printHelp("CommandLineTest", options);
 assertTrue(help.contains("Option A"));
 assertTrue(help.contains("Option B requires an argument"));
 assertTrue(help.contains("group"));
 assertTrue(help.contains("group option 1 requires an argument"));
 assertTrue(help.contains("group option 2 doesn't require an argument"));
 }
}