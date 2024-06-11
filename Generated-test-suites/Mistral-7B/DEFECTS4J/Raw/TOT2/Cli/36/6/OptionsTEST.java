import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Test Option with value");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "Test Group");
 Option option1 = new Option("o1", "Option 1", false, "Test Option 1");
 Option option2 = new Option("o2", "Option 2", false, "Test Option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 assertEquals(3, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = new Options();
 Option option1 = new Option("o", "Option", false, "Test Option");
 options.addOption(option1);
 Option option2 = new Option("o", "Option", false, "Test Option");
 options.addOption(option2);
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 Option opt = options.getOption("o");
 assertEquals(option, opt);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("o1", "Option 1", false, "Test Option 1");
 Option option2 = new Option("o2", "Option 2", false, "Test Option 2");
 Option option3 = new Option("o3", "Option 3", false, "Test Option 3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("o");
 assertEquals(3, matchingOptions.size());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testParse() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("o"));
 } catch (ParseException e) {
 fail("Parse exception thrown");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-x"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithDuplicateOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "-o"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithUnrecognizedOption() {
 Options options = new Options();Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--unrecognized"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithMultipleRequiredOptions() {
 Options options = new Options();
 Option option1 = new Option("o1", "option1", true, "Test Option 1");
 Option option2 = new Option("o2", "option2", true, "Test Option 2");
 options.addOption(option1);
 options.addOption(option2);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o1", "-o2"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("o1"));
 assertTrue(cmd.hasOption("o2"));
 } catch (ParseException e) {
 fail("Parse exception thrown");
 }
 }

 @Test
 public void testParseWithValidArguments() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "value"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("o"));
 assertEquals("value", cmd.getOptionValue("o"));
 } catch (ParseException e) {
 fail("Parse exception thrown");
 }
 }

 @Test
 public void testParseWithInvalidArgument() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Test Option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "invalid"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parse exception not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 assertEquals("Options: [ short {} ] [ long {} ]", options.toString());
 }

 @Test
 public void testHelpFormatter() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test Option");
 options.addOption(option);
 OptionGroup group = new OptionGroup("g", "Test Group");
 Option option1 = new Option("o1", "Option 1", false, "Test Option 1");
 Option option2 = new Option("o2", "Option 2", false, "Test Option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("Test", options);
 assertTrue(help.contains("Test Option"));
 }
}