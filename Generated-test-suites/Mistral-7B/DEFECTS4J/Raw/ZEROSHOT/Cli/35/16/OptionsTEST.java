import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 Options options = new Options();
 }

 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "Short option for alpha");
 options.addOption(option);
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = new Options();
 Option option = new Option("a", "alpha", true, "Short option for alpha with argument");
 options.addOption(option);
 }

 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 Option option = new Option("alpha", "longAlpha", false, "Long option for alpha");
 options.addOption(option);
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = new Options();
 Option option = new Option("alpha", "longAlpha", true, "Long option for alpha with argument");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Short option for alpha with argument");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("g", "groupOption", true, "Option in group");
 group.addOption(option);
 options.addOptionGroup(group);
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "Short option for alpha");
 options.addOption(option);
 List<Option> optionsList = options.getOptions();
 assertFalse(optionsList.isEmpty());
 assertNotNull(optionsList.get(0));
 assertEquals("alpha", optionsList.get(0).getKey());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option option = new Option("a", "alpha", true, "Required option for alpha");
 options.addOption(option);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertFalse(requiredOptions.isEmpty());
 assertEquals(1, requiredOptions.size());
 assertTrue(requiredOptions.get(0).isRequired());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "Short option for alpha");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alpha"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("alpha", "longAlpha", false, "Long option for alpha");
 options.addOption(option);
 assertTrue(options.hasLongOption("longAlpha"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "Short option for alpha");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("ap", "apLongOption", false, "Long option starting with ap");
 Option option2 = new Option("alpha", "longAlphaOption", false, "Long option for alpha");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("ap");
 assertFalse(matchingOptions.isEmpty());
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("apLongOption"));
 assertTrue(matchingOptions.contains("alpha"));
 }

 @Test
 public void testParseOptionsWithParser() throws ParseException {
 Options options = new Options();
 Option option = new Option("a", "alpha", true, "Short option for alpha");
 options.addOption(option);
 String[] args = new String[]{"-a", "value"};
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);
 assertEquals("value", cmd.getOptionValue("alpha"));
 }

 @Test
 public void testParseOptionsWithHelpFormatter() {
 Options options = new Options();Option option = new Option("a", "alpha", true, "Short option for alpha"); options.addOption(option); String[] args = new String[0]; HelpFormatter formatter = new HelpFormatter(); formatter.printHelp("Test", options); }

 @Test (expected = ParseException.class)
 public void testParseOptionsWithInvalidArgument() throws ParseException {
 Options options = new Options(); Option option = new Option("a", "alpha", true, "Short option for alpha"); options.addOption(option); String[] args = new String[]{"-b", "value"}; CommandLineParser parser = new DefaultParser(); CommandLine cmd = parser.parse(options, args); }

 @Test (expected = ParseException.class)
 public void testParseOptionsWithMissingArgument() throws ParseException {
 Options options = new Options(); Option option = new Option("a", "alpha", true, "Short option for alpha"); options.addOption(option); String[] args = new String[]{"-a"}; CommandLineParser parser = new DefaultParser(); CommandLine cmd = parser.parse(options, args); }

 @Test
 public void testParseOptionsWithUnrecognizedOption() throws ParseException {
 Options options = new Options(); Option option = new Option("a", "alpha", true, "Short option for alpha"); options.addOption(option); String[] args = new String[]{"-b"}; CommandLineParser parser = new DefaultParser(); CommandLine cmd = parser.parse(options, args); assertNull(cmd.getOptionValue("alpha")); }

 @Test
 public void testToString() {
 Options options = new Options(); Option option = new Option("a", "alpha", false, "Short option for alpha"); options.addOption(option); assertEquals("[ Options: [ short {a=} ] [ long {} ]]", options.toString()); }
}