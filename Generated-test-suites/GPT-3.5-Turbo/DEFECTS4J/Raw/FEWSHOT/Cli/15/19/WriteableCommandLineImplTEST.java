
package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option rootOption;
    private List<String> arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Option("r", "root", false);
        arguments = new ArrayList<String>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false);
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOption("t"));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument("a");
        commandLine.addValue(option, "value");

        assertEquals(1, commandLine.getValues(option, null).size());
        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false);
        commandLine.addOption(option);

        assertNotNull(commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("a");
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default");

        commandLine.addValue(option, "value");

        assertEquals(1, commandLine.getValues(option, defaultValues).size());
        assertEquals("value", commandLine.getValues(option, defaultValues).get(0));

        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument("a");

        commandLine.addValue(option, "value");

        assertEquals(1, commandLine.getUndefaultedValues(option).size());
        assertEquals("value", commandLine.getUndefaultedValues(option).get(0));

        assertTrue(commandLine.getUndefaultedValues(rootOption).isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));

        assertNull(commandLine.getSwitch(rootOption, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", false);
        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getProperty(option, "key", null));
        assertNull(commandLine.getProperty(option, "invalid", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-r"));
        assertTrue(commandLine.looksLikeOption("--root"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        Option option = new Option("t", "test", false);
        commandLine.addValue(option, "value");
        commandLine.addValue(rootOption, "root");

        assertEquals("value root", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", false);
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "option", false);
        commandLine.addOption(option);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o"));
        assertTrue(commandLine.getOptionTriggers().contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument("a");
        List<String> defaults = new ArrayList<String>();
        defaults.add("default");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
        assertEquals(defaults, commandLine.getValues(option, new ArrayList<String>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("t", "test", false);
        commandLine.addValue(option, "value");

        List<String> normalised = new ArrayList<String>();
        normalised.add("value");

        assertEquals(normalised, commandLine.getNormalised());
    }
}
