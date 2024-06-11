import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 Option option = new Option("t", "test2");
 commandLine.addOption(option);
 Assert.assertEquals(2, commandLine.getOptions().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 Option option = new Option("t", "test2");
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 Assert.assertEquals(2, commandLine.getValues(commandLine.getOption("a"), null).size());
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList<>());
 commandLine.addSwitch(commandLine.getOption("s"), true);
 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("s"), false));
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("x", "unknown"), new ArrayList<>());
 Assert.assertFalse(commandLine.hasOption(commandLine.getOption("x")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 Assert.assertEquals(commandLine.getOption("t"), commandLine.getOption("test"));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 Assert.assertEquals(2, commandLine.getValues(commandLine.getOption("a"), null).size());
 Assert.assertEquals("value1", commandLine.getValues(commandLine.getOption("a"), null).get(0));
 Assert.assertEquals("value2", commandLine.getValues(commandLine.getOption("a"), null).get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
 commandLine.addValue(commandLine.getOption("a"), "value1");
 Assert.assertEquals(1, commandLine.getUndefaultedValues(commandLine.getOption("a")).size());
 Assert.assertEquals("value1", commandLine.getUndefaultedValues(commandLine.getOption("a")).get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList<>());
 commandLine.addSwitch(commandLine.getOption("s"), true);
 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("s"), false));
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("p", "property"), new ArrayList<>());
 commandLine.addProperty(commandLine.getOption("p"), "name", "value");
 Assert.assertEquals("value", commandLine.getProperty(commandLine.getOption("p"), "name"));
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("p", "property"), new ArrayList<>());
 commandLine.addProperty(commandLine.getOption("p"), "name1", "value1");
 commandLine.addProperty(commandLine.getOption("p"), "name2", "value2");
 Set properties = commandLine.getProperties(commandLine.getOption("p"));
 Assert.assertEquals(2, properties.size());
 Assert.assertTrue(properties.contains("name1"));
 Assert.assertTrue(properties.contains("name2"));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 commandLine.addValue(commandLine.getOption("t"), "value1");
 commandLine.addValue(commandLine.getOption("t"), "value2");
 String expected = "value1 value2";
 Assert.assertEquals(expected, commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());commandLine.addOption(new Option("a", "arg"));
 Assert.assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 commandLine.addOption(new Option("a", "arg"));
 Assert.assertEquals(2, commandLine.getOptionTriggers().size());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
 List defaultValues = new ArrayList();
 defaultValues.add("default1");
 commandLine.setDefaultValues(commandLine.getOption("a"), defaultValues);
 Assert.assertEquals(1, commandLine.getValues(commandLine.getOption("a"), null).size());
 Assert.assertEquals("default1", commandLine.getValues(commandLine.getOption("a"), null).get(0));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList<>());
 commandLine.setDefaultSwitch(commandLine.getOption("s"), true);
 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("s"), false));
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option("t", "test"), new ArrayList<>());
 commandLine.addValue(commandLine.getOption("t"), "value1");
 commandLine.addValue(commandLine.getOption("t"), "value2");
 List normalised = commandLine.getNormalised();
 Assert.assertEquals(2, normalised.size());
 Assert.assertEquals("value1", normalised.get(0));
 Assert.assertEquals("value2", normalised.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetDefaultValuesWithNullOption() {
 commandLine.setDefaultValues(null, new ArrayList<>());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetDefaultSwitchWithNullOption() {
 commandLine.setDefaultSwitch(null, true);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetDefaultValuesWithNullValueList() {
 commandLine.setDefaultValues(commandLine.getOption("a"), null);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetDefaultSwitchWithNullBoolean() {
 commandLine.setDefaultSwitch(commandLine.getOption("s"), null);
 }

}