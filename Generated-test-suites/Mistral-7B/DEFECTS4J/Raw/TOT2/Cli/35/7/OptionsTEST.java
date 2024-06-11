package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Test case for addOption method with a single character short option
 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasOption("o"));
 assertFalse(options.hasOption("x"));
 }

 // Test case for addOption method with a long option
 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 Option option = new Option("output", "op", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasOption("output"));
 assertFalse(options.hasOption("x"));
 }

 // Test case for addOption method with a short and a long option
 @Test
 public void testAddOptionBoth() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Option option2 = new Option("output", "op", false, "Output file path");
 options.addOption(option2);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("output"));
 assertFalse(options.hasOption("x"));
 }

 // Test case for addOptionGroup method
 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group description");
 Option option = new Option("o", "option", false, "Option description");
 group.addOption(option);
 Options options = new Options();
 options.addOptionGroup(group);

 assertTrue(options.hasOption("o"));
 }

 // Test case for getOptions method
 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 List<Option> optionsList = options.getOptions();
 Option optionFromList = optionsList.get(0);

 assertEquals(option, optionFromList);
 }

 // Test case for getRequiredOptions method
 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 options.addOption("output", true, "Output file path");

 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals(option, requiredOptions.get(0));
 }

 // Test case for getOption method
 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 Option optionFromSet = options.getOption("o");

 assertEquals(option, optionFromSet);
 }

 // Test case for getMatchingOptions method
 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("opt", "option1", false, "Option1 description");
 Option option2 = new Option("opt", "option2", false, "Option2 description");
 options.addOption(option1);
 options.addOption(option2);

 List<String> optionsList = options.getMatchingOptions("opt");
 assertEquals(2, optionsList.size());
 }

 // Test case for hasOption method
 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasOption("o"));
 assertFalse(options.hasOption("x"));
 }

 // Test case for hasLongOption method
 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("output", "op", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasLongOption("output"));
 assertFalse(options.hasLongOption("x"));
 }

 // Test case for hasShortOption method
 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("x"));
 }

 // Test case for getOptionGroup method
 @Test
 public void testGetOptionGroup() {
 Options options = new Options();OptionGroup group = new OptionGroup("group", "Group description"); Option option = new Option("o", "option", false, "Option description"); group.addOption(option); options.addOptionGroup(group);

 OptionGroup groupFromSet = options.getOptionGroup(option);
 assertEquals(group, groupFromSet);
 }

 // Test case for toString method
 @Test
 public void testToString() {
 Options options = new Options(); Option option = new Option("o", "output", false, "Output file path"); options.addOption(option);

 String optionsString = options.toString();
 assertTrue(optionsString.contains("[ Options: [ short"));
 assertTrue(optionsString.contains("[ long"));
 }

 // Test case for parsing valid options
 @Test
 public void testParseOptionsValid() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("output"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 // Test case for parsing options with values
 @Test
 public void testParseOptionsWithValues() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("output"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 // Test case for parsing options without values
 @Test
 public void testParseOptionsNoValues() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "-o"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("output"));
 }

 // Test case for parsing multiple options
 @Test
 public void testParseOptionsMultiple() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option1 = new Option("o1", "output1", false, "Output file path 1");
 Option option2 = new Option("o2", "output2", false, "Output file path 2");
 options.addOption(option1);
 options.addOption(option2);

 String[] args = new String[]{"Test", "-o1", "-o2"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("output1"));
 assertTrue(cmd.hasOption("output2"));
 }

 // Test case for parsing invalid options
 @Test(expected = ParseException.class)
 public void testParseOptionsInvalid() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "-x"};
 CommandLine cmd = parser.parse(options, args);
 }

 // Test case for parsing invalid long options
 @Test(expected = ParseException.class)
 public void testParseOptionsInvalidLong() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "--outputx"};
 CommandLine cmd = parser.parse(options, args);
 }

 // Test case for parsing invalid short options
 @Test(expected = ParseException.class)
 public void testParseOptionsInvalidShort() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test", "--o"};
 CommandLine cmd = parser.parse(options, args);
 }

 // Test case for parsing missing options
 @Test(expected = ParseException.class)
 public void testParseOptionsMissing() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 String[] args = new String[]{"Test"};
 CommandLine cmd = parser.parse(options, args);
 }
}