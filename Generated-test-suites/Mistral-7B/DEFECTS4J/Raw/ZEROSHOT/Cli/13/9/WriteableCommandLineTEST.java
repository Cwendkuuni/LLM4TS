import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = mock(WriteableCommandLine.class);
 option = mock(Option.class);
 }

 @Test
 public void testAddOption() {
 command.addOption(option);
 verify(command, times(1)).addOption(option);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 command.addOption(option);
 command.addOption(option);
 verify(command, times(1)).addOption(option);
 }

 @Test
 public void testAddValue() {
 command.addValue(option, "value1");
 command.addValue(option, "value2");
 List<Object> values = command.getValuesForOption(option);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testGetValuesForOptionEmpty() {
 List<Object> values = command.getValuesForOption(option);
 assertEquals(Arrays.asList(), values);
 }

 @Test
 public void testSetDefaultValues() {
 List<String> defaultValues = Arrays.asList("default1", "default2");
 command.setDefaultValues(option, defaultValues);
 List<Object> values = command.getValuesForOption(option);
 assertEquals(defaultValues, values);
 }

 @Test
 public void testAddSwitch() {
 command.addSwitch(option, true);
 assertTrue(command.isSet(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 command.addSwitch(option, true);
 command.addSwitch(option, true);
 verify(command, times(1)).addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 command.setDefaultSwitch(option, true);
 assertTrue(command.isSet(option, true));
 }

 @Test
 public void testAddProperty() {
 command.addProperty("property", "value");
 assertEquals("value", command.getProperty("property"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(command.looksLikeOption("argument"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option option = new Option("option", true, "o", "short option");
 command.addOption(option);
 assertTrue(command.looksLikeOption("option"));
 }
}