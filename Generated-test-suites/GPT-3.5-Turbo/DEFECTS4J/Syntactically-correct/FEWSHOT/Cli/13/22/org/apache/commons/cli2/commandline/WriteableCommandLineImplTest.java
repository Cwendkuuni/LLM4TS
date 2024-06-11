package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option rootOption;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Option("root");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";

        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");

        assertNull(commandLine.getOption("test"));

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();

        List<String> values = new ArrayList();
        values.add("value");

        commandLine.addValue(option, "value");

        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        Boolean defaultValue = false;

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, ""));
    }

    @Test
    public void testLooksLikeOption() {
        String prefix = "-";
        String trigger = "-t";

        assertTrue(commandLine.looksLikeOption(trigger));

        commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());
        commandLine.addOption(new Option("test"));

        assertFalse(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        arguments.add("arg1");
        arguments.add("arg2");

        Option option = new Option("test");
        option.setPrefixes("-");
        commandLine.addOption(option);

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("test");
        commandLine.addOption(option);

        List options = new ArrayList();
        options.add(option);

        assertEquals(options, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test");
        commandLine.addOption(option);

        List triggers = new ArrayList();
        triggers.add("test");

        assertEquals(triggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, new ArrayList()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = false;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        List normalised = new ArrayList();
        normalised.add("arg1");
        normalised.add("arg2");

        assertEquals(normalised, commandLine.getNormalised());
    }
}