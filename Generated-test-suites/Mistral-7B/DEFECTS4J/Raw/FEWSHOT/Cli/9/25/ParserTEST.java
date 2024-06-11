package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithDefaultOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] arguments = new String[]{"-a", "-b", "1", "2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A", "aValue");
        options.addRequiredOption("b", false, "Option B");

        String[] arguments = new String[]{"-a"};
        try {
            new DefaultParser().parse(options, arguments);
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Option B is missing", e.getMessage());
        }

        arguments = new String[]{"-a", "-b"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithVariableArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", "value");
        options.addOption("c", true, "Option C", "values");
        options.addOption("d", true, "Option D", true);

        String[] arguments = new String[]{"-a", "-b", "foo", "bar", "-c", "one", "two", "three", "-d"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        List<String> bValues = cmd.getOptionValues("b");
        assertEquals(2, bValues.size());
        assertEquals("foo", bValues.get(0));
        assertEquals("bar", bValues.get(1));

        List<String> cValues = cmd.getOptionValues("c");
        assertEquals(3, cValues.size());
        assertEquals("one", cValues.get(0));
        assertEquals("two", cValues.get(1));
        assertEquals("three", cValues.get(2));

        List<String> dValues = cmd.getOptionValues("d");
        assertNotNull(dValues);
        assertTrue(dValues.isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[]{"-x"};
        new DefaultParser().parse(options, arguments);

        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B", "value");

        String[] arguments = new String[]{"-a", "-b"};
        new DefaultParser().parse(options, arguments);

        fail("MissingArgumentException expected");
    }

    @Test
    public void testParseWithMixedStyles() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B", "value");
        options.addOption("c", 2, "Option C", "values");
        options.addOption("d", true, "Option D");

        String[] arguments = new String[]{"-a", "-b", "value", "-c", "values1", "values2", "-d"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals(2, cmd.getNumOptions("c"));
        List<String> cValues = cmd.getOptionValues("c");
        assertEquals("values1", cValues.get(0));
        assertEquals("values2", cValues.get(1));
        assertTrue(cmd.hasOption("d"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long option A");
        options.addOption("long-option-b", 1, "Long option B", "value");

        String[] arguments = new String[]{"--long-option-a", "--long-option-b", "value"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.hasOption("long-option-b"));
        assertEquals("value", cmd.getOptionValue("long-option-b"));
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Option group");
        group.setRequired(true);

        options.addOptionGroup(group);
        group.addOption("a", true, "Option A", "value");
        group.addOption("b", true, "Option B", "value");

        String[] arguments = new String[]{"--group"};
        try {
            new DefaultParser().parse(options, arguments);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("group is missing", e.getMessage());
        }

        arguments = new String[]{"--group", "--a"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        List<Option> selectedOptions = group.getSelectedOptions();
        assertEquals(2, selectedOptions.size());
        assertEquals("a", selectedOptions.get(0).getKey());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }
}