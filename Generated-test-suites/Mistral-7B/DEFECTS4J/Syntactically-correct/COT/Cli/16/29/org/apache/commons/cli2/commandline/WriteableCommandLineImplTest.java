import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", true, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt2", false, "opt2Desc"));

 Option opt = commandLine.getOption("opt");
 Option opt2 = commandLine.getOption("opt2");

 assertEquals("optDesc", opt.getDescription());
 assertEquals("opt2Desc", opt2.getDescription());
 assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", true, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", true, "optDesc"));
 commandLine.addValue(commandLine.getOption("opt"), "value1");
 commandLine.addValue(commandLine.getOption("opt"), "value2");

 List values = commandLine.getValues(commandLine.getOption("opt"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addSwitch(commandLine.getOption("opt"), true);

 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("opt"), false);
 assertTrue(switchValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addSwitch(commandLine.getOption("opt"), true);
 commandLine.addSwitch(commandLine.getOption("opt"), true);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));

 assertTrue(commandLine.hasOption(commandLine.getOption("opt")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));

 Option opt = commandLine.getOption("opt");
 assertNotNull(opt);
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", true, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", true, "optDesc"));
 commandLine.addValue(commandLine.getOption("opt"), "value1");
 commandLine.addValue(commandLine.getOption("opt"), "value2");

 List values = commandLine.getValues(commandLine.getOption("opt"), null);
 assertEquals(2, values.size());
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", true, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", true, "optDesc"));
 commandLine.addValue(commandLine.getOption("opt"), "value1");
 commandLine.addValue(commandLine.getOption("opt"), "value2");

 List values = commandLine.getUndefaultedValues(commandLine.getOption("opt"));
 assertEquals(2, values.size());
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addSwitch(commandLine.getOption("opt"), false);

 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("opt"), false);
 assertFalse(switchValue);
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addProperty(commandLine.getOption("opt"), "key", "value");

 String propertyValue = commandLine.getProperty(commandLine.getOption("opt"), "key", "defaultValue");
 assertEquals("value", propertyValue);
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 Properties properties = new Properties();
 properties.setProperty("key1", "value1");
 commandLine.addProperty(commandLine.getOption("opt"), properties);

 Set propertiesSet = commandLine.getProperties(commandLine.getOption("opt"));
 assertEquals(1, propertiesSet.size());
 assertTrue(propertiesSet.contains("key1"));
 assertEquals("value1", commandLine.getProperty(commandLine.getOption("opt"), "key1"));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addValue(commandLine.getOption("opt"), "arg1");
 commandLine.addValue(commandLine.getOption("opt"), "arg2");

 String commandLineString = commandLine.toString();
 assertEquals("arg1 arg2", commandLineString);
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addOption(new Option("opt2", false, "optDesc2"));

 List options = commandLine.getOptions();
 assertEquals(2, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addOption(new Option("opt2", false, "optDesc2"));

 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(2, optionTriggers.size());
 assertTrue(optionTriggers.contains("opt"));
 assertTrue(optionTriggers.contains("opt2"));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", true, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", true, "optDesc"));
 commandLine.setDefaultValues(commandLine.getOption("opt"), Arrays.asList("defaultValue1", "defaultValue2"));

 List defaultValues = commandLine.getValues(commandLine.getOption("opt"), null);
 assertEquals(2, defaultValues.size());
 assertEquals("defaultValue1", defaultValues.get(0));
 assertEquals("defaultValue2", defaultValues.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.setDefaultSwitch(commandLine.getOption("opt"), true);

 Boolean defaultSwitch = commandLine.getSwitch(commandLine.getOption("opt"), false);
 assertTrue(defaultSwitch);
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option("opt", false, "optDesc"), Collections.emptyList());
 commandLine.addOption(new Option("opt", false, "optDesc"));
 commandLine.addValue(commandLine.getOption("opt"), "arg1");
 commandLine.addValue(commandLine.getOption("opt"), "arg2");

 List normalised = commandLine.getNormalised();
 assertEquals(2, normalised.size());
 assertEquals("arg1 arg2", normalised.toString());
 }

}