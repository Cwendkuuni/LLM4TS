import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WriteableCommandLine.class})
public class WriteableCommandLineTest {

    private CommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(CommandLine.class);
        option = Mockito.mock(Option.class);
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addOption(option);
        PowerMockito.verifyNew(Option.class).withAnyArguments();
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("addOption", option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addValue(option, "value");
        PowerMockito.verifyPrivate(option).invoke("setValue", "value");
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("addValue", option, "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        PowerMockito.when(option.getValues()).thenReturn(values);
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        List<Object> result = writeableCommandLine.getUndefaultedValues(option);
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("getUndefaultedValues", option);
        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        PowerMockito.when(option.getValues()).thenReturn(values);
        writeableCommandLine.setDefaultValues(option, values);
        PowerMockito.verifyPrivate(option).invoke("setValue", values);
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("setDefaultValues", option, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
        PowerMockito.verifyPrivate(option).times(1).invoke("setValue", true);
        PowerMockito.verifyPrivate(writeableCommandLine).times(1).invoke("addSwitch", option, true);
        PowerMockito.verifyPrivate(writeableCommandLine).times(1).invoke("setDefaultSwitch", option, true);
    }

    @Test
    public void testAddSwitchWithoutValue() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addSwitch(option, false);
        PowerMockito.verifyPrivate(option).times(1).invoke("setValue", false);
        PowerMockito.verifyPrivate(writeableCommandLine).times(1).invoke("addSwitch", option, false);
        PowerMockito.verifyPrivate(writeableCommandLine).times(1).invoke("setDefaultSwitch", option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.setDefaultSwitch(option, true);
        PowerMockito.verifyPrivate(option).invoke("setValue", true);
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("setDefaultSwitch", option, true);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addProperty(option, "property", "value");
        PowerMockito.verifyPrivate(option).invoke("addPropertyValue", "property", "value");
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("addProperty", option, "property", "value");
    }

    @Test
    public void testAddPropertyToDefaultProperties() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        writeableCommandLine.addProperty("property", "value");
        PowerMockito.verifyPrivate(commandLine).invoke("addProperty", "property", "value");
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        Option currentOption = Mockito.mock(Option.class);
        PowerMockito.when(commandLine.getCurrentOption()).thenReturn(currentOption);
        PowerMockito.verifyPrivate(writeableCommandLine).invoke("getCurrentOption");
        assertEquals(currentOption, writeableCommandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine writeableCommandLine = PowerMockito.spy(commandLine);
        Option currentOption = Mockito.mock(Option.class);
        PowerMockito.when(commandLine.getCurrentOption()).thenReturn(null);
        PowerMockito.doNothing().when(commandLine).setCurrentOption(currentOption);
        writeableCommandLine.setCurrentOption(currentOption);
        PowerMockito.verifyPrivate(commandLine).invoke("setCurrentOption", currentOption);
    }
}