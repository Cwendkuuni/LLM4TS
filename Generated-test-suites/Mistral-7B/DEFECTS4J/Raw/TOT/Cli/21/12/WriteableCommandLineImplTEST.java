package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Parameterized.Parameters(name = "{0}: Testing {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Option("input", true, "i"), "Test boolean input switch"},
                {new Option("input", 2, "i"), "Test integer input option with default value"},
                {new Option("input", 2, "i", 3), "Test integer input option without default value"},
                {new Option("input", 2, "s", "value"), "Test string input option with default value"},
                {new Option("input", 2, "s"), "Test string input option without default value"},
                {new Option("input", 2, Arguments.fileType, "path"), "Test file input option"},
                {new Argument("output"), "Test argument without option"},
                {new Argument("output", 1), "Test argument with integer value"},
                {new Argument("output", 2, true), "Test argument with boolean value"},
                {new Argument("output", 2, "value"), "Test argument with string value"}
        });
    }

    @Before
    public void setUp() {
        option = new Option("input", 2, false, "iOption");
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--input", "1"));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option actualOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, actualOption);
    }

    @Test
    public void testGetValues() throws ParseException {
        commandLine.addValue(option, "one");
        commandLine.addValue(option, "two");
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Arrays.asList("one", "two"), values);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set<String> properties = commandLine.getProperties(option);
        assertEquals(Collections.unmodifiableSet(Arrays.asList("property1", "property2")), properties);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--input"));
    }

    @Test
    public void testToString() {
        assertEquals("--input 1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(Collections.singletonList(option.getPreferredName()), optionTriggers);
    }
}