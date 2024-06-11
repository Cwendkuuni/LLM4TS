package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParser_Empty() {
        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            assertTrue(cmd.getArgs().isEmpty());
            assertFalse(cmd.hasOption("help"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_Help() {
        options.addOption("h", "help", false, "Print help");
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--help"});
            assertTrue(cmd.hasOption("help"));
            assertEquals("help", cmd.getOptionValue("help"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_Argument() {
        options.addOption("a", "arg", true, "Argument");
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--arg", "value"});
            assertTrue(cmd.hasOption("arg"));
            assertEquals("value", cmd.getOptionValue("arg"));
            assertEquals(Arrays.asList("value"), cmd.getArgs());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_RequiredOption() {
        options.addOption("r", "required", true, "Required option");
        options.addOption("r", true, "Required option without value");
        options.addRequiredOption("r", "required-with-value", true, "Required option with value");

        try {
            CommandLine cmd = parser.parse(options, new String[]{});
            assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"--required"}));
            assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"--required-with-value"}));
            assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[]{"--required", ""}));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--required", "value1", "--required-with-value"});
            assertTrue(cmd.hasOption("required"));
            assertTrue(cmd.hasOption("required-with-value"));
            assertFalse(cmd.getArgs().isEmpty());
            assertEquals(Arrays.asList("value1"), cmd.getOptionValues("required"));
            assertEquals(Arrays.asList(""), cmd.getOptionValues("required-with-value"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_OptionalArgument() {
        options.addOption("o", "optional", false, "Optional option");
        options.addOption("o", true, "Optional option with value");

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--optional"});
            assertTrue(cmd.hasOption("optional"));
            assertNull(cmd.getOptionValue("optional"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--optional", "value"});
            assertTrue(cmd.hasOption("optional"));
            assertEquals("value", cmd.getOptionValue("optional"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_MultipleOptions() {
        options.addOption("o", "option", true, "Option");
        options.addOption("a", "arg", true, "Argument");

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--option", "value1", "--option", "value2", "--arg", "argValue"});
            assertTrue(cmd.hasOption("option"));
            assertEquals(Arrays.asList("value1", "value2"), cmd.getOptionValues("option"));
            assertTrue(cmd.hasOption("arg"));
            assertEquals("argValue", cmd.getOptionValue("arg"));
            assertEquals(Arrays.asList("argValue"), cmd.getArgs());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_OptionGroup() {
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption(new Option("a", "arg", true, "Argument"));
        options.addOption("g", "group", false, "Group", group);

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--group", "--arg", "value"});
            assertTrue(cmd.hasOption("group"));
            assertTrue(cmd.hasOption("arg"));
            assertEquals("value", cmd.getOptionValue("arg"));
            assertEquals(Arrays.asList("value"), cmd.getArgs());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParser_Properties() {
        options.addOption("p", "property", true, "Property");

        Properties properties = new Properties();
        properties.setProperty("property", "value");

        try {
            CommandLine cmd = parser.parse(options, null, properties);
            assertTrue(cmd.hasOption("property"));
            assertEquals("value", cmd.getOptionValue("property"));
            assertEquals(Arrays.asList("value"), cmd.getArgs());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParser_ParseException() {
        options.addOption("p", "property", true, "Property");
        options.addOption("h", "help", false, "Print help");

        try {
            parser.parse(options, new String[]{"--help", "--property"});
            fail();
        } catch (MissingOptionException e) {
            // expected for --help
        } catch (ParseException ex) {
            throw ex;
        }

        try {
            parser.parse(options, new String[]{"--help", "--property", "--help"});
            fail();
        } catch (MissingOptionException e) {
            // expected for second --help
        } catch (ParseException ex) {
            throw ex;
        }

        try {
            parser.parse(options, new String[]{"--help", "--property=value", "--help"});
            fail();
        } catch (MissingOptionException e) {
            // expected for second --help
        } catch (ParseException ex) {
            throw ex;
        }

        try {
            parser.parse(options, new String[]{"--property=value"});
            fail();
        } catch (MissingOptionException e) {
            // expected for --help is missing
        } catch (ParseException ex) {
            throw ex;
        }

        try {
            parser.parse(options, new String[]{"--property=value", "--property=value2"});
            fail();
        } catch (MissingArgumentException e) {
            // expected for --property=value, requires a value
        } catch (ParseException ex) {
            throw ex;
        }
    }

    @Test(expected = ParseException.class)
    public void testParser_UnrecognizedOption() {
        options.addOption("o", "option", true, "Option");

        try {
            parser.parse(options, new String[]{"--unknown-option"});
            fail();
        } catch (UnrecognizedOptionException e) {
            throw e;
        }
    }

}