import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option with argument");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 option.setLongOpt("long-alias");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("g", "group");
 options.addOptionGroup(optionGroup);
 assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("g", "group").required(true));
 assertEquals(2, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateShortOption() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Option 1");
 Option option2 = new Option("a", "alias2", false, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = new Options();
 Option option1 = new Option("--alias1", "alias1", false, "Option 1");
 Option option2 = new Option("--alias2", "alias1", false, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOptionDescription() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, null);
 options.addOption(option);
 }

 @Test
 public void testParseWithOneShortOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOneLongOption() {
 Options options = new Options();
 Option option = new Option("alias", "An alias option", false, null);
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--alias"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("alias"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOneShortOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option with argument");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "arg"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("arg", cmd.getOptionValue("a"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOneLongOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("alias", "An alias option with argument", true, null);
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--alias", "arg"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("arg", cmd.getOptionValue("alias"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithMultipleOptions() {
 Options options = new Options();
 Option option1 = new Option("a1", "Option 1", false, null);
 Option option2 = new Option("a2", "Option 2", false, null);
 options.addOption(option1);
 options.addOption(option2);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a1", "--a2"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a1"));
 assertTrue(cmd.hasOption("a2"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOptionGroups() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("g", "group");
 Option option1 = new Option("a1", "Option 1", false, null);
 Option option2 = new Option("a2", "Option 2", false, null);
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 options.addOptionGroup(optionGroup);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--group"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a1"));
 assertTrue(cmd.hasOption("a2"));
 } catch (ParseException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-b"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected exception: option -b not recognized.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalid"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected exception: Unknown option: --invalid");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptionArgumentType() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option with argument of type int");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "not an int"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected exception: The value 'not an int' does not match type: class java.lang.String");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected exception: Option 'a' is required");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithRequiredOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("g", "group").required(true);
 options.addOptionGroup(optionGroup);
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected exception: The following options are required: [--group]");
 } catch (ParseException e) {
 // expected
 }
 }
}