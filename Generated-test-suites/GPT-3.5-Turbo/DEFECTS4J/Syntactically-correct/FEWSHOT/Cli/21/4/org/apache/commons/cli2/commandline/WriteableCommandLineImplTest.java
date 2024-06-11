package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("foo", "foo option");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("bar", "bar option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        assertEquals(1, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("bar", "bar option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("bar", "bar option");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("bar", "bar option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("bar"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        List<String> expectedValues = new ArrayList<String>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        assertEquals(expectedValues, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        List<String> expectedValues = new ArrayList<String>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("bar", "bar option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("foo"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        assertEquals("default", commandLine.getProperty("foo", "default"));
    }

    @Test
    public void testGetProperties() {
        Set<String> expectedProperties = new HashSet<String>();
        assertEquals(expectedProperties, commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("value"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("bar1", "bar1 option");
        Option option2 = new Option("bar2", "bar2 option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> expectedOptions = new ArrayList<Option>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("bar1", "bar1 option");
        Option option2 = new Option("bar2", "bar2 option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> expectedTriggers = new HashSet<String>();
        expectedTriggers.add("bar1");
        expectedTriggers.add("bar2");
        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("bar", "bar option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Collections.EMPTY_LIST, commandLine.getNormalised());
    }
}