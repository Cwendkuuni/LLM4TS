package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseResult;
import org.junit.Test;

/**
 * Test class for Parser.java.
 */
public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        // Given
        String[] args = new String[]{"--test", "-a", "-b", "value"};
        Options options = new Options();
        options.addOption("test", true, "t");
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, true);

        // Then
        assertSame(options, cmd.getOptions());
        assertSame(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertSame(args[i], cmd.getArgs().get(i));
        }
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertSame("value", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testParserMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"--test"};
        Options options = new Options();
        options.addOption("test", true, "t");
        options.addOption("a", true, "a");
        options.requireOption("b");

        CommandLineParser parser = new DefaultParser();

        // When
        parser.parse(options, args, true);

        // Then
        // ParseException is thrown
    }

    @Test
    public void testParserProperties() throws ParseException {
        String[] args = new String[]{"--test", "-p", "value=123"};
        Options options = new Options();
        options.addOption("test", true, "t");
        options.addOption("p", true, "p");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, new Properties());

        // Then
        assertSame(options, cmd.getOptions());
        assertSame(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertSame(args[i], cmd.getArgs().get(i));
        }
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("p"));
        assertEquals("123", cmd.getOptionValue("p"));
    }

    @Test
    public void testParserStopAtNonOption() throws ParseException {
        String[] args = new String[]{"--test", "-p", "value", "value2"};
        Options options = new Options();
        options.addOption("test", true, "t");
        options.addOption("p", true, "p");
        options.addOption("q", true, "q");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, true);

        // Then
        assertSame(options, cmd.getOptions());
        assertSame(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertSame(args[i], cmd.getArgs().get(i));
        }
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("p"));
        assertEquals("value", cmd.getOptionValue("p"));
        assertFalse(cmd.hasOption("q"));
        assertSame(2, cmd.getArgs().size());
        assertSame("value2", cmd.getArgs().get(0));
        assertSame("value", cmd.getArgs().get(1));
    }
}