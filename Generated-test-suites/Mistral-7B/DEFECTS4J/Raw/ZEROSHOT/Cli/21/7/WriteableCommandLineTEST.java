import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WriteableCommandLine.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setup() {
 commandLine = PowerMockito.mock(WriteableCommandLine.class);
 option = new BaseOption("option", true, "desc");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 Mockito.verify(commandLine).addOption(option);
 }

 @Test
 public void testAddValue() {
 List<String> values = new ArrayList<>();
 values.add("value1");
 commandLine.addValue(option, values);
 Mockito.verify(commandLine).addValue(option, values.toArray(new String[values.size()]));
 }

 @Test
 public void testGetUndefaultedValues() {
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 PowerMockito.whenNew(ParseResult.class).withNoArguments().thenReturn(new ParseResult());
 PowerMockito.when(commandLine.getParsedResult()).thenReturn(new ParseResult());
 PowerMockito.when(commandLine.getParsedResult().getValues("option")).thenReturn(expectedValues);
 List<String> actualValues = commandLine.getUndefaultedValues(option);
 assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testSetDefaultValues() {
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 commandLine.setDefaultValues(option, defaultValues);
 Mockito.verify(commandLine).setDefaultValues(option, defaultValues.toArray(new String[defaultValues.size()]));
 }

 @Test
 public void testAddSwitch() {
 PowerMockito.when(commandLine.getDefaultSwitch("option")).thenReturn(false);
 commandLine.addSwitch(option, true);
 Mockito.verify(commandLine).addSwitch(option, true);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 PowerMockito.when(commandLine.getDefaultSwitch("option")).thenReturn(false);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 Mockito.verify(commandLine).setDefaultSwitch(option, true);
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property", "value");
 Mockito.verify(commandLine).addProperty(option, "property", "value");
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("property", "value");
 Mockito.verify(commandLine).addProperty("property", "value");
 }

 @Test
 public void testLooksLikeOption() {
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 PowerMockito.when(commandLine.looksLikeOption("--option")).thenReturn(true);
 PowerMockito.verify(commandLine).looksLikeOption(argumentCaptor.capture());
 assertEquals("--option", argumentCaptor.getValue());
 }

 @Test
 public void testGetCurrentOption() {
 Option currentOption = PowerMockito.mock(Option.class);
 PowerMockito.when(commandLine.getCurrentOption()).thenReturn(currentOption);
 assertEquals(currentOption, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option currentOption = PowerMockito.mock(Option.class);
 commandLine.setCurrentOption(currentOption);
 Mockito.verify(commandLine).setCurrentOption(currentOption);
 }
}