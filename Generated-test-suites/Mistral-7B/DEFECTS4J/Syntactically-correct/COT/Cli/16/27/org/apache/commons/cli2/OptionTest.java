import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private WriteableCommandLine commandLine = new BasicParseResults();

 @Test
 public void testProcess() throws OptionException {
 Option option = mock(Option.class);
 ListIterator args = Arrays.asList("--option", "arg1", "arg2").listIterator();
 option.process(commandLine, args);
 assertEquals("arg1", commandLine.getOptionValue("option"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgument() throws OptionException {
 Option option = mock(Option.class);
 ListIterator args = Arrays.asList("--option", "invalidArg").listIterator();
 option.process(commandLine, args);
 fail("OptionException expected");
 }

 @Test
 public void testDefaults() {
 Option option = mock(Option.class);
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("option"));
 }

 @Test
 public void testCanProcessTrue() {
 Option option = mock(Option.class);
 WriteableCommandLine commandLine = spy(new BasicParseResults());
 when(option.canProcess(commandLine, "arg")).thenReturn(true);
 assertTrue(option.canProcess(commandLine, "arg"));
 }

 @Test
 public void testCanProcessFalse() {
 Option option = mock(Option.class);
 WriteableCommandLine commandLine = spy(new BasicParseResults());
 when(option.canProcess(commandLine, "arg")).thenReturn(false);
 assertFalse(option.canProcess(commandLine, "arg"));
 }

 @Test
 public void testGetTriggers() {
 Option option = mock(Option.class);
 Set triggers = option.getTriggers();
 assertNotNull(triggers);
 }

 @Test
 public void testGetPrefixes() {
 Option option = mock(Option.class);
 Set prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 }

 @Test
 public void testValidate() throws OptionException {
 Option option = mock(Option.class);
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidCommandLine() throws OptionException {
 Option option = mock(Option.class);
 option.validate(commandLine);
 option.validate(commandLine); // second call should throw an exception
 fail("OptionException expected");
 }

 @Test
 public void testHelpLines() {
 Option option = mock(Option.class);
 List helpLines = option.helpLines(0, null, null);
 assertNotNull(helpLines);
 }

 @Test
 public void testAppendUsage() {
 Option option = mock(Option.class);
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, null, null);
 assertEquals("", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 Option option = mock(Option.class);
 String preferredName = option.getPreferredName();
 assertNotNull(preferredName);
 }

 @Test
 public void testGetDescription() {
 Option option = mock(Option.class);
 String description = option.getDescription();
 assertNotNull(description);
 }

 @Test
 public void testGetId() {
 Option option = mock(Option.class);
 int id = option.getId();
 assertNotNull(id);
 }

 @Test
 public void testFindOption() {
 Option parentOption = mock(Option.class);
 Option childOption = mock(Option.class);
 when(childOption.getId()).thenReturn(1);
 Option option = new Option() {
 @Override
 public Option findOption(String trigger) {
 return trigger.equals("--parent") ? parentOption : null;
 }
 };
 option.setParent(parentOption);
 assertEquals(parentOption, option.findOption("--parent"));
 assertEquals(null, option.findOption("--child"));
 }

 @Test
 public void testIsRequired() {
 Option option = mock(Option.class);
 assertTrue(option.isRequired());
 }

 @Test
 public void testGetParent() {
 Option parentOption = mock(Option.class);
 Option option = new Option() {
 @Override
 public Option getParent() {
 return parentOption;
 }
 };
 assertEquals(parentOption, option.getParent());
 }

 @Test
 public void testSetParent() {
 Option option = mock(Option.class);
 Option parentOption = mock(Option.class);
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());
 }
}