import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine commandLine = new CliCommandLine(); // Assume CliCommandLine is a concrete implementation of WriteableCommandLine
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 assertEquals(option, commandLine.getOption("optionName"));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("value1"), values);
 }

 @Test
 public void testGetUndefaultedValues_NoValues() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 List values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetUndefaultedValues_WithDefaultValues() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
 List values = commandLine.getUndefaultedValues(option);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getOptionDefaultValues(option));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getOptionIsSet(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitch_OptionHasSwitch() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getOptionIsSet(option, false));
 assertFalse(commandLine.getOptionIsSet(option, true));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addOption(option);
 commandLine.addProperty(option, "propertyName", "propertyValue");
 assertEquals("propertyValue", commandLine.getOptionProperty(option, "propertyName"));
 }

 @Test
 public void testAddProperty_DefaultProperty() {
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.addProperty("propertyName", "propertyValue");
 assertEquals("propertyValue", commandLine.getOptionProperty("propertyName", null));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new CliCommandLine();
 assertTrue(commandLine.looksLikeOption("-o"));
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @Test
 public void testGetCurrentOption_Null() {
 WriteableCommandLine commandLine = new CliCommandLine();
 assertNull(commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new CliCommandLine();
 commandLine.setCurrentOption(option);
 assertSame(option, commandLine.getCurrentOption());
 }
}