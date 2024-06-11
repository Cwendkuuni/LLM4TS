package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--foo", "-b", "-c", "--", "-d", "e" };

        CommandLine cmd = parser.parse(options, args, null);

        assertEquals(6, cmd.getArgs().size());
        assertEquals("--foo", cmd.getOptionValues("foo")[0]);
        assertEquals("e", cmd.getArgs().get(5));

        assertFalse(cmd.hasOption("bar"));
        assertNull(cmd.getOptionValue("bar"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        options.addOption("d", true, "debug");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--foo", "-b", "-c", "--d" };
        Properties properties = new Properties();
        properties.setProperty("d", "value");

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("--foo", cmd.getOptionValues("foo")[0]);
        assertEquals("value", cmd.getOptionValue("d"));
        assertTrue(cmd.hasOption("d"));
        assertEquals("value", cmd.getOptionValue("d", true));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--foo" };

        parser.parse(options, args, null);

        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--foo" };
        Properties properties = new Properties();

        parser.parse(options, args, properties, false);

        fail("ParseException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--bar" };

        parser.parse(options, args, null);

        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--f" };

        parser.parse(options, args, null);

        fail("MissingArgumentException expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] {};

        parser.parse(options, args, null);

        fail("MissingOptionException expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] {};
        Properties properties = new Properties();

        parser.parse(options, args, properties, false);

        fail("MissingOptionException expected");
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[0];

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseEmptyWithProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        Properties properties = new Properties();

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptionValues("d") == null);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "-x", "arg1", "--foo" };

        CommandLine cmd = parser.parse(options, args, null, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithStopAtNonOptionWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "-x", "arg1", "--foo" };
        Properties properties = new Properties();

        CommandLine cmd = parser.parse(options, args, properties, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--f", "arg" };

        CommandLine cmd = parser.parse(options, args, null);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getOptionValues("a")[0]);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithOptionalArgWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "flag");
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--f", "arg" };
        Properties properties = new Properties();
        properties.setProperty("a", "propVal");

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getOptionValues("a")[0]);
        assertEquals("propVal", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--g", "--foo" };

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getOptions().size() == 2);
        assertEquals("--g", cmd.getOptionValues("g")[0]);
        assertEquals("--foo", cmd.getOptionValues("foo")[0]);
    }

    @Test
    public void testParseWithGroupAndRequired() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] {};

        try {
            parser.parse(options, args, null);
            fail();
        } catch (MissingOptionException e) {
            assertTrue(e.getMessage().contains("group"));
        }
    }

    @Test
    public void testParseWithGroupAndMixed() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--foo" };

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOptions().get(0) instanceof Option);
        assertEquals("--foo", cmd.getOptionValues("foo")[0]);
    }

    @Test
    public void testParseWithGroupAndNoOption() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] {};

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getOptions().size() == 0);
    }

    @Test
    public void testParseWithGroupAndOptionalArg() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--g", "arg" };

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getOptions().size() == 2);
        assertEquals("--g", cmd.getOptionValues("g")[0]);
        assertEquals("arg", cmd.getOptionValues("a")[0]);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithGroupAndOptionalArgWithProperties() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--g" };
        Properties properties = new Properties();
        properties.setProperty("a", "argProp");

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.getOptions().size() == 1);
        assertEquals("--g", cmd.getOptionValues("g")[0]);
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithGroupAndMixedArgs() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--g", "arg1", "--foo", "arg2" };

        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.getOptions().size() == 3);
        assertEquals("--g", cmd.getOptionValues("g")[0]);
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("--foo", cmd.getOptionValues("foo")[0]);
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithGroupAndMixedArgsWithProperties() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("g", false, "groupOption");
        options.addOptionGroup(group);
        options.addOption("f", false, "flag");
        Parser parser = new DefaultParser();
        String[] args = new String[] { "--g", "arg1", "--foo" };
        Properties properties = new Properties();
        properties.setProperty("foo", "propVal");

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.getOptions().size() == 2);
        assertEquals("--g", cmd.getOptionValues("g")[0]);
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("propVal", cmd.getOptionValue("foo"));
    }
}