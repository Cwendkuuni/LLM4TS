import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 option = new TestOption();
 }

 @Test
 public void testProcess() {
 option.process(commandLine, commandLine.iterator());
 assertEquals("expected value", commandLine.getValue("testKey"), "testValue");
 }

 @Test
 public void testProcess_noValue() {
 option.process(commandLine, commandLine.iterator());
 assertNull(commandLine.getValue("testKey"));
 }

 @Test
 public void testProcess_invalidArgument() {
 commandLine.addOption(new TestOption("--test", true));
 option.process(commandLine, commandLine.iterator());
 commandLine.addArgument("invalid");
 option.process(commandLine, commandLine.iterator());
 assertFalse(commandLine.hasErrors());
 assertTrue(commandLine.hasOption("test"));
 assertFalse(commandLine.hasOption("invalid"));
 }

 @Test(expected = OptionException.class)
 public void testProcess_invalidArgumentThrowException() {
 commandLine.addOption(new TestOption("--test", true));
 commandLine.addArgument("--test");
 option.process(commandLine, commandLine.iterator());
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertEquals("default value", commandLine.getValue("testKey"), "defaultValue");
 }

 @Test
 public void testCanProcess() {
 assertTrue(option.canProcess(commandLine, commandLine.iterator()));
 assertFalse(option.canProcess(commandLine, commandLine.iterator(), "--invalid"));
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 assertEquals(triggers.size(), 1);
 assertEquals(triggers.iterator().next(), "--test");
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 assertEquals(prefixes.size(), 1);
 assertEquals(prefixes.iterator().next(), "--");
 }

 @Test
 public void testValidate() {
 commandLine.addOption(new TestOption("--test", true));
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidate_required() {
 commandLine.addOption(new TestOption("--test", true, true));
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Set<String> helpSettings = commandLine.getHelpSettings();
 List<HelpLine> helpLines = option.helpLines(1, helpSettings, null);
 assertEquals(helpLines.size(), 1);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, helpSettings, null);
 assertEquals(buffer.toString(), "--test\n  Test option\n");
 }

 @Test
 public void testGetPreferredName() {
 assertEquals(option.getPreferredName(), "--test");
 }

 @Test
 public void testGetDescription() {
 assertEquals(option.getDescription(), " Test option");
 }

 @Test
 public void testGetId() {
 assertEquals(option.getId(), 0);
 }

 @Test
 public void testFindOption() {
 Option found = option.findOption("--test");
 assertEquals(found, option);
 }

 @Test
 public void testIsRequired() {
 assertFalse(option.isRequired());
 }

 @Test
 public void testHasParent() {
 Option group = new TestGroupOption();
 option.setParent(group);
 assertTrue(option.hasParent());
 assertEquals(option.getParent(), group);
 }

 @Test
 public void testHasNoParent() {
 assertNull(option.getParent());
 }
}

class TestOption extends AbstractOption {

 public TestOption() {
 super("test", "testKey", "testValue", true);
 }

 public TestOption(String longOpt, String optParam, String optDescription, boolean required) {
 super(longOpt, optParam, optDescription, required);
 }

 @Override
 public Set<String> getTriggers() {
 return super.getTriggers();
 }

 @Override
 public Set<String> getPrefixes() {
 return super.getPrefixes();
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 super.defaults(commandLine);
 commandLine.setValue("testKey", "defaultValue");
 }
}

class TestGroupOption extends AbstractOptionGroup {

 public TestGroupOption() {
 super();
 }
}