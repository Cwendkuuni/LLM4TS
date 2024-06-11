import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("option"));
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option description");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("option"));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group description");
 Option option = new Option("g", "group-option", false, "Group option description");
 group.addOption(option);
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group"));
 assertTrue(options.getOptionGroups().contains(group));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("option"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group description");
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);
 Option result = options.getOption("o");
 assertEquals(option, result);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("long-option-1", "long-option-1", false, "Option 1 description");
 Option option2 = new Option("long-option-2", "long-option-2", false, "Option 2 description");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("long-opt");
 assertEquals(2, matchingOptions.size());
 assertEquals(option1.getLongOpt(), matchingOptions.get(0));
 assertEquals(option2.getLongOpt(), matchingOptions.get(1));
 }

 @Test
 public void testParseOptions() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "value"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("o"));
 assertEquals("value", cmd.getOptionValue("o"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("parse failed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsMissingRequiredOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "option", true, "Option description");
 options.addOption(option);
 String[] args = {"-a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("parse succeeded, but should have failed due to missing required option");
 } catch (ParseException e) {
 assertEquals("Missing required option '-o'", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsInvalidOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalid"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("parse succeeded, but should have failed due to invalid option");
 } catch (ParseException e) {
 assertEquals("Unrecognized option: --invalid", e.getMessage());
 }
 }
}