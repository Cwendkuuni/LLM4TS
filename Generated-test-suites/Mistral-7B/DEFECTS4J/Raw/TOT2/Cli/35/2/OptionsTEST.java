import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", true, "Optional short option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", true, "Optional short option with argument");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertTrue(option.hasArg());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("a", "myOption", true, "Optional long option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("myOption", option.getLongOpt());
 }

 @Test
 public void testAddOptionWithLongNameAndShortName() {
 Options options = new Options();
 Option option = new Option("a", "myOption", true, "Optional long option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("myOption", option.getLongOpt());
 assertEquals("a", option.getKey());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("myGroup", "My group");
 Option option = new Option("a", true, "Option in group", group);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size());
 assertEquals(1, group.getOptions().size());
 }

 @Test
 public void testRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", true, "Required short option");
 option.setRequired(true);
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertTrue(option.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddRequiredOptionToGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("myGroup", "My group");
 Option option = new Option("a", true, "Required short option", group);
 option.setRequired(true);
 options.addOption(option);
 fail("Should not add a required option to a group");
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", true, "Test option");
 options.addOption(option);
 Option retrievedOption = options.getOption("a");
 assertSame(option, retrievedOption);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("a", true, "Option 1");
 Option option2 = new Option("ab", false, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("a");
 assertEquals(1, matchingOptions.size());
 assertEquals("a", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", true, "Test option");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("longOption", true, "Test long option");
 options.addOption(option);
 assertTrue(options.hasLongOption("longOption"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("a", true, "Test short option");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("a", true, "Test option");
 options.addOption(option);
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals(true, cmd.getOptionValue("a"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArguments() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("a", true, "Test option");
 options.addOption(option);
 String[] args = {"-b"};
 CommandLine cmd = parser.parse(options, args);
 assertNull(cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("a"));
 }
}