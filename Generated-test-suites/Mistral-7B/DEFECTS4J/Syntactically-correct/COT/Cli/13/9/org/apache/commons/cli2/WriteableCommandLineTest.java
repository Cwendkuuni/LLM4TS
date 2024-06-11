import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 assertEquals("value", commandLine.getOptionValue(option));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 List defaultValues = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testAddSwitch() throws IllegalStateException {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() throws IllegalStateException {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getPropertyValues("property").get(0));
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new WriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @Test
 public void testEmptyCommandLine() {
 commandLine = new WriteableCommandLine();
 assertNotNull(commandLine.getOptions());
 assertFalse(commandLine.getOptions().isEmpty());
 }

 @Test
 public void testNullOption() {
 Option nullOption = null;
 commandLine = new WriteableCommandLine();
 assertThrows(NullPointerException.class, () -> commandLine.addOption(nullOption));
 }

 @Test
 public void testNullValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertThrows(NullPointerException.class, () -> commandLine.addValue(option, null));
 }

 @Test
 public void testNullDefaultValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertThrows(NullPointerException.class, () -> commandLine.setDefaultValues(option, null));
 }

 @Test
 public void testNullSwitchValue() throws IllegalStateException {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertThrows(NullPointerException.class, () -> commandLine.addSwitch(option, null));
 }
}