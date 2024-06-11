import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(
 new Option("a", "arg", true, "An argument"),
 new ArrayList() {{
 add("argValue");
 }}
 );

 // Test addOption
 Option optionA = new Option("a", "arg", true, "An argument");
 commandLine.addOption(optionA);
 Assert.assertEquals(optionA, commandLine.getOption("arg"));

 // Test addValue
 commandLine.addValue(optionA, "value1");
 List values = commandLine.getValues(optionA, null);
 Assert.assertEquals(values.size(), 1);
 Assert.assertEquals(values.get(0), "value1");

 // Test addSwitch
 Option switchOption = new Option("s", "switch", false, "A switch");
 commandLine.addSwitch(switchOption, true);
 Assert.assertTrue(commandLine.getSwitch(switchOption, false));

 // Test hasOption
 Assert.assertTrue(commandLine.hasOption(optionA));

 // Test getOption
 Option option = commandLine.getOption("arg");
 Assert.assertEquals(option, optionA);

 // Test getValues
 List values1 = commandLine.getValues(optionA, null);
 Assert.assertEquals(values1.size(), 1);
 Assert.assertEquals(values1.get(0), "argValue");

 List values2 = commandLine.getValues(optionA, Collections.singletonList("value1"));
 Assert.assertEquals(values2.size(), 2);
 Assert.assertEquals(values2.get(0), "argValue");
 Assert.assertEquals(values2.get(1), "value1");

 // Test getUndefaultedValues
 List values3 = commandLine.getUndefaultedValues(optionA);
 Assert.assertEquals(values3.size(), 1);
 Assert.assertEquals(values3.get(0), "argValue");

 // Test getSwitch
 Assert.assertTrue(commandLine.getSwitch(switchOption, false));
 Assert.assertFalse(commandLine.getSwitch(switchOption, true));

 // Test getProperty
 commandLine.addProperty(optionA, "property", "propertyValue");
 Assert.assertEquals(commandLine.getProperty(optionA, "property"), "propertyValue");
 Assert.assertEquals(commandLine.getProperty(optionA, "anotherProperty"), null);

 // Test getProperties
 Set properties = commandLine.getProperties(optionA);
 Assert.assertEquals(properties.size(), 1);
 Assert.assertTrue(properties.contains("property"));

 // Test looksLikeOption
 Assert.assertTrue(commandLine.looksLikeOption("a"));
 Assert.assertFalse(commandLine.looksLikeOption("b"));

 // Test toString
 Assert.assertEquals("arg argValue", commandLine.toString());

 // Test getOptions
 Assert.assertEquals(1, commandLine.getOptions().size());

 // Test getOptionTriggers
 Assert.assertEquals(1, commandLine.getOptionTriggers().size());

 // Test setDefaultValues
 commandLine.setDefaultValues(optionA, null);
 Assert.assertNull(commandLine.getDefaultValues(optionA));
 commandLine.setDefaultValues(optionA, new ArrayList() {{
 add("defaultValue1");
 add("defaultValue2");
 }});
 List defaults = commandLine.getDefaultValues(optionA);
 Assert.assertEquals(defaults.size(), 2);
 Assert.assertEquals(defaults.get(0), "defaultValue1");
 Assert.assertEquals(defaults.get(1), "defaultValue2");

 // Test setDefaultSwitch
 commandLine.setDefaultSwitch(switchOption, null);
 Assert.assertFalse(commandLine.getDefaultSwitch(switchOption, false));
 commandLine.setDefaultSwitch(switchOption, true);
 Assert.assertTrue(commandLine.getDefaultSwitch(switchOption, false));
 }
}