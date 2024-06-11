package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private Argument arg;
 private WriteableCommandLine cmdLine;

 @Before
 public void setup() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine = new WriteableCommandLine();
 }

 @Test
 public void testProcessValues() {
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 assertEquals(1, cmdLine.getValues(arg).size());
 assertEquals("expectedValue", cmdLine.getValues(arg).get(0));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesInvalidArgument() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName"); // consume remaining
 cmdLine.getArgs().add("--anotherOption");
 cmdLine.getArgs().add("expectedValue");
 cmdLine.getArgs().add("anotherValue");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 fail("Expected OptionException");
 }

 @Test
 public void testProcessValuesMultipleValues() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName expectedValue1 expectedValue2");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 assertEquals(2, cmdLine.getValues(arg).size());
 assertEquals("expectedValue1", cmdLine.getValues(arg).get(0));
 assertEquals("expectedValue2", cmdLine.getValues(arg).get(1));
 }

 @Test
 public void testProcessValuesInvalidSeparator() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, 'x', null, null, null, 0);
 cmdLine.getArgs().add("--argName value1 x value2");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 fail("Expected OptionException");
 }

 @Test
 public void testProcessValuesMinimum() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName value1");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 assertEquals(1, cmdLine.getValues(arg).size());
 assertEquals("value1", cmdLine.getValues(arg).get(0));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesMinimumNotMet() {
 arg = new ArgumentImpl("argName", "Argument description", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName value1");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 fail("Expected OptionException");
 }

 @Test
 public void testProcessValuesMaximum() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName value1 value2 value3");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 assertEquals(3, cmdLine.getValues(arg).size());
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesMaximumExceeded() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 cmdLine.getArgs().add("--argName value1 value2 value3 value4");
 arg.processValues(cmdLine, cmdLine.getArgs().listIterator(), arg);
 fail("Expected OptionException");
 }
}