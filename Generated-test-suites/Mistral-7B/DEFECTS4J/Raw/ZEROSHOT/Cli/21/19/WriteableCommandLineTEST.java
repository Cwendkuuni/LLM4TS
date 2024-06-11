import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineTest {

    @Mock
    private Options options;
    @Mock
    private Option option;
    @Mock
    private ParseContext parseContext;

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        when(commandLine.getOptions()).thenReturn(options);
        commandLine.addOption(option);
        verify(options).addOption(option);
    }

    @Test
    public void testAddValue() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getOption(anyString())).thenReturn(option);

        commandLine.addValue(option, "value1");
        verify(option).setValue("value1");

        commandLine.addValue(option, "value2");
        List<String> values = option.getValues();
        assertEquals(values.size(), 2);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getOption(anyString())).thenReturn(option);
        when(option.getValue(parseContext)).thenReturn("value1");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value1");
    }

    @Test
    public void testSetDefaultValues() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getOption(anyString())).thenReturn(option);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.setDefaultValues(option, values);
        verify(option).setValue(values);
    }

    @Test
    public void testAddSwitch() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getOption(anyString())).thenReturn(option);

        commandLine.addSwitch(option, true);
        verify(option).setValue(true);

        try {
            commandLine.addSwitch(option, false);
            fail();
        } catch (IllegalStateException e) {
            // expected
        }

        verify(option, times(1)).setValues(Collections.singletonList(Boolean.TRUE));
    }

    @Test
    public void testSetDefaultSwitch() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getOption(anyString())).thenReturn(option);

        commandLine.setDefaultSwitch(option, true);
        verify(option).setDefaultValue(true);
    }

    @Test
    public void testAddProperty() {
        when(commandLine.getOptions()).thenReturn(options);

        commandLine.addProperty(option, "property", "value");
        verify(option).addProperty("property", "value");
    }

    @Test
    public void testAddPropertyDefault() {
        when(commandLine.getOptions()).thenReturn(options);

        commandLine.addProperty("property", "value");
        verify(options).addProperty("property", "value");
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("--no-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        when(commandLine.getOptions()).thenReturn(options);
        when(commandLine.getCurrentOption()).thenReturn(option);

        Option currentOption = commandLine.getCurrentOption();
        assertEquals(currentOption, option);

        verify(commandLine).setCurrentOption(option);
    }

    @Test
    public void testSetCurrentOption() {
        when(commandLine.getOptions()).thenReturn(options);

        commandLine.setCurrentOption(option);
        verify(option).setParsed(true);
    }
}