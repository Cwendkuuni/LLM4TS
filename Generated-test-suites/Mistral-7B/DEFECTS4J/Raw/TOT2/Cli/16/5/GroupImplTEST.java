package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

 // Test for constructor with options, name, description, minimum, maximum
 @Test
 public void testGroupImplConstructionWithOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2"),
 new Option("o3", "Option 3", false, "d3"),
 new Option("a1", "Anonymous Argument 1", true, "a1")
 );

 GroupImpl group = new GroupImpl(options, "Group", "A group", 1, 3);
 assertEquals("Group", group.getName());
 assertEquals("A group", group.getDescription());
 assertEquals(1, group.getMinimum());
 assertEquals(3, group.getMaximum());
 assertEquals(4, group.getOptions().size());
 assertEquals(1, group.getAnonymous().size());
 }

 // Test for canProcess method with valid options
 @Test
 public void testCanProcessWithValidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);

 assertTrue(group.canProcess(cmd, "o1"));
 assertTrue(group.canProcess(cmd, "o2"));
 }

 // Test for canProcess method with invalid options
 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);

 assertFalse(group.canProcess(cmd, "invalidOption"));
 }

 // Test for process method with valid options
 @Test
 public void testProcessWithValidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);

 group.process(cmd, cmd.listIterator());

 assertEquals("o1", cmd.getOptionValue("o1"));
 assertNull(cmd.getOptionValue("o2"));
 }

 // Test for process method with invalid options
 @Test(expected = OptionException.class)
 public void testProcessWithInvalidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);

 group.process(cmd, cmd.listIterator());
 cmd.addOption("invalidOption", true);

 assertFalse(cmd.hasOption("o2"));
 assertFalse(cmd.hasOption("invalidOption"));
 }

 // Test for validate method with valid options
 @Test
 public void testValidateWithValidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);

 group.validate(cmd);
 }

 // Test for validate method with invalid options - missing options
 @Test(expected = OptionException.class)
 public void testValidateWithInvalidOptionsMissing() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);
 cmd.removeOption("o1");

 group.validate(cmd);
 }

 // Test for validate method with invalid options - extra options
 @Test(expected = OptionException.class)
 public void testValidateWithInvalidOptionsExtra() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 CommandLine cmd = new CommandLine();
 cmd.parse(group);
 cmd.addOption("o3", true, "d3");

 group.validate(cmd);
 }

 // Test for appendUsage method with valid options
 @Test
 public void testAppendUsageWithValidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> settings = new HashSet<>();
 settings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

 group.appendUsage(buffer, settings, null);

 String expectedUsage = "Group:\n" +
 "  Option 1 <o1> -d1 (required)\n" +
 "  Option 2\n";
 assertEquals(expectedUsage, buffer.toString());
 }

 // Test for appendUsage method with invalid options - missing options
 @Test
 public void testAppendUsageWithInvalidOptionsMissing() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> settings = new HashSet<>();
 settings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

 group.appendUsage(buffer, settings, null);
 cmd.removeOption("o1");

 String expectedUsage = "Group:\n";
 assertEquals(expectedUsage, buffer.toString());
 }

 // Test for appendUsage method with invalid options - extra options
 @Test
 public void testAppendUsageWithInvalidOptionsExtra() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> settings = new HashSet<>();
 settings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

 group.appendUsage(buffer, settings, null);
 cmd.addOption("o3", true, "d3");

 String expectedUsage = "Group:\n" +
 "  Option 1 <o1> -d1 (required)\n" +
 "  Option 2\n" +
 "  Option 3 -d3\n";
 assertEquals(expectedUsage, buffer.toString());
 }

 // Test for helpLines method with valid options
 @Test
 public void testHelpLinesWithValidOptions() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 List<HelpLine> helpLines = group.helpLines(0, null, null);
 String expectedHelpText = "Group:\n" +
 "  Option 1 <o1> -d1 (required)\n" +
 "  Option 1: An option\n" +
 "  Option 2: An option\n";

 for (HelpLine helpLine : helpLines) {
 assertEquals(expectedHelpText, helpLine.getHelpLine());
 }
 }

 // Test for helpLines method with invalid options - missing options
 @Test
 public void testHelpLinesWithInvalidOptionsMissing() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);

 List<HelpLine> helpLines = group.helpLines(0, null, null);
 String expectedHelpText = "Group:\n";

 for (HelpLine helpLine : helpLines) {
 assertEquals(expectedHelpText, helpLine.getHelpLine());
 }
 }

 // Test for helpLines method with invalid options - extra options
 @Test
 public void testHelpLinesWithInvalidOptionsExtra() {
 List<Option> options = Arrays.asList(
 new Option("o1", "Option 1", true, "d1"),
 new Option("o2", "Option 2", false, "d2")
 );
 GroupImpl group = new GroupImpl(options, null, null, 1, 2);
 cmd.addOption("o3", true, "d3");

 List<HelpLine> helpLines = group.helpLines(0, null, null);
 String expectedHelpText = "Group:\n" +
 "  Option 1 <o1> -d1 (required)\n" +
 "  Option 2\n" +
 "  Option 3 -d3\n" +
 "  Option 3: An option\n";

 for (HelpLine helpLine : helpLines) {
 assertEquals(expectedHelpText, helpLine.getHelpLine());
 }
 }
}