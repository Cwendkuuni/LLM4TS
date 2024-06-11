import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class WriteableCommandLineImplTest {

 WriteableCommandLineImpl commandLine;
 Option option;
 List arguments;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option("option", true, "opt"), new ArrayList());
 option = new Option("testOption", true, "testOpt");
 arguments = new ArrayList();
 arguments.add("testArgument");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 List options = commandLine.getOptions();
 assertTrue(options.contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 List values = commandLine.getValues(option, null);
 assertEquals("testValue", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 commandLine.addOption(option);
 Option returnedOption = commandLine.getOption("testOption");
 assertSame(option, returnedOption);
 }

 @Test
 public void testGetValues() {
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue1");
 commandLine.addValue(option, "testValue2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 }

 @Test
 public void testGetSwitch() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("testProperty", "testValue");
 String propertyValue = commandLine.getProperty("testProperty", null);
 assertEquals("testValue", propertyValue);
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty("testProperty", "testValue");
 String propertyValue = commandLine.getProperty("testProperty", null);
 assertEquals("testValue", propertyValue);
 String defaultValue = commandLine.getProperty("defaultProperty", null);
 assertNull(defaultValue);
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty("testProperty1", "testValue1");
 commandLine.addProperty("testProperty2", "testValue2");
 Set properties = commandLine.getProperties();
 assertTrue(properties.contains("testProperty1"));
 assertTrue(properties.contains("testProperty2"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("testOption"));
 assertFalse(commandLine.looksLikeOption("testOtherOption"));
 }

 @Test
 public void testToString() {
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 commandLine.addArgument("testArgument");
 commandLine.addArgument("testArgument2");
 String commandLineString = commandLine.toString();
 String expected = "testOption testValue testArgument testArgument2";
 assertEquals(expected, commandLineString);
 }

 @Test
 public void testGetOptions() {
 commandLine.addOption(option);
 List options = commandLine.getOptions();
 assertEquals(1, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine.addOption(option);
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(1, optionTriggers.size());
 assertTrue(optionTriggers.contains("testOption"));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine.addOption(option);commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
 List defaults = commandLine.getValues(option, null);
 assertEquals(2, defaults.size());
 assertEquals("defaultValue1", defaults.get(0));
 assertEquals("defaultValue2", defaults.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 Boolean defaultSwitch = commandLine.getSwitch(option, false);
 assertTrue(defaultSwitch);
 }

 @Test
 public void testGetNormalised() {
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 commandLine.addArgument("testArgument");
 commandLine.addArgument("testArgument2");
 List normalised = commandLine.getNormalised();
 assertEquals(4, normalised.size());
 }

 @Test(expected = ClassCastException.class)
 public void testGetValuesWithNullOption() {
 commandLine.getValues(null, null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetSwitchWithNullOption() {
 commandLine.getSwitch(null, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithEmptyOptionValueList() {
 commandLine.addOption(option);
 commandLine.addValue(option, new ArrayList());
 commandLine.getValues(option, null).get(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetSwitchWithEmptySwitchMap() {
 commandLine.addOption(option);
 commandLine.getSwitch(option, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyProperties() {
 commandLine.addProperty("testProperty", "testValue");
 commandLine.setProperty("testProperty", "modifiedValue");
 commandLine.getProperty("testProperty");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyOptions() {
 commandLine.addOption(option);
 commandLine.removeOption(option);
 commandLine.getOptions().size();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyValues() {
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 commandLine.setValue(option, "modifiedValue");
 commandLine.getValues(option, null).get(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifySwitches() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.setSwitch(option, false);
 commandLine.getSwitch(option, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyDefaultValues() {
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
 commandLine.setDefaultValues(option, null);
 commandLine.getValues(option, null).size();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyDefaultSwitches() {
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 commandLine.setDefaultSwitch(option, false);
 commandLine.getSwitch(option, null);
 }
}