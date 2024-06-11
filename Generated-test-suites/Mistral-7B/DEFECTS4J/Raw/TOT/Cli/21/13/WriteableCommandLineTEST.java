import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
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
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert(values.equals(Arrays.asList("value1", "value2")));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assert(values.equals(Arrays.asList("value1", "value2")));

 Option optionWithDefaultValues = Option.builder("o").longOpt("option").build();
 commandLine.setDefaultValues(optionWithDefaultValues, Arrays.asList("defaultValue1", "defaultValue2"));
 List<String> defaultValues = commandLine.getUndefaultedValues(optionWithDefaultValues);
 assert(defaultValues.isEmpty());
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assert(!option.getValues().contains(Boolean.TRUE));
 try {
 commandLine.addSwitch(option, true);
 } catch (IllegalStateException e) {
 // expected
 }

 Option optionWithDefaultSwitch = Option.builder("o").longOpt("option").build();
 commandLine.setDefaultSwitch(optionWithDefaultSwitch, true);
 assert(optionWithDefaultSwitch.getValues().contains(Boolean.TRUE));
 }

 @Test
 public void testLooksLikeOption() {
 assert(commandLine.looksLikeOption("o"));
 assert(!commandLine.looksLikeOption("value"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 }

 @Test
 public void testAddDefaultProperty() {
 commandLine.addProperty("property", "defaultValue");
 assert(commandLine.getGlobalOptions().get(0).getProperties().get("property").getValue().equals("defaultValue"));
 }
}