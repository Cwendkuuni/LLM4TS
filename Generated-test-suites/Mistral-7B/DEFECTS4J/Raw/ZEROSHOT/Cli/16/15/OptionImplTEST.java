import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

 @Test
 public void testOptionImplConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertNotNull(option);
 }

 @Test
 public void testOptionCanProcess() {
 OptionImpl option = new OptionImpl(1, true);
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();
 assertFalse(option.canProcess(commandLine, arguments));

 arguments.next(); // remove the first argument
 arguments.add("--option"); // add the option argument
 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testOptionToString() {
 OptionImpl option = new OptionImpl(1, true);
 StringBuilder buffer = new StringBuilder();
 option.appendUsage(buffer, DisplaySetting.ALL, null);
 String expectedUsage = "  <option>...\n";
 assertTrue(buffer.toString().startsWith(expectedUsage));
 }

 @Test
 public void testOptionEquals() {
 OptionImpl option1 = new OptionImpl(1, true, null, null, null, null, null, null);
 OptionImpl option2 = new OptionImpl(1, true, null, null, null, null, null, null);
 OptionImpl option3 = new OptionImpl(2, false, null, null, null, null, null, null);

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2)); // same id
 assertFalse(option1.equals(option3)); // different id
 }

 @Test
 public void testOptionHashCode() {
 OptionImpl option1 = new OptionImpl(1, true, null, null, null, null, null, null);
 OptionImpl option2 = new OptionImpl(1, true, null, null, null, null, null, null);
 OptionImpl option3 = new OptionImpl(2, false, null, null, null, null, null, null);

 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode()); // same id
 assertNotEquals(option1.hashCode(), option3.hashCode()); // different id
 }

 @Test
 public void testOptionFindOption() {
 OptionImpl option = new OptionBuilder()
 .withLongOpt("option")
 .withDescription("An option")
 .create();
 OptionImpl anotherOption = new OptionBuilder()
 .withLongOpt("another-option")
 .withDescription("Another option")
 .create();

 Set<String> triggers = option.getTriggers();
 triggers.add("option");
 triggers.add("opt");

 Set<String> anotherTriggers = anotherOption.getTriggers();
 anotherTriggers.add("another-option");
 anotherTriggers.add("ao");

 assertTrue(option.findOption("option") == option);
 assertTrue(option.findOption("opt") == option);
 assertTrue(option.findOption("another-option") == anotherOption);
 assertNull(option.findOption("nonexistent"));
 }

 @Test
 public void testOptionIsRequired() {
 OptionImpl option = new OptionImpl(1, true);
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionDefaults() {
 OptionImpl option = new OptionImpl(1, true);
 WriteableCommandLine commandLine = new CommandLine();
 option.defaults(commandLine);
 assertNotNull(commandLine.getOption(option.getId()));
 }

 @Test
 public void testOptionCheckPrefixes() {
 OptionImpl option = new OptionBuilder()
 .withLongOpt("option")
 .withDescription("An option")
 .create();
 Set<String> prefixes = option.getPrefixes();
 prefixes.add("opt");

 try {
 option.checkPrefixes(prefixes);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCheckPrefixesWithMissingPrefix() {
 OptionImpl option = new OptionBuilder()
 .withLongOpt("option")
 .withDescription("An option")
 .create();
 Set<String> prefixes = option.getPrefixes();
 prefixes.clear();
 prefixes.add("nonexistent");
 option.checkPrefixes(prefixes);
 }
}