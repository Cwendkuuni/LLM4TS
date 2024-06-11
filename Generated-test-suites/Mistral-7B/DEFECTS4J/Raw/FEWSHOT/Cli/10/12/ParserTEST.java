package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertNull(cmd.getOption(null));
    }

    @Test
    public void testParseWithOneRequiredOption() throws ParseException {
        Option option = new Option(null, "o", false, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"o"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Option option = new Option(null, "o", false, "Option o");
        options.addOption(option);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
        fail("Missing required option: o");
    }

    @Test
    public void testParseWithOneOptionalOption() throws ParseException {
        Option option = new Option(null, "o", true, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertSame("value", cmd.getOptionValue("o"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionalArgument() throws ParseException {
        Option option = new Option(null, "o", true, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"o"};
        parser.parse(options, arguments);
        fail("Missing argument for option: o");
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Option option1 = new Option(null, "o1", false, "Option o1");
        Option option2 = new Option(null, "o2", false, "Option o2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"o1", "o2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o1"));
        assertTrue(cmd.hasOption("o2"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Option option = new Option(null, "long-option", false, "Long option");
        options.addOption(option);
        String[] arguments = new String[]{"--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("long-option"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Option option = new Option(null, "o", false, "Option o");
        option.setLongOpt("long-option");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("o", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.hasOption("o"));
        assertSame("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithDashOption() throws ParseException {
        Option option = new Option(null, "o", false, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"-o"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"x"};
        parser.parse(options, arguments);
        fail("Unrecognized option: x");
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Option option = new Option(null, "o", true, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"--", "-o", "value"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("o"));
        assertSame("value", cmd.getOptionValue("o"));
        assertArrayEquals(new String[]{"--"}, cmd.getArgs());
    }

    @Test
    public void testParseWithExtraArgs() throws ParseException {
        Option option = new Option(null, "o", true, "Option o");
        options.addOption(option);
        String[] arguments = new String[]{"--", "-o", "value1", "value2"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertTrue(cmd.hasOption("o"));
        assertSame("value1", cmd.getOptionValue("o"));
        assertArrayEquals(new String[]{"value2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        OptionGroup group = new OptionGroup("group", "Group");
        Option option1 = new Option(null, "o1", false, "Option o1", group);
        Option option2 = new Option(null, "o2", false, "Option o2", group);
        options.addOption(group);
        String[] arguments = new String[]{"--group", "--o1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o1"));
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException {
        Option option1 = new Option(null, "s", false, "Short option 1");
        Option option2 = new Option(null, "l", false, "Long option 2");
        Option option3 = new Option(null, "g", false, "Group option", new OptionGroup("group"));
        Option option4 = new Option(null, "l", true, "Long option 4");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        String[] arguments = new String[]{"-s", "--l", "--group", "--l=value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("s"));
        assertTrue(cmd.hasOption("l"));
        assertTrue(group.isSelected());
        assertSame("value", cmd.getOptionValue("l"));
    }
}