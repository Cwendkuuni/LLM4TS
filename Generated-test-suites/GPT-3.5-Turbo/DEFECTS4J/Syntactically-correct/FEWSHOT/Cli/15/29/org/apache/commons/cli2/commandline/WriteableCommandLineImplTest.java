package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root Option");

        List<String> arguments = new ArrayList<>();
        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
        assertEquals(option, writeableCommandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        argument.setId("arg1");

        Option option = new Option("test", "Test Option", argument);
        writeableCommandLine.addOption(option);

        Integer value = 10;
        writeableCommandLine.addValue(option, value);

        assertEquals(value, writeableCommandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.hasOption(option));
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        argument.setId("arg1");

        Option option = new Option("test", "Test Option", argument);
        writeableCommandLine.addOption(option);

        Integer value = 10;
        writeableCommandLine.addValue(option, value);

        List<Integer> defaultValues = new ArrayList<>();
        defaultValues.add(5);

        assertEquals(value, writeableCommandLine.getValues(option, defaultValues).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();
        argument.setId("arg1");

        Option option = new Option("test", "Test Option", argument);
        writeableCommandLine.addOption(option);

        Integer value = 10;
        writeableCommandLine.addValue(option, value);

        assertEquals(value, writeableCommandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("value", writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("value", writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("default", writeableCommandLine.getProperty(option, "unknown", "default"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals(1, writeableCommandLine.getProperties(option).size());
        assertTrue(writeableCommandLine.getProperties(option).contains("property"));
    }

    @Test
    public void testGetPropertiesNoProperties() {
        Option option = new Option("test", "Test Option");

        assertTrue(writeableCommandLine.getProperties(option).isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test", "Test Option");

        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        Argument argument = new Argument();
        argument.setId("arg1");

        Option option = new Option("test", "Test Option", argument);
        writeableCommandLine.addOption(option);

        Integer value = 10;
        writeableCommandLine.addValue(option, value);

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");

        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, arguments);

        assertEquals("arg1", writeableCommandLineImpl.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        assertEquals(1, writeableCommandLine.getOptions().size());
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        assertEquals(1, writeableCommandLine.getOptionTriggers().size());
        assertTrue(writeableCommandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.setDefaultValues(option, null);

        assertNull(writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("test", "Test Option");
        writeableCommandLine.addOption(option);

        List<String> arguments = new ArrayList<>();
        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, writeableCommandLine.getNormalised());
    }
}