import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class GroupImplTest {

 @Before
 public void setUp() {
 // create a test command line
 List<Option> options = new ArrayList<>();
 options.add(Option.builder("a").longOpt("optionA").build());
 options.add(Option.builder("b").longOpt("optionB").build());
 options.add(Option.builder("c").longOpt("optionC").build());
 Group group = new GroupImpl(options, "My Group", "This is my group");
 CommandLine commandLine = new DefaultParser().parse(group.parser(), new String[]{"--optionA", "--optionB"});
 this.commandLine = commandLine;
 }

 @Test
 public void testCanProcessWithValidArguments() {
 // test that the group can process valid arguments
 Group group = new GroupImpl(this.commandLine.getOptions(), "My Group", "This is my group");
 assertTrue(group.canProcess(commandLine, "--optionA"));
 assertTrue(group.canProcess(commandLine, "--optionB"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidArgument() {
 // test that the group can't process an invalid argument
 Group group = new GroupImpl(this.commandLine.getOptions(), "My Group", "This is my group");
 assertFalse(group.canProcess(commandLine, "invalidArg"));
 }

 @Test
 public void testValidateWithValidArguments() throws OptionException {
 // test that the group validates with valid arguments
 Group group = new GroupImpl(this.commandLine.getOptions(), "My Group", "This is my group");
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidArguments() throws OptionException {
 // test that the group validates an invalid argument
 List<Option> options = new ArrayList<>();
 options.add(Option.builder("a").longOpt("optionA").build());
 Group group = new GroupImpl(options, "My Group", "This is my group");
 commandLine.addOption("--invalidOption");
 group.validate(commandLine);
 }

 @Test
 public void testProcess() throws OptionException {
 // test that the group processes the correct arguments
 Group group = new GroupImpl(this.commandLine.getOptions(), "My Group", "This is my group");
 group.process(commandLine, commandLine.listIterator());
 assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAppendUsage() {
 // test that the group appends the correct usage information
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> helpSettings = new HashSet<>(Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME));
 group.appendUsage(buffer, helpSettings, null);
 String usage = buffer.toString();
 assertTrue(usage.contains("My Group"));
 assertTrue(usage.contains("This is my group"));
 }

 @Test
 public void testHelpLines() {
 // test that the group returns the correct help lines
 HelpFormatter formatter = new HelpFormatter();
 List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
 formatter.printHelp(System.out, group.parser().getDescription(), helpLines);
 }

 @Test
 public void testGetOptions() {
 // test that the group returns the correct options
 List<Option> options = group.getOptions();
 assertEquals(2, options.size());
 assertTrue(options.get(0) instanceof Option);
 assertTrue(options.get(1) instanceof Group);
 }

 @Test
 public void testFindOption() {
 // test that the group can find the correct option by its trigger
 Option optionA = group.findOption("optionA");
 assertEquals("optionA", optionA.getLongOpt());
 }

 @Test
 public void testGetMinimum() {
 // test that the group returns the correct minimum number of arguments
 assertEquals(0, group.getMinimum());
 }

 @Test
 public void testIsRequired() {
 // test that the group is not required
 assertFalse(group.isRequired());
 }

 @Test
 public void testGetMaximum() {
 // test that the group has no maximum number of arguments
 assertEquals(Integer.MAX_VALUE, group.getMaximum());
 }
}