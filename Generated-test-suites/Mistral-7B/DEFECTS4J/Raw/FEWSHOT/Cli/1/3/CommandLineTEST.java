package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

 CommandLine cmd = new CommandLine();

 @Test
 public void testHasOption() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption('a'));
 }

 @Test
 public void testHasOptionFalse() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 assertFalse(cmd.hasOption("b"));
 assertFalse(cmd.hasOption('b'));
 }

 @Test
 public void testGetOptionValue() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 cmd.addArg("--optionA=value");
 assertEquals("value", cmd.getOptionValue("a"));
 assertEquals("value", cmd.getOptionValue('a'));
 }

 @Test
 public void testGetOptionValueNull() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 assertNull(cmd.getOptionValue("b"));
 assertNull(cmd.getOptionValue('b'));
 }

 @Test
 public void testGetOptionValues() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 cmd.addArg("--optionA=value1 value2");
 String[] values = cmd.getOptionValues("a");
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 @Test
 public void testGetOptionValuesEmpty() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 assertNull(cmd.getOptionValues("b"));
 }

 @Test
 public void testGetOptions() {
 cmd.addOption(new Option("a", "optionA", true, "This is option A"));
 cmd.addOption(new Option("b", "optionB", true, "This is option B"));
 Option[] options = cmd.getOptions();
 assertEquals(2, options.length);
 assertEquals("optionA", options[0].getLongOpt());
 assertEquals("optionB", options[1].getLongOpt());
 }

 @Test
 public void testAddArg() {
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 String[] args = cmd.getArgs();
 assertEquals(4, args.length);
 assertEquals("arg1", args[0]);
 assertEquals("arg2", args[1]);
 assertEquals("arg3", args[2]);
 assertEquals("arg4", args[3]);
 }

 @Test
 public void testGetArgList() {
 cmd.addArg("arg1");
 cmd.addArg("arg2");
 cmd.addArg("arg3");
 cmd.addArg("arg4");
 List argList = cmd.getArgList();
 assertEquals(4, argList.size());
 assertEquals("arg1", argList.get(0));
 assertEquals("arg2", argList.get(1));
 assertEquals("arg3", argList.get(2));
 assertEquals("arg4", argList.get(3));
 }
}