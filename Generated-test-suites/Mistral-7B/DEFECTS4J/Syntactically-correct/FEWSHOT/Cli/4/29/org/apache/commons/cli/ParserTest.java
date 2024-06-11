package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmptyArguments() throws ParseException {
        final Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        final Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        assertNotNull(cmd);
        assertEquals("arg", cmd.getArgs().get(0));
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        final Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertNotNull(cmd);
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseArgumentsWithOptions() throws ParseException {
        final Options options = new Options();
        options.addOption("o", "opt", false, "An option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"o"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt"));

        cmd = parser.parse(options, new String[]{"o", "arg"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt"));
        assertEquals("arg", cmd.getOptionValue("opt"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        final Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"missingOption"});
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        final Options options = new Options();
        options.addOption("o", "opt", false, "An option");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"x"});
    }

    @Test
    public void testParseOptionsWithValues() throws ParseException {
        final Options options = new Options();
        options.addOption("o", "opt", true, "An option with value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"o", "value"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));

        cmd = parser.parse(options, new String[]{"o"});
        assertNotNull(cmd);
        assertNull(cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseLongOptionName() throws ParseException {
        final Options options = new Options();
        options.addOption("l-o-p", "longOption", false, "A long option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"l-o-p"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseDoubleDashOption() throws ParseException {
        final Options options = new Options();
        options.addOption("--doubleDash", true, "A double-dash option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--doubleDash"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("doubleDash"));
    }

    @Test
    public void testParseRequiredOption() throws ParseException {
        final Options options = new Options();
        options.addOption("r", "required", true, "A required option");
        options.addRequiredOption("r");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"r"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("required"));

        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseOptionGroup() throws ParseException {
        final Options options = new Options();
        final OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption("g", "groupOption", false, "A group option");
        options.addOptionGroup(group);
        options.addOption("o", "opt", false, "An option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"g"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("groupOption"));
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseProperties() throws ParseException {
        final Options options = new Options();
        final Properties properties = new Properties();
        properties.setProperty("opt1", "value1");
        properties.setProperty("opt2", "value2");

        options.parseRequiredOption("opt1", false);
        options.parseOptionalValue("opt1", true, "default1");
        options.parseOptionalValue("opt2", false, "default2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt1"));
        assertEquals("value1", cmd.getOptionValue("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertEquals("value2", cmd.getOptionValue("opt2"));
        assertNull(cmd.getOptionValue("opt3"));
    }

    @Test
    public void testParsePropertiesWithDefaultValue() throws ParseException {
        final Options options = new Options();
        final Properties properties = new Properties();

        options.parseOptionalValue("opt1", true, "default1");

        properties.setProperty("opt1", "value1");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt1"));
        assertEquals("value1", cmd.getOptionValue("opt1"));

        properties.clear();
        cmd = parser.parse(options, null, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt1"));
        assertEquals("default1", cmd.getOptionValue("opt1"));
    }
}