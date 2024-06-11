package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Argument(0, "arg");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(option2));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(2, commandLine.getOptionTriggers().size());

        List<Object> values1 = commandLine.getValues(option1, null);
        assertEquals(0, values1.size());

        List<Object> values2 = commandLine.getValues(option2, null);
        assertEquals(0, values2.size());

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        values1 = commandLine.getValues(option1, null);
        assertEquals(2, values1.size());
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));

        values2 = commandLine.getValues(option2, null);
        assertEquals(1, values2.size());
        assertEquals("value3", values2.get(0));

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);

        Boolean switch1 = commandLine.getSwitch(option1, null);
        assertTrue(switch1);

        switch1 = commandLine.getSwitch(option1, null);
        assertFalse(switch1);

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", null);

        String property1 = commandLine.getProperty("property1", "defaultValue");
        assertEquals("value1", property1);

        String property2 = commandLine.getProperty("property2", "defaultValue");
        assertNull(property2);

        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());

        assertEquals("opt1", normalised.get(0));
        assertEquals("opt2", normalised.get(1));
        assertEquals("arg", normalised.get(2));

        commandLine.setDefaultValues(option1, Collections.singletonList("defaultValue1"));
        commandLine.setDefaultSwitch(option1, true);

        values1 = commandLine.getValues(option1, null);
        assertEquals(2, values1.size());
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));

        values1 = commandLine.getValues(option1, Collections.singletonList("defaultValue1"));
        assertEquals(3, values1.size());
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));
        assertEquals("defaultValue1", values1.get(2));

        Boolean switch1Default = commandLine.getSwitch(option1, null);
        assertTrue(switch1Default);

        switch1Default = commandLine.getSwitch(option1, null);
        assertTrue(switch1Default);

        commandLine.setDefaultSwitch(option1, false);

        switch1Default = commandLine.getSwitch(option1, null);
        assertFalse(switch1Default);

        switch1Default = commandLine.getSwitch(option1, null);
        assertFalse(switch1Default);

        commandLine.setDefaultValues(option1, null);

        values1 = commandLine.getValues(option1, null);
        assertEquals(0, values1.size());

        switch1Default = commandLine.getSwitch(option1, null);
        assertNull(switch1Default);
    }
}