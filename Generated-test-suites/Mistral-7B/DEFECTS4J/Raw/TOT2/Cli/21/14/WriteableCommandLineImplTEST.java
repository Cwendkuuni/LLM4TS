import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 // Given a WriteableCommandLineImpl instance
 Option option1 = new Option("o", "output", true, "output file path");
 Option option2 = new Option("i", "input", true, "input file path");
 commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("test"));
 }

 @Test
 public void testAddOption() {
 // When adding an option
 Option opt = new Option("t", "test", false, "Run tests");
 commandLine.addOption(opt);
 // Then the option should be present in the command line
 assertTrue(commandLine.hasOption(opt));
 }

 @Test
 public void testAddValue() {
 // Given an option with no value
 Option option = new Option("o", "output", true, "output file path");
 commandLine.addOption(option);
 // When adding a value to the option
 commandLine.addValue(option, "output.txt");
 // Then the value should be present in the command line
 assertEquals("output.txt", commandLine.getValues(option, null).get(0));
 }

 @Test
 public void testAddSwitch() {
 // Given an option with no switch value
 Option option = new Option("v", "verbose", false, "Print verbose output");
 commandLine.addOption(option);
 // When adding a switch value to the option
 commandLine.addSwitch(option, true);
 // Then the switch value should be present in the command line
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 // Given an option
 Option option = new Option("o", "output", true, "output file path");
 commandLine.addOption(option);
 // When checking if the option is present in the command line
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 // Given an option
 Option option = new Option("o", "output", true, "output file path");
 commandLine.addOption(option);
 // When retrieving the option from the command line
 Option retrievedOption = commandLine.getOption("o");
 // Then the retrieved option should be the same as the original option
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 // Given an option with a value
 Option option = new Option("o", "output", true, "output file path");
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 // When retrieving the values of the option
 List values = commandLine.getValues(option, null);
 // Then the values should be equal to the expected value
 assertEquals("output.txt", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 // Given an option with no default value
 Option option = new Option("o", "output", true, "output file path");
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 // When retrieving the unfaulted values of the option
 List values = commandLine.getUndefaultedValues(option);
 // Then the values should be equal to the actual values in the command line
 assertEquals("output.txt", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 // Given an option with a switch value
 Option option = new Option("v", "verbose", false, "Print verbose output");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 // When retrieving the switch value of the option
 Boolean switchValue = commandLine.getSwitch(option, false);
 // Then the switch value should be true
 assertTrue(switchValue);
 }

 @Test
 public void testGetProperty() {
 // Given an option with a property
 Option option = new Option("p", "property", true, "Property value");
 commandLine.addOption(option);
 commandLine.addProperty(option, "property.name", "property.value");
 // When retrieving the property value of the option
 String propertyValue = commandLine.getProperty(option, "property.name", null);
 // Then the property value should be equal to the expected value
 assertEquals("property.value", propertyValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 // Given an option
 Option option = new Option("v", "verbose", false, "Print verbose output");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 // When trying to add a switch value to the option a second time
 // Then an IllegalStateException should be thrown
 }

 @Test
 public void testToString() {
 // Given a WriteableCommandLineImpl instance
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file path"), Collections.singletonList("test"));
 commandLine.addOption(new Option("i", "input", true, "input file path"));
 commandLine.addValue(commandLine.getOption("o"), "output.txt");
 commandLine.addValue(commandLine.getOption("i"), "input.txt");
 commandLine.addSwitch(commandLine.getOption("o"), true);
 // When calling the toString method on the command line
 String commandLineString = commandLine.toString();
 // Then the command line string should be equal to the expected value
 assertEquals("output.txt input.txt -o -o -v", commandLineString);
 }
}