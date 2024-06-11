import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

public class WriteableCommandLineTest {

 private CommandLine commandLine = new DefaultCommandLine();

 @Test
 public void testAddOption() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddOptionThrowsExceptionWhenCommandLineIsFixed() {
 CommandLine commandLine = new DefaultCommandLine("commandName", null);
 commandLine.setParsed(true);
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List values = commandLine.getOptionValues("opt");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddValueToNullOption() {
 Option option = null;
 commandLine.addValue(option, "value1");
 assertNull(commandLine.getOptionValues("opt"));
 }

 @Test
 public void testGetOptionValues() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List values = commandLine.getOptionValues("opt");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetOptionValuesEmptyList() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 List values = commandLine.getOptionValues("opt");
 assertFalse(values.isEmpty());
 assertNull(values.get(0));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 List defaultValues = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getOptionValues("opt");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 Option parsedOption = commandLine.parseOption("--longOpt", true);
 assertEquals(option, parsedOption);
 assertTrue(parsedOption.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchThrowsExceptionWhenSwitchIsSet() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.setDefaultSwitch(option, true);
 Option parsedOption = commandLine.parseOption("--longOpt", false);
 assertFalse(parsedOption.isSet());
 Option parsedOption2 = commandLine.parseOption("--longOpt", true);
 assertTrue(parsedOption2.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getProperty("property1"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("arg"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option option = Option.builder("opt").longOpt("longOpt").build();
 commandLine.addOption(option);
 assertTrue(commandLine.looksLikeOption("--longOpt"));
 }
}