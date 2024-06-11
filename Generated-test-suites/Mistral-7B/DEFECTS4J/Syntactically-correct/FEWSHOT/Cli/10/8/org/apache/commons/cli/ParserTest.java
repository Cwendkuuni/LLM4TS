package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParseEmpty() throws ParseException {
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, null, false);
        assertNull(cmd.getArgs());
        assertTrue(cmd.options.isEmpty());
    }

    @Test
    public void testParseSingleArg() throws ParseException {
        String[] args = {"arg1"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
        assertTrue(cmd.options.isEmpty());
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        options.addOption("o", "opt", false, "option");
        String[] args = {"-o"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.options.size() == 1);
        Option opt = cmd.options.get(0);
        assertTrue(opt.isSet());
    }

    @Test
    public void testParseSingleOptionWithArgument() throws ParseException {
        options.addOption("a", "arg", true, "argument");
        String[] args = {"-a", "argValue"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.options.size() == 1);
        Option opt = cmd.options.get(0);
        assertTrue(opt.isSet());
        assertArrayEquals(new String[]{"argValue"}, opt.getValues());
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        options.addOption("o1", "opt1", false, "option1");
        options.addOption("o2", "opt2", false, "option2");
        String[] args = {"-o1", "-o2"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.options.size() == 2);
        Option opt1 = cmd.options.get(0);
        Option opt2 = cmd.options.get(1);
        assertTrue(opt1.isSet());
        assertTrue(opt2.isSet());
    }

    @Test
    public void testParseMultipleOptionsWithArguments() throws ParseException {
        options.addOption("a1", "arg1", true, "argument1");
        options.addOption("a2", "arg2", true, "argument2");
        String[] args = {"-a1", "argValue1", "-a2", "argValue2"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.options.size() == 2);
        Option opt1 = cmd.options.get(0);
        Option opt2 = cmd.options.get(1);
        assertTrue(opt1.isSet());
        assertArrayEquals(new String[]{"argValue1"}, opt1.getValues());
        assertTrue(opt2.isSet());
        assertArrayEquals(new String[]{"argValue2"}, opt2.getValues());
    }

    @Test
    public void testParseMixedOptionsAndArgs() throws ParseException {
        options.addOption("o", "opt", false, "option");
        options.addOption("a", "arg", true, "argument");
        String[] args = {"-o", "value", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertTrue(cmd.options.size() == 2);
        Option opt = cmd.options.get(0);
        assertTrue(opt.isSet());
        assertTrue(opt.getValue()
            .equals("value"));
        Option argOpt = cmd.findOption("a");
        assertTrue(argOpt.isSet());
        assertArrayEquals(new String[]{"arg1", "arg2"}, argOpt.getValues());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("p", "prop", true, "property");
        Properties properties = new Properties();
        properties.setProperty("prop", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        assertTrue(cmd.options.isEmpty());
        assertTrue(cmd.options.get(0).isSet());
        assertEquals("value", cmd.options.get(0).getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("a", "arg", true, "argument");
        String[] args = {"opt1", "-a", "argValue"};
        CommandLine cmd = parser.parse(options, args, null, true);
        assertTrue(cmd.options.size() == 1);
        Option opt = cmd.options.get(0);
        assertTrue(opt.isSet());
        assertArrayEquals(new String[]{"opt1"}, cmd.getArgs());
        assertArrayEquals(new String[]{"argValue"}, opt.getValues());
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Options group = new Options();
        group.addOption("g", "group", false, "group option");
        group.addOption("g1", "group1", false, "group option 1");
        group.addOption("g2", "group2", false, "group option 2");
        options.addOptionGroup(group);
        String[] args = {"-g", "-g1", "-g2"};
        CommandLine cmd = parser.parse(options, args, null, false);
        Option group = cmd.findOption("g");
        assertTrue(group.isSet());
        Option group1 = cmd.findOption("g1");
        assertTrue(group1.isSet());
        Option group2 = cmd.findOption("g2");
        assertTrue(group2.isSet());
    }

    @Test
    public void testParseWithMissingOption() throws ParseException {
        options.addOption("o", "opt", false, "option");
        String[] args = {"-x"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption("o", "opt", true, "option");
        options.addRequiredOption("r", "req", true, "required option");
        String[] args = {"-o"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        options.addOption("a", "arg", true, "argument");
        String[] args = {"-a"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParseWithInvalidArgument() throws ParseException {
        options.addOption("a", "arg", true, "argument");
        String[] args = {"-a", "invalid"};
        CommandLine cmd = parser.parse(options, args, null, false);
        Option opt = cmd.findOption("a");
        assertTrue(opt.isSet());
        assertArrayEquals(new String[]{"invalid"}, opt.getValues());
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        options.addOption("o", "opt", false, "option");
        String[] args = {"--invalid"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParseWithGroupAndMissingOption() throws ParseException {
        Options group = new Options();
        group.addOption("g", "group", false, "group option");
        group.addOption("g1", "group1", false, "group option 1");
        group.addOption("g2", "group2", false, "group option 2");
        options.addOptionGroup(group);
        String[] args = {"-g", "-g1"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }
}