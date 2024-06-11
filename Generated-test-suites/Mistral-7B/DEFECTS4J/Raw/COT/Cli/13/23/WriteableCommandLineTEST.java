import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
 List<Object> values = commandLine.getValuesForOption(option);
 assertEquals(2, values.size());
 assertEquals("file1.txt", values.get(0));
 assertEquals("file2.txt", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, "default1.txt", "default2.txt");
 assertEquals(2, commandLine.getNumDefaultValues(option));
 assertEquals("default1.txt", commandLine.getDefaultValue(option, 0));
 assertEquals("default2.txt", commandLine.getDefaultValue(option, 1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchMultiple() {
 Option option = new Option("silent", false, "s");
 commandLine.addSwitch(option, true);
 assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, false));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("silent", false, "s");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption(option.getLongOpt()));
 assertTrue(commandLine.isSetOption(option.getLongOpt()));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("silent", false, "s");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getDefaultBooleanValue(option));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getProperty("property1"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("arg"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option option = new Option("input", true, "i");
 commandLine.addOption(option);
 assertTrue(commandLine.looksLikeOption("input"));
 }
}