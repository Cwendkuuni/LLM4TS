package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        assertEquals(1, writeableCommandLine.getOptions().size());
        Option retrievedOption = writeableCommandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List valueList = new ArrayList();
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        assertEquals(2, valueList.size());
        List retrievedValues = writeableCommandLine.getValues(option, null);
        assertEquals(valueList, retrievedValues);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("option", false, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("option", false, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty("nonExistentProperty", "defaultValue"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");
        Set properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
        assertFalse(properties.contains("nonExistentProperty"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        Set prefixes = writeableCommandLine.getPrefixes();
        assertTrue(prefixes.contains(""));
        assertTrue(prefixes.contains("--"));
        assertFalse(prefixes.contains("-"));
        String trigger = "--option";
        assertTrue(writeableCommandLine.looksLikeOption(trigger));
        trigger = "-o";
        assertFalse(writeableCommandLine.looksLikeOption(trigger));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        String expected = "--option value1 value2";
        String actual = writeableCommandLine.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        Option retrievedOption = (Option) options.get(0);
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("option"));
        assertFalse(optionTriggers.contains("nonExistentOption"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List retrievedDefaultValues = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, retrievedDefaultValues);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("option", false, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List normalised = new ArrayList();
        normalised.add("normalisedValue1");
        normalised.add("normalisedValue2");
        writeableCommandLine.setNormalised(normalised);
        List retrievedNormalised = writeableCommandLine.getNormalised();
        assertEquals(normalised, retrievedNormalised);
    }
}