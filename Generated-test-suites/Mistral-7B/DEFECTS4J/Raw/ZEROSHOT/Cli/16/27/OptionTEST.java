import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 @Test
 public void testProcess() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(true)
 .isRequired(false)
 .build();

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--option", "value").listIterator();
 option.process(commandLine, args);
 assertEquals("value", commandLine.getOptionValue("option"));

 args = Arrays.asList("--option").listIterator();
 assertThrows(OptionException.class, () -> option.process(commandLine, args));
 }

 @Test
 public void testDefaults() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 CommandLine commandLine = new CommandLine();
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("option"));
 }

 @Test
 public void testCanProcess_String() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 Set triggers = new HashSet();
 triggers.add("option");
 Set prefixes = new HashSet();
 prefixes.add("--");

 assertTrue(option.canProcess(new WriteableCommandLine(), triggers, prefixes, "option"));
 assertFalse(option.canProcess(new WriteableCommandLine(), triggers, prefixes, "unknown"));
 }

 @Test
 public void testCanProcess_ListIterator() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 Set triggers = new HashSet();
 triggers.add("option");
 Set prefixes = new HashSet();
 prefixes.add("--");

 List<String> args = Arrays.asList("--option").listIterator();
 assertTrue(option.canProcess(new WriteableCommandLine(), triggers, prefixes, args));

 args = Arrays.asList("--unknown").listIterator();
 assertFalse(option.canProcess(new WriteableCommandLine(), triggers, prefixes, args));
 }

 @Test
 public void testGetTriggers() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 Set triggers = option.getTriggers();
 assertNotNull(triggers);
 assertTrue(triggers.contains("option"));
 }

 @Test
 public void testGetPrefixes() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 Set prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertTrue(prefixes.contains("--"));
 }

 @Test
 public void testValidate() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 Set helpSettings = new HashSet();
 CommandLine commandLine = new CommandLine();
 commandLine.putString("option", "value");
 option.validate(commandLine);

 commandLine.removeOption("option");
 assertThrows(OptionException.class, () -> option.validate(commandLine));
 }

 @Test
 public void testHelpLines() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 int depth = 0;
 Set helpSettings = new HashSet();
 Comparator comp = null;
 List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);
 assertNotNull(helpLines);
 assertTrue(helpLines.size() > 0);
 }

 @Test
 public void testAppendUsage() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();

 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, new HashSet(), null);
 assertFalse(buffer.toString().isEmpty());
 }

 @Test
 public void testGetPreferredName() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 assertEquals("option", option.getPreferredName());
 }

 @Test
 public void testGetDescription() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 assertNotNull(option.getDescription());
 }

 @Test
 public void testgetId() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 assertEquals(0, option.getId());
 }

 @Test
 public void testFindOption() throws Exception {
 Option parentOption = OptionBuilder.withLongOpt("parent")
 .hasArg(false)
 .isRequired(false)
 .build();
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 option.setParent(parentOption);

 Option foundOption = option.findOption("option");
 assertEquals(option, foundOption);

 foundOption = option.findOption("unknown");
 assertNull(foundOption);
 }

 @Test
 public void testIsRequired() throws Exception {
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 assertFalse(option.isRequired());

 option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(true)
 .build();
 assertTrue(option.isRequired());
 }

 @Test
 public void testGetParent() throws Exception {
 Option parentOption = OptionBuilder.withLongOpt("parent")
 .hasArg(false)
 .isRequired(false)
 .build();
 Option option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());

 option = OptionBuilder.withLongOpt("option")
 .hasArg(false)
 .isRequired(false)
 .build();
 assertNull(option.getParent());
 }
}