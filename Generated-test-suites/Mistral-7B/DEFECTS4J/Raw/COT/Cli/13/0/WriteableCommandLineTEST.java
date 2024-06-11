import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").hasArg(false).build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 List values = commandLine.getOptionValues("o");
 assertEquals("value", values.get(0));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine.addOption(option);
 List defaultValues = Arrays.asList("def1", "def2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("def1", values.get(0));
 assertEquals("def2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build().required(false);
 commandLine.addSwitch(option, false);
 assertFalse(option.getValues().isEmpty());
 option.setValues(Collections.singletonList("on"));
 commandLine.addSwitch(option, true);
 assertTrue(option.getValues().contains("on"));
 assertTrue(option.getValues().contains("on"));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build().required(false);
 commandLine.addSwitch(option, false);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValues().isEmpty());
 assertFalse(option.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOptionWithShortOption() {
 commandLine = new WriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionWithLongOption() {
 commandLine = new WriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").build().required(false);
 commandLine.addSwitch(option, false);
 try {
 commandLine.addSwitch(option, true);
 } catch (IllegalStateException e) {
 assertEquals("Option 'o' already has switch value 'false'", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testGetEmptyValues() {
 commandLine = new WriteableCommandLine();
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine.addOption(option);
 List values = commandLine.getOptionValues("o");
 assertTrue(values.isEmpty());
 }

}