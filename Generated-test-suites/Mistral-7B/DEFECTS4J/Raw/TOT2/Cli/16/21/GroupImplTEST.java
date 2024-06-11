import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

 // Test method for constructor GroupImpl(List options, String name, String description, int minimum, int maximum)
 @Test
 public void testGroupImplConstructor() {
 List options = new ArrayList();
 options.add(OptionBuilder.withName("option1")
 .withDescription("Option 1 description")
 .create());
 options.add(OptionBuilder.withName("option2")
 .withDescription("Option 2 description")
 .create());
 GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 2);
 assertEquals("groupName", group.getName());
 assertNotNull(group.getDescription());
 assertEquals(2, group.getMaximum());
 assertTrue(group.isRequired());
 }

 // Test method for method canProcess(WriteableCommandLine commandLine, String arg)
 @Test
 public void testCanProcess() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 1);
 CommandLine commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--option", "value"});
 assertTrue(group.canProcess(commandLine, "--option"));

 commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--anotherOption", "--option", "value"});
 assertFalse(group.canProcess(commandLine, "--option"));

 commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--option"});
 assertFalse(group.canProcess(commandLine, "--option"));
 }

 // Test method for method process(WriteableCommandLine commandLine, ListIterator arguments)
 @Test
 public void testProcess() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 1);
 CommandLine commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--option"});
 List<String> arguments = new ArrayList<String>();
 arguments.add("value1");
 arguments.add("value2");
 group.process(commandLine, arguments.listIterator());
 assertEquals("value1", commandLine.getValueForOption("option"));
 assertEquals("value2", commandLine.getValueForOption("option", 1));
 }

 // Test method for method validate(WriteableCommandLine commandLine)
 @Test(expected = OptionException.class)
 public void testValidateMinimum() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 0);
 CommandLine commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--option", "--option"});
 group.validate(commandLine);
 fail("OptionException should have been thrown");
 }

 @Test(expected = OptionException.class)
 public void testValidateMaximum() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 2);
 CommandLine commandLine = TestUtils.createCommandLineWithParsedArgs(new String[]{"--option", "--option", "--option"});
 group.validate(commandLine);
 fail("OptionException should have been thrown");
 }

 // Test method for method appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp)
 @Test
 public void testAppendUsage() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 1);
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> helpSettings = new HashSet<DisplaySetting>();
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
 group.appendUsage(buffer, helpSettings, null);
 String expectedUsage = "groupName (an optional group)\n" +
 "option\n" +
 "  Option 1 description\n" +
 "  an argument for option (required)\n" +
 "  an optional second argument for option\n";
 assertEquals(expectedUsage, buffer.toString());
 }

 // Test method for method helpLines(int depth, Set helpSettings, Comparator comp)
 @Test
 public void testHelpLines() {
 GroupImpl group = new GroupImpl(Collections.singletonList(OptionBuilder.withName("option").create()), null, null, 1, 1);
 List<HelpLine> helpLines = group.helpLines(1, null, null);
 String expectedUsage = "groupName:\n" +
 "  an optional group\n" +
 "Options:\n" +
 "  option\n" +
 "    Option 1 description\n" +
 "      an argument for option (required)\n" +
 "      an optional second argument for option\n";
 String expectedHelp = "groupName:\n" +
 "  an optional group\n" +
 "Options:\n" +
 "  option\n" +
 "    Option 1 description\n";
 for (HelpLine helpLine : helpLines) {
 assertEquals(expectedHelp, helpLine.getShortOptionString());
 assertEquals(expectedUsage, helpLine.getLongOptionString());
 }
 }
}