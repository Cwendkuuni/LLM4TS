package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithDefaultOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", false, "B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("A value", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addRequiredOption("b", false, "B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a" };
        try {
            parser.parse(options, arguments);
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }

        String[] argumentsWithOption = new String[] { "-a", "--b" };
        CommandLine cmd = parser.parse(options, argumentsWithOption);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("A value", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithValueOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "--b", "2" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A value");
        options.addOption("longOptionB", true, "Long option B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "--longOptionA", "1", "--longOptionB", "2" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("longOptionA"));
        assertEquals("2", cmd.getOptionValue("longOptionB"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Short option A value");
        options.addOption("b", true, "Short option B value");
        options.addOption("longOptionC", true, "Long option C value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "--longOptionC", "3" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("3", cmd.getOptionValue("longOptionC"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMultipleValuesForOneOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("a", true, "Another A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "-a", "2" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        String[] aValues = cmd.getOptionValues("a");
        assertArrayEquals(new String[] { "1", "2" }, aValues);
    }

    @Test
    public void testParseWithValueOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        options.addOption("c", true, "C value");
        options.addOption("d", true, "D value [ARG]");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "--b", "2", "--c", "3", "--d", "4" };
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
        assertEquals("4", cmd.getOptionValue("d"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.put("a", "1");
        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals(0, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "--", "notAnOption" };
        CommandLine cmd = parser.parse(options, arguments, true);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("notAnOption", cmd.getArg(0));
    }

    @Test
    public void testParseWithMixedShortAndLongOptionsWithStopAtNonOption()
            throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Short option A value");
        options.addOption("longOptionB", true, "Long option B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[] { "-a", "1", "--longOptionB", "2", "--" };
        CommandLine cmd = parser.parse(options, arguments, true);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("longOptionB"));
    }
}