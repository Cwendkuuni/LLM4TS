package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option testOption;
    private Argument testArgument;

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        testOption = new Option("t");
        testArgument = new Argument();
        commandLine = new WriteableCommandLineImpl(testOption, null);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(testOption);
        assertTrue(commandLine.hasOption(testOption));
    }

    @Test
    public void testAddValue() {
        String value = "test value";
        commandLine.addValue(testOption, value);

        assertEquals(value, commandLine.getValues(testOption, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(testOption, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(testOption, null));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(testOption);
        assertTrue(commandLine.hasOption(testOption));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(testOption);
        assertEquals(testOption, commandLine.getOption("t"));
    }

    @Test
    public void testGetValues() {
        String value = "test value";
        commandLine.addValue(testOption, value);

        assertEquals(value, commandLine.getValues(testOption, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        String value = "test value";
        commandLine.addValue(testOption, value);

        assertEquals(value, commandLine.getUndefaultedValues(testOption).get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(testOption, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(testOption, null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, "default"));
    }

    @Test
    public void testGetProperty() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(new PropertyOption(), property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetProperties() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(property, value);

        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String value = "test value";
        commandLine.addValue(testArgument, value);

        assertEquals(value, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(testOption);

        assertTrue(commandLine.getOptions().contains(testOption));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(testOption);

        assertTrue(commandLine.getOptionTriggers().contains("t"));
    }

    @Test
    public void testSetDefaultValues() {
        String value = "default value";
        commandLine.setDefaultValues(testOption, null);

        assertNull(commandLine.getValues(testOption, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(testOption, Boolean.TRUE);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(testOption, null));
    }

    @Test
    public void testGetNormalised() {
        String value = "test value";
        commandLine.addValue(testOption, value);

        assertTrue(commandLine.getNormalised().contains(value));
    }
}