import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineTest {

 private CommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(Arrays.asList("value1"), values);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueDuplicate() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testGetValuesEmpty() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 List<String> values = commandLine.getOptionValues("o");
 assertNull(values);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 List<String> defaults = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(defaults, values);
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchDuplicate() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("argument"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertTrue(commandLine.looksLikeOption("o"));
 }
}