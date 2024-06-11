import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLine writeableCommandLine;
 private Option option;

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("option").build(), Collections.emptyList());
 assertEquals("WriteableCommandLineImpl is instantiated correctly", writeableCommandLine, writeableCommandLine);
 }

 @Test
 public void testAddOption() {
 option = new Option.Builder("option").build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 assertEquals("Option is added to options list", option, writeableCommandLine.getOption("option"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 option = new Option.Builder("option").build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addOption(option);
 fail("Option should not be able to be added twice");
 }

 @Test
 public void testAddValue() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals("Values are set correctly", Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testAddSwitch() {
 option = new Option.Builder("switch").build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue("Switch is set to true", writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 assertTrue("Option exists in options list", writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 Option result = writeableCommandLine.getOption("option");
 assertEquals("Retrieved option matches added option", option, result);
 }

 @Test
 public void testGetValues() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals("Values are retrieved correctly", Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testGetSwitch() {
 option = new Option.Builder("switch").build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue("Switch is retrieved correctly", writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testAddProperty() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("option").build(), Collections.emptyList());
 writeableCommandLine.addProperty("property", "value");
 assertEquals("Property is set correctly", "value", writeableCommandLine.getProperty("property", ""));
 }

 @Test
 public void testGetProperties() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option.Builder("option").build(), Collections.emptyList());
 writeableCommandLine.addProperty("property1", "value1");
 writeableCommandLine.addProperty("property2", "value2");
 Set properties = writeableCommandLine.getProperties();
 assertTrue("Properties are set correctly", properties.contains("property1"));
 assertTrue("Properties are set correctly", properties.contains("property2"));
 }

 @Test
 public void testToString() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 String result = writeableCommandLine.toString();
 String expected = "option value1 value2";
 assertEquals("ToString method returns expected string", expected, result);
 }

 @Test
 public void testGetOptions() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 List options = writeableCommandLine.getOptions();
 assertEquals("Options are set correctly", Arrays.asList(option), options);
 }

 @Test
 public void testGetOptionTriggers() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 Set optionTriggers = writeableCommandLine.getOptionTriggers();
 assertTrue("Option triggers are set correctly", optionTriggers.contains("option"));
 }

 @Test
 public void testSetDefaultValues() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 writeableCommandLine.setDefaultValues(option, defaultValues);
 List values = writeableCommandLine.getValues(option, null);
 assertEquals("Default values are set correctly", Arrays.asList("defaultValue1", "defaultValue2"), values);
 }

 @Test
 public void testSetDefaultSwitch() {
 option = new Option.Builder("switch").build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultSwitch(option, true);
 assertTrue("Default switch is set to true", writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 option = new Option.Builder("option").required(false).build();
 writeableCommandLine = new WriteableCommandLineImpl(option, Collections.asList("normalisedValue1", "normalisedValue2"));
 List normalised = writeableCommandLine.getNormalised();
 assertEquals("Normalised values are set correctly", Arrays.asList("normalisedValue1", "normalisedValue2"), normalised);
 }
}