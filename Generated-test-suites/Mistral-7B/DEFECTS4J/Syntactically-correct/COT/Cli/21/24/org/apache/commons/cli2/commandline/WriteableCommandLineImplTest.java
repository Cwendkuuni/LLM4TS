import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
 commandLine.addOption(new Option("optionB"));

 Assert.assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addValue(commandLine.getOption("option"), "test");

 Assert.assertEquals("test", commandLine.getValues(commandLine.getOption("option"), null).get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", null, true));
 commandLine.addSwitch(commandLine.getOption("option"), true);

 Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option"));

 Assert.assertTrue(commandLine.hasOption(commandLine.getOption("option")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option"));

 Option option = commandLine.getOption("option");

 Assert.assertEquals("option", option.getLongOpt());
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addValue(commandLine.getOption("option"), "test1");
 commandLine.addValue(commandLine.getOption("option"), "test2");

 List values = commandLine.getValues(commandLine.getOption("option"), null);

 Assert.assertEquals(2, values.size());
 Assert.assertEquals("test1", values.get(0));
 Assert.assertEquals("test2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addValue(commandLine.getOption("option"), "test1");
 commandLine.addValue(commandLine.getOption("option"), "test2");

 List values = commandLine.getUndefaultedValues(commandLine.getOption("option"));

 Assert.assertEquals(1, values.size());
 Assert.assertEquals("test1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", null, true));
 commandLine.addSwitch(commandLine.getOption("option"), true);
 commandLine.addSwitch(commandLine.getOption("option"), false);

 Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("option"), true));
 Assert.assertFalse(commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test
 public void testgetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new PropertyOption("property", "defaultValue"));
 commandLine.addProperty(commandLine.getOption("option"), "testProperty");

 Assert.assertEquals("testProperty", commandLine.getProperty(commandLine.getOption("option"), "defaultValue"));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addProperty(commandLine.getOption("option"), new Properties());
 commandLine.setDefaultValues(commandLine.getOption("option"), new ArrayList("defaultValue1", "defaultValue2"));

 List defaults = commandLine.getValues(commandLine.getOption("option"), null);

 Assert.assertEquals(2, defaults.size());
 Assert.assertEquals("defaultValue1", defaults.get(0));
 Assert.assertEquals("defaultValue2", defaults.get(1));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addValue(commandLine.getOption("option"), "test");

 String result = commandLine.toString();

 String expected = "'option' test";

 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
 commandLine.addOption(new Option("optionB"));

 List options = commandLine.getOptions();

 Assert.assertEquals(1, options.size());
 Assert.assertEquals(commandLine.getOption("optionB"), options.get(0));
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
 commandLine.addOption(new Option("optionB"));

 Set optionTriggers = commandLine.getOptionTriggers();

 Assert.assertEquals(1, optionTriggers.size());
 Assert.assertEquals("optionB", optionTriggers.iterator().next());
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", null, true));
 commandLine.setDefaultSwitch(commandLine.getOption("option"), true);

 Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("option"), null));
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
 commandLine.addOption(new Option("option", new Argument("value")));
 commandLine.addValue(commandLine.getOption("option"), "test");

 List normalised = commandLine.getNormalised();

 Assert.assertEquals(1, normalised.size());
 Assert.assertEquals("option test", normalised.get(0));
 }
}