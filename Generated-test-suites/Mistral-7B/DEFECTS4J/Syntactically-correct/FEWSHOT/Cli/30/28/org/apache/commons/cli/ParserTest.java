package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addRequiredOption("r", false, "Required Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "1", "-b", "2", "-c", "3", "--r"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("r"));

        assertEquals(1, cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b"));
        assertEquals(3, cmd.getOptionValue("c"));
        assertNull(cmd.getOptionValue("r"));

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithArgs() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addRequiredOption("r", false, "Required Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "1", "-b", "2", "-c", "3", "--r", "arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("r"));

        assertEquals(1, cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b"));
        assertEquals(3, cmd.getOptionValue("c"));
        assertEquals("arg1", cmd.getOptionValue("r"));

        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-x", "1", "-a", "1", "-b", "2"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));

        assertEquals(1, cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b"));

        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("-x", args.get(0));

        try {
            cmd.getOptionValue("x");
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("r", false, "Required Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "1"};
        CommandLine cmd = parser.parse(arguments);

        assertFalse(cmd.hasOption("r"));
        assertTrue(cmd.getRequiredOptions().isEmpty());

        try {
            cmd.parse();
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseWithUnrecognizedArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--unrecognized"};
        CommandLine cmd = parser.parse(arguments, false);

        assertFalse(cmd.hasOption("unrecognized"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("--unrecognized", cmd.getArgs().get(0));

        try {
            cmd.parse();
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseWithUnrecognizedArgumentStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--unrecognized"};
        CommandLine cmd = parser.parse(arguments, true);

        assertFalse(cmd.hasOption("unrecognized"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("--unrecognized", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithInvalidBooleanArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "foo"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithInvalidIntArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "foo"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals(0, cmd.getOptionValue("a").intValue());
    }

    @Test
    public void testParseWithInvalidIntArgument2() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "bar"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals(0, cmd.getOptionValue("a").intValue());
    }

    @Test
    public void testParseWithValidIntArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "42"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals(42, cmd.getOptionValue("a").intValue());
    }

    @Test
    public void testParseWithValidIntArrayArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A", "nums");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "1,2,3"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertArrayEquals(new Integer[]{1, 2, 3}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithValidStringArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "foo"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("foo", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValidStringArrayArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A", "values");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "foo,bar,baz"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithValueFromProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "10");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(properties);

        assertTrue(parser.getCmd().hasOption("a"));
        assertTrue(parser.getCmd().getOptionValue("a"));
        assertEquals(10, parser.getCmd().getOptionValue("b").intValue());
    }
}