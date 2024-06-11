import org.apache.commons.cli2.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("o", "option", true, "An option with value");
 commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("testValue"));
 }

 @After
 public void tearDown() {
 commandLine = null;
 option = null;
 }

 @Test
 public void testAddOption() {
 assertFalse(commandLine.getOptions().contains(option));
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 assertFalse(commandLine.getValues(option, null).isEmpty());
 commandLine.addValue(option, "newValue");
 assertFalse(commandLine.getValues(option, null).contains("testValue"));
 assertTrue(commandLine.getValues(option, null).contains("newValue"));
 }

 @Test
 public void testAddSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option retrievedOption = commandLine.getOption("o");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(option, null);
 assertEquals(Collections.singletonList("testValue"), values);
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(Collections.singletonList("testValue"), values);
 }

 @Test
 public void testGetSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testgetProperty() {
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getProperty(option, "property"));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Map properties = commandLine.getProperties(option);
 assertEquals(2, properties.size());
 assertTrue(properties.containsKey("property1"));
 assertTrue(properties.containsKey("property2"));
 }

 @Test
 public void testGetProperties() {
 Map properties = commandLine.getProperties(option);
 assertTrue(properties.isEmpty());
 }

 @Test
 public void testToString() {
 assertEquals("testValue", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testGetOptionTriggers() {
 assertEquals(1, commandLine.getOptionTriggers().size());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine.setDefaultValues(option, Collections.singletonList("defaultValue"));
 assertEquals(Collections.singletonList("defaultValue"), commandLine.getValues(option, null));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testGetNormalised() {
 assertEquals(1, commandLine.getNormalised().size());
 }
}