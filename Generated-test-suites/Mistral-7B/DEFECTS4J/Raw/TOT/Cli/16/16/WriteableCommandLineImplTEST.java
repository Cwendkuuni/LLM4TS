package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option exampleOption;

    @Before
    public void setUp() {
        exampleOption = new Option("e", "example", true, "An example value");
        commandLine = new WriteableCommandLineImpl(new Option("main", "main", 1), new ArrayList<>());

        commandLine.addOption(exampleOption);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(exampleOption, "value1");
        List values = commandLine.getValues(exampleOption, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(exampleOption, "value2");
        values = commandLine.getValues(exampleOption, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(exampleOption);
        assertTrue(commandLine.hasOption(exampleOption));

        commandLine.removeOption(exampleOption);
        assertFalse(commandLine.hasOption(exampleOption));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(exampleOption);
        Option option = commandLine.getOption("e");
        assertEquals(exampleOption, option);

        Option unknownOption = commandLine.getOption("unknown");
        assertNull(unknownOption);
    }

    @Test(expected = ParseException.class)
    public void testAddSwitchWithValue() {
        Option booleanOption = new Option("b", "boolean", false, "A boolean option");
        commandLine.addSwitch(booleanOption, true);

        try {
            commandLine.addSwitch(booleanOption, "value");
        } catch (ParseException e) {
            assertEquals("Switch option 'boolean' is not allowed to have a value", e.getMessage());
        }
    }

    @Test
    public void testGetSwitch() {
        Option booleanOption = new Option("b", "boolean", false, "A boolean option");
        commandLine.addSwitch(booleanOption, true);
        assertTrue(commandLine.getSwitch(booleanOption, false));

        commandLine.addSwitch(booleanOption, false);
        assertFalse(commandLine.getSwitch(booleanOption, true));

        commandLine.setDefaultSwitch(booleanOption, true);
        assertTrue(commandLine.getSwitch(booleanOption, false));
    }

    @Test
    public void testGetProperties() {
        Option propertyOption = new Option("p", "property", true, "A property option");
        commandLine.addOption(propertyOption);

        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");

        Map properties = commandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        Map allProperties = commandLine.getProperties();
        assertEquals(0, allProperties.size());
    }

    @Test
    public void testToString() {
        commandLine.addOption(exampleOption);
        commandLine.addValue(exampleOption, "value1");

        String commandLineString = commandLine.toString();
        String expected = "-e value1 main";
        assertEquals(expected, commandLineString);
    }
}