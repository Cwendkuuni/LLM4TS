package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "arg", true, "an argument");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"foo"});
        assertNotNull(cmd);
        assertFalse(cmd.hasOption("a"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("foo", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        options.addOption("b", true, "an option b");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"foo", "bar", "baz"});
        assertNotNull(cmd);
        assertNull(cmd.getOption("a"));
        assertEquals(3, cmd.getArgs().size());
        assertEquals("foo", cmd.getArgs().get(0));
        assertEquals("bar", cmd.getArgs().get(1));
        assertEquals("baz", cmd.getArgs().get(2));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        options.addOption("b", true, "an option b");
        options.option("c", "c", 1, "an option c with an optional argument");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "foo", "--c", "bar"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
        assertEquals(1, cmd.getValues("c").length);
        assertEquals("bar", cmd.getValues("c")[0]);
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithMultipleArgumentsAndOptionalArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        options.addOption("b", true, "an option b");
        options.option("c", "c", 1, "an option c with an optional argument");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "foo", "-b", "bar", "--c", "qux"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("foo", cmd.getArgs().get(0));
        assertEquals("bar", cmd.getArgs().get(1));
        assertEquals(1, cmd.getValues("c").length);
        assertEquals("qux", cmd.getValues("c")[0]);
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "a long option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--longOption"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("longOption"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithLongOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "a long option");
        options.addOptionValue("longOption", "value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--longOption=value"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("longOption"));
        assertEquals("value", cmd.getOptionValue("longOption"));
        assertNull(cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-b"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        options.addOption("a", true, "an option a");
        options.requiredOption("a", "a required option");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        options.addOption("a", true, "an option a");
        options.option("b", "b", 1, "an option b with an optional argument");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertNull(cmd.getValues("b"));
        throw new AssertionError("Should have thrown MissingArgumentException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an option a");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--x"});
    }
}