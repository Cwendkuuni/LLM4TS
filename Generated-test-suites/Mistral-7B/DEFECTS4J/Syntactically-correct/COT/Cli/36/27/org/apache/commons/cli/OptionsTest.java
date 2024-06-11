import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();
 options.addOption("a", "alert", true, "Display an alert message.");
 options.addOption("b", "big", false, "Make the output big.");
 options.addOptionGroup(new OptionGroup("group1", "Group 1", true, "Options in group 1"));
 options.addOption("c", "color", true, "Set the color.", "RED|GREEN|BLUE");
 options.addOptionGroup(new OptionGroup("group2", "Group 2", false, "Options in group 2"));
 options.addOption("d", "debug", false, "Enable debug mode.");
 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 assertNotNull(optionA);
 assertEquals("alert", optionA.getLongOpt());
 assertTrue(optionA.isRequired());
 }

 @Test
 public void testGetOptions() {
 Options options = createTestOptions();
 List<Option> optionsList = options.getOptions();
 assertEquals(5, optionsList.size());
 }

 @Test
 public void testHasOption() {
 Options options = createTestOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alert"));
 assertTrue(options.hasOption("b"));
 assertFalse(options.hasOption("x"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createTestOptions();
 List<String> optionsList = options.getMatchingOptions("opt");
 assertEquals(1, optionsList.size());
 assertEquals("group1", optionsList.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option optionA = new Option("a", "alert", true, "Display an alert message.");
 options.addOption(optionA);
 try {
 options.addOption(optionA);
 } catch (IllegalArgumentException e) {
 assertEquals("Option 'a' is already defined", e.getMessage());
 }
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 Option optionA = new Option("a", "arg", true, "Display an argument.");
 options.addOption(optionA);
 Option parsedOption = new Option("a", "arg", true, "Display an argument.");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Option cmdOption = cmd.getOption("a");
 assertEquals("value", cmdOption.getValue());
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-x"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithMissingRequiredOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-b"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertFalse(cmd.hasOption("a"));
 } catch (ParseException e) {
 fail("Expected ParseException");
 }
 }

 @Test
 public void testParseWithMissingGroupOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--group1"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithValidOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value", "--big", "--group1", "--color=RED"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("value", cmd.getOption("a").getValue());
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertEquals("RED", cmd.getOption("c").getValue());
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test
 public void testParseWithMixedOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--group1", "--color=RED", "-a", "value", "--big", "-d"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("value", cmd.getOption("a").getValue());
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertEquals("RED", cmd.getOption("c").getValue());
 assertTrue(cmd.hasOption("d"));
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test
 public void testParseWithInvalidArgument() {
 Options options = createTestOptions();
 options.getOption("c").setArgParse("false");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value", "--color=INVALID"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithMissingValueArg() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value", "--color"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithValueArg() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value1", "--color=value2"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("value1", cmd.getOption("a").getValue());
 assertEquals("value2", cmd.getOption("c").getValue());
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test
 public void testParseWithInvalidOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--invalidGroup"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithValidOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--group1"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.getOption("c").isAllowedValue("RED"));
 assertTrue(cmd.getOption("c").isAllowedValue("GREEN"));
 assertTrue(cmd.getOption("c").isAllowedValue("BLUE"));
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test
 public void testParseWithMixedOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--group1", "--color=BLUE", "--group2", "--debug"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.getOption("c").isAllowedValue("RED"));
 assertTrue(cmd.getOption("c").isAllowedValue("GREEN"));
 assertTrue(cmd.getOption("c").isAllowedValue("BLUE"));
 assertTrue(cmd.hasOption("d"));
 } catch (ParseException e) {
 fail("Failed to parse command line");
 }
 }

 @Test
 public void testToString() {
 Options options = createTestOptions();
 assertEquals("[ Options: [ short {a={alert=true, required=true}}, {b={}} ], [ long {} ], [ requiredOptions=[], optionGroups={group1={}, group2={}}] ]", options.toString());
 }
}