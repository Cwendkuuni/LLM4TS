import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 assertNotNull(commandLine.getOptions().get(0));
 }

 @Test
 public void testAddValue() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.addValue(option, "file.txt");
 assertEquals("file.txt", commandLine.getOptionValue(option));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.addValue(option, "file1.txt");
 commandLine.addValue(option, "file2.txt");
 List<String> values = commandLine.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("file1.txt", values.get(0));
 assertEquals("file2.txt", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("default1.txt", "default2.txt"));
 List<String> defaults = commandLine.getOptionDefaultValues(option);
 assertEquals(2, defaults.size());
 assertEquals("default1.txt", defaults.get(0));
 assertEquals("default2.txt", defaults.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("verbose", false, "v");
 commandLine.addOption(option);
 assertFalse(option.getValue().isPresent());
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("verbose", false, "v");
 commandLine.addOption(option);
 assertFalse(option.getValue().isPresent());
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertTrue(option.getValue().get());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getProperty("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--input"));
 assertTrue(commandLine.looksLikeOption("-i"));
 assertFalse(commandLine.looksLikeOption("file.txt"));
 assertFalse(commandLine.looksLikeOption(""));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithExisting() {
 Option option = new Option("verbose", false, "v");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testGetOptionValueWithNoValue() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 assertNull(commandLine.getOptionValue(option));
 }

 @Test
 public void testGetOptionValuesWithNoValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 List<String> values = commandLine.getOptionValues(option);
 assertNotNull(values);
 assertEquals(0, values.size());
 }

 @Test
 public void testGetOptionValueWithEmptyValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.addValue(option, "");
 List<String> values = commandLine.getOptionValues(option);
 assertNotNull(values);
 assertEquals(0, values.size());
 }

 @Test
 public void testGetOptionValueWithNullValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.addValue(option, null);
 List<String> values = commandLine.getOptionValues(option);
 assertNotNull(values);
 assertEquals(0, values.size());
 }

 @Test
 public void testGetOptionValueWithUnexpectedType() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.addValue(option, 42);
 List<String> values = commandLine.getOptionValues(option);
 assertNotNull(values);
 assertEquals(0, values.size());
 }
}