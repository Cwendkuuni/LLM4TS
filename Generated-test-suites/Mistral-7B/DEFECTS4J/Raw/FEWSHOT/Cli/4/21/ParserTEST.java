package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testEmptyParse() throws ParseException {
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(new Options(), new String[0]);
        assertFalse(cmd.hasOptions());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "1"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();

        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertTrue(cmd.getArgs().size() == 2);
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.parse(options, new String[]{"-b"});
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValue() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        parser.parse(options, new String[]{"-a"});
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");
        options.addOption("long-option-b", 1, "Long Option B");

        CommandLine cmd = parser.parse(options, new String[]{"--long-option-a", "--long-option-b", "1"});
        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.hasOption("long-option-b"));
        assertEquals("1", cmd.getOptionValue("long-option-b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("l", "long-option-a", true, "Long Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("L", "long-option-b", 1, "Long Option B");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "--long-option-a", "-b", "--long-option-b", "1"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("long-option-b"));
        assertEquals("1", cmd.getOptionValue("long-option-b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptionalValues() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 1, "Option C, with default value");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "2"});
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("c"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithRequiredValues() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B, with value required");

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertTrue(cmd.getArgs().isEmpty());

        try {
            cmd = parser.parse(options, new String[]{"-a", "2"});
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Option B requires an argument"));
        }
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        OptionGroup group = new OptionGroup("MyGroup", "My Group");
        group.setRequired(false);

        group.addOption("a", true, "Option A in group");
        group.addOption("b", true, "Option B in group");

        options.addOptionGroup(group);
        options.addOption("c", true, "Option C, not in group");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertSame(group, cmd.getOption("a").getOptGroup());
        assertSame(group, cmd.getOption("b").getOptGroup());
        assertNull(cmd.getOption("a").getValues());
        assertNull(cmd.getOption("b").getValues());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithGroupAndValues() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        OptionGroup group = new OptionGroup("MyGroup", "My Group");
        group.setRequired(false);

        group.addOption("a", true, "Option A in group");
        group.addOption("b", true, "Option B in group");

        options.addOptionGroup(group);
        options.addOption("c", true, "Option C, not in group");

        CommandLine cmd = parser.parse(options, new String[]{"--my-group", "-a", "-b", "--my-group", "-c"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertSame(group, cmd.getOption("a").getOptGroup());
        assertSame(group, cmd.getOption("b").getOptGroup());
        assertNotNull(cmd.getOption("a").getValues());
        assertNotNull(cmd.getOption("b").getValues());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b.value", "valueB");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithInvalidProperties() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("invalid.property", "valueB");

        try {
            parser.parse(options, new String[0], properties);
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Invalid property: invalid.property"));
        }
    }
}