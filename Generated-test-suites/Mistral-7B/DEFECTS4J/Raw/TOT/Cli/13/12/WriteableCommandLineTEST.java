import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse("--option"); // create an empty command line
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build(); // create an option
 cmd.addOption(option); // add the option to the command line
 assertEquals(option, cmd.getOption("o")); // check if the option was added correctly
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse("--option value"); // create an empty command line with an option value
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build(); // create an option
 cmd.addOption(option); // add the option to the command line
 cmd.addValue(option, "value"); // add a value to the option
 assertEquals("value", cmd.getOptionValue("o")); // check if the value was added correctly
 }

 @Test
 public void testGetValues() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse("--option value1 value2"); // create a command line with multiple values for an option
 Option option = Option.builder("o").longOpt("option").valueSeparator(' ').build(); // create an option that accepts multiple values
 cmd.addOption(option); // add the option to the command line
 cmd.addValue(option, "value1"); // add the first value
 cmd.addValue(option, "value2"); // add the second value
 List<String> values = cmd.getOptionValues("o"); // get the values for the option
 assertEquals(2, values.size()); // check if there are two values
 assertEquals("value1", values.get(0)); // check if the first value is correct
 assertEquals("value2", values.get(1)); // check if the second value is correct
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse("--switch"); // create an empty command line
 Option option = Option.builder("s").longOpt("switch").build(); // create a switch
 cmd.addSwitch(option, true); // add the switch to the command line
 try {
 cmd.addSwitch(option, true); // try to add the switch again
 } catch (IllegalStateException e) {
 // expected exception
 }
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse(""); // create an empty command line
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build(); // create an option
 List<String> defaults = new ArrayList<>();
 defaults.add("default1");
 defaults.add("default2");
 cmd.setDefaultValues(option, defaults); // set the default values for the option
 assertEquals(defaults, cmd.getOptionValues("o")); // check if the default values were set correctly
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse("--option value"); // create a command line with an option value
 assertTrue(cmd.looksLikeOption("--option")); // check if the option trigger looks like an option
 assertTrue(cmd.looksLikeOption("--option=")); // check if the option trigger with an empty value looks like an option
 assertTrue(cmd.looksLikeOption("--option value")); // check if the option trigger with a value looks like an option
 assertFalse(cmd.looksLikeOption("value")); // check if a value doesn't look like an option trigger
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine cmd = new CliCommandLineParser().parse(""); // create an empty command line
 cmd.addProperty("property", "value"); // add a property to the command line
 assertEquals("value", cmd.getProperty("property")); // check if the property was added correctly
 }
}