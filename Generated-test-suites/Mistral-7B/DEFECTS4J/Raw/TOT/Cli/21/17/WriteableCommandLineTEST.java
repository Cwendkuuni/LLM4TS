import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new CommonsCli2WriterCommandLine(); // Assuming a specific implementation of WriteableCommandLine
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);

 assert(commandLine.getOptions().size() == 1);
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .required(true)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");

 assert(commandLine.getOptions().get(0).getValue().equals(Arrays.asList("value")));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator(',')
 .required(true)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 List values = commandLine.getUndefaultedValues(option);

 assert(values.size() == 2);
 assert(values.get(0).equals("value1"));
 assert(values.get(1).equals("value2"));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator(',')
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));

 List values = commandLine.getUndefaultedValues(option);

 assert(values.isEmpty());
 assert(commandLine.getOptionValue(option, "defaultValue1") != null);
 assert(commandLine.getOptionValue(option, "defaultValue2") != null);
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("s").longOpt("switch")
 .required(false)
 .build();
 commandLine.addOption(option);

 try {
 commandLine.addSwitch(option, true);
 } catch (IllegalStateException e) {
 // Expected, switch has not been set to false
 }

 commandLine.addSwitch(option, false);
 commandLine.addSwitch(option, true);

 assert(commandLine.getOption(option).getSwitch() == true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("s").longOpt("switch")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);

 assert(commandLine.getOption(option).getSwitch() == true);
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("p").longOpt("property")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "name", "value");

 assert(commandLine.getOptions().get(0).getProperties().get("name").equals("value"));
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("name", "defaultValue");

 assert(commandLine.getProperty("name").equals("defaultValue"));
 }

 @Test
 public void testLooksLikeOption() {
 assert(commandLine.looksLikeOption("o") == true);
 assert(commandLine.looksLikeOption("--option") == true);
 assert(commandLine.looksLikeOption("--property") == false);
 }
}