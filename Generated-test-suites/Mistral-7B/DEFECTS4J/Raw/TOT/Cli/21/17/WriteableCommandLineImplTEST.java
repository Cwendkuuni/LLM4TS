package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("example")
                .withDescription("an example option")
                .create(false);
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        CommandLine parsedCommandLine = writeableCommandLine.parse(new String[]{"--example"});
        assertTrue(parsedCommandLine.hasOption(option));
        assertEquals(1, parsedCommandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        CommandLine parsedCommandLine = writeableCommandLine.parse(new String[]{"--example", "value1"});
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "propertyValue");
        CommandLine parsedCommandLine = writeableCommandLine.parse(new String[]{"--example", "--property=propertyValue"});
        Map<String, Object> properties = parseProperties(parsedCommandLine.getProperties());
        assertEquals("propertyValue", properties.get("property"));
    }

    @Test
    public void testParseOptions() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String[] args = {"--example", "--property=propertyValue"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        assertEquals(1, parsedCommandLine.getOptions().size());
        assertTrue(parsedCommandLine.hasOption(option));
        Map<String, Object> properties = parseProperties(parsedCommandLine.getProperties());
        assertEquals("propertyValue", properties.get("property"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String[] args = {"--invalid"};
        writeableCommandLine.parse(args);
    }

    @Test
    public void testParseMissingOption() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String[] args = {"--property"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        assertFalse(parsedCommandLine.hasOption(option));
    }

    @Test
    public void testParseMultipleValues() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        String[] args = {"--example", "value1", "value2"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseNoValue() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        String[] args = {"--example"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        List values = writeableCommandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testParseSwitches() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
        String[] args = {"--example", "--no-example"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertFalse(switchValue);
    }

    @Test
    public void testParseSwitch() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        String[] args = {"--example"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testParseNoSwitch() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        String[] args = {"--example"};
        CommandLine parsedCommandLine = writeableCommandLine.parse(args);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertNull(switchValue);
    }

    @Test
    public void testParseEmpty() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        CommandLine parsedCommandLine = writeableCommandLine.parse(new String[0]);
        assertTrue(parsedCommandLine.getOptions().isEmpty());
        assertTrue(parsedCommandLine.getProperties().isEmpty());
    }

    private Map<String, Object> parseProperties(Map<String, Object> properties) {
        Map<String, Object> result = new HashMap<>();
        properties.forEach((key, value) -> result.put(key.toString(), value));
        return result;
    }
}