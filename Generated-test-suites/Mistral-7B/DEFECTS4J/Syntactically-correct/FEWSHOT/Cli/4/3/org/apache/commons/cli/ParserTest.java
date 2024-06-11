package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, new String[0]);

        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithSimpleArgs() throws ParseException {
        String[] arguments = {"--foo", "-b", "123", "--bar=456"};
        Options options = new Options();

        Option foo = new Option("foo", true, false, "Foo option");
        Option bar = new Option("bar", true, false, "Bar option");

        options.addOption(foo);
        options.addOption(bar);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = new ArrayList<>(cmd.getOptions().size());
        cmd.getOptions().forEachRemaining(optionsList::add);

        assertEquals(4, cmd.getArgs().length);
        assertArrayEquals(new String[]{"123"}, cmd.getArgs());
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertTrue(optionsList.get(1) instanceof Option);
        assertEquals("Foo option", optionsList.get(0).getLongOpt());
        assertEquals("Bar option", optionsList.get(1).getLongOpt());
    }

    @Test
    public void testParseWithComplexArgs() throws ParseException {
        String[] arguments = {"--foo", "-b", "123", "--bar=456", "--baz=789",
                "--quux", "-d", "file.txt"};
        Options options = new Options();

        Option foo = new Option("foo", true, false, "Foo option");
        Option bar = new Option("bar", true, false, "Bar option");
        Option quux = new Option("quux", true, false, "Quux option");

        options.addOption(foo);
        options.addOption(bar);
        options.addOption(quux);

        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption(new Option("g", "Group", false, "Group option"));

        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = new ArrayList<>(cmd.getOptions().size());
        cmd.getOptions().forEachRemaining(optionsList::add);

        assertEquals(5, cmd.getArgs().length);
        assertArrayEquals(new String[]{"file.txt"}, cmd.getArgs());
        assertEquals(5, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertTrue(optionsList.get(1) instanceof Option);
        assertTrue(optionsList.get(2) instanceof Option);
        assertTrue(optionsList.get(3) instanceof Option);
        assertTrue(optionsList.get(4) instanceof Option);
        assertEquals("Foo option", optionsList.get(0).getLongOpt());
        assertEquals("Bar option", optionsList.get(1).getLongOpt());
        assertEquals("Quux option", optionsList.get(2).getLongOpt());
        assertEquals("Group option", optionsList.get(3).getLongOpt());
        assertNull(optionsList.get(4).getValue());
        assertSame(group, optionsList.get(3).getOptGroup());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"--foo", "-b", "123", "--bar=456", "--quux=789"};
        Properties properties = new Properties();
        properties.setProperty("bar", "456");
        properties.setProperty("quux", "789");

        Options options = new Options();

        Option foo = new Option("foo", true, false, "Foo option");

        options.addOption(foo);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        List<Option> optionsList = new ArrayList<>(cmd.getOptions().size());
        cmd.getOptions().forEachRemaining(optionsList::add);

        assertEquals(3, cmd.getArgs().length);
        assertArrayEquals(new String[]{"123"}, cmd.getArgs());
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertTrue(optionsList.get(1) instanceof Option);
        assertEquals("Foo option", optionsList.get(0).getLongOpt());
        assertEquals("bar", optionsList.get(1).getLongOpt());
        assertArrayEquals(new String[]{"456"}, optionsList.get(1).getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"--foo", "-b", "123", "--bar=456", "--quux=789"};
        Options options = new Options();

        Option foo = new Option("foo", true, false, "Foo option");
        Option bar = new Option("bar", true, false, "Bar option");
        Option quux = new Option("quux", true, false, "Quux option");

        options.addOption(foo);
        options.addOption(bar);
        options.addOption(quux);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, true);

        List<Option> optionsList = new ArrayList<>(cmd.getOptions().size());
        cmd.getOptions().forEachRemaining(optionsList::add);

        assertEquals(5, cmd.getArgs().length);
        assertArrayEquals(new String[]{"123", "--quux=789"}, cmd.getArgs());
        assertEquals(3, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertTrue(optionsList.get(1) instanceof Option);
        assertTrue(optionsList.get(2) instanceof Option);
        assertEquals("Foo option", optionsList.get(0).getLongOpt());
        assertEquals("Bar option", optionsList.get(1).getLongOpt());
        assertArrayEquals(new String[]{"456"}, optionsList.get(1).getValue());
        assertEquals("Quux option", optionsList.get(2).getLongOpt());
        assertNull(optionsList.get(2).getValue());
    }
}