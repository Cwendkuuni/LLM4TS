package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSimpleArgs() throws ParseException {
        String optString = "a:b,c::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true, "b"), new Option("c", 1)));
        String[] arguments = new String[]{"-a", "value1", "--c", "value2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals("value2", cmd.getArgs().get(1));
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertSame(options.findOption("c"), cmd.getOption("c"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String optString = "long-option:,long-option2::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("long-option", true, "long"), new Option("long-option2", 1, "long2")));
        String[] arguments = new String[]{"--long-option", "value1", "--long-option2", "value2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals("value2", cmd.getArgs().get(1));
        assertSame(options.findOption("long-option"), cmd.getOption("long-option"));
        assertSame(options.findOption("long-option2"), cmd.getOption("long-option2"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        String optString = "a:b,c::,d::,e::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true, "b"), new Option("c", 1), new Option("d"), new Option("e")));
        String[] arguments = new String[]{"-a", "value1", "--c", "value2", "--d", "--e"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(4, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals("value2", cmd.getArgs().get(1));
        assertNull(cmd.getOption("d"));
        assertNull(cmd.getOption("e"));
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertSame(options.findOption("c"), cmd.getOption("c"));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        String optString = "a::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true)));
        String[] arguments = new String[]{"-a", "value"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("value", cmd.getArgs().get(0));
        assertSame(options.findOption("a"), cmd.getOption("a"));
    }

    @Test
    public void testParseWithRequiredArg() throws ParseException {
        String optString = "a:";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true, "requiredArg")));
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingArgumentException e) {
            assertEquals("'requiredArg' requires a value", e.getMessage());
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        String optString = "a:b";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true, "b")));
        String[] arguments = new String[]{"--unrecognizedOption"};

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: unrecognizedOption", e.getMessage());
        }
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        String optString = "a:b,c::,d::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true, "b"), new Option("c", 1), new Option("d")));
        String[] arguments = new String[]{"-a", "value1", "--c", "value2", "--d"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals("value2", cmd.getArgs().get(1));
        assertNull(cmd.getOption("d"));
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertSame(options.findOption("c"), cmd.getOption("c"));
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException {
        String optString = "a:";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true)));
        String[] arguments = new String[]{"-a", "value1", "-a", "value2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals("value2", cmd.getArgs().get(1));
        List<String> values = ((List) cmd.getOption("a").getValue()).subList(0, 2);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String optString = "a:,b::";
        Options options = new Options();
        options.addOptions(Arrays.asList(new Option("a", true), new Option("b", true)));
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("value1", cmd.getOption("a"));
        assertEquals("value2", cmd.getOption("b"));
    }

    @Test
    public void testParseWithHelp() throws ParseException {
        String optString = "h";
        Options options = new Options();
        options.addHelpOption(true);
        options.addOptions(Arrays.asList(new Option("h", true)));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.getHelpMessage().startsWith("Usage: "));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String optString = "g::,a::,b::";
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g", true));
        group.addOption(new Option("a", true, "group"));
        group.addOption(new Option("b", true, "group"));
        options.addOptionGroup(group);
        options.addOptions(Arrays.asList(new Option("h", true)));

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertSame(group, e.getMissingOptions().get(0).getOptGroup());
        }
    }
}