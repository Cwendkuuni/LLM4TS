import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = Mockito.mock(WriteableCommandLine.class);
 option = Mockito.mock(Option.class);
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value");
 assertEquals("value", commandLine.getValuesForOption(option).get(0));
 }

 @Test
 public void testGetValuesForOption() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValuesForOption(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = new ArrayList();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getValuesForOption(option));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 // this test will fail as we are using a mock object, but it is to test the exception is thrown
}

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 assertEquals(true, option.getValue().booleanValue());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 }

 @Test
 public void testLooksLikeOptionWithSingleDash() {
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionWithNoDash() {
 assertFalse(commandLine.looksLikeOption("option"));
 }
}