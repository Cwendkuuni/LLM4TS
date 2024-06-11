package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

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
        Option rootOption = new Option("root", true, true, "root option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        // test empty WriteableCommandLine
        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertNull(writeableCommandLine.getProperties());
        assertEquals(Collections.emptyMap(), writeableCommandLine.optionToProperties);
        assertEquals(Collections.emptyMap(), writeableCommandLine.values);
        assertEquals(Collections.emptyMap(), writeableCommandLine.switches);
        assertEquals(Collections.emptyMap(), writeableCommandLine.defaultValues);
        assertEquals(Collections.emptyMap(), writeableCommandLine.defaultSwitches);
        assertNull(writeableCommandLine.normalised);
        assertNull(writeableCommandLine.prefixes);
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option1 = new Option("option1", true, true, "option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option1);

        Option expectedOption1 = new Option("option1", true, true, "option1");

        assertEquals(Collections.singletonList(expectedOption1), writeableCommandLine.getOptions());
        assertEquals(expectedOption1, writeableCommandLine.getOption("option1"));
        assertEquals(Collections.singletonMap("option1", expectedOption1), writeableCommandLine.nameToOption);
        assertTrue(writeableCommandLine.optionToProperties.isEmpty());
        assertNull(writeableCommandLine.values);
        assertNull(writeableCommandLine.switches);
        assertNull(writeableCommandLine.defaultValues);
        assertNull(writeableCommandLine.defaultSwitches);
        assertNull(writeableCommandLine.normalised);
        assertSame(rootOption.getPrefixes(), writeableCommandLine.prefixes);
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        List values = new ArrayList();
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addValue(option, "value3");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertEquals(Collections.singletonMap("option", option), writeableCommandLine.nameToOption);
        assertEquals(expectedValues, writeableCommandLine.values.get(option));
        assertTrue(writeableCommandLine.optionToProperties.isEmpty());
        assertTrue(writeableCommandLine.switches.isEmpty());
        assertTrue(writeableCommandLine.defaultValues.isEmpty());
        assertTrue(writeableCommandLine.defaultSwitches.isEmpty());
        assertNull(writeableCommandLine.normalised);
        assertSame(rootOption.getPrefixes(), writeableCommandLine.prefixes);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean expectedSwitch = Boolean.TRUE;

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertEquals(Collections.singletonMap("option", option), writeableCommandLine.nameToOption);
        assertSame(expectedSwitch, writeableCommandLine.switches.get(option));
        assertTrue(writeableCommandLine.optionToProperties.isEmpty());
        assertTrue(writeableCommandLine.values.isEmpty());
        assertTrue(writeableCommandLine.defaultValues.isEmpty());
        assertTrue(writeableCommandLine.defaultSwitches.isEmpty());
        assertNull(writeableCommandLine.normalised);
        assertSame(rootOption.getPrefixes(), writeableCommandLine.prefixes);
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(new Option("other", true, true, "other")));
        assertFalse(writeableCommandLine.hasOption(null));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        Option expectedOption = new Option("option", true, true, "option");

        assertEquals(expectedOption, writeableCommandLine.getOption("option"));
        assertNull(writeableCommandLine.getOption("other"));
        assertNull(writeableCommandLine.getOption(null));
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        List values = new ArrayList();
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addValue(option, "value3");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        assertEquals(expectedValues, writeableCommandLine.getValues(option, null));
        assertEquals(Collections.emptyList(), writeableCommandLine.getValues(option, Collections.singletonList("defaultValue")));
        assertNull(writeableCommandLine.getValues(new Option("other"), null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        List values = new ArrayList();
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addValue(option, "value3");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        assertEquals(expectedValues, writeableCommandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);

        Boolean expectedSwitch = Boolean.TRUE;
        Boolean defaultSwitch = Boolean.FALSE;

        writeableCommandLine.addSwitch(option, expectedSwitch);

        assertEquals(expectedSwitch, writeableCommandLine.getSwitch(option, defaultSwitch));
        assertEquals(defaultSwitch, writeableCommandLine.getSwitch(option, null));
        assertNull(writeableCommandLine.getSwitch(new Option("other"), null));
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        assertEquals("value1", writeableCommandLine.getProperty(option, "property1", null));
        assertEquals("value2", writeableCommandLine.getProperty(option, "property2", null));
        assertEquals("value1", writeableCommandLine.getProperty(option, "property1", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty(option, "property3", null));
        assertNull(writeableCommandLine.getProperty(new Option("other"), null));
    }

    @Test
    public void testAddProperty() {
        Option rootOption = new Option("root", true, true, "root option");
        Option option = new Option("option", true, true, "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("property1", "value1");
        expectedProperties.setProperty("property2", "value2");

        assertEquals(expectedProperties, writeableCommandLine.getProperties(option));
        assertEquals(Collections.emptySet(), writeableCommandLine.getProperties());
        assertNull(writeableCommandLine.getProperties(new Option("other")));
    }
}