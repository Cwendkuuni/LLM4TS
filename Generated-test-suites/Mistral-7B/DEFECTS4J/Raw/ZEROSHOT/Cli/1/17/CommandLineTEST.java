package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for CommandLine class.
 */
public class CommandLineTest {

 @Test
 public void testCreateCommandLine() {
 CommandLine commandLine = new CommandLine();
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 options.addOption(true, "test", "t");
 commandLine.parse(options.parse(new String[]{"-t"}));

 assertTrue(commandLine.hasOption("t"));
 assertTrue(commandLine.hasOption('t'));
 assertFalse(commandLine.hasOption("nonexistent"));
 assertFalse(commandLine.hasOption('n'));
 }

 @Test
 public void testHasOptionWithValue() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 options.addOption(true, "test", "t", "value");
 commandLine.parse(options.parse(new String[]{"-t", "someValue"}));

 assertTrue(commandLine.hasOption("t"));
 assertTrue(commandLine.hasOption('t'));
 assertTrue(commandLine.hasOptionValue("t").equals("someValue"));
 }

 @Test(expected = ParseException.class)
 public void testNoSuchOption() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 commandLine.parse(options.parse(new String[]{}));
 commandLine.parse(options.parse(new String[]{"--nonexistent"}));
 }

 @Test
 public void testGetOptionObject() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 options.addOption(true, "test", "t");
 commandLine.parse(options.parse(new String[]{"-t"}));

 assertNull(commandLine.getOptionObject("nonexistent"));
 assertNull(commandLine.getOptionObject('n'));
 assertNull(commandLine.getOptionObject("t")); // boolean

 options.addOption(null, "test", "t", "testOption");
 commandLine.parse(options.parse(new String[]{"-t", "someValue"}));
 Object option = commandLine.getOptionObject("t");
 assertSame(option.getClass(), Option.class);
 assertTrue(((Option) option).getValue().equals("someValue"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 options.addOption(true, "test", "t");
 commandLine.parse(options.parse(new String[]{}));
 assertNull(commandLine.getOptionValue("nonexistent"));
 assertNull(commandLine.getOptionValue('n'));
 assertNull(commandLine.getOptionValue("t")); // boolean

 options.addOption(true, "test", "t");
 commandLine.parse(options.parse(new String[]{"-t", "someValue"}));
 assertSame(commandLine.getOptionValue("t"), "someValue");
 assertSame(commandLine.getOptionValue('t'), "someValue");
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 options.addOption(true, "test", "t");
 commandLine.parse(options.parse(new String[]{"-t", "value1", "--test=value2"}));
 String[] values = commandLine.getOptionValues("t");
 assertSame(values.length, 2);
 assertSame(values[0], "value1");
 assertSame(values[1], "value2");
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 commandLine.parse(options.parse(new String[]{"test1", "test2", "--option=value"}));
 String[] args = commandLine.getArgs();
 assertSame(args.length, 1);
 assertSame(args[0], "test1");
 }

 @Test
 public void testGetArgList() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 commandLine.parse(options.parse(new String[]{"test1", "test2", "--option=value"}));
 List argList = commandLine.getArgList();
 assertSame(argList.size(), 2);
 assertTrue(argList.contains("test1"));
 assertTrue(argList.contains("test2"));
 }

 @Test
 public void testToString() {
 CommandLine commandLine = new CommandLine();

 Options options = new Options();
 commandLine.parse(options.parse(new String[]{"--test", "value1", "--option=value2"}));
 assertTrue(commandLine.toString().contains("value1"));
 assertTrue(commandLine.toString().contains("value2"));
 }

 @Test
 public void testAddArg() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new Options().parse(new String[0]));
 commandLine.addArg("testArg");
 List args = commandLine.getArgList();
 assertSame(args.size(), 1);
 assertTrue(args.contains("testArg"));
 }

 @Test
 public void testAddOption() {
 CommandLine commandLine = new CommandLine();
 CommandLine result = null;

 Options options = new Options();
 options.addOption(true, "test", "t");
 options.addOption(null, "testArg", true, "testArgValue");
 result = new CommandLine();
 result.parse(options.parse(new String[]{"-t", "--testArg=testArgValue"}));
 commandLine.parse(options.parse(new String[0]));
 commandLine.addOption(new Option("testArg", true, "targ", "testArgValue"));
 Option addedOption = commandLine.getOption("testArg");
 assertSame(addedOption, result.getOption("testArg"));
 }

 @Test
 public void testIterator() {
 CommandLine commandLine = new CommandLine();
 CommandLine result = null;

 Options options = new Options();
 options.addOption(true, "test1", "t1");
 options.addOption(true, "test2", "t2");
 result = new CommandLine();
 result.parse(options.parse(new String[]{"-t1", "--test2"}));
 commandLine.parse(options.parse(new String[0]));
 commandLine.addOption(new Option("test1", true, "t1", "test1Value"));
 commandLine.addOption(new Option("test2", true, "t2", "test2Value"));
 Iterator it = commandLine.iterator();
 Option option = null;
 while (it.hasNext()) {
 option = (Option) it.next();
 assertSame(option.getClass(), ResultSetConsumer.class); // result is not an Option but make assertion more readable
 assertSame(option, result.getOption("test1"));
 }
 }

 @Test
 public void testGetOptions() {
 CommandLine commandLine = new CommandLine();
 CommandLine result = null;

 Options options = new Options();
 options.addOption(true, "test1", "t1");
 options.addOption(true, "test2", "t2");
 result = new CommandLine();
 result.parse(options.parse(new String[]{"-t1", "--test2"}));
 CommandLine parsedCommandLine = commandLine.parse(options.parse(new String[0]));
 Option[] optionsArray = parsedCommandLine.getOptions();
 Option[] resultOptions = result.getOptions();
 assertSame(optionsArray.length, resultOptions.length);
 for (int i = 0; i < optionsArray.length; i++) {
 assertSame(optionsArray[i], resultOptions[i]);
 }
 }
}