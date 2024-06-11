package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        String[] args = {"-a", "valueA", "-b", "valueB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] args = {"-a", "valueA", "-b", "valueB", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOptionWithStopAtNonOption() throws ParseException {
        String[] args = {"--unknown", "value"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithMultipleUnrecognizedOptions() throws ParseException {
        String[] args = {"--unknown1", "--unknown2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNotNull(cmd);
        assertTrue(cmd.hasArgs());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("--unknown1", cmd.getArgs()[0]);
        assertEquals("--unknown2", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        String[] args = {"-a", "valueA", "-a", "valueB"};

        Options options = new Options();
        options.addOption("a", true, "Option A with an optional argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertEquals("valueB", cmd.getOptionValue("a", "defaultValue"));
        assertFalse(cmd.hasArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = {"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A with a required argument");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgumentWithStopAtNonOption() throws ParseException {
        String[] args = {"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A with a required argument");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"--required"};

        Options options = new Options();
        options.addOption("required", true, "A required option");
        options.addOption("optional", true, "An optional option");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptionWithStopAtNonOption() throws ParseException {
        String[] args = {"--required"};

        Options options = new Options();
        options.addOption("required", true, "A required option");
        options.addOption("optional", true, "An optional option");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--property1=value1", "--property2=value2"};

        Options options = new Options();
        options.addOption("property1", true, "Property 1");
        options.addOption("property2", true, "Property 2");

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("property1"));
        assertTrue(cmd.hasOption("property2"));
        assertEquals("value1", cmd.getOptionValue("property1"));
        assertEquals("value2", cmd.getOptionValue("property2"));
        assertFalse(cmd.hasArgs());
    }
}