package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;
    private CommandLine cmd;

    @Before
    public void setUp() {
        parser = Mockito.spy(Parser.class);
        options = new Options();
        cmd = new CommandLine();
    }

    @Test
    public void testSetOptions() {
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"-a", "-b", "-c"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        assertTrue(Arrays.equals(arguments, flattenedArguments));
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] arguments = {"-a", "-b", "-c"};
        CommandLine result = parser.parse(options, arguments);
        assertTrue(Arrays.equals(arguments, result.getArgs()));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndProperties() throws ParseException {
        String[] arguments = {"-a", "-b", "-c"};
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        CommandLine result = parser.parse(options, arguments, properties);
        assertTrue(Arrays.equals(arguments, result.getArgs()));
        assertTrue(result.hasOption("key"));
        assertEquals("value", result.getOptionValue("key"));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndPropertiesAndStopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "-b", "-c"};
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        CommandLine result = parser.parse(options, arguments, properties, true);
        assertTrue(Arrays.equals(arguments, result.getArgs()));
        assertTrue(result.hasOption("key"));
        assertEquals("value", result.getOptionValue("key"));
    }

    @Test
    public void testProcessProperties() throws ParseException {
        String[] arguments = {"-a", "-b", "-c"};
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        Option option = new Option("key");
        options.addOption(option);
        CommandLine result = parser.parse(options, arguments, properties);
        assertTrue(result.hasOption("key"));
        assertEquals("value", result.getOptionValue("key"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        options.addRequiredOption("a");
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a");
        ListIterator iter = Arrays.asList("value").listIterator();
        parser.processArgs(option, iter);
        assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingValue() throws ParseException {
        Option option = new Option("a");
        ListIterator iter = Arrays.asList().listIterator();
        parser.processArgs(option, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a");
        Mockito.doNothing().when(parser).processArgs(Mockito.any(Option.class), Mockito.any(ListIterator.class));
        ListIterator iter = Arrays.asList("value").listIterator();
        parser.processOption("a", iter);
        assertEquals("value", option.getValue());
    }
}