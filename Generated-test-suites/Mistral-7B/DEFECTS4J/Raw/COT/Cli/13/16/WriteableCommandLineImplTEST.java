import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("optionA", "option-a", false, "Option A"), new ArrayList<>());
 commandLine.addOption(new Option("optionB", "option-b", true, "Option B"));
 List options = commandLine.getOptions();
 assertEquals(2, options.size());
 Option optionA = (Option) options.get(0);
 assertEquals("option-a", optionA.getLongOpt());
 assertEquals(false, optionA.isRequired());
 Option optionB = (Option) options.get(1);
 assertEquals("option-b", optionB.getLongOpt());
 assertEquals(true, optionB.isRequired());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", true, "Option"));
 commandLine.addValue(new Option("option"), "value1");
 commandLine.addValue(new Option("option"), "value2");
 List values = commandLine.getValues(new Option("option"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 commandLine.addSwitch(new Option("option"), true);
 Boolean switchValue = commandLine.getSwitch(new Option("option"), false);
 assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 assertTrue(commandLine.hasOption(new Option("option")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 Option option = new Option("option", "opt");
 assertEquals(option, commandLine.getOption(new Option("option").getLongOpt()));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 commandLine.addValue(new Option("option"), "value1");
 commandLine.addValue(new Option("option"), "value2");
 List values = commandLine.getValues(new Option("option"), null);
 assertEquals(2, values.size());
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 commandLine.addSwitch(new Option("option"), true);
 Boolean switchValue = commandLine.getSwitch(new Option("option"), false);
 assertTrue(switchValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));
 commandLine.addSwitch(new Option("option"), true);
 commandLine.addSwitch(new Option("option"), true);
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));commandLine.setDefaultValues(new Option("option"), Arrays.asList("default1", "default2"));List values = commandLine.getValues(new Option("option"), null);assertEquals(2, values.size());assertEquals("default1", values.get(0));assertEquals("default2", values.get(1)); }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));commandLine.setDefaultSwitch(new Option("option"), true);Boolean switchValue = commandLine.getSwitch(new Option("option"), false);assertTrue(switchValue); }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());
 commandLine.addOption(new Option("option", "opt", false, "Option"));commandLine.addValue(new Option("option"), "value1");commandLine.addValue(new Option("option"), "value2");assertEquals("value1 value2", commandLine.toString()); }

 @Test
 public void testLooksLikeOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());commandLine.addOption(new Option("option", "opt", false, "Option"));assertTrue(commandLine.looksLikeOption("opt")); }

 @Test
 public void testGetProperties() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());commandLine.addProperty("property", "value");Set properties = commandLine.getProperties();assertEquals(1, properties.size());assertEquals("property", properties.iterator().next()); }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option"), new ArrayList<>());commandLine.addProperty("property", "value");assertEquals("value", commandLine.getProperty("property", "")); }

}