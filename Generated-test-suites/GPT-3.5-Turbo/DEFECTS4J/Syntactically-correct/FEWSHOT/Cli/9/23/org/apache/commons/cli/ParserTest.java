package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg")
                .desc("Option with argument").build());
        options.addOption(Option.builder("b").desc("Option without argument").build());
        options.addOption(Option.builder("c").desc("Flag option").build());

        String[] arguments = {"-a", "valueA", "-b", "-c"};

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("valueA", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        String[] arguments = {};

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        Properties properties = new Properties();
        properties.setProperty("a", "valueA");

        String[] arguments = {};

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("valueA", commandLine.getOptionValue("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a"};

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-b"};

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);
    }
}