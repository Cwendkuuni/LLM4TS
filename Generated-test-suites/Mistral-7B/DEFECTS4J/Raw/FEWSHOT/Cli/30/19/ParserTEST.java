package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

 // Test for abstract method flatten
 @SuppressWarnings("unchecked")
 @Test
 public void testFlatten() {
 Options options = new Options();
 String[] arguments = {"-a", "--optionB=valueB", "-b", "--optionC=valueC"};
 Parser parser = new DefaultParser();
 List requiredOptions = parser.getOptions().getRequiredOptions();
 List<String> expected = Arrays.asList("-a", "--optionB", "valueB", "--optionC", "valueC");
 List tokenList = parser.flatten(options, arguments, false);
 ListIterator<String> iterator = tokenList.listIterator();
 List<String> actual = new ArrayList<>();
 while (iterator.hasNext()) {
 actual.add((String) iterator.next());
 }
 assertEquals(expected, actual);
 assertFalse(requiredOptions.isEmpty());
 }

 // Test for parse method with no options or arguments
 @Test
 public void testParseNoArgs() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0]);
 assertTrue(cmd.getOptions().isEmpty());
 assertTrue(cmd.getArgs().isEmpty());
 }

 // Test for parse method with only options
 @Test
 public void testParseOnlyOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0]);
 assertTrue(cmd.getOptions().size() > 0);
 assertFalse(cmd.getArgs().isEmpty());
 }

 // Test for parse method with only arguments
 @Test
 public void testParseOnlyArgs() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
 assertTrue(cmd.getArgs().size() > 0);
 assertFalse(cmd.getOptions().isEmpty());
 }

 // Test for parse method with options and arguments
 @Test
 public void testParseOptionsAndArgs() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"--a", "arg1", "--b=valB", "arg2"});
 assertTrue(cmd.getOptions().size() > 0);
 assertTrue(cmd.getArgs().size() > 0);
 assertEquals("valB", cmd.getOptionValue("b"));
 }

 // Test for parse method with unrecognized option
 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 Options invalidOption = new Options();
 invalidOption.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 parser.parse(options, new String[]{"--invalidOption"});
 }

 // Test for parse method with required option missing
 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B", true);
 Parser parser = new DefaultParser();
 parser.parse(options, new String[]{"--a"});
 }

 // Test for parse method with required option and value missing
 @Test(expected = MissingArgumentException.class)
 public void testMissingRequiredArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B", true);
 Parser parser = new DefaultParser();
 parser.parse(options, new String[]{"--a"});
 }

 // Test for parse method with multiple required options
 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 Parser parser = new DefaultParser();
 parser.parse(options, new String[]{"--a"});
 }
}