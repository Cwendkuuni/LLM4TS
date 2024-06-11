import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl writeableCommandLine;

 @Test
 public void testAddOption() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("opt2", "option2"));
 Option option = writeableCommandLine.getOption("opt");
 Option option2 = writeableCommandLine.getOption("opt2");
 Assert.assertNotNull(option);
 Assert.assertNotNull(option2);
 }

 @Test
 public void testAddValue() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("arg", "argument").required(true));
 writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value");
 List values = writeableCommandLine.getValues(writeableCommandLine.getOption("arg"), null);
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("switch"), true);
 Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("switch"), false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("opt", "option"));
 Assert.assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("opt")));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("switch"), true);
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("switch"), false);
 }

 @Test
 public void testGetOption() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("opt", "option"));
 Option option = writeableCommandLine.getOption("opt");
 Assert.assertNotNull(option);
 }

 @Test
 public void testGetValues() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("arg", "argument").required(true));
 writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value1");
 writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value2");
 List values = writeableCommandLine.getValues(writeableCommandLine.getOption("arg"), null);
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("arg", "argument").required(true));
 writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value1");
 List values = writeableCommandLine.getUndefaultedValues(writeableCommandLine.getOption("arg"));
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("switch"), true);
 Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("switch"), false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testGetProperty() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("prop", "property"), Collections.emptyList());
 writeableCommandLine.addProperty(writeableCommandLine.getOption("prop"), "propValue", "propertyValue");
 String propertyValue = writeableCommandLine.getProperty(writeableCommandLine.getOption("prop"), "defaultValue");
 Assert.assertEquals("propertyValue", propertyValue);
 }

 @Test
 public void testAddProperty() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("prop", "property"), Collections.emptyList());
 Properties properties = new Properties();
 properties.setProperty("propKey", "propValue");
 writeableCommandLine.addProperty(writeableCommandLine.getOption("prop"), "propKey", properties);
 Set propertiesSet = writeableCommandLine.getProperties(writeableCommandLine.getOption("prop"));
 Assert.assertEquals(1, propertiesSet.size());
 Assert.assertEquals("propKey", propertiesSet.iterator().next());
 }

 @Test
 public void testToString() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("arg", "argument").required(true));
 writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value");
 String result = writeableCommandLine.toString();
 String expected = "'arg' value='value'";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetOptions() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("opt", "option"));
 writeableCommandLine.addOption(new Option("opt2", "option2"));
 List options = writeableCommandLine.getOptions();
 Assert.assertEquals(2, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
 writeableCommandLine.addOption(new Option("opt", "option"));
 writeableCommandLine.addOption(new Option("opt2", "option2"));
 Set optionTriggers = writeableCommandLine.getOptionTriggers();
 Assert.assertEquals(2, optionTriggers.size());
 }
}