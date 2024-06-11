package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.AbstractCommandLineParser;
import org.apache.commons.cli2.util.Arguments;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.util.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option simpleOption;
    private Option switchOption;
    private Option argumentOption;
    private Option groupOption;

    @Before
    public void setUp() {
        simpleOption = OptionBuilder.withLongOpt("simple")
                .withDescription("A simple option")
                .create(false);

        switchOption = OptionBuilder.withLongOpt("switch")
                .withDescription("A switch option")
                .create(true);

        argumentOption = OptionBuilder.withLongOpt("argument")
                .withDescription("An option with argument")
                .isRequired(true)
                .create(new Argument(Argument.TYPE_STRING, "an argument"));

        groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("A group option")
                .create(false);

        commandLine = new WriteableCommandLineImpl(groupOption, new ArrayList());

        commandLine.addOption(simpleOption);
        commandLine.addOption(switchOption);
        commandLine.addOption(argumentOption);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#hasOption(Option)}
     */
    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(simpleOption));
        assertTrue(commandLine.hasOption(switchOption));
        assertFalse(commandLine.hasOption(new Option("non-existent", "non-existent")));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOption(String)}
     */
    @Test
    public void testGetOption() {
        assertEquals(simpleOption, commandLine.getOption("simple"));
        assertEquals(switchOption, commandLine.getOption("switch"));
        assertNull(commandLine.getOption("non-existent"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getValues(Option, List)}
     */
    @Test
    public void testGetValues() {
        List expectedList = new ArrayList();
        expectedList.add("expectedValue");

        commandLine.addValue(simpleOption, expectedList);

        List actualList = commandLine.getValues(simpleOption, null);
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getSwitch(Option, Boolean)}
     */
    @Test
    public void testGetSwitch() {
        assertTrue(commandLine.getSwitch(switchOption, false));
        assertFalse(commandLine.getSwitch(simpleOption, false));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addProperty(String, String)}
     */
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        Properties properties = commandLine.getProperties();
        assertEquals("value", properties.getProperty("property"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(String, String)}
     */
    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", "defaultValue"));
        assertEquals("defaultValue", commandLine.getProperty("non-existent", "defaultValue"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#toString()}
     */
    @Test
    public void testToString() {
        String expected = "simple -simple " +
                "switch --switch " +
                "argument an argument ";

        assertEquals(expected, commandLine.toString());
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOptions()}
     */
    @Test
    public void testGetOptions() {
        List expectedList = new ArrayList();
        expectedList.add(simpleOption);
        expectedList.add(switchOption);
        expectedList.add(argumentOption);

        List actualList = commandLine.getOptions();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOptionTriggers()}
     */
    @Test
    public void testGetOptionTriggers() {
        Set expectedSet = commandLine.getOptionTriggers();
        expectedSet.add("simple");
        expectedSet.add("switch");
        expectedSet.add("argument");

        Set actualSet = commandLine.getOptionTriggers();
        assertEquals(expectedSet, actualSet);
    }
}