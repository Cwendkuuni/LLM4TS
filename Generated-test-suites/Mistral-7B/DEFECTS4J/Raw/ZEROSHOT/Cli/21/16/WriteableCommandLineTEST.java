import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = PowerMockito.mock(WriteableCommandLine.class);
 option = new Option("option", "option", true, "Option description");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value");
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 opt.setValue("value");
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = commandLine.getUndefaultedValues(option);
 assertNull(values);
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 CommandLine.populateOptions(commandLine, Arrays.asList(option), false);
 PowerMockito.verifyStatic(List.class);
 List list = Mockito.mock(List.class);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testSetDefaultValues() {
 List defaults = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaults);
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 opt.setValue("");
 opt.setDefaultValues(defaults);
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 opt.setLongOpt("option");
 opt.setValue(null);
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 Option swOption = Mockito.mock(Option.class);
 swOption.setLongOpt("option");
 swOption.setValues(Collections.singletonList(Boolean.TRUE));
 PowerMockito.verifyStatic(Option.class);
 Option option = Mockito.mock(Option.class);
 Option option = PowerMockito.mockStatic(Option.class).when(Option.class, "builder").withArguments("option", "option", true, "Option description").get();
 PowerMockito.verifyStatic();
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 opt.setLongOpt("option");
 opt.setValues(Collections.singletonList(Boolean.TRUE));
 opt.setDefaultValue(Boolean.TRUE);
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property", "value");
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 Option opt = option;
 opt.setLongOpt("option");
 opt.setValue(null);
 opt.putLongOptValue("property", "value");
 CommandLine.populateOptions(commandLine, Arrays.asList(opt), false);
 PowerMockito.verifyStatic();
 }

 @Test
 public void testAddPropertyToDefaultProperties() {
 commandLine.addProperty("property", "value");
 PowerMockito.verifyStatic(CommandLine.class);
 CommandLine commandLine = Mockito.mock(CommandLine.class);
 CommandLine.populateOptions(commandLine, Collections.emptyList(), false);
 CommandLine.putDefaultProperty(commandLine, "property", "value");
 PowerMockito.verifyStatic();
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("--no-option"));
 assertFalse(commandLine.looksLikeOption("value"));
 PowerMockito.verifyStatic(CommandLine.class);
 PowerMockito.verifyNoMoreInteractions(CommandLine.class);
 }

 @Test
 public void testGetCurrentOption() {
 PowerMockito.when(commandLine.getCurrentOption()).thenReturn(option);
 Option currentOption = commandLine.getCurrentOption();
 assertEquals(option, currentOption);
 PowerMockito.verifyStatic(CommandLine.class);
 PowerMockito.verify(CommandLine.class).setCurrentOption(option);
 PowerMockito.verifyStatic();
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 PowerMockito.verifyStatic(Option.class);
 PowerMockito.verify(Option.class).builder().withArguments("option", "option", true, "Option description").build();
 PowerMockito.verify(Option.class).builder().withArguments("option", "option", true, "Option description").build();
 PowerMockito.verifyThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, true));
 PowerMockito.verifyStatic();
 }
}