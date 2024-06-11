import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new CliCommandLine(); // Assuming CliCommandLine is a concrete implementation of WriteableCommandLine
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);

 assert(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 List<String> values = commandLine.getUndefaultedValues(option);
 assert(values.containsAll(Arrays.asList("value1", "value2")));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 List<String> values = commandLine.getUndefaultedValues(option);
 assert(values.containsAll(Arrays.asList("value1", "value2")));

 List<String> emptyValues = commandLine.getUndefaultedValues(new Option("", ""));
 assert(emptyValues.isEmpty());
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);

 List<String> values = commandLine.getUndefaultedValues(option);
 assert(values.isEmpty());

 List<String> retrievedDefaultValues = commandLine.getValues(option);
 assert(retrievedDefaultValues.containsAll(defaultValues));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);

 assert(commandLine.getOptionState(option).getValue().equals(Boolean.TRUE));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, Boolean.TRUE);

 assert(commandLine.getOptionState(option).getValue().equals(Boolean.TRUE));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "propertyValue");

 assert(commandLine.getProperties().get("option.property").equals("propertyValue"));
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("property", "propertyValue");

 assert(commandLine.getProperty("property").equals("propertyValue"));
 }

 @Test
 public void testLooksLikeOption() {
 assert(!commandLine.looksLikeOption(""));
 assert(commandLine.looksLikeOption("--option"));
 }
}