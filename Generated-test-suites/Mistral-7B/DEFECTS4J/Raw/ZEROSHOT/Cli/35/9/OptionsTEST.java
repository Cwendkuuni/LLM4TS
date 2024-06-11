import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli. ParseException;
import org.junit.Before;
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
 public void testAddOptionShort() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 assertEquals("i", options.getOption("i").getKey());
 }

 @Test
 public void testAddOptionShortWithValue() {
 Option option = new Option("i", "inputFile", true, "Set input file [FILE]");
 options.addOption("i", option.getLongOpt(), true, option.getDescription());
 assertEquals("i", options.getOption("i").getKey());
 assertEquals("FILE", options.getOption("i").getArgName());
 }

 @Test
 public void testAddOptionLong() {
 Option option = new Option("inputFile", "if");
 options.addOption(option);
 assertEquals("if", options.getOption("if").getLongOpt());
 }

 @Test
 public void testAddOptionLongWithValue() {
 Option option = new Option("inputFile", "if", true, "Set input file [FILE]");
 options.addOption(option);
 assertEquals("if", options.getOption("if").getLongOpt());
 assertEquals("FILE", options.getOption("if").getArgName());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("testGroup", "Test Group");
 options.addOptionGroup(group);
 assertTrue(options.getOptionGroups().contains(group));
 }

 @Test
 public void testGetRequiredOptions() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 options.addOptionGroup(new OptionGroup("testGroup", "Test Group"));
 List<String> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("i", requiredOptions.get(0));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 Option result = options.getOption("i");
 assertEquals(option, result);
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("io", "inputOption");
 Option option2 = new Option("ip", "inputOption2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("io");
 assertEquals(1, matchingOptions.size());
 assertEquals(option1.getLongOpt(), matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 assertTrue(options.hasOption("i"));
 assertTrue(options.hasOption("i"));
 assertTrue(options.hasOption("I"));
 assertTrue(options.hasOption("if"));
 assertFalse(options.hasOption("o"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 assertTrue(options.hasLongOption("if"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("i", "inputFile", false, "Set input file");
 options.addOption("i", option.getLongOpt(), false, option.getDescription());
 assertTrue(options.hasShortOption("i"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("testGroup", "Test Group");
 Option option = new Option("test", "testOption", false, "Test option");
 group.addOption(option);
 options.addOptionGroup(group);
 Option result = options.getOptionGroup(option);
 assertEquals(group, result);
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("i", "inputFile", false, "Set input file");
 Option option2 = new Option("o", "outputFile", false, "Set output file");options.addOption("i", option1.getLongOpt(), false, option1.getDescription());options.addOption("o", option2.getLongOpt(), false, option2.getDescription());
 assertEquals("[ Options: [ short {i={inputFile=Set input file}, o={outputFile=Set output file}} ] [ long {if=Set input file, outputFile=Set output file}] ]", options.toString());
 }

 @Test
 public void testParse() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-i", "input.txt", "--output", "output.txt"};
 options.parse(parser, args);
 Option inputOption = options.getOption("i");
 Option outputOption = options.getOption("output");
 assertEquals("input.txt", inputOption.getValue());
 assertEquals("output.txt", outputOption.getValue());
 }

}