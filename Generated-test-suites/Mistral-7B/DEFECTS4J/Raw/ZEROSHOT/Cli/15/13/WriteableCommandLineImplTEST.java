import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("t", "test", false, "Test Option");
 commandLine = new WriteableCommandLineImpl(option, Arrays.asList("testOption"));
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 List options = commandLine.getOptions();
 assertTrue(options.contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value1");
 List values = commandLine.getValues(option, null);
 assertTrue(values.contains("value1"));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option optionFromCommandLine = commandLine.getOption("t");
 assertSame(option, optionFromCommandLine);
 }

 @Test
 public void testGetValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertTrue(values.contains("value1"));
 assertTrue(values.contains("value2"));
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 assertNull(commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty(option, "property", "value1");
 String propertyValue = commandLine.getProperty(option, "property");
 assertEquals("value1", propertyValue);
 }

 @Test
 public void testAddProperty() {
 Properties properties = new Properties();
 properties.put("property1", "value1");
 commandLine.addProperty(option, "property", "value2");
 commandLine.addProperty(option, "property1", properties);
 Map propertiesMap = commandLine.getProperties(option);
 assertEquals(properties, propertiesMap.get("property1"));
 assertEquals("value2", propertiesMap.getProperty("property"));
 }

 @Test
 public void testGetProperties() {
 Properties properties = new Properties();
 properties.put("property1", "value1");
 commandLine.addProperty(option, "property", "value2");
 commandLine.addProperty(option, "property1", properties);
 Set propertiesSet = commandLine.getProperties(option);
 assertTrue(propertiesSet.contains("property"));
 assertTrue(propertiesSet.contains("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("t"));
 }

 @Test
 public void testToString() {
 String commandLineString = commandLine.toString();
 assertEquals("testOption", commandLineString);
 }

 @Test
 public void testGetOptions() {
 List optionsList = commandLine.getOptions();
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testGetOptionTriggers() {
 Set optionTriggers = commandLine.getOptionTriggers();
 assertTrue(optionTriggers.contains("t"));
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValues(option, null);
 assertTrue(values.containsAll(defaultValues));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 List expectedNormalised = Arrays.asList("testOption");
 assertTrue(normalised.containsAll(expectedNormalised));
 }
}