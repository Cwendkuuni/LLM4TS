import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine = new CmdLineParser().parse("--optionValue value --option --anotherOption --property=value");

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 assertEquals("value", commandLine.getOptionValue(option));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addSwitch(option, true);
 assertEquals(true, commandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.setDefaultSwitch(option, true);
 assertEquals(true, commandLine.getOptionValue(option));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getOptionValue(option, "property"));
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getOptionValue("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("--optionValue"));
 }

 @Test
 public void testGetCurrentOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.parse("--optionValue value --option --anotherOption --property=value");
 assertEquals(option, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.parse("--optionValue value --option --anotherOption --property=value");
 commandLine.setCurrentOption(option);
 assertEquals(option, commandLine.getCurrentOption());
 }
}