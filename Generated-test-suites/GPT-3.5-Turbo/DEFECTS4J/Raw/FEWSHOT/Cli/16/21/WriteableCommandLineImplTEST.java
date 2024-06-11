package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private Option rootOption;
    private List arguments;
    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setup() {
        rootOption = new Option.Builder("root")
            .create();
        arguments = new ArrayList();
        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option.Builder("testOption")
            .create();
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
        assertEquals(option, writeableCommandLine.getOption("testOption"));
        assertFalse(writeableCommandLine.getOptions().isEmpty());
        assertFalse(writeableCommandLine.getOptionTriggers().isEmpty());
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        writeableCommandLine.addValue(argument, "value");

        assertTrue(writeableCommandLine.hasOption(argument));
        List values = writeableCommandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option.Builder('f')
            .create();
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.hasOption(option));
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option.Builder("testOption")
            .create();
        assertFalse(writeableCommandLine.hasOption(option));

        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option.Builder("testOption")
            .create();
        assertNull(writeableCommandLine.getOption("testOption"));

        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        List defaultValues = Arrays.asList("default");

        List values = writeableCommandLine.getValues(argument, defaultValues);
        assertNotNull(values);
        assertEquals(defaultValues, values);

        writeableCommandLine.addValue(argument, "value");
        values = writeableCommandLine.getValues(argument, defaultValues);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();

        List values = writeableCommandLine.getUndefaultedValues(argument);
        assertNotNull(values);
        assertTrue(values.isEmpty());

        writeableCommandLine.addValue(argument, "value");
        values = writeableCommandLine.getUndefaultedValues(argument);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option.Builder('f')
            .create();
        assertNull(writeableCommandLine.getSwitch(option, null));

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String defaultValue = "default";
        assertNull(writeableCommandLine.getProperty(property, defaultValue));

        writeableCommandLine.addProperty(property, "value");
        String propertyValue = writeableCommandLine.getProperty(property, defaultValue);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "value";
        writeableCommandLine.addProperty(property, value);

        String propertyValue = writeableCommandLine.getProperty(property);
        assertEquals(value, propertyValue);
        assertTrue(writeableCommandLine.getProperties().contains(property));
    }

    @Test
    public void testAddProperties() {
        Option option = new Option.Builder("testOption")
            .create();
        String property = "testProperty";
        String value = "value";
        Properties properties = new Properties();
        properties.setProperty(property,"value");
        writeableCommandLine.addProperty(option, properties);

        String propertyValue = writeableCommandLine.getProperty(option, property);
        assertEquals(value, propertyValue);
        assertTrue(writeableCommandLine.getProperties(option).contains(property));
    }

    @Test
    public void testToString() {
        String arg1 = "argument1";
        String arg2 = "\"argument 2\"";
        String arg3 = "argument 3";
        arguments.add(arg1);
        arguments.add(arg2);
        arguments.add(arg3);

        String commandLineString = writeableCommandLine.toString();
        assertEquals(arg1 + " " + arg2 + " " + arg3, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option.Builder("option1")
            .create();
        Option option2 = new Option.Builder("option2")
            .create();
        Option option3 = new Option.Builder("option3")
            .create();

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);

        List options = writeableCommandLine.getOptions();
        assertNotNull(options);
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option.Builder("option1")
            .create();
        Option option2 = new Option.Builder("option2")
            .create();
        Option option3 = new Option.Builder("option3")
            .create();

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);

        Set triggers = writeableCommandLine.getOptionTriggers();
        assertNotNull(triggers);
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
        assertTrue(triggers.contains("option3"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option.Builder("testOption")
            .create();
        List defaults = Arrays.asList("default");

        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        assertNotNull(values);
        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option.Builder('f')
            .create();
        Boolean defaultSwitch = true;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        String arg1 = "argument1";
        String arg2 = "argument2";
        String arg3 = "argument3";
        arguments.add(arg1);
        arguments.add(arg2);
        arguments.add(arg3);

        List normalised = writeableCommandLine.getNormalised();
        assertNotNull(normalised);
        assertEquals(arguments, normalised);
    }
}