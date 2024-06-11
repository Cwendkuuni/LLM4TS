import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<Object> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 1);
 assertEquals(values.get(0), "value1");
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<Object> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "value1");
 assertEquals(values.get(1), "value2");
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 List<Object> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List<Object> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "defaultValue1");
 assertEquals(values.get(1), "defaultValue2");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 fail("Should throw IllegalStateException when adding switch value twice");
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getOption(option.getLongOpt()).isSet());
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getOption(option.getLongOpt()).isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLine();
 commandLine.addProperty("propertyName", "propertyValue");
 assertEquals(commandLine.getProperties().get("propertyName"), "propertyValue");
 }

 @Test
 public void testLooksLikeOptionWithLongOptionName() {
 commandLine = new WriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--optionName"));
 }

 @Test
 public void testLooksLikeOptionWithShortOptionName() {
 commandLine = new WriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionWithEmptyString() {
 commandLine = new WriteableCommandLine();
 assertFalse(commandLine.looksLikeOption(""));
 }

 @Test
 public void testLooksLikeOptionWithOptionValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 assertFalse(commandLine.looksLikeOption("value"));
 }
}