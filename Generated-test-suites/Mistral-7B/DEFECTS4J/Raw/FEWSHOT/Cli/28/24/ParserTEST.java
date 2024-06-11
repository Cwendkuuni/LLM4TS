package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

 public CommandLine parse(String[] args) throws ParseException {
 Parser parser = new DefaultParser();
 return parser.parse(getOptions(), args);
 }

 @Test
 public void testParseEmptyArgs() throws ParseException {
 CommandLine cmd = parse(new String[0]);
 assertNull(cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseSingleArg() throws ParseException {
 CommandLine cmd = parse(new String[]{"arg"});
 assertEquals("arg", cmd.getArgs()[0]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseSingleOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 CommandLine cmd = parse(new String[]{"-a"});
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseSingleOptionWithValue() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 CommandLine cmd = parse(new String[]{"-a", "value"});
 assertTrue(cmd.hasOption("a"));
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 CommandLine cmd = parse(new String[]{"-a", "-b"});
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseMultipleOptionsWithValues() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 CommandLine cmd = parse(new String[]{"-a", "valueA", "-b", "valueB"});
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 }

 @Test
 public void testParseMultipleOptionsMixed() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 CommandLine cmd = parse(new String[]{"-a", "valueA", "--", "-b", "valueB"});
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertFalse(cmd.hasArgs());
 }

 @Test
 public void testParseMultipleArgs() throws ParseException {
 CommandLine cmd = parse(new String[]{"arg1", "arg2"});
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 }

 @Test
 public void testParseMultipleArgsWithOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 CommandLine cmd = parse(new String[]{"arg1", "-a", "arg2"});
 assertTrue(cmd.hasOption("a"));
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 parse(new String[]{"missingOption"});
 }

 @Test(expected = ParseException.class)
 public void testParseMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B", true, "required argument");
 parse(new String[]{"-a"});
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() throws ParseException {
 Options options = new Options();
 parse(new String[]{"invalidOption"});
 }

 @Test
 public void testParseProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("optionA", "valueA");
 properties.setProperty("optionB.subOption", "valueB");
 Options options = new Options();
 options.addOption("optionA", true, "option A");
 options.addOption("optionB", false, "option B", true, "option with sub-option");
 CommandLine cmd = parse(new String[]{}, properties);
 assertTrue(cmd.hasOption("optionA"));
 assertEquals("valueA", cmd.getOptionValue("optionA"));
 assertTrue(cmd.hasOption("optionB"));
 assertNull(cmd.getOptionValue("optionB.subOption"));
 }

 @Test
 public void testParseRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 List requiredOptions = options.getRequiredOptions();
 CommandLine cmd = parse(new String[]{"-a"});
 assertEquals(1, requiredOptions.size());
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 requiredOptions.clear();
 cmd = parse(new String[]{"-a", "-b"});
 assertEquals(0, requiredOptions.size());
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 List requiredOptions = options.getRequiredOptions();
 parse(new String[]{"-b"});
 }
}