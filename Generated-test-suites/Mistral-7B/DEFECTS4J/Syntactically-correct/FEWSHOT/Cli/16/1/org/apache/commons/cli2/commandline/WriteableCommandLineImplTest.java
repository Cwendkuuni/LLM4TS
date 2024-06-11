package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.OptionBuilder;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOptionAndValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
                OptionBuilder.withLongOpt("option1")
                        .withArgName("arg1")
                        .hasArg(true)
                        .create(),
                Arrays.asList("--option1", "arg1", "arg2"));

        Option option1 = writeableCommandLine.getOption("option1");
        List values = writeableCommandLine.getValues(option1, null);

        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        writeableCommandLine.addValue(option1, "newValue");
        values = writeableCommandLine.getValues(option1, null);

        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("newValue", values.get(2));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
                OptionBuilder.withLongOpt("switch1")
                        .create(),
                Arrays.asList("--switch1"));

        Option switch1 = writeableCommandLine.getOption("switch1");
        Boolean value = writeableCommandLine.getSwitch(switch1, false);

        assertFalse(value);

        writeableCommandLine.addSwitch(switch1, true);
        value = writeableCommandLine.getSwitch(switch1, false);

        assertTrue(value);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
                OptionBuilder.withLongOpt("propertyOption")
                        .create(),
                Arrays.asList("--propertyOption", "property=value"));

        Option propertyOption = writeableCommandLine.getOption("propertyOption");
        String propertyValue = writeableCommandLine.getProperty(propertyOption, "defaultValue");

        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
                OptionBuilder.withLongOpt("propertyOption")
                        .create(),
                Arrays.asList("--propertyOption"));

        Option propertyOption = writeableCommandLine.getOption("propertyOption");
        writeableCommandLine.addProperty(propertyOption, "property", "value");

        Map<String, String> properties = writeableCommandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertEquals("value", properties.get("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
                OptionBuilder.withLongOpt("option1")
                        .withArgName("arg1")
                        .hasArg(true)
                        .create(),
                Arrays.asList("--option1", "arg1", "arg2"));

        Option option1 = writeableCommandLine.getOption("option1");
        String expected = "--option1 arg1 arg2";

        assertEquals(expected, writeableCommandLine.toString());
    }
}