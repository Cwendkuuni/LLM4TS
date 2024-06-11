package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine cmd;
    private Option option;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("input")
                .withDescription("an input file")
                .create(false);
        cmd = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        cmd.addOption(option);
    }

    @After
    public void tearDown() {
        cmd = null;
        option = null;
    }

    @Test
    public void testAddValue() {
        cmd.addValue(option, "file1.txt");
        List values = cmd.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("file1.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        cmd.addSwitch(option, true);
        boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(cmd.hasOption(option));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        Option invalidOption = OptionBuilder.withLongOpt("invalid")
                .withDescription("an invalid option")
                .create(false);
        cmd.addOption(invalidOption);
        cmd.parse("--invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        cmd.addValue(option, null);
    }

    @Test
    public void testGetOption() {
        Option resultOption = cmd.getOption("input");
        assertEquals(option, resultOption);
    }

    @Test
    public void testGetValues() {
        cmd.addValue(option, "file1.txt");
        cmd.addValue(option, "file2.txt");
        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("file1.txt", values.get(0));
        assertEquals("file2.txt", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        cmd.addValue(option, "file1.txt");
        List values = cmd.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("file1.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        cmd.addProperty(option, "prop", "value");
        String propertyValue = cmd.getProperty(option, "prop", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        cmd.addProperty(option, "prop1", "value1");
        cmd.addProperty(option, "prop2", "value2");
        Set properties = cmd.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("prop1"));
        assertTrue(properties.contains("prop2"));
    }

    @Test
    public void testToString() {
        cmd.addValue(option, "file1.txt");
        cmd.addValue(option, "file2.txt");
        String cmdLine = cmd.toString();
        String expected = "--input file1.txt --input file2.txt";
        assertEquals(expected, cmdLine);
    }

    @Test
    public void testGetOptions() {
        List options = cmd.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.get(0) instanceof Option);
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = cmd.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("input"));
    }
}