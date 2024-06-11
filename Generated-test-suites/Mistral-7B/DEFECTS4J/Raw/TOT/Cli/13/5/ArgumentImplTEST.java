package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ArgumentImplTest {

 private Argument argument;
 private HelpFormatter formatter;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 argument = new ArgumentImpl("argName", "description", 1, 1, '--', ' ', null, null, null, 0);
 formatter = new HelpFormatter();
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testValidArgument() {
 argument.process(commandLine, commandLine.parser().parse("argName value"));
 assertEquals(1, commandLine.getValues(argument).size());
 assertEquals("value", commandLine.getValues(argument).get(0));
 }

 @Test
 public void testMultipleValidArguments() {
 argument.process(commandLine, commandLine.parser().parse("argName value1 value2"));
 assertEquals(2, commandLine.getValues(argument).size());
 assertEquals("value1", commandLine.getValues(argument).get(0));
 assertEquals("value2", commandLine.getValues(argument).get(1));
 }

 @Test
 public void testConsumesRemainingArguments() {
 argument = new ArgumentImpl("argName", "description", 1, Integer.MAX_VALUE, '--', ' ', null, "--", null, 0);
 argument.process(commandLine, commandLine.parser().parse("argName arg1 arg2 arg3"));
 assertEquals(3, commandLine.getValues(argument).size());
 assertEquals("arg1", commandLine.getValues(argument).get(0));
 assertEquals("arg2", commandLine.getValues(argument).get(1));
 assertEquals("arg3", commandLine.getValues(argument).get(2));
 }

 @Test
 public void testEmptyArgument() {
 argument = new ArgumentImpl("argName", "description", 0, 1, '--', ' ', null, null, null, 0);
 argument.process(commandLine, commandLine.parser().parse("argName"));
 assertNull(commandLine.getValues(argument));
 }

 @Test
 public void testMissingArgument() {
 argument = new ArgumentImpl("argName", "description", 1, 1, '--', ' ', null, null, null, 0);
 assertThrows(OptionException.class, () -> argument.process(commandLine, commandLine.parser().parse("")));
 }

 @Test
 public void testInvalidArgument() {
 argument = new ArgumentImpl("argName", "description", 1, 1, '--', ' ', null, null, null, 0);
 assertThrows(OptionException.class, () -> argument.process(commandLine, commandLine.parser().parse("argName invalidValue")));
 }

 @Test
 public void testHelp() {
 final String help = formatter.printHelp("test", argument, true);
 assertNotNull(help);
 }
}