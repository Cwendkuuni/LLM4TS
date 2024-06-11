import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new DefaultWriteableCommandLine();
 option = new MockOption();
 }

 @Test
 public void testProcess() {
 option.process(commandLine, commandLine.iterator());
 assertTrue(commandLine.hasOption("mock"));
 }

 @Test
 public void testProcessWithInvalidArgument() {
 commandLine.setArgs(new String[]{"--mock", "invalid"});
 assertThrows(OptionException.class, () -> option.process(commandLine, commandLine.iterator()));
 }

 @Test
 public void testProcessWithMissingArgument() {
 commandLine.setArgs(new String[]{"--mock"});
 assertFalse(option.process(commandLine, commandLine.iterator()));
 assertNull(commandLine.getOptionValue("mock"));
 }

 @Test
 public void testProcessWithValidArgument() {
 commandLine.setArgs(new String[]{"--mock", "value"});
 assertTrue(option.process(commandLine, commandLine.iterator()));
 assertEquals("value", commandLine.getOptionValue("mock"));
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("mock"));
 }

 @Test
 public void testCanProcess() {
 assertTrue(option.canProcess(commandLine, "--mock"));
 assertTrue(option.canProcess(commandLine, "--mock value"));
 assertFalse(option.canProcess(commandLine, "--unknown"));
 }

 @Test
 public void testGetTriggers() {
 Set triggers = option.getTriggers();
 assertNotNull(triggers);
 assertTrue(triggers.contains("--mock"));
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertTrue(prefixes.contains("--"));
 }

 @Test
 public void testValidate() {
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithMissingOption() {
 commandLine.setArgs(new String[0]);
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidArgument() {
 commandLine.setArgs(new String[]{"--mock", "invalid"});
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 List helpLines = option.helpLines(1, Set.of(), null);
 assertNotNull(helpLines);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, Set.of(), null);
 assertNotNull(buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 String preferredName = option.getPreferredName();
 assertEquals("mock", preferredName);
 }

 @Test
 public void testGetDescription() {
 String description = option.getDescription();
 assertNotNull(description);
 }

 @Test
 public void testgetId() {
 int id = option.getId();
 assertEquals(1, id);
 }

 @Test
 public void testFindOption() {
 Option foundOption = option.findOption("mock");
 assertNotNull(foundOption);
 assertEquals(option, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertFalse(option.isRequired());
 }

 @Test
 public void testHasParent() {
 Option parentOption = new MockParentOption();
 option.setParent(parentOption);
 Option childOption = new MockChildOption();
 parentOption.addOption(childOption);
 Option foundOption = parentOption.findOption("child");
 assertNotNull(foundOption);
 assertEquals(childOption, foundOption);
 }
}