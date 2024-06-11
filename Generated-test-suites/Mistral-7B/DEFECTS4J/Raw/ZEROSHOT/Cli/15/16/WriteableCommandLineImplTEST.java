import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2. WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder().withLongOpt("inputFile")
 .withValueVariableName("file")
 .withRequiredArg()
 .create(), Arrays.asList("input.txt"));

 // Test adding an option
 Option option = new Option.Builder().withLongOpt("outputFile")
 .withValueVariableName("output")
 .create();
 commandLine.addOption(option);

 // Test adding a value for an option
 commandLine.addValue(option, "output.txt");

 // Test setting a switch for an option
 commandLine.addSwitch(option, true);

 // Test getting an option
 Option optionFromCommandLine = commandLine.getOption("outputFile");
 assertEquals(option, optionFromCommandLine);

 // Test getting the value of an option
 List values = commandLine.getValues(option, null);
 assertEquals("output.txt", values.get(0));

 // Test getting a switch for an option
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);

 // Test getting properties for an option
 commandLine.addProperty(option, "property", "value");
 Properties properties = commandLine.getProperties(option);
 assertEquals(1, properties.size());
 assertEquals("value", properties.getProperty("property"));

 // Test getting all properties
 Properties allProperties = commandLine.getProperties();
 assertEquals(0, allProperties.size());

 // Test looking like an option
 assertTrue(commandLine.looksLikeOption("outputFile"));
 assertFalse(commandLine.looksLikeOption("nonexistentOption"));

 // Test getting options
 List options = commandLine.getOptions();
 assertEquals(1, options.size());
 assertEquals(option, options.get(0));

 // Test getting option triggers
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(1, optionTriggers.size());
 assertEquals("outputFile", optionTriggers.iterator().next());

 // Test getting the normalised command line
 List normalised = commandLine.getNormalised();
 assertEquals(3, normalised.size());
 assertEquals("input.txt", normalised.get(0));
 assertEquals("outputFile", normalised.get(1));
 assertEquals("output.txt", normalised.get(2));
 }
}