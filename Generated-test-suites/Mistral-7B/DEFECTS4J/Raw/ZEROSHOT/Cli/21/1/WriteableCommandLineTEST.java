import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertNotNull(commandLine.getOption("o"));
 }

 @Test
 public void testAddValue() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("v").longOpt("value").valueSeparator(':').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("test"), values);
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("v").longOpt("value").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test1");
 commandLine.addValue(option, "test2");
 List<String> values1 = commandLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("test1", "test2"), values1);
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("d").longOpt("default").build();
 List<String> defaults = new ArrayList<>();
 defaults.add("default1");
 defaults.add("default2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 List<String> defaultsValues = commandLine.getOptionValues("d");
 assertEquals(defaults, defaultsValues);
 }

 @Test
 public void testAddSwitch() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 assertFalse(option.getValues().isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithExists() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.getValues().isEmpty());
 }

 @Test
 public void testAddProperty() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("p").longOpt("property").build();
 commandLine.addProperty(option, "name", "value");
 assertEquals("value", commandLine.getPropertyValues("p")[0]);
 }

 @Test
 public void testAddPropertyWithMultipleValues() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("p").longOpt("property").build();
 commandLine.addProperty(option, "name", "value1, value2");
 List<String> values = Arrays.asList(commandLine.getPropertyValues("p"));
 assertTrue(values.size() > 1);
 }

 @Test
 public void testAddPropertyWithDefaultValue() {
 commandLine = new CliCommandLine();
 commandLine.addProperty("name", "defaultValue");
 assertEquals("defaultValue", commandLine.getPropertyValues("name")[0]);
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new CliCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 commandLine = new CliCommandLine();
 assertFalse(commandLine.looksLikeOption("--test"));
 assertFalse(commandLine.looksLikeOption("-t"));
 }

 @Test
 public void testCurrentOption() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.parse("--option value");
 assertEquals(option, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 commandLine = new CliCommandLine();
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 commandLine.parse("--option1 value1 --option2 value2");
 commandLine.setCurrentOption(option1);
 assertEquals(option1, commandLine.getCurrentOption());
 commandLine.setCurrentOption(option2);
 assertEquals(option2, commandLine.getCurrentOption());
 }
}