import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_OPTIONS = new String[]{"-a", "--optionB=valueB", "-c=valueC", "--optionD"};
 private static final String[] ARGS_WITH_INVALID_OPTION = new String[]{"--invalidOption"};
 private static final String[] ARGS_WITH_MISSING_REQUIRED_OPTION = new String[]{"--optionA"};
 private static final String[] ARGS_WITH_MISSING_REQUIRED_OPTIONS = new String[]{"--optionA", "--optionB"};
 private static final String[] ARGS_WITH_OPTION_GROUP = new String[]{"--group1", "--optionA"};

 @Test
 public void testParseWithOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");
 options.addOption("d", true, "Option D");
 options.addOptionGroup(new OptionGroup("group1", "Group 1"));
 options.addOption(new Option("g", "group", false, "Group 1"));
 options.addOption("a", true, "Option A (in group)");
 options.addOption("e", false, "Option E (hidden)");

 CommandLine cmd = parser.parse(options, ARGS_WITH_OPTIONS, true);
 assertEquals(4, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertEquals("valueC", cmd.getOptionValue("c"));
 assertTrue(cmd.hasOption("d"));
 assertFalse(cmd.hasOption("e"));
 assertTrue(cmd.getOptionGroupValues("group1").contains("group1"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 CommandLine cmd = parser.parse(options, ARGS_WITH_INVALID_OPTION, true);
 fail("ParseException expected");
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A (required)");
 options.addOption("b", true, "Option B");

 try {
 CommandLine cmd = parser.parse(options, ARGS_WITH_MISSING_REQUIRED_OPTION, true);
 fail("MissingOptionException expected");
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("Missing required option: a"));
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A (required)");
 options.addOption("b", true, "Option B (required)");

 try {
 CommandLine cmd = parser.parse(options, ARGS_WITH_MISSING_REQUIRED_OPTIONS, true);
 fail("MissingOptionException expected");
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("Missing required options: [a, b]"));
 }
 }
}