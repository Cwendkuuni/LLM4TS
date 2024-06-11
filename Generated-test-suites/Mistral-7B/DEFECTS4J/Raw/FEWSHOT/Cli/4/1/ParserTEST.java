package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.UnrecognizedOptionException;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        final Parser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(new Options(), new String[0]);

        assertNull(cmd);
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Options options = new Options().addOption(option);
        final String[] arguments = new String[0];

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testParseSingleOptionalArgument() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = OptionBuilder.withLongOpt("l")
                                           .withArgName("arg")
                                           .withDescription("an option with an argument")
                                           .isOptional()
                                           .create("option");
        final Options options = new Options().addOption(option);
        final String[] arguments = new String[] {"--l", "arg"};

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertTrue(cmd.hasArgs());
        assertEquals("arg", cmd.getArgs()[0]);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option1 = new Option("o1", "option1", false, "Option 1 description");
        final Option option2 = new Option("o2", "option2", false, "Option 2 description");
        final Options options = new Options().addOption(option1).addOption(option2);
        final String[] arguments = new String[0];

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.getOptions().containsAll(Arrays.asList(option1, option2)));
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option argOption = new Option("a", "arg", true, "An argument");
        final Options options = new Options().addOption(argOption);
        final String[] arguments = new String[] {"arg"};

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.getOptions().isEmpty());
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("arg", cmd.getArgs()[0]);
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option argOption = new Option("a", "arg", true, "An argument");
        final Options options = new Options().addOption(argOption);
        final String[] arguments = new String[] {"arg1", "arg2"};

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.getOptions().isEmpty());
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Options options = new Options().addOption(option);
        final Properties properties = new Properties();
        properties.put("o", "value");

        final CommandLine cmd = parser.parse(options, new String[0], properties);

        assertNotNull(cmd);
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.getOptions().contains(option));
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Options options = new Options().addOption(option);

        final String[] arguments = new String[] {"arg1", "--o", "arg2"};

        final CommandLine cmd = parser.parse(options, arguments, true);

        assertNotNull(cmd);
        assertFalse(cmd.hasArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.getOptions().contains(option));
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        final Parser parser = new DefaultParser();

        final OptionGroup group = new OptionGroup("group");
        final Option option1 = new Option("o1", "option1", false, "Option 1 description");
        final Option option2 = new Option("o2", "option2", false, "Option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        final Options options = new Options().addOptionGroup(group);
        final String[] arguments = new String[0];

        final CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.getOptions().isEmpty());
        assertFalse(group.isSelected());

        final String[] expectedArgs = new String[2];
        expectedArgs[0] = "option1";
        expectedArgs[1] = "option2";

        group.setSelected(cmd.getOption(group.getKey()));

        assertSame(cmd, parser.parse(options, arguments));
        assertEquals(2, cmd.getArgs().length);
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithRequiredGroup() throws ParseException {
        final Parser parser = new DefaultParser();

        final OptionGroup group = new OptionGroup("group");
        final Option option1 = new Option("o1", "option1", false, "Option 1 description");
        final Option option2 = new Option("o2", "option2", false, "Option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        group.setRequired(true);
        final Options options = new Options().addOptionGroup(group);
        final String[] arguments = new String[0];

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        final String[] expectedArgs = new String[2];
        expectedArgs[0] = "option1";
        expectedArgs[1] = "option2";

        group.setSelected(cmd -> true);
        final CommandLine cmd = parser.parse(options, arguments);

        assertSame(cmd, parser.parse(options, arguments));
        assertEquals(2, cmd.getArgs().length);
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option1 = new Option("o1", "option1", false, "Option 1 description");
        option1.setRequired(true);
        final Option option2 = new Option("o2", "option2", false, "Option 2 description");
        final Option option3 = new Option("o3", "option3", false, "Option 3 description");
        final Options options = new Options().addOption(option1).addOption(option2).addOption(option3);
        final String[] arguments = new String[0];

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        final String[] expectedArgs = new String[3];
        expectedArgs[0] = "option1";
        expectedArgs[1] = "option2";
        expectedArgs[2] = "option3";

        final CommandLine cmd = parser.parse(options, arguments, true);

        assertSame(cmd, parser.parse(options, arguments));
        assertEquals(3, cmd.getArgs().length);
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Option argOption = new Option("a", "arg", true, "An argument");
        option.setRequiredArg(argOption);
        final Options options = new Options().addOption(option).addOption(argOption);
        final String[] arguments = new String[0];

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMissingSingleArgument() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Option argOption = new Option("a", "arg", true, "An argument");
        option.setArgName("arg");
        option.setRequiredArg(argOption);
        final Options options = new Options().addOption(option).addOption(argOption);
        final String[] arguments = new String[] {"--o"};

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMissingMultipleArguments() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Option argOption1 = new Option("a1", "arg1", true, "An argument 1");
        final Option argOption2 = new Option("a2", "arg2", true, "An argument 2");
        option.setArgName("args");
        option.setRequiredArg(true);
        option.setArgStrings(argOption1, argOption2);
        final Options options = new Options().addOption(option).addOption(argOption1).addOption(argOption2);
        final String[] arguments = new String[0];

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        final Parser parser = new DefaultParser();

        final Option option = new Option("o", "option", false, "Option description");
        final Options options = new Options().addOption(option);
        final String[] arguments = new String[] {"--x"};

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }
}