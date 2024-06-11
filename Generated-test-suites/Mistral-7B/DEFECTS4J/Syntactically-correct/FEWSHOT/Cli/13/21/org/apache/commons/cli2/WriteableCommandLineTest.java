package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        cl.addOption(option);

        // verify call to AddOption method in mocked CommandLine
        assertTrue(cl.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupported() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        cl.addValue(option, "value");
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        when(option.getValueGlob()).thenReturn(new ArrayList<String>());

        cl.addOption(option);
        cl.addValue(option, "value");

        // verify call to add value to Option's value list
        List<String> values = option.getValueGlob();
        assertTrue(values.size() > 0);
        assertTrue(values.get(0).equals("value"));

        // verify call to add Option to CommandLine's options list
        List<Option> options = cl.getOptions();
        assertTrue(options.size() > 0);
        assertTrue(options.get(0).equals(option));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Option option2 = mock(Option.class);
        List<Object> values = new ArrayList<Object>();
        values.add("value1");
        values.add("value2");

        when(option.getValueGlob()).thenReturn(values);
        when(cl.getOptions()).thenReturn(Arrays.asList(option, option2));

        List<String> result = cl.getOptionValues("option");

        // verify call to get Option by name
        Option optionFromCommandLine = cl.getOption("option");
        assertTrue(optionFromCommandLine.equals(option));

        // verify call to get Option's value
        List<String> optionValues = optionFromCommandLine.getValueGlob();
        assertTrue(optionValues.size() == 2);
        assertTrue(optionValues.get(0).equals("value1"));
        assertTrue(optionValues.get(1).equals("value2"));

        // verify that other Option doesn't have values
        Option option2FromCommandLine = cl.getOption("option2");
        assertNull(option2FromCommandLine.getValueGlob());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<Object> values = new ArrayList<Object>();
        values.add("value1");
        values.add("value2");

        when(cl.getOptions()).thenReturn(Arrays.asList(option));

        cl.setDefaultValues(option, values);

        // verify call to set Option's values
        List<String> optionValues = option.getValueGlob();
        assertTrue(optionValues.size() == 2);
        assertTrue(optionValues.get(0).equals("value1"));
        assertTrue(optionValues.get(1).equals("value2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        cl.addSwitch(option, true);
        cl.addSwitch(option, "value");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        cl.addSwitch(option, true);

        // verify call to add Switch to Option
        List<Boolean> switches = option.getSwitches();
        assertTrue(switches.size() > 0);
        assertTrue(switches.get(0).equals(true));

        // verify call to add Option to CommandLine
        List<Option> options = cl.getOptions();
        assertTrue(options.size() > 0);
        assertTrue(options.get(0).equals(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        cl.setDefaultSwitch(option, true);

        // verify call to set Option's default Switch
        List<Boolean> switches = option.getSwitches();
        assertTrue(switches.size() > 0);
        assertTrue(switches.get(0).equals(true));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);

        cl.addProperty("property", "value");

        // verify call to set CommandLine's property
        Map<String, String> properties = cl.getProperties();
        assertTrue(properties.size() == 1);
        assertTrue(properties.get("property").equals("value"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cl = mock(WriteableCommandLine.class);

        assertTrue(cl.looksLikeOption("--option"));
        assertTrue(cl.looksLikeOption("-o"));
        assertFalse(cl.looksLikeOption("value"));
        assertFalse(cl.looksLikeOption("optionValue"));
    }
}