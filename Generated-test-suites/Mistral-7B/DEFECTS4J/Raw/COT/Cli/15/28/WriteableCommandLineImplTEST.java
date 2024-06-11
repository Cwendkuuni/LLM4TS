import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl cmd;

 @Test
 public void testAddOption() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", "b", true, "Value for b"));
 Assert.assertEquals(1, cmd.getOptions().size());
 }

 @Test
 public void testGetOption() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", "b", true, "Value for b"));
 Option option = cmd.getOption("b");
 Assert.assertNotNull(option);
 Assert.assertEquals("b", option.getName());
 Assert.assertEquals("Value for b", option.getLongOpt());
 }

 @Test
 public void testHasOption() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", "b", true, "Value for b"));
 Assert.assertTrue(cmd.hasOption(new Option("b")));
 Assert.assertFalse(cmd.hasOption(new Option("c")));
 }

 @Test
 public void testGetValues() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true, "bValue"));
 cmd.addValue("b", "value1");
 cmd.addValue("b", "value2");
 List<Object> values = cmd.getValues(new Option("b"), Collections.emptyList());
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", false));
 cmd.addSwitch(new Option("b"), true);
 Boolean switchValue = cmd.getSwitch(new Option("b"), false);
 Assert.assertTrue(switchValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", false));
 cmd.addSwitch(new Option("b"), true);
 cmd.addSwitch(new Option("b"), false);
 }

 @Test
 public void testgetProperty() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new PropertyOption("p", "p", "propValue", "Property Option"));
 cmd.addProperty(new PropertyOption("p"), "propName", "propertyValue");
 String value = cmd.getProperty(new PropertyOption("p"), "propName");
 Assert.assertEquals("propertyValue", value);
 }

 @Test
 public void testAddProperty() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new PropertyOption("p", "p", "propValue", "Property Option"));
 cmd.addProperty(new PropertyOption("p"), "propName1", "propValue1");
 cmd.addProperty(new PropertyOption("p"), "propName2", "propValue2");
 Set<String> properties = cmd.getProperties(new PropertyOption("p"));
 Assert.assertEquals(2, properties.size());
 Assert.assertTrue(properties.contains("propName1"));
 Assert.assertTrue(properties.contains("propName2"));
 }

 @Test
 public void testToString() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true));
 cmd.addValue("b", "value1");
 cmd.addValue("b", "value2");
 cmd.addSwitch(new Option("c"), true);
 String cmdString = cmd.toString();
 String expected = "value1 value2";
 Assert.assertEquals(expected, cmdString.substring(cmdString.indexOf(" "), cmdString.length()));
 }

 @Test
 public void testGetOptions() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true));
 cmd.addOption(new Option("c", true));
 List<Option> options = cmd.getOptions();
 Assert.assertEquals(2, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true));
 cmd.addOption(new Option("c", true));
 Set<String> triggers = cmd.getOptionTriggers();
 Assert.assertEquals(2, triggers.size());
 Assert.assertTrue(triggers.contains("b"));
 Assert.assertTrue(triggers.contains("c"));
 }

 @Test
 public void testSetDefaultValues() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true));
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 cmd.setDefaultValues(new Option("b"), defaultValues);
 List<Object> values = cmd.getValues(new Option("b"), Collections.emptyList());
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("defaultValue1", values.get(0));
 Assert.assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", false));
 cmd.setDefaultSwitch(new Option("b"), true);
 Boolean switchValue = cmd.getSwitch(new Option("b"), false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testGetNormalised() {
 cmd = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
 cmd.addOption(new Option("b", true));
 cmd.addValue("b", "value1");
 cmd.addValue("b", "value2");
 List<String> normalised = cmd.getNormalised();
 Assert.assertEquals(3, normalised.size());
 }

}