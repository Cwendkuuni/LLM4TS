package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissigOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParserAbstractClass() {
        assertTrue(Parser.class.isInterface());
    }

    @Test
    public void testSetOptions() {
        Parser parser = new TestParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new TestParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("option1").build();
        Option option2 = OptionBuilder.withLongOpt("option2").build();
        parser.setOptions(options);
        options.addOption(option1);
        options.addOption(option2);
        parser.setRequiredOptions(Arrays.asList(option1));
        List<Option> requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option1, requiredOptions.get(0));
    }

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = new String[0];
        String[] expectedResult = new String[0];
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testFlattenSimpleArguments() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--option1", "optionValue1", "--option2"};
        String[] expectedResult = {"--option1", "optionValue1", "--option2"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(new Options(), arguments);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSimpleArguments() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--option1", "optionValue1", "--option2"};
        CommandLine commandLine = parser.parse(new Options(), arguments);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        Option option1 = options.get(0);
        assertEquals("optionValue1", option1.getValue());
        Option option2 = options.get(1);
        assertNull(option2.getValue());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("option1", "optionValue1");
        CommandLine commandLine = parser.parse(new Options(), arguments, properties);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option option1 = options.get(0);
        assertEquals("optionValue1", option1.getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--unrecognizedOption"};
        Options options = new Options();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--option1", "--unrecognizedOption", "--option2"};
        Options options = new Options();
        parser.parse(options, arguments, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOption() throws ParseException {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("option1").build();
        parser.setOptions(options);
        parser.setRequiredOptions(Arrays.asList(option));
        parser.parse(options, new String[0]);
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new TestParser();
        Properties properties = new Properties();
        properties.setProperty("option1", "optionValue1");
        parser.processProperties(properties);
        List<Option> options = parser.cmd.getOptions();
        assertEquals(1, options.size());
        Option option1 = options.get(0);
        assertEquals("optionValue1", option1.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentException() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--option1"};
        Option option = OptionBuilder.withLongOpt("option1").hasArg().build();
        parser.setOptions(new Options());
        parser.parse(parser.getOptions(), arguments, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptionException() throws ParseException {
        Parser parser = new TestParser();
        String[] arguments = {"--unrecognizedOption"};
        Options options = new Options();
        parser.parse(options, arguments, false);
    }

    /**
     * Test class for Parser with test methods for abstract methods.
     */
    public static class TestParser extends Parser {

        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                throws ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
            throw new UnsupportedOperationException();
        }
    }
}