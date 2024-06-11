import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new Cmd(); // Assume Cmd is a concrete implementation of WriteableCommandLine
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 assertEquals("testValue", commandLine.getUndefaultedValues(option).get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueToExistingOption() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 commandLine.addValue(option, "anotherValue");
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue1");
 commandLine.addValue(option, "testValue2");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("testValue1", values.get(0));
 assertEquals("testValue2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .build();
 List defaults = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaults);
 List values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 assertArrayEquals(defaults.toArray(), commandLine.getValues(option).toArray());
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getValue(option.getName(), Boolean.class, null));
 commandLine.addSwitch(option, false);
 assertFalse(commandLine.getValue(option.getName(), Boolean.class, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchToExistingOption() {
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getValue(option.getName(), Boolean.class, null));
 commandLine.setDefaultSwitch(option, false);
 assertFalse(commandLine.getValue(option.getName(), Boolean.class, null));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.addProperty(option, "property", "testProperty");
 assertEquals("testProperty", commandLine.getValue(option.getName() + ".property", String.class, null));
 }

 @Test
 public void testAddDefaultProperty() {
 commandLine.addProperty("property", "testProperty");
 assertEquals("testProperty", commandLine.getValue("property", String.class, null));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("testValue"));
 }

 @Test
 public void testgetCurrentOption() {
 // Assuming getCurrentOption() returns the currently processed option
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.addOption(option);
 commandLine.parse("--option value");
 assertEquals(option, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 // Assuming setCurrentOption() sets the currently processed option
 Option option = Option.builder("o").longOpt("option")
 .build();
 commandLine.setCurrentOption(option);
 assertEquals(option, commandLine.getCurrentOption());
 }
}