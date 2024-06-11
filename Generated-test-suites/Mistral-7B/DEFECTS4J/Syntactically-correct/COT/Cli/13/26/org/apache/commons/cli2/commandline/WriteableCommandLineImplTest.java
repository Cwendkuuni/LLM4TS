import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
 commandLine.addOption(new Option("option2"));
 assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.addValue(commandLine.getOption("option"), "value1");
 commandLine.addValue(commandLine.getOption("option"), "value2");
 assertEquals(1, commandLine.getValues(commandLine.getOption("option"), Collections.emptyList()).size());
 assertEquals("value1", commandLine.getValues(commandLine.getOption("option"), Collections.emptyList()).get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.addSwitch(commandLine.getOption("option"), true);
 assertTrue(commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.addSwitch(commandLine.getOption("option"), true);
 commandLine.addSwitch(commandLine.getOption("option"), true);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 assertTrue(commandLine.hasOption(commandLine.getOption("option")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 Option option = commandLine.getOption("option");
 assertEquals("option", option.getLongOpt());
 assertEquals("option", option.getOpt());
 assertEquals("option", option.getName());
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.addValue(commandLine.getOption("option"), "value1");
 commandLine.addValue(commandLine.getOption("option"), "value2");
 List values = commandLine.getValues(commandLine.getOption("option"), Collections.emptyList());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.addSwitch(commandLine.getOption("option"), true);
 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
 assertTrue(switchValue);
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", ""));
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", ""));
 assertEquals("", commandLine.getProperty("property2", ""));
 }

 @Test
 public void testGetProperties() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addProperty("property1", "value1");
 commandLine.addProperty("property2", "value2");
 Set properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.contains("property1"));
 assertTrue(properties.contains("property2"));
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addProperty("prefix", "prefix");
 assertTrue(commandLine.looksLikeOption("prefixoption"));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addProperty("prefix", "prefix");
 commandLine.addOption(new Option("option"));
 commandLine.addValue(commandLine.getOption("option"), "value");
 String result = commandLine.toString();
 String expected = "prefixoption option value";
 assertEquals(expected, result);
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option1"));
 commandLine.addOption(new Option("option2"));
 List options = commandLine.getOptions();
 assertEquals(2, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option1"));
 commandLine.addOption(new Option("option2"));
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(2, optionTriggers.size());
 assertTrue(optionTriggers.contains("option1"));
 assertTrue(optionTriggers.contains("option2"));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.setDefaultValues(commandLine.getOption("option"), Arrays.asList("defaultValue1", "defaultValue2"));
 List defaultValues = commandLine.getValues(commandLine.getOption("option"), Collections.emptyList());
 assertEquals(2, defaultValues.size());
 assertEquals("defaultValue1", defaultValues.get(0));
 assertEquals("defaultValue2", defaultValues.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option"));
 commandLine.setDefaultSwitch(commandLine.getOption("option"), true);
 assertTrue(commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Arrays.asList("normalisedOption1", "normalisedOption2"));
 List normalised = commandLine.getNormalised();
 assertEquals(2, normalised.size());
 assertEquals("normalisedOption1", normalised.get(0));
 assertEquals("normalisedOption2", normalised.get(1));
 }
}