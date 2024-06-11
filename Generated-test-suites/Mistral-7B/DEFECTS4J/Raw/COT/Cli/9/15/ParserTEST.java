package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        List requiredOptions = parser.getRequiredOptions();
        assertNull(requiredOptions);

        options.addRequiredOption("option1");
        parser.setOptions(options);
        requiredOptions = parser.getRequiredOptions();
        assertSame(options.getOption("option1"), requiredOptions.get(0));
    }

    @Test(expected = ParseException.class)
    public void testFlattenNullOptions() {
        Parser parser = new TestParser();
        String[] arguments = new String[0];
        parser.flatten(null, arguments, false);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithOptions() {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option2");
        String[] arguments = {"--option1", "--option2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option2");
        String[] arguments = {"--option1", "arg1", "--option2", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testParseNullOptions() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = new String[0];
        parser.parse(null, arguments, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseNullArguments() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        parser.parse(options, null, null, false);
    }

    @Test
    public void testParseEmptyArguments() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option2");
        String[] arguments = {"--option1", "--option2"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 2);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1", true);
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().getOptionValue("option1") != null);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1");
        String[] arguments = {"--option1", "arg1", "--option2"};
        CommandLine cmd = parser.parse(options, arguments, null, true);
        assertFalse(cmd.getArgs().isEmpty());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addRequiredOption("option1");
        String[] arguments = new String[0];
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1");
        String[] arguments = {"--unrecognizedOption"};
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1", true);
        String[] arguments = {"--option1"};
        parser.parse(options, arguments, null, false);
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1", true);
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        parser.processProperties(properties);
        Option option = options.getOption("option1");
        assertTrue(option.getValues().length > 0);
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addRequiredOption("option1");
        String[] arguments = new String[0];
        parser.parse(options, arguments, null, false);
        throw new AssertionError("Expected MissingOptionException");
    }

    @Test
    public void testParseWithMultipleProperties() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        options.addOption("option1", true);
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        Option option1 = cmd.getOptions().getOption("option1");
        Option option2 = cmd.getOptions().getOption("option2");
        assertTrue(option1.getValues().length > 0);
        assertEquals("value1", option1.getValue());
        assertTrue(option2.getValues().length == 0);
        assertNull(option2.getValue());
    }

    private static class TestParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return super.flatten(opts, arguments, stopAtNonOption);
        }
    }
}