import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 Option addedOption = commandLine.getOption("o");
 assertSame(option, addedOption);
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("o").longOpt("option").build();
 List defaults = List.of("value1", "value2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getOption("s").isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getOption("s").isSet());
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = new CliOptions();
 Option option = Option.builder("p").longOpt("property").build();
 commandLine.addProperty(option, "name", "value");
 assertEquals("value", commandLine.getOption("p").getValue("name"));
 }

 @Test
 public void testAddPropertyDefault() {
 WriteableCommandLine commandLine = new CliOptions();
 commandLine.addProperty("name", "value");
 assertEquals("value", commandLine.getValue("name"));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new CliOptions();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }
}