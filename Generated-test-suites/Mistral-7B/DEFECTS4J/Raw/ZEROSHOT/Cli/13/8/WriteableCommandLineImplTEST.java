import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Test
 public void testCreateWriteableCommandLineImpl() {
 // Given a rootOption and some arguments
 Option rootOption = new Option("option", "option", true, "Option description");
 List arguments = new ArrayList();
 arguments.add("--option value1");
 arguments.add("--option value2");

 // When creating a new WriteableCommandLineImpl
 commandLine = new WriteableCommandLineImpl(rootOption, arguments);

 // Then the command line should have the correct rootOption and arguments
 assertEquals(rootOption, commandLine.getRootOption());
 assertEquals(arguments, commandLine.getNormalised());
 }

 @Test
 public void testAddOption() {
 // Given a WriteableCommandLineImpl and an Option
 option = new Option("option", "option", false, "Option description");
 commandLine.addOption(option);

 // When adding the Option to the WriteableCommandLineImpl
 // Then the Option should be present in the options list
 assertTrue(commandLine.getOptions().contains(option));

 // And its name should be mapped to the Option
 assertEquals(option, commandLine.getOption("option"));
 }

 @Test
 public void testAddValue() {
 // Given a WriteableCommandLineImpl and an Option with no values
 Option option = new Option("option", "option", false, "Option description");
 commandLine.addOption(option);

 // When adding a value to the Option
 commandLine.addValue(option, "value1");

 // Then the value should be present in the values list for the Option
 List values = commandLine.getValues(option, null);
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 // Given a WriteableCommandLineImpl and an Option with no switch value
 Option option = new Option("option", "option", false, "Option description");
 commandLine.addOption(option);

 // When setting the switch value for the Option
 commandLine.addSwitch(option, true);

 // Then the switch value should be present in the switches map for the Option
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 // Given a WriteableCommandLineImpl with an Option
 commandLine.addOption(option);

 // When checking if the WriteableCommandLineImpl has the Option
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 // Given a WriteableCommandLineImpl with an Option
 commandLine.addOption(option);

 // When getting the Option from the WriteableCommandLineImpl by name
 Option retrievedOption = commandLine.getOption("option");

 // Then the retrieved Option should be the same as the original Option
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 // Given a WriteableCommandLineImpl with an Option and some values
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 // When getting the values for the Option
 List values = commandLine.getValues(option, null);

 // Then the values should be returned as an unmodifiable list
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 // Given a WriteableCommandLineImpl with an Option and a switch value
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);

 // When getting the switch value for the Option
 Boolean switchValue = commandLine.getSwitch(option, false);

 // Then the switch value should be returned
 assertTrue(switchValue);
 }

 @Test
 public void testAddProperty() {
 // Given a WriteableCommandLineImpl
 commandLine.addProperty("property", "value");

 // When getting the property value
 String propertyValue = commandLine.getProperty("property", null);

 // Then the property value should be returned
 assertEquals("value", propertyValue);
 }

 @Test
 public void testGetProperty() {
 // Given a WriteableCommandLineImpl with a property
 commandLine.addProperty("property", "value");

 // When getting the property value
 String propertyValue = commandLine.getProperty("property", null);

 // Then the property value should be returned
 assertEquals("value", propertyValue);

 // And the property should be present in the properties set
 Set properties = commandLine.getProperties();
 assertTrue(properties.contains("property"));
 }

 @Test
 public void testToString() {
 // Given a WriteableCommandLineImpl
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 // When getting the toString representation of the WriteableCommandLineImpl
 String toString = commandLine.toString();

 // Then the toString representation should match the expected value
 assertEquals("--option value1 value2", toString);
 }

 @Test
 public void testGetOptions() {
 // Given a WriteableCommandLineImpl with some Options
 commandLine.addOption(option);
 commandLine.addOption(new Option("option2", "option2", false, "Option2 description"));

 // When getting the list of Options
 List options = commandLine.getOptions();

 // Then the list of Options should be returned as an unmodifiable list
 assertEquals(2, options.size());
 assertTrue(options.contains(option));
 assertTrue(options.contains(new Option("option2", "option2", false, "Option2 description")));
 }

 @Test
 public void testGetOptionTriggers() {
 // Given a WriteableCommandLineImpl with some Options
 commandLine.addOption(option);
 commandLine.addOption(new Option("option2", "option2", false, "Option2 description"));

 // When getting the set of Option triggers
 Set optionTriggers = commandLine.getOptionTriggers();

 // Then the set of Option triggers should be returned as an unmodifiable set
 assertEquals(2, optionTriggers.size());
 assertTrue(optionTriggers.contains("option"));
 assertTrue(optionTriggers.contains("option2"));
 }

 @Test
 public void testSetDefaultValues() {
 // Given a WriteableCommandLineImpl and an Option with default values
 Option option = new Option("option", "option", false, "Option description");
 commandLine.addOption(option);
 List defaultValues = new ArrayList();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);

 // When getting the default values for the Option
 List values = commandLine.getValues(option, null);

 // Then the default values should be returned as an unmodifiable list
 assertEquals(2, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 // Given a WriteableCommandLineImpl and an Option with a default switch value
 Option option = new Option("option", "option", false, "Option description");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);

 // When getting the default switch value for the Option
 Boolean switchValue = commandLine.getSwitch(option, false);

 // Then the default switch value should be returned
 assertTrue(switchValue);
 }

 @Test
 public void testGetNormalised() {
 // Given a WriteableCommandLineImpl with some arguments
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 commandLine.addOption(new Option("option2", "option2", false, "Option2 description"));
 commandLine.addValue(option2, "value3");
 commandLine.addValue(option2, "value4");

 // When getting the normalised list of arguments
 List normalised = commandLine.getNormalised();

 // Then the normalised list of arguments should be returned as an unmodifiable list
 assertEquals(5, normalised.size());
 assertTrue(normalised.contains("--option value1 value2"));
 assertTrue(normalised.contains("--option2 value3 value4"));
 }
}