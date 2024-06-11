package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private CommandLine commandLine;
    private Option optionA;
    private Option optionB;

    @Before
    public void setUp() {
        optionA = OptionBuilder.withLongOpt("optionA")
                .withDescription("Option A")
                .create();

        optionB = OptionBuilder.withLongOpt("optionB")
                .withDescription("Option B")
                .create();
    }

    @Test
    public void testBasicUsage() throws ParseException {
        List args = new ArrayList();
        args.add("--optionA someValue");
        args.add("--optionB");
        args.add("someArgument");

        commandLine = new WriteableCommandLineImpl(optionA, args);

        assertTrue(commandLine.hasOption(optionA));
        assertTrue(commandLine.hasOption(optionB));
        assertFalse(commandLine.hasOption(new Option("unknownOption")));
        assertEquals("someValue", commandLine.getValues(optionA, null).get(0));
        assertFalse(commandLine.getSwitch(optionB, false));
    }

    @Test
    public void testMissingOption() throws ParseException {
        List args = new ArrayList();
        args.add("someArgument");

        try {
            commandLine = new WriteableCommandLineImpl(optionA, args);
            fail("ParseException expected");
        } catch (ParseException e) {
            // expected
        }

        assertFalse(commandLine.hasOption(optionA));
    }

    @Test
    public void testMissingValue() throws ParseException {
        List args = new ArrayList();
        args.add("--optionA");

        commandLine = new WriteableCommandLineImpl(optionA, args);

        assertTrue(commandLine.hasOption(optionA));
        assertTrue(commandLine.getValues(optionA, null).isEmpty());
    }

    @Test
    public void testMultipleValues() throws ParseException {
        List args = new ArrayList();
        args.add("--optionA value1 value2");

        commandLine = new WriteableCommandLineImpl(optionA, args);

        assertTrue(commandLine.hasOption(optionA));
        assertEquals(2, commandLine.getValues(optionA, null).size());
        assertEquals("value1", commandLine.getValues(optionA, null).get(0));
        assertEquals("value2", commandLine.getValues(optionA, null).get(1));
    }

    @Test
    public void testPropertyOption() throws ParseException {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("Property Option")
                .withValueSeparator('=')
                .create();

        List args = new ArrayList();
        args.add("--propertyOption=key1=value1");
        args.add("--propertyOption=key2=value2");

        commandLine = new WriteableCommandLineImpl(propertyOption, args);

        Properties properties = commandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertEquals("key1", properties.stringPropertyNames()[0]);
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("key2", properties.stringPropertyNames()[1]);
        assertEquals("value2", properties.getProperty("key2"));
    }

    @Test
    public void testSwitchOption() throws ParseException {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("Switch Option")
                .create();

        List args = new ArrayList();
        args.add("--switchOption");

        commandLine = new WriteableCommandLineImpl(switchOption, args);

        assertTrue(commandLine.hasOption(switchOption));
        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test(expected = ParseException.class)
    public void testSwitchOptionWithValue() throws ParseException {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("Switch Option")
                .create();

        List args = new ArrayList();
        args.add("--switchOption=value");

        commandLine = new WriteableCommandLineImpl(switchOption, args);

        fail("ParseException expected");
    }

    @Test
    public void testMixedOptions() throws ParseException {
        List args = new ArrayList();
        args.add("--optionA value1");
        args.add("--optionB");
        args.add("--propertyOption=key1=value1");
        args.add("--propertyOption=key2=value2");
        args.add("--switchOption");
        args.add("--switchOption");

        commandLine = new WriteableCommandLineImpl(optionA, args);

        assertTrue(commandLine.hasOption(optionA));
        assertEquals(1, commandLine.getValues(optionA, null).size());
        assertEquals("value1", commandLine.getValues(optionA, null).get(0));
        assertTrue(commandLine.hasOption(optionB));
        assertFalse(commandLine.getSwitch(optionB, false));
        assertEquals(2, commandLine.getProperties(propertyOption).size());
        assertEquals("key1", commandLine.getProperties(propertyOption).stringPropertyNames()[0]);
        assertEquals("value1", commandLine.getProperties(propertyOption).getProperty("key1"));
        assertEquals("key2", commandLine.getProperties(propertyOption).stringPropertyNames()[1]);
        assertEquals("value2", commandLine.getProperties(propertyOption).getProperty("key2"));
        assertTrue(commandLine.hasOption(switchOption));
        assertTrue(commandLine.getSwitch(switchOption, false));
    }
}