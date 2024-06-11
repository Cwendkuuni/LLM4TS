package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupImplTest {

 @BeforeClass
 public static void initialize() {
 List options = new ArrayList();
 options.add(OptionBuilder.withLongOpt("option1")
 .withDescription("description for option1")
 .withValueSeparator('=')
 .create());
 options.add(OptionBuilder.withLongOpt("option2")
 .withDescription("description for option2")
 .withArgs(1)
 .create());
 options.add(OptionBuilder.withLongOpt("option3")
 .withDescription("description for option3")
 .create());
 options.add(OptionBuilder.withLongOpt("group")
 .withDescription("description for group")
 .withOptionalArg(true)
 .withArgs(1)
 .withValuesSeparator(':')
 .withMinValues(1)
 .withMaxValues(3)
 .create());

 GroupImpl group = new GroupImpl(options, "group", "description for group", 1, 3);
 }

 @Rule
 public ExpectedException exception = ExpectedException.none();

 @Test
 public void testCanProcess_ValidGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option2=2"});
 assertTrue(group.canProcess(commandLine, "option1"));
 assertTrue(group.canProcess(commandLine, "option2"));
 }

 @Test
 public void testCanProcess_InvalidGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "invalidOption"});
 assertFalse(group.canProcess(commandLine, "invalidOption"));
 }

 @Test
 public void testCanProcess_BurstingGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option1=2", "option1=3"});
 assertTrue(group.canProcess(commandLine, "option1"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcess_InvalidBurstingGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option1=2", "option3"});
 }

 @Test
 public void testCanProcess_AnonymousArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "arg"});
 Option option1 = group.findOption("option1");
 Argument arg = (Argument) option1.getValues().get(0);
 assertNotNull(arg);
 assertEquals("arg", arg.getValue());
 }

 @Test
 public void testCanProcess_ArgumentWithValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "option1=value"});
 Option option1 = group.findOption("option1");
 assertNotNull(option1);
 assertEquals("value", option1.getValue());
 }

 @Test
 public void testCanProcess_MultipleArguments() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "option1=value1", "option1=value2"});
 Option option1 = group.findOption("option1");
 List values = option1.getValues();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test(expected = OptionException.class)
 public void testCanProcess_InvalidArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "invalidArg"});
 }

 @Test
 public void testProcess_ValidGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option2=2"});
 GroupImpl group = new GroupImpl(group.getOptions(), null, null, 1, 3);
 group.process(commandLine, commandLine.iterator());
 Option option1 = group.findOption("option1");
 Argument arg = (Argument) option1.getValues().get(0);
 assertEquals("a", arg.getValue());
 Option option2 = group.findOption("option2");
 assertEquals("2", option2.getValue());
 }

 @Test
 public void testProcess_BurstingGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option1=2", "option1=3"});
 GroupImpl group = new GroupImpl(group.getOptions(), null, null, 1, 3);
 group.process(commandLine, commandLine.iterator());
 Option option1 = group.findOption("option1");
 Argument arg = (Argument) option1.getValues().get(0);
 assertEquals("a", arg.getValue());
 Option option1Value1 = group.findOption("option1");
 Argument arg1 = (Argument) option1Value1.getValues().get(0);
 assertEquals("1", arg1.getValue());
 Option option1Value2 = group.findOption("option1");
 Argument arg2 = (Argument) option1Value2.getValues().get(1);
 assertEquals("2", arg2.getValue());
 Option option1Value3 = group.findOption("option1");
 Argument arg3 = (Argument) option1Value3.getValues().get(2);
 assertEquals("3", arg3.getValue());
 }

 @Test(expected = OptionException.class)
 public void testProcess_InvalidBurstingGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option1=2", "option3"});
 GroupImpl group = new GroupImpl(group.getOptions(), null, null, 1, 3);
 group.process(commandLine, commandLine.iterator());
 }

 @Test(expected = OptionException.class)
 public void testProcess_InvalidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "invalidOption"});
 GroupImpl group = new GroupImpl(group.getOptions(), null, null, 1, 3);
 group.process(commandLine, commandLine.iterator());
 }

 @Test(expected = OptionException.class)
 public void testProcess_InvalidArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "invalidArg"});
 GroupImpl group = new GroupImpl(group.getOptions(), null, null, 1, 3);
 group.process(commandLine, commandLine.iterator());
 }

 @Test
 public void testValidate_ValidGroup() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option2=2"});
 try {
 group.validate(commandLine);
 } catch (OptionException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test(expected = OptionException.class)
 public void testValidate_MissingOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[0]);
 }

 @Test(expected = OptionException.class)
 public void testValidate_TooManyOptions() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "a", "option1=1", "option1=2", "option1=3", "option1=4"});
 }

 @Test
 public void testValidate_OptionWithValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "option1=1"});
 try {
 group.validate(commandLine);
 } catch (OptionException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test(expected = OptionException.class)
 public void testValidate_MissingArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group"});
 }

 @Test(expected = OptionException.class)
 public void testValidate_InvalidArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "invalidArg"});
 }

 @Test(expected = OptionException.class)
 public void testValidate_TooManyArguments() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(group.getOptions().toArray(new Option[0]), new String[]{"group", "option1=1", "option1=2", "option1=3", "option1=4"});
 }

 @Test
 public void testHelpLines() {
 HelpFormatter formatter = new HelpFormatter();
 List<String> lines = group.helpLines(0, Collections.emptyMap(), null);
 Map<String, String> expectedLines = new HashMap();
 expectedLines.put("", "usage: GroupImpl[-group <description for group>] [options]");
 expectedLines.put("options:", "  -option1 <description for option1> (=value) ...\n");
 expectedLines.put("  -option2 <description for option2>\n");
 expectedLines.put("  -group <description for group>:<value1> [<value2> ...]\n");
 assertEquals(expectedLines, lines);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 group.appendUsage(buffer, Collections.emptySet(), null);
 String expectedUsage = "|group| (description for group)\n" +
 "  options:\n" +
 "    |option1| (=value) ...\n" +
 "    |option2|\n" +
 "  group:<value1> [<value2> ...]\n";
 assertEquals(expectedUsage, buffer.toString());
 }
}