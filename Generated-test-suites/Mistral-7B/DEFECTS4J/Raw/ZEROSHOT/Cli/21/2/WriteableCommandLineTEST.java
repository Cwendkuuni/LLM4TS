import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 @Mock
 private Option option;

 @Before
 public void setUp() {
 commandLine = Mockito.spy(new DefaultWriteableCommandLine());
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 Mockito.verify(commandLine).addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value");
 Mockito.verify(option).setValue(ArgumentMatchers.eq("value"));
 }

 @Test
 public void testGetUndefaultedValues() {
 List<String> values = Arrays.asList("value1", "value2");
 Mockito.when(option.getValues()).thenReturn(values);
 List<Object> result = commandLine.getUndefaultedValues(option);
 Mockito.verify(option).getValues();
 Assert.assertEquals(values, result);
 }

 @Test
 public void testSetDefaultValues() {
 List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 Mockito.verify(option).setValue(defaultValues);
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 Mockito.verify(option).setValue(ArgumentMatchers.eq(true));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchAlreadyAdded() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 Mockito.verify(option).setDefaultValue(ArgumentMatchers.eq(true));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property", "value");
 Mockito.verify(option).addPropertyValue("property", "value");
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("property", "value");
 Mockito.verify(commandLine).addProperty(ArgumentMatchers.eq("property"), ArgumentMatchers.eq("value"));
 }

 @Test
 public void testLooksLikeOption() {
 String argument = "--option";
 boolean result = commandLine.looksLikeOption(argument);
 Mockito.verifyNoInteractions(option);
 Assert.assertTrue(result);
 }

 @Test
 public void testLooksLikeOptionNoDash() {
 String argument = "option";
 boolean result = commandLine.looksLikeOption(argument);
 Mockito.verifyNoInteractions(option);
 Assert.assertFalse(result);
 }

 @Test
 public void testGetCurrentOption() {
 Option currentOption = Mockito.mock(Option.class);
 commandLine.setCurrentOption(currentOption);
 Option result = commandLine.getCurrentOption();
 Mockito.verify(commandLine).getCurrentOption();
 Assert.assertEquals(currentOption, result);
 }

 @Test
 public void testSetCurrentOption() {
 Option currentOption = Mockito.mock(Option.class);
 commandLine.setCurrentOption(currentOption);
 }
}