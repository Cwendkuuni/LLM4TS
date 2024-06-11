package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));

        assertEquals(2, cmd.getNormalised().size());
        assertEquals("arg1", cmd.getNormalised().get(0));
        assertEquals("arg2", cmd.getNormalised().get(1));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());

        cmd.addOption(option);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument(null, "arg", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List emptyValues = cmd.getUndefaultedValues(option);

        assertTrue(emptyValues.isEmpty());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        assertEquals(option, cmd.getOption("opt"));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption("prop", "property", null, "Property description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addProperty(option, "property", "value");

        Set properties = cmd.getProperties(option);

        assertEquals(1, properties.size());
        assertEquals("property", properties.iterator().next());

        Map optionsMap = cmd.getOptionToProperties();
        Map expectedMap = new HashMap();
        expectedMap.put(option, new Properties() {{
            setProperty("property", "value");
        }});

        assertEquals(expectedMap, optionsMap);
    }
}