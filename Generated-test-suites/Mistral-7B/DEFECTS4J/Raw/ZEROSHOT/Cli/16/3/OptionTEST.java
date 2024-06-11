import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.BasicOption;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testProcess() {
 Option option = new BasicOption("option", true, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = commandLine.getArgs().listIterator();
 args.next(); // skip first argument

 try {
 option.process(commandLine, args);
 assertTrue(commandLine.hasOption("option"));
 assertEquals("This is an option", commandLine.getOptionValue("option"));
 } catch (OptionException e) {
 fail("Should not throw an exception");
 }
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalidArgument() {
 Option option = new BasicOption("option", false, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = commandLine.getArgs().listIterator();
 args.next(); // skip first argument
 args.next(); // skip second argument

 try {
 option.process(commandLine, args);
 fail("Should throw an exception");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testDefaults() {
 Option option = new BasicOption("option", true, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.defaults(commandLine);
 assertTrue(commandLine.hasOption("option"));
 assertEquals("defaultValue", commandLine.getOptionValue("option"));
 }

 @Test
 public void testCanProcess() {
 Option option = new BasicOption("option", true, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = commandLine.getArgs().listIterator();
 args.next(); // skip first argument

 assertTrue(option.canProcess(commandLine, args));
 args.next(); // skip second argument
 assertFalse(option.canProcess(commandLine, args));
 }

 @Test
 public void testGetTriggers() {
 Option option = new BasicOption("option", true, "This is an option");
 Set triggers = option.getTriggers();
 assertTrue(triggers.contains("option"));
 }

 @Test
 public void testGetPrefixes() {
 Option option = new BasicOption("option", true, "This is an option");
 Set prefixes = option.getPrefixes();
 assertTrue(prefixes.contains("-"));
 assertTrue(prefixes.contains("--"));
 }

 @Test
 public void testValidate() {
 Option option = new BasicOption("option", true, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 try {
 option.validate(commandLine);
 } catch (OptionException e) {
 fail("Should not throw an exception");
 }
 }

 @Test(expected = OptionException.class)
 public void testValidateMissingArgument() {
 Option option = new BasicOption("option", true, "This is an option");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 try {
 option.validate(commandLine);
 fail("Should throw an exception");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testHelpLines() {
 Option option = new BasicOption("option", true, "This is an option");
 int depth = 1;
 Set helpSettings = commandLine -> {};
 Comparator comp = null;
 List helpLines = option.helpLines(depth, helpSettings, comp);
 assertEquals(1, helpLines.size());
 }

 @Test
 public void testAppendUsage() {
 Option option = new BasicOption("option", true, "This is an option");
 StringBuffer buffer = new StringBuffer();
 Set helpSettings = commandLine -> {};
 Comparator comp = null;
 option.appendUsage(buffer, helpSettings, comp);
 assertTrue(buffer.toString().contains("--option"));
 assertTrue(buffer.toString().contains("This is an option"));
 }

 @Test
 public void testGetPreferredName() {
 Option option = new BasicOption("option", true, "This is an option");
 assertEquals("option", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 Option option = new BasicOption("option", true, "This is an option");
 assertEquals("This is an option", option.getDescription());
 }

 @Test
 public void testGetId() {
 Option option = new BasicOption("option", true, "This is an option");
 assertEquals(0, option.getId());
 }

 @Test
 public void testFindOption() {
 Option option1 = new BasicOption("option1", true, "This is option1");
 Option option2 = new BasicOption("option2", true, "This is option2");
 Option option3 = new BasicOption("option1", true, "This is option1");
 WriteableCommandLine commandLine = new WriteableCommandLine();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 Option foundOption = option3.findOption("option1");
 assertEquals(option3, foundOption);
 }

 @Test
 public void testIsRequired() {
 Option option = new BasicOption("option", true, "This is an option");
 assertFalse(option.isRequired());
 }

 @Test
 public void testFindOptionNullParent() {
 Option option = new BasicOption("option", true, "This is an option");
 assertNull(option.getParent());
 }

 @Test
 public void testFindOptionWithParent() {
 Option option1 = new BasicOption("option1", true, "This is option1");
 Option option2 = new Option() {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 // do nothing
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 // do nothing
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
 return false;
 }

 @Override
 public Set getTriggers() {
 return null;
 }

 @Override
 public Set getPrefixes() {
 return null;
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // do nothing
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return null;
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // do nothing
 }

 @Override
 public String getPreferredName() {
 return "groupOption";
 }

 @Override
 public String getDescription() {
 return "This is a group option";
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
 return option1;
 }
 };
 WriteableCommandLine commandLine = new WriteableCommandLine();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 Option foundOption = option2.findOption("option1");
 assertEquals(option1, foundOption.getParent());
 assertEquals(option2, foundOption);
 }
}