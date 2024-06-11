package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // test addOption
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("o"));
        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());

        // test addValue
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        // test addSwitch
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));

        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, true));

        // test hasOption
        assertTrue(writeableCommandLine.hasOption(option));

        Option emptyOption = new Option("empty", null, false, null);
        assertFalse(writeableCommandLine.hasOption(emptyOption));

        // test getOption
        Option getOption = writeableCommandLine.getOption("o");
        assertSame(option, getOption);

        // test getValues
        List getValues = writeableCommandLine.getValues(option, null);
        assertEquals(2, getValues.size());
        assertEquals("value1", getValues.get(0));
        assertEquals("value2", getValues.get(1));

        // test getSwitch
        Boolean getSwitch = writeableCommandLine.getSwitch(option, false);
        assertTrue(getSwitch);

        // test addProperty
        writeableCommandLine.addProperty("property", "value");
        Properties properties = writeableCommandLine.getProperties();
        assertEquals("value", properties.getProperty("property"));

        // test getProperty
        assertEquals("defaultValue", writeableCommandLine.getProperty("property", "defaultValue"));

        // test getProperties
        Set getProperties = writeableCommandLine.getProperties();
        assertEquals(1, getProperties.size());

        // test looksLikeOption
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertFalse(writeableCommandLine.looksLikeOption("x"));

        // test toString
        String toString = writeableCommandLine.toString();
        assertTrue(toString.contains("option value1 value2"));

        // test getOptions
        assertEquals(1, writeableCommandLine.getOptions().size());

        // test getOptionTriggers
        Set getOptionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, getOptionTriggers.size());

        // test setDefaultValues
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List getDefaultValues = writeableCommandLine.getValues(option, null);
        assertEquals(1, getDefaultValues.size());
        assertEquals("defaultValue1", getDefaultValues.get(0));

        // test setDefaultSwitch
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));

        // test getNormalised
        List getNormalised = writeableCommandLine.getNormalised();
        assertEquals(0, getNormalised.size());
    }
}