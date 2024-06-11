import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Value;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 CommandLineParser parser = new BaseParser();
 Option option = Option.builder("o")
 .longOpt("option")
 .required(false)
 .valueSeparator('=')
 .build();
 Option valueOption = Option.builder("v")
 .longOpt("value")
 .required(true)
 .valueSeparator(Value.NONE)
 .build();
 commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addOption(valueOption);
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o")
 .longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 Option option = Option.builder("o")
 .longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 try {
 commandLine.addOption(option);
 } catch (IllegalStateException e) {
 assertEquals("Option 'option' already added", e.getMessage());
 }
 }

 @Test
 public void testAddValue() {
 Option option = (Option) commandLine.getOptions().get(0);
 Object value = new Object();
 commandLine.addValue(option, value);
 assertEquals(value, option.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueToNonOption() {
 commandLine.addValue(null, "value");
 fail("Should not be able to add value to null");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueToOptionWithValue() {
 Option option = (Option) commandLine.getOptions().get(0);
 option.setValue("value");
 try {
 commandLine.addValue(option, "newValue");
 } catch (IllegalStateException e) {
 assertEquals("Option 'option' already has a value 'value'", e.getMessage());
 }
 }

 @Test
 public void testGetValues() {
 Option option = (Option) commandLine.getOptions().get(1);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<Object> values = commandLine.getValuesForOption(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0).toString());
 assertEquals("value2", values.get(1).toString());
 }

 @Test
 public void testSetDefaultValues() {
 Option option = (Option) commandLine.getOptions().get(0);
 List<String> defaultValues = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<Object> values = commandLine.getValuesForOption(option);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0).toString());
 assertEquals("default2", values.get(1).toString());
 }

 @Test
 public void testAddSwitch() {
 Option option = (Option) commandLine.getOptions().get(0);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValues().contains(Boolean.TRUE));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchToOptionWithoutSwitch() {
 Option option = (Option) commandLine.getOptions().get(1);
 commandLine.addSwitch(option, true);
 fail("Should not be able to add switch to non-switch option");
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = (Option) commandLine.getOptions().get(0);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValues().contains(Boolean.TRUE));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("value"));
 }
}