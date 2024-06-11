import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option optionWithValue;
 private Option optionWithSwitch;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option("test", false, null, "Test option"), new ArrayList());
 optionWithValue = new Option("value", true, Integer.TYPE, "Specify a value");
 optionWithSwitch = new Option("switch", false, null, "Set a switch");
 commandLine.addOption(optionWithValue);
 commandLine.addOption(optionWithSwitch);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(optionWithValue, 42);
 List values = commandLine.getValues(optionWithValue, Collections.emptyList());
 assertEquals(1, values.size());
 assertEquals(42, values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(optionWithSwitch, true);
 assertTrue(commandLine.getSwitch(optionWithSwitch, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchAlreadySet() {
 commandLine.addSwitch(optionWithSwitch, true);
 commandLine.addSwitch(optionWithSwitch, true);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(optionWithValue));
 assertFalse(commandLine.hasOption(new Option("unknown", false, null, "Unknown option")));
 }

 @Test
 public void testGetOption() {
 Option option = commandLine.getOption("value");
 assertSame(optionWithValue, option);
 }

 @Test
 public void testGetValues() {
 commandLine.addValue(optionWithValue, "value1");
 commandLine.addValue(optionWithValue, "value2");
 List values = commandLine.getValues(optionWithValue, Collections.emptyList());
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine.addValue(optionWithValue, "value1");
 List values = commandLine.getUndefaultedValues(optionWithValue);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addSwitch(optionWithSwitch, true);
 assertTrue(commandLine.getSwitch(optionWithSwitch, false));
 }

 @Test
 public void testGetProperty() {
 Option propertyOption = new Option("property", false, null, "Set a property");
 commandLine.addOption(propertyOption);
 commandLine.addProperty(propertyOption, "name", "value");
 assertEquals("value", commandLine.getProperty(propertyOption, ""));
 }

 @Test
 public void testAddProperty() {
 Option propertyOption = new Option("property", false, null, "Set a property");
 commandLine.addOption(propertyOption);
 commandLine.addProperty(propertyOption, "name1", "value1");
 commandLine.addProperty(propertyOption, "name2", "value2");
 Map properties = commandLine.getProperties(propertyOption);
 assertEquals(2, properties.size());
 assertEquals("value1", properties.get("name1"));
 assertEquals("value2", properties.get("name2"));
 }

 @Test
 public void testGetProperties() {
 Map properties = commandLine.getProperties();
 assertNull(properties);
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("test"));
 assertFalse(commandLine.looksLikeOption("unknown"));
 }

 @Test
 public void testToString() {
 String expected = "test 42 value1 value2";
 assertEquals(expected, commandLine.toString());
 }
}