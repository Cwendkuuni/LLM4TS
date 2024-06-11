import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(TestUtils.JUnit4TestRunner.class)
public class GroupImplTest {

 private GroupImpl group;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();

 List<Option> optionsList = Arrays.asList(
 new Option("g", "group", true, "group name"),
 new Option("a", "arg", false, "anonymous argument"),
 new Option("o1", "opt1", true, "option1"),
 new Option("o2", "opt2", false, "option2"),
 new Argument("arg1", "argument1")
 );

 Option groupOption = new Option("g", "group", true, "a group option")
 .required(false)
 .args(optionsList);

 group = new GroupImpl(optionsList, "group", "A group", 1, 3, Collections.singletonList(groupOption), null, null);
 }

 @Test
 public void testCanProcess() {
 CommandLine commandLine = TestUtils.createTestCommandLine();

 // test canProcess with valid option
 commandLine.addOption("o1");
 assertTrue(group.canProcess(commandLine, "o1"));

 // test canProcess with anonymous argument
 commandLine.addArgument("arg1");
 assertTrue(group.canProcess(commandLine, "arg1"));

 // test canProcess with invalid option
 assertFalse(group.canProcess(commandLine, "invalid"));

 // test canProcess with bursting option
 commandLine.addOption("o1");
 commandLine.addOption("o1");
 commandLine.addOption("o2");
 assertTrue(group.canProcess(commandLine, "o1"));
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = group.getPrefixes();
 assertNotNull(prefixes);
 assertEquals(0, prefixes.size());
 }

 @Test
 public void testGetTriggers() {
 Set triggers = group.getTriggers();
 assertNotNull(triggers);
 assertEquals(1, triggers.size());
 assertEquals("g", triggers.iterator().next());
 }

 @Test
 public void testProcess() {
 CommandLine commandLine = TestUtils.createTestCommandLine();
 commandLine.addOption("g");
 commandLine.addOption("o1");
 commandLine.addOption("o1");
 commandLine.addOption("o2");
 commandLine.addArgument("arg1");

 group.process(commandLine, commandLine.listIterator());

 assertEquals(2, commandLine.getOptions().size());
 assertEquals("opt1", commandLine.getOption("o1").getLongOpt());
 assertEquals("opt2", commandLine.getOption("o2").getLongOpt());

 List<Option> groupOptions = group.getOptions();
 assertEquals(3, groupOptions.size());
 assertEquals("group", groupOptions.get(0).getLongOpt());
 assertEquals("opt1", groupOptions.get(1).getLongOpt());
 assertEquals("opt2", groupOptions.get(2).getLongOpt());
 }

 @Test
 public void testValidate() {
 CommandLine commandLine = TestUtils.createTestCommandLine();
 commandLine.addOption("g");
 commandLine.addOption("o1");
 commandLine.addOption("o1");
 commandLine.addOption("o1");
 commandLine.addOption("o1");
 commandLine.addArgument("arg1");
 commandLine.addArgument("arg1");

 group.validate(commandLine);
 assertEquals(6, commandLine.getOptions().size());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("group", group.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("A group", group.getDescription());
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 group.appendUsage(buffer, null, null);
 assertEquals("-g, --group <group>\n" +
                 "-g, --group [group]\n" +
                 "[options]\n" +
                 "[arguments]\n", buffer.toString());
 }

 @Test
 public void testHelpLines() {
 List<String> helpLines = group.helpLines(0, null, null);
 assertEquals(2, helpLines.size());
 assertEquals("-g, --group <group>\n" +
                 "[options]\n" +
                 "[arguments]\n", helpLines.get(0));
 assertEquals("-g, --group [group]\n" +
                 "[options]\n" +
                 "[arguments]\n", helpLines.get(1));
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = group.getOptions();
 assertEquals(3, optionsList.size());
 }

 @Test
 public void testGetAnonymous() {
 List<Option> anonymous = group.getAnonymous();
 assertEquals(1, anonymous.size());
 }

 @Test
 public void testFindOption() {
 Option option = group.findOption("g");
 assertNotNull(option);
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, group.getMinimum());
 }

 @Test
 public void testGetMaximum() {
 assertEquals(3, group.getMaximum());
 }

 @Test
 public void testIsRequired() {
 assertFalse(group.isRequired());
 }

 @Test
 public void testDefaults() {
 CommandLine commandLine = TestUtils.createTestCommandLine();
 group.defaults(commandLine);

 assertEquals(0, commandLine.getOptions().size());
 }
}

class ReverseStringComparator implements Comparator<String> {
 @Override
 public int compare(String o1, String o2) {
 return o2.compareTo(o1);
 }
}