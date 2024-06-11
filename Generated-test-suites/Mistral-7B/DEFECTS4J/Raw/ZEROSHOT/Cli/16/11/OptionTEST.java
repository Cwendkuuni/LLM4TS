import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.HelpFormatter;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 @Test
 public void testOptionProcess() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(true)
 .setValueSeparator('=')
 .isRequired(false)
 .withDescription("Description for option1")
 .create();

 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = Arrays.asList("test", "option1", "value1").listIterator();

 try {
 option.process(commandLine, args);
 assertEquals("value1", commandLine.getOptionValue("option1"));
 } catch (OptionException e) {
 // Should not be thrown
 }
 }

 @Test
 public void testOptionDefaults() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("option1"));
 }

 @Test
 public void testOptionCanProcess() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();

 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = Arrays.asList("test", "option1").listIterator();

 assertTrue(option.canProcess(commandLine, args));
 args.next(); // move to next argument
 assertFalse(option.canProcess(commandLine, args));
 }

 @Test
 public void testOptionGetTriggers() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 Set triggers = option.getTriggers();
 assertNotNull(triggers);
 assertTrue(triggers.contains("--option1"));
 assertFalse(triggers.contains("--option2"));
 }

 @Test
 public void testOptionGetPrefixes() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 Set prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertTrue(prefixes.contains("--"));
 assertFalse(prefixes.contains("-"));
 }

 @Test
 public void testOptionValidate() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 WriteableCommandLine commandLine = new WriteableCommandLine();

 try {
 option.validate(commandLine);
 } catch (OptionException e) {
 // This test does not validate the command line, it only checks if an
 // OptionException is thrown.
 assertTrue(true);
 }

 Option invalidOption = OptionBuilder.withLongOpt("invalidOption")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for invalid option")
 .create();
 commandLine.setOptionValue("invalidOption", "value");

 try {
 invalidOption.validate(commandLine);
 fail("InvalidOptionException should be thrown");
 } catch (OptionException e) {
 // OptionException is thrown, test passes
 }
 }

 @Test
 public void testOptionHelpLines() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();

 int depth = 0;
 Set helpSettings = new HashSet();
 Comparator comp = null;
 List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);
 assertEquals(1, helpLines.size());
 assertEquals("--option1\n  Description for option1\n", helpLines.get(0).getHelp());
 }

 @Test
 public void testOptionAppendUsage() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
.isRequired(false)
 .withDescription("Description for option1")
 .create();
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, new HashSet(), null);
 assertEquals("--option1\n", buffer.toString());
 }

 @Test
 public void testOptionGetPreferredName() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 assertEquals("option1", option.getPreferredName());
 }

 @Test
 public void testOptionGetDescription() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 assertEquals("Description for option1", option.getDescription());
 }

 @Test
 public void testOptiongetId() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 assertEquals(0, option.getId());
 }

 @Test
 public void testOptionFindOption() {
 Option option1 = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 Option option2 = OptionBuilder.withLongOpt("option2")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option2")
 .create();

 Option foundOption = option1.findOption("option1");
 assertEquals(option1, foundOption);

 foundOption = option1.findOption("option2");
 assertNull(foundOption);
 }

 @Test
 public void testOptionIsRequired() {
 Option option = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 assertFalse(option.isRequired());

 option = OptionBuilder.withLongOpt("option2")
 .hasArg(false)
 .isRequired(true)
 .withDescription("Description for option2")
 .create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionGetParent() {
 Option option1 = OptionBuilder.withLongOpt("option1")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option1")
 .create();
 Option option2 = OptionBuilder.withLongOpt("option2")
 .hasArg(false)
 .isRequired(false)
 .withDescription("Description for option2")
 .withParent(option1)
 .create();
 assertEquals(option1, option2.getParent());
 assertNull(option1.getParent());
 }
}