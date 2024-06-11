import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.cli2.OptionBuilder.withLongOpt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private BaseParser parser;

    @Before
    public void setUp() {
        parser = Mockito.mock(BaseParser.class);
        commandLine = PowerMockito.mock(WriteableCommandLine.class);
        option = OptionBuilder.withLongOpt("option")
                .withValueSeparator(':')
                .withDescription("Test option")
                .create(parser);
        PowerMockito.when(commandLine.addOption(Mockito.any(Option.class)))
                .thenReturn(commandLine);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        commandLine.addValue(option, values);
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = new ArrayList<>();
        PowerMockito.when(commandLine.getUndefaultedValues(option))
                .thenReturn(values);
        assertSame(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(defaultSwitch.equals(commandLine.getOptionState(option).getValue()));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        assertSame("value", commandLine.getOptionState(option).getValue("property"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("property", "value");
        assertSame("value", commandLine.getOptionState(option).getValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        ParseContext parseContext = Mockito.mock(ParseContext.class);
        PowerMockito.when(commandLine.getCurrentOption())
                .thenReturn(option);
        assertSame(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        assertSame(currentOption, commandLine.getCurrentOption());
    }
}