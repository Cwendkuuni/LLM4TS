package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", 1);
        options.addOption("c", 2);
        options.addOption("d", 3);
        options.addRequiredOption("e", true);

        String[] args = {"-a", "--b", "-c", "2", "--d", "3", "--e"};
        CommandLine cmd = new DefaultParser().parse(options, args);

        assertEquals(args.length, cmd.getArgs().size());
        assertArrayEquals(Arrays.copyOfRange(args, args.length - cmd.getNumArgs(), args.length), cmd.getArgs().toArray(new String[0]));

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(2, cmd.getOptionValue("c", (String)null));
        assertEquals(3, cmd.getOptionValue("d", (String)null));
        assertTrue(cmd.hasOption("e"));
        assertFalse(cmd.getArgList().isEmpty());
        assertEquals(args[args.length - 1], cmd.getArgs().get(cmd.getArgs().size() - 1));

        assertEquals(true, options.isRequiredOption("e"));
        assertEquals(true, cmd.hasRequiredOption("e"));

        assertNull(cmd.getOptionValue("f", (String)null));
        assertNull(cmd.getOptionValue("g", (String)null));
        assertNull(cmd.getOptionValue("h", (String)null));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true);
        options.addRequiredOption("c", true);

        String[] args = {"-a", "--b"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", true).requiredArg();

        String[] args = {"-a"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true);

        String[] args = {"-x", "-a"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", true).requiredArg();

        String[] args = {"-a", "invalid"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testMixedOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", 1);

        String[] args = {"-a", "1", "--b", "2", "-c"};
        new DefaultParser().parse(options, args);

        assertEquals(3, args.length);
        assertEquals(2, cmd.getNumOptions());
        assertEquals(1, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));
        assertArrayEquals(new String[]{"1"}, cmd.getArgs().toArray(new String[0]));
    }

    @Test
    public void testProcessProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true);

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        new DefaultParser().parse(options, null, properties);

        assertEquals(true, options.getOption("a").getValue().length > 0);
    }

    @Test
    public void testParseWithHelpOption() throws Exception {
        Options options = new Options();
        options.addHelpOption(true);

        String[] args = {"--help"};
        CommandLine cmd = new DefaultParser().parse(options, args);

        assertTrue(cmd.hasHelpOption());
        assertTrue(cmd.hasHelpOption(false));
    }

    @Test
    public void testParseWithInvalidHelpOption() throws Exception {
        Options options = new Options();
        options.addHelpOption(true);

        String[] args = {"-h"};
        CommandLine cmd = new DefaultParser().parse(options, args);

        assertFalse(cmd.hasHelpOption());
        assertFalse(cmd.hasHelpOption(false));
    }
}