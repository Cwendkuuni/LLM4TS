package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateInstance() {
        Option rootOption = new Option("root", "Root option");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertSame(rootOption, writeableCommandLine.getOption(rootOption.getPreferredName()));
        assertEquals(arguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "Root option");
        Option option1 = new Option("option1", "Option 1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option1);

        assertTrue(writeableCommandLine.getOptions().contains(option1));
        assertEquals(option1, writeableCommandLine.getOption(option1.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option argOption = new Argument(0, "arg", true, "Arg value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(argOption);

        writeableCommandLine.addValue(argOption, "value1");
        List values = writeableCommandLine.getValues(argOption, Collections.emptyList());

        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(argOption, "value2");
        values = writeableCommandLine.getValues(argOption, Collections.emptyList());

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option("switch", "Switch option", false, "Switch value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(switchOption);

        writeableCommandLine.addSwitch(switchOption, true);
        Boolean booleanValue = writeableCommandLine.getSwitch(switchOption, false);

        assertFalse(booleanValue);

        writeableCommandLine.addSwitch(switchOption, false);
        booleanValue = writeableCommandLine.getSwitch(switchOption, true);

        assertTrue(booleanValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option1", "Option 1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option1", "Option 1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Argument argOption = new Argument(0, "arg", true, "Arg value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(argOption);

        List values1 = writeableCommandLine.getValues(argOption, Collections.emptyList());
        assertEquals(0, values1.size());

        writeableCommandLine.addValue(argOption, "value1");
        values1 = writeableCommandLine.getValues(argOption, Collections.emptyList());
        assertEquals(1, values1.size());
        assertEquals("value1", values1.get(0));

        writeableCommandLine.addValue(argOption, "value2");
        List values2 = writeableCommandLine.getValues(argOption, Collections.emptyList());
        assertEquals(2, values2.size());
        assertEquals("value1", values2.get(0));
        assertEquals("value2", values2.get(1));

        writeableCommandLine.setDefaultValues(argOption, Arrays.asList("default1", "default2"));
        values2 = writeableCommandLine.getValues(argOption, Collections.emptyList());
        assertEquals(4, values2.size());
        assertEquals("value1", values2.get(0));
        assertEquals("value2", values2.get(1));
        assertEquals("default1", values2.get(2));
        assertEquals("default2", values2.get(3));
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = new Option("switch", "Switch option", false, "Switch value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(switchOption);

        assertFalse(writeableCommandLine.hasOption(switchOption));
        assertNull(writeableCommandLine.getSwitch(switchOption, false));

        writeableCommandLine.addSwitch(switchOption, true);
        Boolean booleanValue = writeableCommandLine.getSwitch(switchOption, false);
        assertFalse(booleanValue);

        writeableCommandLine.addSwitch(switchOption, false);
        booleanValue = writeableCommandLine.getSwitch(switchOption, true);
        assertTrue(booleanValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addProperty("property1", "value1");

        assertEquals("value1", writeableCommandLine.getProperty("property1", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty("property2", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Set properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("option", "Option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Arrays.asList("arg1", "arg2", "arg3"));
        writeableCommandLine.addOption(option);

        String expected = String.format("%s [%s, %s, %s] %s", new Object[]{"Root option", "arg1", "arg2", "arg3", option.getDescription()});
        assertEquals(expected, writeableCommandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());

        assertTrue(writeableCommandLine.looksLikeOption("root"));
        assertTrue(writeableCommandLine.looksLikeOption("--root"));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));
        assertFalse(writeableCommandLine.looksLikeOption("--arg"));
        assertFalse(writeableCommandLine.looksLikeOption("--"));
    }
}