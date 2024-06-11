import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.commandline.Option;
import org.apache.commons.cli2.commandline.ValueDescription;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 commandLine = new BaseParser().parse(options, new String[0]);
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "option", true, "Option description");
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = NullPointerException.class)
 public void testAddOptionNullOption() {
 commandLine.addOption(null);
 }

 @Test
 public void testAddValue() {
 Option option = new Option("o", "option", true, "Option description");
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 List<String> values = commandLine.getOptionValue(option.getLongOpt(), 0);
 assertEquals("value", values.get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddValueNullOption() {
 commandLine.addValue(null, "value");
 }

 @Test
 public void testGetValues() {
 Option option = new Option("o", "option", true, "Option description");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues(option.getLongOpt());
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("o", "option", true, "Option description");
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues(option.getLongOpt());
 assertEquals(2, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("o", "option", false, "Option description");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.isSet(option.getLongOpt()));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchAlreadySet() {
 Option option = new Option("o", "option", false, "Option description");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("o", "option", false, "Option description");
 commandLine.setDefaultSwitch(option, true);
 assertFalse(commandLine.isSet(option.getLongOpt()));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "propertyValue");
 assertEquals("propertyValue", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }
}