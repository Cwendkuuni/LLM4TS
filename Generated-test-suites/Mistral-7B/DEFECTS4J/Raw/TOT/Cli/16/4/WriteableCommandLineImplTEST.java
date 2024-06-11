package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "output", true, "Output file");

        commandLine = new WriteableCommandLineImpl(new Option("app", "MyApp"), new ArrayList<>());
        commandLine.addOption(option);
    }

    @Test
    public void testAddOption() {
        assertEquals("MyApp", commandLine.getName());
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption(option));
        assertSame(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, false);
        assertTrue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "output1.txt");
        List values1 = commandLine.getValues(option, null);
        assertEquals(1, values1.size());
        assertEquals("output1.txt", values1.get(0));

        commandLine.addValue(option, "output2.txt");
        List values2 = commandLine.getValues(option, null);
        assertEquals(2, values2.size());
        assertEquals("output1.txt", values2.get(0));
        assertEquals("output2.txt", values2.get(1));

        commandLine.addValue(option, "output3.txt");
        List values3 = commandLine.getValues(option, null);
        assertEquals(3, values3.size());
        assertEquals("output1.txt", values3.get(0));
        assertEquals("output2.txt", values3.get(1));
        assertEquals("output3.txt", values3.get(2));

        List values4 = commandLine.getValues(option, Collections.singletonList("output3.txt"));
        assertEquals(1, values4.size());
        assertEquals("output3.txt", values4.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "output1.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("output1.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, false);
        assertTrue(value);

        commandLine.addSwitch(option, false);
        value = commandLine.getSwitch(option, true);
        assertFalse(value);

        commandLine.setDefaultSwitch(option, true);
        value = commandLine.getSwitch(option, false);
        assertTrue(value);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "foo", "bar");
        String value = commandLine.getProperty(option, "foo");
        assertEquals("bar", value);
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "output.txt");
        commandLine.addSwitch(option, true);
        commandLine.addProperty(option, "foo", "bar");

        String expected = "-o output.txt --output --foo bar";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }
}