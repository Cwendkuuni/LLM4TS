package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);

        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
    }

    @Test
    public void testParseSingleValueOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("value", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "-b"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(2, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertSame(options.findOption("b"), cmd.getOption("b"));
    }

    @Test
    public void testParseMultipleValuesOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "value1", "-a", "value2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        List<String> values = Arrays.asList(cmd.getOptionValues("a"));
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"arg1", "arg2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "arg"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
    }

    @Test
    public void testParseWithArgumentsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        String[] arguments = new String[]{"arg1", "arg2"};

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "value1", "arg2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertEquals("value1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithOptionalArgumentAndValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-a", "value1", "value2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("value2", cmd.getArgs()[1]);
        assertEquals(1, cmd.getOptions().size());
        assertSame(options.findOption("a"), cmd.getOption("a"));
        assertEquals(Arrays.asList(new String[]{"value1", "value2"}), cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithNestedOptions() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("g", "group", false, "Group option"));
        group.addOption(new Option("a", true, "A value in group"));
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-g", "-a", "value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(2, cmd.getOptions().size());
        Option groupOption = cmd.getOption("g");
        Option groupAOption = cmd.getOption("a");
        assertEquals("Group option", groupOption.getLongOpt());
        assertEquals(true, groupOption.isRequired());
        assertEquals("A value in group", groupAOption.getLongOpt());
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMissingRequiredOptionAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"arg1", "arg2"};

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMissingRequiredOptionAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0], properties));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-x"};

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }
}