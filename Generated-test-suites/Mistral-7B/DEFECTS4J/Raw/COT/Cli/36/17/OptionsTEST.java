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

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("a", "a", false, "Option A");
 options.addOption(option);
 Option retrievedOption = options.getOption("a");
 assertSame(option, retrievedOption);
 }

 @Test
 public void testAddLongOption() {
 Option option = new Option("long-option", "long option", false, "Long option description");
 options.addOption(option);
 Option retrievedOption = options.getOption("long-option");
 assertSame(option, retrievedOption);
 }

 @Test
 public void testAddRequiredOption() {
 Option option = new Option("r", "required", true, "Required option");
 options.addOption(option);
 List<Option> optionsList = options.helpOptions();
 Option retrievedOption = optionsList.get(0);
 assertTrue(retrievedOption.isRequired());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("g", "group-option", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Option retrievedOption = options.getOption("g");
 assertSame(option, retrievedOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Option option = new Option("a", "a", false, "Option A");
 options.addOption(option);
 options.addOption(option);
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("opt1", "opt1", false, "Option 1");
 Option option2 = new Option("opt2", "opt2", false, "Option 2");
 Option option3 = new Option("opt1-long", "opt1 long", false, "Option 1 long");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("opt1");
 assertEquals(2, matchingOptions.size());
 assertEquals("opt1", matchingOptions.get(0));
 assertEquals("opt1-long", matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("a", "a", false, "Option A");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("long-option", "long option", false, "Long option description");
 options.addOption(option);
 assertTrue(options.hasLongOption("long-option"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("a", "a", false, "Option A");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testHasOptionWithNoMatchingShortOrLong() {
 assertFalse(options.hasOption("nonexistent"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("g", "group-option", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 OptionGroup retrievedGroup = options.getOptionGroup(option);
 assertSame(group, retrievedGroup);
 }

 @Test
 public void testGetRequiredOptions() {
 Option option1 = new Option("r1", "required1", true, "Required 1");
 Option option2 = new Option("r2", "required2", true, "Required 2");
 options.addOption(option1);
 options.addOption(option2);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(2, requiredOptions.size());
 assertTrue(requiredOptions.get(0).isRequired());
 assertTrue(requiredOptions.get(1).isRequired());
 }

 @Test
 public void testParseWithInvalidInput() {
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, new String[]{"test", "invalid-input"});
 fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithValidInput() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("a", "a", false, "Option A"));
 try {
 parser.parse(options, new String[]{"test", "-a"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertNull(commandLine.getOptionValues("a"));
 }

 @Test
 public void testParseWithValue() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("v", "value", true, "Value option"));
 try {
 parser.parse(options, new String[]{"test", "-v", "value"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertEquals(new String[]{"value"}, commandLine.getOptionValues("value"));
 }

 @Test
 public void testParseWithLongOption() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("long", "long option", false, "Long option"));
 try {
 parser.parse(options, new String[]{"test", "--long"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertTrue(commandLine.hasOption("long"));
 }

 @Test
 public void testParseWithShortAndLongOption() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("s", "short option", false, "Short option"));
 options.addOption(new Option("l", "long option", false, "Long option"));
 try {
 parser.parse(options, new String[]{"test", "-s", "--long"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertTrue(commandLine.hasOption("s"));
 assertTrue(commandLine.hasOption("l"));
 }

 @Test
 public void testParseWithValueForShortOption() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("v", "value", true, "Value option"));
 try {
 parser.parse(options, new String[]{"test", "-v", "value"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertEquals(new String[]{"value"}, commandLine.getOptionValues("value"));
 }

 @Test
 public void testParseWithValueForLongOption() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("l", "value", true, "Value option"));
 try {
 parser.parse(options, new String[]{"test", "--value", "value"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertEquals(new String[]{"value"}, commandLine.getOptionValues("value"));
 }

 @Test
 public void testParseWithShortAndLongOptionWithValue() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("s", "short option", false, "Short option"));
 options.addOption(new Option("l", "long option", false, "Long option"));
 options.addOption(new Option("v", "value", true, "Value option"));
 try {
 parser.parse(options, new String[]{"test", "-s", "--long", "-v", "value"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertTrue(commandLine.hasOption("s"));
 assertTrue(commandLine.hasOption("l"));
 assertEquals(new String[]{"value"}, commandLine.getOptionValues("value"));
 }

 @Test
 public void testParseWithMultipleValuesForOption() {
 CommandLineParser parser = new DefaultParser();
 options.addOption(new Option("v", "values", true, "Values option"));
 try {
 parser.parse(options, new String[]{"test", "-v", "value1", "value2"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertEquals("test", commandLine.getName());
 assertEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("values"));
 }

 @Test
 public void testParseWithGroup() {
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("g", "group-option", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 try {
 parser.parse(options, new String[]{"test", "-g"});
 } catch (ParseException e) {
 fail();
 }
 CommandLine commandLine = parser.getParseResult();
 assertTrue(commandLine.hasOption("g"));
 }

 @Test
 public void testToString() {
 String expected = "[ Options: [ short {} ] [ long {} ]]";
 assertEquals(expected, options.toString());
 }
}