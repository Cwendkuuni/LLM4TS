import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 assertEquals(option, commandLine.getOption("a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(null);
 }

 @Test
 public void testAddValue() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 assertEquals("value1", commandLine.getValuesForOption("a").get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNullOption() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", true, "append value");
 commandLine.addValue(option, null);
 }

 @Test
 public void testGetValuesForOption() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getValuesForOption("a");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("default1");
 defaultValues.add("default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getValuesForOption("a");
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", false, "append value");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption("a"));
 assertTrue(commandLine.isSet("a"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchExistingSwitch() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", false, "append value");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 CommandLine commandLine = new CommandLine();
 Option option = new Option("a", "append", false, "append value");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.isSet("a"));
 }

 @Test
 public void testAddProperty() {
 CommandLine commandLine = new CommandLine();
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getProperties().getProperty("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 CommandLine commandLine = new CommandLine();
 // negative test: not an option
 assertFalse(commandLine.looksLikeOption("--arg"));
 // positive test: option with a value
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 // test option with no value
 assertFalse(commandLine.looksLikeOption("a"));
 // test option with value
 commandLine.addValue(option, "value");
 assertTrue(commandLine.looksLikeOption("a value"));
 // test option with long name
 option.setLongOpt("long-option");
 assertTrue(commandLine.looksLikeOption("--long-option"));
 }

 @Test
 public void testLooksLikeOptionWithDash() {
 CommandLine commandLine = new CommandLine();
 // negative test: not an option
 assertFalse(commandLine.looksLikeOption("-a"));
 // positive test: option with a value
 Option option = new Option("a", "append", true, "append value");
 commandLine.addOption(option);
 // test option with no value
 assertFalse(commandLine.looksLikeOption("-a"));
 // test option with value
 commandLine.addValue(option, "value");
 assertTrue(commandLine.looksLikeOption("-a value"));
 // test option with long name
 option.setLongOpt("long-option");
 assertTrue(commandLine.looksLikeOption("--long-option"));
 // test option with short name
 option.setShortOpt('a');
 assertTrue(commandLine.looksLikeOption("-a"));
 }
}