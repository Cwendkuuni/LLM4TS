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
    public void testParser() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo", "-b", "--bar=baz", "-b", "--", "arg1", "arg2"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertNotNull(cmd);
            assertEquals(Arrays.asList("--foo", "-b", "--bar=baz"), cmd.getOptions());
            assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgs());
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test(expected = ParseException.class)
    public void testParserMissingOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo", "-b", "--bar=baz"};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParserUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo", "--qux", "--bar=baz"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParserOptionalOption() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo", "--bar=baz", "-b"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("bar"));
            assertNull(cmd.getOptionValue("bar"));
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParserOptionalOptionValue() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo", "--bar=qux", "--bar=quux"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("bar"));
            assertEquals("quux", cmd.getOptionValue("bar"));
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParserRequiredOption() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("foo"));
            assertFalse(cmd.hasOption("qux"));
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: qux", e.getMessage());
        }
    }

    @Test
    public void testParserRequiredOptionValue() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--foo=qux", "--foo=quux"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("foo"));
            assertEquals("quux", cmd.getOptionValue("foo"));
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParserOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(true);

        options.addOptionGroup(group);
        options.addOption(group.getOptions().get(0));
        options.addOption(group.getOptions().get(1));

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--g", "--qux", "--bar=baz"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("qux"));
            assertTrue(cmd.hasOption("bar"));
            assertFalse(cmd.hasOption("g"));
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParserOptionGroupOptional() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");

        options.addOptionGroup(group);
        options.addOption(group.getOptions().get(0));
        options.addOption(group.getOptions().get(1));

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"--g", "--qux"};

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("qux"));
            assertFalse(cmd.hasOption("g"));
        } catch (ParseException e) {
            // should not reach here
        }
    }
}