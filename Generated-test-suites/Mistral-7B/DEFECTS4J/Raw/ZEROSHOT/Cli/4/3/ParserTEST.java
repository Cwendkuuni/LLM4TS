package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--option1", "value1", "--option2", "value2", "-o", "option3"};

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");
 options.addOption("option2", true, "Option 2 with value");
 options.addOption("option3", false, "Option 3 without value");

 CommandLine commandLine = parser.parse(options, ARGUMENTS, null);

 assertEquals(3, commandLine.getArgList().size());
 assertEquals("value1", commandLine.getOptionValue("option1"));
 assertEquals("value2", commandLine.getOptionValue("option2"));
 assertNull(commandLine.getOptionValue("option3"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");

 CommandLine commandLine = parser.parse(options, new String[0]);

 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testMissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");
 options.addOption("option2", true, "Option 2 with value");
 options.addOption("option3", true, "Option 3 with required argument");

 CommandLine commandLine = parser.parse(options, ARGUMENTS, null);

 assertEquals(3, commandLine.getArgList().size());
 assertNull(commandLine.getOptionValue("option3"));

 try {
 commandLine.getOptionValue("option3.arg");
 } catch (MissingOptionException e) {
 // Expected
 }

 try {
 commandLine.getOptionValue("option3.arg", new MissingArgumentException(""));
 } catch (MissingArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");
 options.addOption("option2", true, "Option 2 with value");
 options.addOption("option3", true, "Option 3 with value");

 Properties properties = new Properties();
 properties.setProperty("option1", "value1");
 properties.setProperty("option2", "value2");
 properties.setProperty("option3", "value3");

 CommandLine commandLine = parser.parse(options, new String[0], properties);

 assertEquals(0, commandLine.getArgList().size());
 assertEquals("value1", commandLine.getOptionValue("option1"));
 assertEquals("value2", commandLine.getOptionValue("option2"));
 assertEquals("value3", commandLine.getOptionValue("option3"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");

 CommandLine commandLine = parser.parse(options, new String[]{"--invalid"}, null);

 fail("Expected ParseException");
 }

 @Test
 public void testParseWithLongOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("long-option", true, "Long option with value");

 CommandLine commandLine = parser.parse(options, new String[]{"--long-option", "value"}, null);

 assertEquals(1, commandLine.getArgList().size());
 assertEquals("value", commandLine.getOptionValue("long-option"));
 }

 @Test
 public void testParseWithGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(false);
 group.setExclusive(false);
 group.addOption(new Option("o", "option1", true, "Option 1"));
 group.addOption(new Option("o", "option2", true, "Option 2"));
 options.addOptionGroup(group);
 options.addOption("option3", true, "Option 3 without value");

 CommandLine commandLine = parser.parse(options, new String[]{"--option1", "value1", "--option2", "value2", "--option3"}, null);

 assertEquals(3, commandLine.getArgList().size());
 assertEquals("value1", commandLine.getOptionValue("option1"));
 assertEquals("value2", commandLine.getOptionValue("option2"));
 assertNull(commandLine.getOptionValue("option3"));

 Option optionGroup = options.findOption("group");
 assertTrue(optionGroup.isSelected());
 }

 @Test
 public void testParseWithMixedArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Option 1 with value");
 options.addOption("option2", true, "Option 2 with value");
 options.addOption("option3", false, "Option 3 without value");

 CommandLine commandLine = parser.parse(options, new String[]{"--option1", "value1", "argument1", "--option2", "value2"}, null);

 assertEquals(3, commandLine.getArgList().size());
 assertEquals("value1", commandLine.getOptionValue("option1"));
 assertEquals("value2", commandLine.getOptionValue("option2"));
 assertEquals("argument1", commandLine.getArgList().get(0));
 assertNull(commandLine.getOptionValue("option3"));
 }
}