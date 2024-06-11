import org.apache.commons.cli.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OptionsTest {

 @BeforeAll
 static void setUp() {
 // Create a sample Options object
 Options options = new Options();
 options.addOption("a", "alias", false, "Test option A");
 options.addOption("b", "big", false, "Test option B");
 options.addOptionGroup(new OptionGroup("group1")
 .addOption(Option.builder("g1a").longOpt("group1-option-a").value(true).build())
 .addOption(Option.builder("g1b").longOpt("group1-option-b").build()));
 options.addOptionGroup(new OptionGroup("group2")
 .addOption(Option.builder("g2a").longOpt("group2-option-a").value(true).build())
 .addOption(Option.builder("g2b").longOpt("group2-option-b").build()));
 }

 @Test
 void testAddOption() {
 // Test adding a single option
 Options options = new Options();
 Option option = Option.builder("option").longOpt("long-option").build();
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 }

 @Test
 void testAddOptionGroup() {
 // Test adding an option group
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group");
 options.addOptionGroup(optionGroup);
 assertTrue(options.getOptionGroups().contains(optionGroup));
 }

 @Test
 void testGetMatchingOptions() {
 // Test getting matching options
 Options options = new Options();
 options.addOption("optionA", true, "Test option A");
 options.addOption("optionB", true, "Test option B");
 options.addOption("optionC", true, "Test option C");
 List<String> matchingOptions = options.getMatchingOptions("optio");
 assertEquals(2, matchingOptions.size());
 assertEquals("optionA", matchingOptions.get(0));
 assertEquals("optionB", matchingOptions.get(1));
 }

 @Test
 void testGetOption() {
 // Test getting an option by name
 Options options = new Options();
 Option option = Option.builder("option").longOpt("long-option").build();
 options.addOption(option);
 Option retrievedOption = options.getOption("option");
 assertEquals(option, retrievedOption);
 }

 @Test
 void testHasOption() {
 // Test checking if an option exists
 Options options = new Options();
 Option option = Option.builder("option").longOpt("long-option").build();
 options.addOption(option);
 assertTrue(options.hasOption("option"));
 }

 @Test
 void testHasLongOption() {
 // Test checking if a long option exists
 Options options = new Options();
 Option option = Option.builder("option").longOpt("long-option").build();
 options.addOption(option);
 assertTrue(options.hasLongOption("long-option"));
 }

 @Test
 void testHasShortOption() {
 // Test checking if a short option exists
 Options options = new Options();
 Option option = Option.builder('o', "option").longOpt("long-option").build();
 options.addOption(option);
 assertTrue(options.hasShortOption('o'));
 }

 @Test
 void testParse() {
 // Test parsing options
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions("--long-option"), new DefaultParseResult());
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 void testParseWithValue() {
 // Test parsing options with values
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option", true, "Test option");
 CommandLine cmd = parser.parse(options.parseOptions("--option value"), new DefaultParseResult());
 assertEquals("value", cmd.getOptionValue("option"));
 }

 @Test
 void testParseWithInvalidOption() {
 // Test parsing with an invalid option
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options.parseOptions("--invalid"), new DefaultParseResult());
 assertFalse(cmd.hasOptions());
 }

 @Test
 void testParseWithInvalidLongOption() {
 // Test parsing with an invalid long option
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option", true, "Test option");
 CommandLine cmd = parser.parse(options.parseOptions("--invalid-long-option"), new DefaultParseResult());
 assertFalse(cmd.hasOptions());
 }

 @Test
 void testParseWithInvalidShortOption() {
 // Test parsing with an invalid short option
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option", true, "Test option");
 CommandLine cmd = parser.parse(options.parseOptions("--z option"), new DefaultParseResult());
 assertFalse(cmd.hasOptions());
 }

 @Test
 void testParseWithGroup() {
 // Test parsing with an option group
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group")
 .addOption(Option.builder("g").longOpt("group-option").build()));
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions("--group"), new DefaultParseResult());
 assertTrue(cmd.hasOption("g"));
 }

 @Test
 void testParseWithGroupValue() {
 // Test parsing with an option group and value
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group")
 .addOption(Option.builder("g").longOpt("group-option").build())
 .addOption(Option.builder("gv").longOpt("group-value").hasArg().build()));
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions("--group value"), new DefaultParseResult());
 assertEquals("value", cmd.getOptionValue("gv"));
 }

 @Test
 void testParseWithGroupRequired() {
 // Test parsing with an option group and required option
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group")
 .addOption(Option.builder("g").longOpt("group-option").build())
 .addOption(Option.builder("gr").longOpt("group-required-option").isRequired().build());
 options.addOptionGroup(optionGroup);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions("--group"), new DefaultParseResult());
 assertTrue(cmd.hasOption("g"));
 assertTrue(cmd.hasOption("gr"));
 }

 @Test
 void testParseWithGroupRequiredValue() {
 // Test parsing with an option group, required option, and value
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group")
 .addOption(Option.builder("g").longOpt("group-option").build())
 .addOption(Option.builder("gr").longOpt("group-required-option").isRequired().hasArg().build());
 options.addOptionGroup(optionGroup);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions("--group value"), new DefaultParseResult());
 assertEquals("value", cmd.getOptionValue("gr"));
 }
}