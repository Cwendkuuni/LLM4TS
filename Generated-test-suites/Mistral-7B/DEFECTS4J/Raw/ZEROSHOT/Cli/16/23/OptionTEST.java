import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultCommandLine();
 option = new MockOption();
 }

 @Test
 public void testProcess() {
 option.process(commandLine, commandLine.iterator());
 assertTrue(commandLine.hasOption("testOption"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgument() throws OptionException {
 option.process(commandLine, commandLine.iterator());
 commandLine.addOption("--testOption,--invalid");
 option.process(commandLine, commandLine.iterator());
 fail("Should throw OptionException");
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertTrue(commandLine.hasOption("testOption"));
 assertTrue(commandLine.getOptionValue("testOption") != null);
 }

 @Test
 public void testCanProcessSingleArgument() {
 assertTrue(option.canProcess(commandLine, commandLine.iterator()));
 }

 @Test
 public void testCanProcessMultipleArguments() {
 List<String> args = new ArrayList(Arrays.asList("test", "Option", "--testOption", "arg1", "arg2"));
 commandLine.setArgs(args);
 Iterator<String> iterator = commandLine.iterator();
 assertTrue(option.canProcess(commandLine, iterator));
 commandLine.removeArgument();
 }

 @Test
 public void testCannotProcessInvalidArgument() {
 List<String> args = new ArrayList(Arrays.asList("test", "Option", "--testOption", "arg1", "--invalidOption"));
 commandLine.setArgs(args);
 Iterator<String> iterator = commandLine.iterator();
 assertFalse(option.canProcess(commandLine, iterator));
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 assertTrue(triggers != null);
 assertTrue(triggers.contains("testOption"));
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 assertTrue(prefixes != null);
 assertTrue(prefixes.contains("-"));
 assertTrue(prefixes.contains("--"));
 }

 @Test
 public void testValidate() throws OptionException {
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithMissingRequiredOption() throws OptionException {
 option.setRequired(true);
 commandLine.removeOption("testOption");
 option.validate(commandLine);
 fail("Should throw OptionException");
 }

 @Test
 public void testHelpLines() {
 Set<DisplaySetting> helpSettings = new HashSet<>();
 List<HelpLine> helpLines = option.helpLines(1, helpSettings, null);
 assertEquals(1, helpLines.size());
 assertEquals("testOption", helpLines.get(0).getOptionSpec().getLongOpt());
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, helpSettings, null);
 assertEquals("--testOption\n", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("testOption", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("This is a test option", option.getDescription());
 }

 @Test
 public void testGetId() {
 assertEquals(1, option.getId());
 }

 @Test
 public void testFindOption() {
 Option testOption2 = new MockOption();
 testOption2.setId(2);
 commandLine.parse("--testOption --testOption2");
 Option foundOption = option.findOption(commandLine, "testOption");
 assertEquals(option, foundOption);
 }

 @Test
 public void testIsRequired() {
 option.setRequired(true);
 assertFalse(commandLine.hasOption("testOption"));
 option.process(commandLine, commandLine.iterator());
 assertTrue(commandLine.hasOption("testOption"));
 }

 @Test
 public void testGetParent() {
 Option parentOption = new MockOption();
 parentOption.setId(10);
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());
 }

 @Test
 public void testSetParent() {
 Option parentOption = new MockOption();
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());
 }
}

class MockOption implements Option {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.addOption("testOption", true, "t", "This is a test option");
 commandLine.setValue("testOption", "testValue");
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return true;
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
 return true;
 }

 @Override
 public Set getTriggers() {
 return new HashSet(Arrays.asList("testOption"));
 }

 @Override
 public Set getPrefixes() {
 return new HashSet(Arrays.asList("-", "--"));
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return null;
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 }

 @Override
 public String getPreferredName() {
 return "testOption";
 }

 @Override
 public String getDescription() {
 return "This is a test option";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 }
}