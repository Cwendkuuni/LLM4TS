package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private Option option;
 private WriteableCommandLine commandLine;
 private CommandLineParser parser;

 @Before
 public void setUp() {
 option = new OptionBuilder()
 .withDescription("A test option")
 .withLongOpt("testOption")
 .withArgName("arg")
 .withValueSeparator('=')
 .withValuesSeparator(',')
 .isRequired()
 .create();
 commandLine = new WriteableCommandLine();
 parser = new DefaultParser();
 }

 @Test
 public void testCanProcessTrue() {
 commandLine.addArgument("arg1");
 commandLine.addArgument("testOption=value1,value2");
 assertTrue(option.canProcess(commandLine, commandLine.iterator()));
 }

 @Test
 public void testCanProcessFalse() {
 commandLine.addArgument("arg1");
 assertFalse(option.canProcess(commandLine, commandLine.iterator()));
 }

 @Test
 public void testCanProcessNoArguments() {
 commandLine.addArgument("arg1");
 commandLine.addArgument("testOption");
 assertFalse(option.canProcess(commandLine, commandLine.iterator()));
 }

 @Test
 public void testCanProcessNoOption() {
 commandLine.addArgument("arg1");
 commandLine.addArgument("arg2");
 assertFalse(option.canProcess(commandLine, commandLine.iterator()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixes() {
 Set prefixes = java.util.Collections.singleton("test");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testToString() {
 assertEquals("--testOption=ARG[,ARG...]", option.toString());
 }

 @Test
 public void testFindOption() {
 Option testOption = option;
 Option testOption2 = new OptionBuilder()
 .withDescription("Another test option")
 .withLongOpt("anotherTestOption")
 .create();
 commandLine.addOption(testOption);
 commandLine.addOption(testOption2);
 Option foundOption = option.findOption((Iterator<Option>) commandLine.getOptions().iterator());
 assertEquals(option, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertEquals("", commandLine.getValueForOption("testOption"));
 }

 @Test
 public void testEquals() {
 Option option1 = new OptionBuilder()
 .withDescription("A test option")
 .withLongOpt("testOption")
 .create();
 Option option2 = new OptionBuilder()
 .withDescription("A test option")
 .withLongOpt("testOption")
 .create();
 assertTrue(option1.equals(option2));
 }

 @Test
 public void testHashCode() {
 Option option1 = new OptionBuilder()
 .withDescription("A test option")
 .withLongOpt("testOption")
 .create();
 Option option2 = new OptionBuilder()
 .withDescription("A test option")
 .withLongOpt("testOption")
 .create();
 assertEquals(option1.hashCode(), option2.hashCode());
 }

 @Test
 public void testFindOptionNull() {
 Set<Option> options = java.util.Arrays.asList(option, new OptionBuilder()
 .withDescription("Another test option")
 .withLongOpt("anotherTestOption")
 .create());
 Iterator<Option> optionIterator = options.iterator();
 Option foundOption = option.findOption((Iterator<Option>) optionIterator);
 assertNull(foundOption);
 }

 @Test
 public void testDefaultParser() {
 commandLine.parse(parser, new String[]{"--testOption=value1,value2"});
 assertEquals("value1,value2", commandLine.getValueForOption("testOption"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefaultParserWithNoValue() {
 commandLine.parse(parser, new String[]{"--testOption"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultParserWithInvalidValue() {
 commandLine.parse(parser, new String[]{"--testOption=arg"});
 }

 @Test
 public void testDefaultParserWithMultipleValues() {
 commandLine.parse(parser, new String[]{"--testOption=value1,value2"});assertEquals("value1,value2", commandLine.getValueForOption("testOption"));
 }

 @Test
 public void testDefaultParserWithArgument() {
 Option argOption = new OptionBuilder()
 .withDescription("An argument option")
 .withLongOpt("argOption")
 .isRequired()
 .create();
 option.addOptionArg(argOption, new ArgumentBuilder()
 .withName("argName")
 .withDescription("An argument")
 .isRequired()
 .create());
 commandLine.parse(parser, new String[]{"--testOption=value1,value2", "argName=argValue"});
 assertEquals("argValue", commandLine.getValueForName("argName"));
 }

 @Test
 public void testDefaultParserWithArgumentNoValue() {
 Option argOption = new OptionBuilder()
 .withDescription("An argument option")
 .withLongOpt("argOption")
 .isRequired()
 .create();
 option.addOptionArg(argOption, new ArgumentBuilder()
 .withName("argName")
 .withDescription("An argument")
 .isRequired()
 .create());
 commandLine.parse(parser, new String[]{"--testOption=value1,value2"});
 assertNull(commandLine.getValueForName("argName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultParserWithInvalidArgument() {
 Option argOption = new OptionBuilder()
 .withDescription("An argument option")
 .withLongOpt("argOption")
 .isRequired()
 .create();
 option.addOptionArg(argOption, new ArgumentBuilder()
 .withName("argName")
 .withDescription("An argument")
 .isRequired()
 .create());
 commandLine.parse(parser, new String[]{"--testOption=value1,value2", "argName=invalidArg"});
 }

 @Test
 public void testDefaultParserWithAllowUnboundedMultipleValues() {
 option.setArgParameter(new Option.NumberValue(true));
 commandLine.parse(parser, new String[]{"--testOption=value1,value2,value3"});
 assertEquals("value1,value2,value3", commandLine.getValueForOption("testOption"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefaultParserWithAllowUnboundedMultipleValuesNoValue() {
 option.setArgParameter(new Option.NumberValue(true));
 commandLine.parse(parser, new String[]{"--testOption"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultParserWithAllowUnboundedMultipleValuesInvalidValue() {
 option.setArgParameter(new Option.NumberValue(true));
 commandLine.parse(parser, new String[]{"--testOption=arg"});
 }
}