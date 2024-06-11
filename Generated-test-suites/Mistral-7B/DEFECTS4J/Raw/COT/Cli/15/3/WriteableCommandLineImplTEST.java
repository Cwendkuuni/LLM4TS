import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("b", "bool", false));

 Option optionA = commandLine.getOption("a");
 Option optionB = commandLine.getOption("b");

 Assert.assertNotNull(optionA);
 Assert.assertNotNull(optionB);
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg", true, new String[]{"value"}));
 commandLine.addValue(commandLine.getOption("a"), "testValue");

 Argument argument = (Argument) commandLine.getOption("a");

 Assert.assertEquals("testValue", argument.getValue()[0]);
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg", false));
 commandLine.addSwitch(commandLine.getOption("a"), true);

 Option option = commandLine.getOption("a");

 Assert.assertTrue(option.isSet());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg", false));
 commandLine.addSwitch(commandLine.getOption("a"), true);
 commandLine.addSwitch(commandLine.getOption("a"), false);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg"));

 Assert.assertTrue(commandLine.hasOption(commandLine.getOption("a")));
 }

 @Test
 public void testNoOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());

 Assert.assertFalse(commandLine.hasOption(new Option("b", "bool")));
 }

 @Test
 public void testGetValue() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg", true, new String[]{"value1", "value2"}), new ArrayList());
 commandLine.addValue(commandLine.getOption("a"), "testValue");

 Argument argument = (Argument) commandLine.getOption("a");

 Assert.assertArrayEquals(new String[]{"testValue"}, argument.getValue());
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg", true, new String[]{"value1", "value2"}), new ArrayList());

 List values = commandLine.getUndefaultedValues(commandLine.getOption("a"));

 Assert.assertTrue(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg", false), new ArrayList());
 commandLine.addSwitch(commandLine.getOption("a"), true);

 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("a"), false);

 Assert.assertTrue(switchValue);
 }

 @Test
 public void testHasProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addProperty(commandLine.getOption("a"), "property", "value");

 Option option = commandLine.getOption("a");

 Assert.assertTrue(option.hasProperty("property"));
 Assert.assertEquals("value", option.getProperty("property"));
 }

 @Test
 public void testNoProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());

 Option option = commandLine.getOption("a");

 Assert.assertFalse(option.hasProperty("property"));
 }

 @Test
 public void testGetProperties() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addProperty(commandLine.getOption("a"), "property1", "value1");
 commandLine.addProperty(commandLine.getOption("a"), "property2", "value2");

 Set properties = commandLine.getProperties(commandLine.getOption("a"));

 Assert.assertEquals(2, properties.size());
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addValue(commandLine.getOption("a"), "value");

 String expected = "value";
 String actual = commandLine.toString();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetListOfOptions() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("b", "bool", false));
 commandLine.addOption(new Option("c", "char", 'c'));

 List options = commandLine.getOptions();

 Assert.assertEquals(3, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg"));
 commandLine.addOption(new Option("b", "bool"));

 Set optionTriggers = commandLine.getOptionTriggers();

 Assert.assertEquals(2, optionTriggers.size());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg", true, new String[]{"value1", "value2"}));
 commandLine.setDefaultValues(commandLine.getOption("a"), Arrays.asList("defaultValue"));

 Argument argument = (Argument) commandLine.getOption("a");

 Assert.assertArrayEquals(new String[]{"defaultValue"}, argument.getValue());
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addOption(new Option("a", "arg", false));
 commandLine.setDefaultSwitch(commandLine.getOption("a"), true);

 Option option = commandLine.getOption("a");

 Assert.assertTrue(option.isSet());
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");

 List normalised = commandLine.getNormalised();

 Assert.assertEquals(2, normalised.size());
 }

}