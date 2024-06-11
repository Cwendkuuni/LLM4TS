import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Basic test case for addOption(String opt, String description) method
 @Test
 public void testAddOptionWithDescription() {
 Options options = new Options();
 options.addOption("a", "option A", true);
 assertEquals("option A", options.getOption("a").getLongOpt());
 assertEquals("option A", options.getOption("a").getOptString());
 assertEquals("option A", options.getOption("a").getDescription());
 }

 // Basic test case for addOption(String opt, boolean hasArg, String description) method
 @Test
 public void testAddOptionWithHasArgAndDescription() {
 Options options = new Options();
 options.addOption("b", "option B", false, "option B description");
 assertEquals("option B", options.getOption("b").getLongOpt());
 assertEquals("option B", options.getOption("b").getOptString());
 assertEquals("option B description", options.getOption("b").getDescription());
 }

 // Basic test case for addOption(String opt, String longOpt, boolean hasArg, String description) method
 @Test
 public void testAddOptionWithLongOptAndDescription() {
 Options options = new Options();
 options.addOption("c", "option C", true, "option C description");
 assertEquals("option C", options.getOption("c").getLongOpt());
 assertEquals("option C", options.getOption("c").getOptString());
 assertEquals("option C description", options.getOption("c").getDescription());
 }

 // Test for addOptionGroup(OptionGroup group) method
 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group");
 group.addOption(new Option("g", "option G"));
 Options options = new Options();
 options.addOptionGroup(group);
 assertEquals("group", options.getOptionGroup("group").getName());
 assertTrue(options.getOptions().contains(group.getOptions().get(0)));
 }

 // Test for getOptionGroups() method
 @Test
 public void testGetOptionGroups() {
 OptionGroup group = new OptionGroup("group");
 group.addOption(new Option("g", "option G"));
 Options options = new Options();
 options.addOptionGroup(group);
 assertEquals(Collections.singletonList(group), options.getOptionGroups());
 }

 // Test for getOptions() method
 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 Option optionB = new Option("b", "option B", false, "option B description");
 Option optionC = new Option("c", "option C", true, "option C description");
 options.addOption(optionA);
 options.addOption(optionB);
 options.addOption(optionC);
 List<Option> optionsList = options.getOptions();
 assertEquals(3, optionsList.size());
 assertTrue(optionsList.contains(optionA));
 assertTrue(optionsList.contains(optionB));
 assertTrue(optionsList.contains(optionC));
 }

 // Test for hasOption(String opt) method
 @Test
 public void testHasOption() {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 options.addOption(optionA);
 assertTrue(options.hasOption("a"));
 }

 // Test for hasLongOption(String opt) method
 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option optionC = new Option("c", "option C", true, "option C description");
 options.addOption(optionC);
 assertTrue(options.hasLongOption("c"));
 }

 // Test for hasShortOption(String opt) method
 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 options.addOption(optionA);
 assertTrue(options.hasShortOption("a"));
 }

 // Test for getOption(String opt) method
 @Test
 public void testGetOption() {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 Option optionB = new Option("b", "option B", false, "option B description");
 Option optionC = new Option("c", "option C", true, "option C description");
 options.addOption(optionA);
 options.addOption(optionB);
 options.addOption(optionC);
 Option option = options.getOption("a");
 assertEquals("option A", option.getLongOpt());
 assertEquals("a", option.getKey());
 }

 // Test for getRequiredOptions() method
 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 Option optionB = new Option("b", "option B", false, "option B description");
 Option optionC = new Option("c", "option C", true, "option C description");
 options.addOption(optionA);
 options.addOption(optionB);
 options.addOption(optionC);
 options.addOptionGroup(new OptionGroup("group", "group options"));
 options.addOption(new Option("g", "group option", false, "group option description"));
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("a", requiredOptions.get(0).getKey());
 }

 // Test for parse() method with valid options
 @Test
 public void testParseWithValidOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "option A", true);
 CommandLine cmd = options.parse("--a value");
 assertEquals("value", cmd.getOptionValue("a"));
 }

 // Test for parse() method with invalid options
 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "option A", true);
 CommandLine cmd = options.parse("--z value");
 }

 // Test for parse() method with long and short options
 @Test
 public void testParseWithLongAndShortOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "option A", true);
 options.addOption("z", "option A", true);
 CommandLine cmd = options.parse("--a value --z value");
 assertEquals("value", cmd.getOptionValue("a"));
 assertEquals("value", cmd.getOptionValue("z"));
 }

 // Test for parse() method with long options having the same name but different descriptions
 @Test
 public void testParseWithLongOptionsHavingSameNameButDifferentDescriptions() throws ParseException {
 Options options = new Options();
 options.addOption("z", "option Z1", true);
 options.addOption("z", "option Z2", false);
 CommandLine cmd = options.parse("--z value --z false");
 assertTrue(cmd.hasOption("z1"));
 assertFalse(cmd.hasOption("z2"));
 }

 // Test for parse() method with missing required options
 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOptions() throws ParseException {
 Options options = new Options();
 Option optionA = new Option("a", "option A", true);
 Option optionB = new Option("b", "option B", true);
 options.addOption(optionA);
 options.addOption(optionB);
 CommandLine cmd = options.parse("--a value");
 }

 // Test for parse() method with ambiguous options
 @Test(expected = ParseException.class)
 public void testParseWithAmbiguousOptions() throws ParseException {
 Options options = new Options();
 Option optionA = new Option("a", "option A1", true);
 Option optionB = new Option("a", "option A2", false);
 options.addOption(optionA);
 options.addOption(optionB);
 CommandLine cmd = options.parse("--a value");
 }
}