package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

 abstract class TestParser extends Parser {
 @Override
 protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException {
 throw new UnsupportedOperationException();
 }
 }

 @Test
 public void testParseRequiredOptions() throws Exception {
 Options options = new Options();
 options.addRequiredOption("a", "a", true, "An option requiring an argument");
 options.addRequiredOption("b", "b", true, "Another required option");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"-a", "arg1", "-b"};
 CommandLine cmd = parser.parse(args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("arg1", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws Exception {
 Options options = new Options();
 options.addRequiredOption("a", "a", true, "An option requiring an argument");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"-b"};
 parser.parse(args);
 fail("Missing required option exception expected");
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws Exception {
 Options options = new Options();
 options.addRequiredOption("a", "a", true, "An option requiring an argument");
 options.addRequiredOption("b", "b", true, "Another required option");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"-a"};
 parser.parse(args);
 fail("Missing required options exception expected");
 }

 @Test
 public void testParseOptionalOptions() throws Exception {
 Options options = new Options();
 options.addOption("a", "a", false, "An optional option");
 options.addOption("b", "b", false, "Another optional option");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"-a", "-b"};
 CommandLine cmd = parser.parse(args);
 assertTrue(cmd.hasOption("a"));
 assertNull(cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertNull(cmd.getOptionValue("b"));
 }

 @Test
 public void testParseWithValue() throws Exception {
 Options options = new Options();
 options.addOption("a", "a", true, "An option requiring an argument");
 options.addOption("b", "b", false, "An optional option with a value");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"-a", "ValueA", "-b", "ValueB"};
 CommandLine cmd = parser.parse(args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("ValueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("ValueB", cmd.getOptionValue("b"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() throws Exception {
 Options options = new Options();
 options.addOption("a", "a", false, "An option without an argument");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"--invalid"};
 parser.parse(args);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testParseLongOptions() throws Exception {
 Options options = new Options();
 options.addLongOption("longOptionA", "loa", true, "A long option requiring an argument");
 options.addLongOption("longOptionB", "lob", false, "An optional long option");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"--longOptionA", "ValueA", "--longOptionB"};
 CommandLine cmd = parser.parse(args);
 assertTrue(cmd.hasOption("longOptionA"));
 assertEquals("ValueA", cmd.getOptionValue("longOptionA"));
 assertTrue(cmd.hasOption("longOptionB"));
 }

 @Test
 public void testParseWithProperties() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("optionA", "ValueA");
 properties.setProperty("optionB", "ValueB");

 Options options = new Options();
 options.addOption("a", "a", false, "An option with a property value");

 TestParser parser = new TestParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(null, properties);
 assertTrue(cmd.hasOption("a"));
 assertEquals("ValueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("optionB"));
 assertNull(cmd.getOptionValue("optionB"));
 }

 @Test
 public void testParseWithGroups() throws Exception {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Group 1");
 group.setRequired(true);
 group.addOption("optionA", "oa", true, "An option in a required group");
 group.addOption("optionB", "ob", false, "An optional option in a required group");
 options.addOption("optionC", "oc", false, "An optional option not in a group");
 options.addOptionGroup(group);

 TestParser parser = new TestParser();
 parser.setOptions(options);
 String[] args = {"--group1", "--optionA", "--optionC"};
 CommandLine cmd = parser.parse(args);
 assertTrue(cmd.hasOption("optionA"));
 assertTrue(cmd.hasOption("optionB"));
 assertFalse(cmd.hasOption("optionC"));
 assertTrue(group.isSelected());
 }
}