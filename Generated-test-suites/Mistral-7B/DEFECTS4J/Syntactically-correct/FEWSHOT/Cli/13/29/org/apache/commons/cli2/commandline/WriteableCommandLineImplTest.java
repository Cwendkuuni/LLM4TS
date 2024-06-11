package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "output.txt");

        List<Option> options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromWriteableCommandLine = options.get(0);
        assertEquals("o", optionFromWriteableCommandLine.getShortName());
        assertEquals("output", optionFromWriteableCommandLine.getLongName());
        assertTrue(optionFromWriteableCommandLine.isSettable());
        assertEquals("Output file", optionFromWriteableCommandLine.getDescription());
        assertEquals("output.txt", optionFromWriteableCommandLine.getValue());

        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertEquals("o", optionTriggers.iterator().next());

        Properties properties = writeableCommandLine.getProperties();
        assertTrue(properties.isEmpty());

        writeableCommandLine.addProperty("property1", "value1");
        properties = writeableCommandLine.getProperties();
        assertEquals("value1", properties.getProperty("property1"));
        assertTrue(properties.containsKey("property1"));

        writeableCommandLine.addProperty("property2", null);
        properties = writeableCommandLine.getProperties();
        assertNull(properties.getProperty("property2"));
        assertFalse(properties.containsKey("property2"));

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertNull(switchValue);

        writeableCommandLine.addSwitch(option, true);
        switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue != null);
        assertTrue(switchValue);

        writeableCommandLine.setDefaultValues(option, new ArrayList<>());
        values = writeableCommandLine.getValues(option, null);
        assertTrue(values.isEmpty());

        writeableCommandLine.setDefaultValues(option, new ArrayList<>(Collections.singletonList("defaultValue")));
        values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));

        writeableCommandLine.setDefaultSwitch(option, true);
        switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue != null);
        assertTrue(switchValue);

        writeableCommandLine.setDefaultSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, null);
        assertFalse(switchValue != null);
    }
}