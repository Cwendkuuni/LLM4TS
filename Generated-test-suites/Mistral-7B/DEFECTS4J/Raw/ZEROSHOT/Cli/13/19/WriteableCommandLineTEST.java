import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new Cmd(); // Assuming Cmd is a class implementing WriteableCommandLine
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(values.size(), 1);
 assertEquals(values.get(0), "value");
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "value1");
 assertEquals(values.get(1), "value2");
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "default1");
 assertEquals(values.get(1), "default2");
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals(commandLine.getProperty("property"), "value");
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @After
 public void tearDown() {
 commandLine = null;
 }

}