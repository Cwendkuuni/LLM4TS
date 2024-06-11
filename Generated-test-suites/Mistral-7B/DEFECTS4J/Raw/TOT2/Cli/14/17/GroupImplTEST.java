import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.GroupImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

 @Test
 public void testGroupImplCreation() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 1);

 assertEquals("MyGroup", group.getPreferredName());
 assertEquals("This is my group", group.getDescription());
 assertEquals(3, group.getOptions().size());
 }

 @Test
 public void testCanProcess() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 1);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--long-option2", group);
 assertTrue(group.canProcess(commandLine, "--long-option2"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithWrongArgument() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 1);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--wrong-argument", group);
 assertFalse(group.canProcess(commandLine, "--wrong-argument"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidArgumentForOption() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 1);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--option1 option", group);
 assertFalse(group.canProcess(commandLine, "--option1 option"));
 }

 @Test
 public void testProcessWithAnonymousArgument() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 0,
 1);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--arg my-argument", group);
 group.process(commandLine, commandLine.iterator());
 assertEquals("my-argument", commandLine.getOptionValue("arg"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgumentForAnonymousArgument() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 0,
 1);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--arg invalid-argument", group);
 assertFalse(group.canProcess(commandLine, "--arg invalid-argument"));
 }

 @Test
 public void testProcessWithMultipleAnonymousArguments() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("arg1")
 .withDescription("This is argument 1")
 .isRequired(false)
 .create());
 add(OptionBuilder.withName("arg2")
 .withDescription("This is argument 2")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 0,
 Integer.MAX_VALUE);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--arg1 value1 --arg2 value2", group);
 assertEquals("value1", commandLine.getOptionValue("arg1"));
 assertEquals("value2", commandLine.getOptionValue("arg2"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgumentForOptionalOption() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("invalid-option")
 .withDescription("This is invalid option")
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 Integer.MAX_VALUE);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--invalid-option invalid-argument", group);
 assertFalse(group.canProcess(commandLine, "--invalid-option invalid-argument"));
 }

 @Test
 public void testValidate() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 0,
 Integer.MAX_VALUE);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--option1 value1 --option2 value2 --arg my-argument", group);
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithMissingOption() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 1,
 Integer.MAX_VALUE);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--option1 value1 --arg my-argument", group);
 assertFalse(group.canProcess(commandLine, "--option1 value1 --arg my-argument"));
 }

 @Test(expected = OptionException.class)
 public void testValidateWithTooManyOptions() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .withLongOpt("long-option2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group",
 2,
 Integer.MAX_VALUE);

 CommandLine commandLine = new CommandLine();
 commandLine.parse("--option1 value1 --option1 value2 --arg my-argument", group);
 assertFalse(group.canProcess(commandLine, "--option1 value1 --option1 value2 --arg my-argument"));
 }

 @Test
 public void testAppendUsage() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group");

 StringBuffer buffer = new StringBuffer();
 group.appendUsage(buffer, null, null);
 String expected = "=== MyGroup ===\n" +
 "This is my group\n\n" +
 "Option 1:\n" +
 "- option1\n" +
 "-long-option1\n" +
 "This is option 1\n\n" +
 "Option 2:\n" +
 "- option2\n" +
 "-long-option2\n" +
 "This is option 2\n\n" +
 "Arg:\n" +
 "- arg\n" +
 "This is an argument\n";
 assertEquals(expected, buffer.toString());
 }

 @Test
 public void testHelpLines() {
 GroupImpl group = new GroupImpl(
 new ArrayList<Option>() {{
 add(OptionBuilder.withName("option1")
 .withDescription("This is option 1")
 .create());
 add(OptionBuilder.withName("option2")
 .withDescription("This is option 2")
 .create());
 add(OptionBuilder.withName("arg")
 .withDescription("This is an argument")
 .isRequired(false)
 .create());
 }},
 "MyGroup",
 "This is my group");

 List<String> helpLines = group.helpLines(0, null, null);
 String expected = "=== MyGroup ===\n" +
 "This is my group\n\n" +
 "Option 1:\n" +
 "  -option1\n" +
 "  --long-option1\n" +
 "This is option 1\n\n" +
 "Option 2:\n" +
 "  -option2\n" +
 "  --long-option2\n" +
 "This is option 2\n\n" +
 "Arg:\n" +
 "  --arg ARG\n" +
 "This is an argument\n";
 assertEquals(expected, helpLines.get(0));
 }
}