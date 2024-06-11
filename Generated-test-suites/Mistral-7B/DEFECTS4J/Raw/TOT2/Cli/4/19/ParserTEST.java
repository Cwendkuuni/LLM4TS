package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] TEST_ARGS = new String[]{"-a", "--optionA", "--optionB=valueB", "-o", "output.txt"};

 @Test
 public void testParseSimple() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, TEST_ARGS, false);

 assertEquals("The number of options should match the number of provided arguments.", TEST_ARGS.length, cmd.getOptions().size());

 for (Option option : cmd.getOptions()) {
 if (option.isRequired()) {
 assertTrue("Required option '" + option.getKey() + "' should be present.", cmd.hasOption(option.getKey()));
 }
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"--invalidOption"};
 parser.parse(options, arguments, null, false);
 fail("Should throw ParseException for missing option.");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"--unknownOption"};
 try {
 parser.parse(options, arguments, null, false);
 fail("Should throw UnrecognizedOptionException for unrecognized option.");
 } catch (UnrecognizedOptionException e) {
 assertEquals("Unrecognized option: unknownOption", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Option option = new Option("a", "argA", true, "A required argument");
 options.addOption(option);
 String[] arguments = new String[]{"-a"};
 try {
 parser.parse(options, arguments, null, false);
 fail("Should throw MissingArgumentException for missing argument.");
 } catch (MissingArgumentException e) {
 assertEquals("Missing argument for option: a", e.getMessage());
 }
 }

 @Test
 public void testProcessOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = TEST_ARGS;
 CommandLine cmd = parser.parse(options, arguments, null, false);

 Option optionA = options.getOption("optionA");
 Option optionB = options.getOption("optionB");
 Option optionO = options.getOption("o");

 assertEquals("The value of optionA should be null.", null, cmd.getOptionValue("optionA"));
 assertEquals("The value of optionB should be 'valueB'.", "valueB", cmd.getOptionValue("optionB"));
 assertEquals("The value of optionO should be 'output.txt'.", "output.txt", cmd.getOptionValue("o"));
 }

 @Test
 public void testProcessProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("optionA", "valueA");
 properties.setProperty("optionB", "valueB");

 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = TEST_ARGS;
 CommandLine cmd = parser.parse(options, arguments, properties, false);

 Option optionA = options.getOption("optionA");
 Option optionB = options.getOption("optionB");

 assertEquals("The value of optionA from properties should be 'valueA'.", "valueA", cmd.getOptionValue("optionA"));
 assertEquals("The value of optionB should be 'valueB'.", "valueB", cmd.getOptionValue("optionB"));
 }

 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List<Option> requiredOptions = Arrays.asList(
 new Option("a", true, false, "Option A is required.")
 );
 options.setRequiredOptions(requiredOptions);
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments, null, false);
 fail("Should throw MissingOptionException for missing required options.");
 } catch (MissingOptionException e) {
 assertEquals("All required options are missing.", e.getMessage());
 }
 }

 @Test
 public void testParseWithOptionalOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List<Option> requiredOptions = Arrays.asList(new Option("a", true, false, "Option A is required."));
 options.setRequiredOptions(requiredOptions);
 String[] arguments = new String[]{"-a"};
 CommandLine cmd = parser.parse(options, arguments, null, false);
 assertFalse("Option A should not be required.", cmd.hasOption("a"));
 }

 @Test
 public void testParseWithOptionGroups() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "A group of options.");
 group.setRequired(false);
 options.addOptionGroup(group);
 Option optionA = new Option("a", "argA", true, "Option A");
 group.addOption(optionA);
 String[] arguments = new String[]{"-a"};
 CommandLine cmd = parser.parse(options, arguments, null, false);
 assertTrue("Option A should be a member of the group.", cmd.hasOption("a"));
 assertTrue("Option A should be a member of the group.", group.getOptions().contains(optionA));
 }
}