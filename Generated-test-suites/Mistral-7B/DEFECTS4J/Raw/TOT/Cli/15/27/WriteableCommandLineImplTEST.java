import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg", true, "an argument"), new ArrayList());
 option = new Option("t", "test", false, "a test flag");
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option actualOption = commandLine.getOption("t");
 assertEquals(option, actualOption);
 }

 @Test
 public void testGetValues_whenOptionHasValues_shouldReturnListOfValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetValues_whenOptionHasNoValues_shouldReturnEmptyList() {
 List values = commandLine.getValues(option, null);
 assertEquals(0, values.size());
 }

 @Test
 public void testGetUndefaultedValues_whenOptionHasValues_shouldReturnListOfValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues_whenOptionHasNoValues_shouldReturnEmptyList() {
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(0, values.size());
 }

 @Test
 public void testGetSwitch_whenOptionHasSwitchSet_shouldReturnTrue() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitch_whenOptionHasNoSwitchSet_shouldReturnFalse() {
 assertFalse(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitch_whenOptionHasDefaultSwitchSet_shouldReturnDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty_whenOptionHasPropertySet_shouldReturnPropertyValue() {
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getProperty(option, "property", null));
 }

 @Test
 public void testGetProperty_whenOptionHasNoPropertySet_shouldReturnNull() {
 assertEquals(null, commandLine.getProperty(option, "property", null));
 }

 @Test
 public void testGetProperties_whenOptionHasPropertiesSet_shouldReturnSetOfProperties() {
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Set properties = commandLine.getProperties(option);
 assertEquals(2, properties.size());
 assertTrue(properties.contains("property1"));
 assertTrue(properties.contains("property2"));
 }

 @Test
 public void testGetProperties_whenOptionHasNoPropertiesSet_shouldReturnEmptySet() {
 Set properties = commandLine.getProperties(option);
 assertEquals(0, properties.size());
 }

 @Test
 public void testToString() {
 assertEquals("-t --arg=value1 --arg=value2", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testGetOptionTriggers() {
 assertEquals(1, commandLine.getOptionTriggers().size());
 }
}