
package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("r", "root", true, "Root option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("f"));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("f"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.addSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getProperty(option, "key", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getProperty(option, "key", null));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addProperty(option, "key", "value");

        assertEquals("default", commandLine.getProperty(option, "nonexistent", "default"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        assertEquals(2, commandLine.getProperties(option).size());
        assertTrue(commandLine.getProperties(option).contains("key1"));
        assertTrue(commandLine.getProperties(option).contains("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("-f"));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "file", false, "File option");
        commandLine.addOption(option);
        commandLine.addValue(option, "filename.txt");

        assertEquals("-f filename.txt", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("f"));
        assertTrue(commandLine.getOptionTriggers().contains("v"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file", true, "File option");
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.setDefaultSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("value", normalised.get(0));
    }
}
