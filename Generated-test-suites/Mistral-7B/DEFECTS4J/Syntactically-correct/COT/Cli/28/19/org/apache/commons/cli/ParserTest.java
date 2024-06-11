package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(new Option("o", "opt", true, "Option test"),
                new Option("r", "req", true, "Required option test"));
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(parser.getOptions(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArg() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1"};
        String[] expected = {"arg1"};
        String[] result = parser.flatten(parser.getOptions(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleArgs() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(parser.getOptions(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenStopAtNonOptionTrue() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "--opt", "arg3"};
        parser.flatten(parser.getOptions(), arguments, true);
    }

    @Test
    public void testFlattenStopAtNonOptionFalse() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "--opt", "arg3"};
        String[] expected = {"arg1", "--opt", "arg3"};
        String[] result = parser.flatten(parser.getOptions(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(parser.getOptions(), new String[0]);
        assertTrue(cmd.isEmpty());
    }

    @Test
    public void testParseSingleArg() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(parser.getOptions(), new String[]{"arg1"});
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--opt", "value"};
        Options options = new Options();
        options.addOption("opt", true, "Option test", "opt");
        CommandLine cmd = parser.parse(options, arguments);
        Option option = cmd.getOption("opt");
        assertEquals("value", option.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--unknow", "value"};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test
    public void testParseRequiredOptionMissing() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(new Option("o", "opt", true, "Option test"),
                new Option("r", "req", true, "Required option test"));
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        String[] arguments = {"--opt", "arg1"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("req"));
        assertNull(cmd.getOptionValue("req"));
        assertThrows(MissingOptionException.class, () -> parser.checkRequiredOptions());
    }

    @Test
    public void testParseRequiredOptionPresent() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(new Option("o", "opt", true, "Option test"),
                new Option("r", "req", true, "Required option test"));
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        String[] arguments = {"--opt", "arg1", "--req", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("req"));
        assertEquals("arg2", cmd.getOptionValue("req"));
        assertSame(requiredOptions, parser.getRequiredOptions());
        assertThrows(MissingOptionException.class, () -> parser.checkRequiredOptions());
    }

    @Test
    public void testParsePropertiesEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(parser.getOptions(), new String[0], new Properties());
        assertTrue(cmd.isEmpty());
    }

    @Test
    public void testParsePropertiesSingleProperty() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test", "opt");
        options.addOption(option);
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("opt", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option optionResult = cmd.getOption("opt");
        assertEquals("value", optionResult.getValue());
    }

    @Test
    public void testParsePropertiesMultipleProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option test1", "opt1");
        Option option2 = new Option("o2", "opt2", true, "Option test2", "opt2");
        options.addOption(option1);
        options.addOption(option2);
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("opt1", "value1");
        properties.setProperty("opt2", "value2");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option option1Result = cmd.getOption("opt1");
        Option option2Result = cmd.getOption("opt2");
        assertEquals("value1", option1Result.getValue());
        assertEquals("value2", option2Result.getValue());
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!expected.getClass().isArray() || !actual.getClass().isArray()) {
            assertEquals(expected, actual);
            return;
        }

        Object[] e = (Object[]) expected;
        Object[] a = (Object[]) actual;
        if (e.length != a.length) {
            fail("Arrays have different lengths.");
        }

        for (int i = 0; i < e.length; i++) {
            assertEquals(e[i], a[i]);
        }
    }
}