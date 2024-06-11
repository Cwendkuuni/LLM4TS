import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

 private Options options;
 private GroupImpl group;

 @Before
 public void setup() {
 // create a list of options
 List<Option> optionsList = new ArrayList<>();
 optionsList.add(new Option("a", "optionA", false, "Option A"));
 optionsList.add(new Option("b", "optionB", true, "Option B"));
 optionsList.add(new Option("c", "optionC", true, "Option C", 1, Integer.class, "number"));
 optionsList.add(new Option("d", "optionD", false, "Option D"));

 // create the group
 group = new GroupImpl(optionsList, "MyGroup", "My Group", 1, 3);
 options = new Options();
 options.addOptionGroup(group);
 }

 @Test
 public void testCanProcess_ValidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--number=2"});
 assertTrue(group.canProcess(commandLine, "--number"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcess_InvalidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--invalid"});
 assertFalse(group.canProcess(commandLine, "--invalid"));
 }

 @Test
 public void testCanProcess_BurstingOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--number", "--number"});
 assertTrue(group.canProcess(commandLine, "--number"));
 }

 @Test
 public void testCanProcess_CommandLineWithAnonymous() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--anonymous"});
 assertTrue(group.canProcess(commandLine, "--anonymous"));
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = group.getPrefixes();
 assertNotNull(prefixes);
 assertTrue(prefixes.isEmpty());
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = group.getTriggers();
 assertNotNull(triggers);
 assertEquals(3, triggers.size());
 assertTrue(triggers.contains("number"));
 }

 @Test
 public void testProcess_ValidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--number=2"});
 group.process(commandLine, commandLine.iterator());
 assertEquals(2, group.getMinimum());
 assertEquals(3, group.getMaximum());
 assertEquals(3, group.getOptions().size());
 assertEquals(1, group.getAnonymous().size());
 }

 @Test(expected = OptionException.class)
 public void testProcess_InvalidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--invalid"});
 group.process(commandLine, commandLine.iterator());
 fail("Should throw OptionException");
 }

 @Test
 public void testProcess_CommandLineWithAnonymous() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--anonymous"});
 group.process(commandLine, commandLine.iterator());
 assertTrue(group.getAnonymous().get(0).getValue().isSet());
 }

 @Test
 public void testValidate_Minimum() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a"});
 try {
 group.validate(commandLine);
 fail("Should throw OptionException with MISSING_OPTION");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testValidate_Maximum() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--number=5"});
 try {
 group.validate(commandLine);
 fail("Should throw OptionException with UNEXPECTED_TOKEN");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testValidate_Valid() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options, new String[]{"-a", "-b", "--number=3"});
 group.validate(commandLine);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 group.appendUsage(buffer, null, null);
 String usage = buffer.toString();
 String expected = "My Group:\n" +
 "  Option A\n" +
 "      This is option A\n" +
 "  Option B\n" +
 "      This is option B\n" +
 "  Option C\n" +
 "      -c NUMBER: number (required)\n" +
 "          This is option C\n" +
 "  Option D\n" +
 "      This is option D\n" +
 "  Anonymous Arguments:\n" +
 "      none\n";
 assertEquals(expected, usage);
 }

 @Test
 public void testHelpLines() {
 List<HelpLine> helpLines = group.helpLines(0, null, null);
 assertEquals(5, helpLines.size());
 }
}