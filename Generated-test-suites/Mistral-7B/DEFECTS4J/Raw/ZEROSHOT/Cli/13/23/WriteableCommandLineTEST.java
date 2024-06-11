import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private CommandLine commandLine = new DefaultCommandLine();

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("i")
 .longOpt("input")
 .valueSeparator('=')
 .required(true)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 List<String> values = commandLine.getOptionValues("input");
 assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("option");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("i")
 .longOpt("input")
 .required(true)
 .build();
 commandLine.addOption(option);
 List<String> defaultValues = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues("input");
 assertTrue(values.isEmpty());
 List<String> defaultValue = commandLine.getOptionDefaultValues("input");
 assertEquals(2, defaultValue.size());
 assertEquals("default1", defaultValue.get(0));
 assertEquals("default2", defaultValue.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("d").longOpt("debug")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption("debug"));
 assertTrue(commandLine.getOptionValues("debug").isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("d").longOpt("debug")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("d").longOpt("debug")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getOptionValues("debug").isEmpty());
 assertTrue(commandLine.isSet("debug"));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getOptionValue("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("-o"));
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("--option="));
 assertFalse(commandLine.looksLikeOption("--option=" + "value"));
 assertFalse(commandLine.looksLikeOption("option"));
 }

}