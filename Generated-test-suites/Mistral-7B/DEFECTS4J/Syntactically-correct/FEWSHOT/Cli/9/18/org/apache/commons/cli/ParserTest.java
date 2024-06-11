package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithSingleArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        assertNull(cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertNull(cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithSingleOptionalArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg"});
        assertEquals(Collections.singletonList("arg"), cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMultipleArgs() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", true, "Description for option b");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertNull(cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMultipleOptionalArgs() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "-a", "arg2"});
        List<String> args = cmd.getArgs();
        assertEquals(Arrays.asList("arg1", "arg2"), args);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.put("a", "arg");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals(Collections.singletonList("arg"), cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-b"});
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-x"});
        assertNull(cmd.getArgs());
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("x"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", true, "Description for option b");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", true, "Description for option b", true, "requiredOption");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
        List<String> requiredOptions = parser.getRequiredOptions();
        assertEquals(Collections.singletonList("requiredOption"), requiredOptions);
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));

        cmd = parser.parse(options, new String[]{"-a", "-b"});
        requiredOptions = parser.getRequiredOptions();
        assertNull(requiredOptions);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", true, "Description for option b", true, "requiredOption");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
    }
}