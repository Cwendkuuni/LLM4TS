package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", false, "Prints option B");

        String[] arguments = new String[]{"-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("-a", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithValues() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", true, "Prints option B");

        String[] arguments = new String[]{"-a", "A", "-b", "B"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("A", cmd.getOptionValue("a"));
        assertEquals("B", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithValuesGrouped() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption("a", true, "Prints option A");
        group.addOption("b", true, "Prints option B");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(group, cmd.findOptionGroup("group"));
        assertEquals("A", cmd.getOptionValue("a"));
        assertEquals("B", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequired() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", true, "Prints option B");
        options.addOption("c", true, "Prints option C");

        String[] arguments = new String[]{"-a", "-b"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        arguments = new String[]{"-a", "-b", "-c"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithRequiredGroup() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        group.addOption("a", true, "Prints option A");
        group.addOption("b", true, "Prints option B");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        arguments = new String[]{"--group", "-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", true, "Prints option B");

        String[] arguments = new String[]{"--", "-a", "A", "-b", "B"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Arrays.asList("A", "B"), cmd.getArgList());
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithArgumentsAndOptions() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", true, "Prints option B");

        String[] arguments = new String[]{"--", "-a", "A", "-b", "B", "-c", "C"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Arrays.asList("A", "B", "C"), cmd.getArgList());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", false, "Prints option A");
        parser.setProperties(new Properties(){{
            setProperty("a", "1");
        }});

        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithLongArguments() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("longOption", true, "Prints option with long name");

        String[] arguments = new String[]{"--longOption"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseWithShortAndLongArguments() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("l", "longOption", true, "Prints option with long name");

        String[] arguments = new String[]{"-l", "--longOption"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseWithHiddenOption() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("hidden", false, false, "Prints hidden option");
        options.hideOption("hidden");

        String[] arguments = new String[]{"--hidden"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A");
        options.addOption("b", true, "Prints option B");
        options.addOption("c", true, "Prints option C");
        options.addOption("d", true, "Prints option D");
        options.addOption("e", true, "Prints option E");
        options.addOption("f", true, "Prints option F");

        String[] arguments = new String[]{"-a", "-b", "--c", "-d", "--e", "--f"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Arrays.asList("--c", "-d", "--e", "--f"), cmd.getArgList());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithValueOptions() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A with value");

        String[] arguments = new String[]{"-a", "5"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("5", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValueOptionsGrouped() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption("a", true, "Prints option A with value");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "-a", "5"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("5", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValueOptionsMultiple() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A with value");

        String[] arguments = new String[]{"-a", "5", "-a", "10"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> values = (List<String>) cmd.getOptionValues("a");
        assertEquals(Arrays.asList("5", "10"), values);
    }

    @Test
    public void testParseWithValueOptionsMultipleGrouped() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption("a", true, "Prints option A with value");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "-a", "5", "--group", "-a", "10"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> values = (List<String>) cmd.getOptionValues("a");
        assertEquals(Arrays.asList("5", "10"), values);
    }

    @Test
    public void testParseWithValueOptionsNoValue() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A with value");

        String[] arguments = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValueOptionsNoValueGrouped() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption("a", true, "Prints option A with value");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValueOptionsMissing() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        options.addOption("a", true, "Prints option A with value");

        String[] arguments = new String[]{"-a", "5", "-b"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithValueOptionsMissingGrouped() throws ParseException {
        ParseMock parser = new ParseMock();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption("a", true, "Prints option A with value");
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "-a", "5", "--group", "-b"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    private static class ParseMock extends Parser {

        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return Arrays.copyOfRange(arguments, 0, arguments.length);
        }

        @Override
        public void processArgs(Option opt, ListIterator iter) throws ParseException {
            // not implemented
        }

        @Override
        public void processOption(String arg, ListIterator iter) throws ParseException {
            if (getOptions().hasOption(arg)) {
                Option opt = getOptions().getOption(arg);
                opt.setValue(iter.next());
                iter.next();
            } else {
                throw new UnrecognizedOptionException("Unrecognized option: " + arg);
            }
        }

        @Override
        public CommandLine parse(Options opts, String[] arguments, Properties props, boolean stopAtNonOption)
                throws ParseException {
            return super.parse(opts, arguments, props, true);
        }

        @Override
        public CommandLine parse(Options opts, String[] arguments, boolean stopAtNonOption)
                throws ParseException {
            return super.parse(opts, arguments, new Properties(), true);
        }

        @Override
        public CommandLine parse(Options opts, String[] arguments) throws ParseException {
            return super.parse(opts, arguments, new Properties(), false);
        }
    }
}