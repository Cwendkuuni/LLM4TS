package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testSimpleUseCase() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "This is an option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"o"});

        assertTrue(cmd.hasOption("option"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertEquals(Arrays.asList("o"), cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "This is an option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[0]);

        assertFalse(cmd.hasOption("option"));
    }

    @Test
    public void testOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "arg", true, "This option takes an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"a", "value"});

        assertTrue(cmd.hasOption("arg"));
        assertEquals("value", cmd.getOptionValue("arg"));
        assertEquals(Arrays.asList(), cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "arg", true, "This option takes an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"a"});

        assertFalse(cmd.hasOption("arg"));
        assertNull(cmd.getOptionValue("arg"));
    }

    @Test
    public void testMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o1", "option1", false, "Option 1");
        options.addOption("o2", "option2", false, "Option 2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"o1", "o2"});

        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(Arrays.asList(), cmd.getArgs());
    }

    @Test
    public void testLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", "long option", false, "Long option description");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"--longOption"});

        assertTrue(cmd.hasOption("longOption"));
        assertEquals(Arrays.asList(), cmd.getArgs());
    }

    @Test
    public void testRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "This is an option");
        options.addRequiredOption("r", "requiredOption", false, "This is a required option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"o"});

        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOption("requiredOption"));

        try {
            cmd = parser.parse(options.toArray(new String[0]), new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(true);
        group.addOption("g1", "option1", false, "Option group 1");
        group.addOption("g2", "option2", false, "Option group 2");

        options.addOptionGroup(group);
        options.addOption("o", "option", false, "This is an option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"g1"});

        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("option"));
    }

    @Test
    public void testParsingWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "This is an option");

        Properties properties = new Properties();
        properties.setProperty("option", "value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                new String[0], properties);

        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
        assertEquals(Arrays.asList(), cmd.getArgs());
    }
}