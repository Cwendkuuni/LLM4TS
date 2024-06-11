package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);

        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();
        Parser parser = new DefaultParser();

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.getOptions().isEmpty());
        assertEquals(2, cmd.getArgs().size());
    }

    @Test
    public void testParseWithOptionalOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "arg1", "--b=val1", "--c"};
        Options options = new Options();
        Option aOption = new Option("a", "aOption", false, "A optional option");
        Option cOption = new Option("c", "cOption", false, "C optional option");

        options.addOption(aOption);
        options.addOption(cOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"--a=val1", "--b=val2"};
        Options options = new Options();
        Option aOption = new Option("a", "aOption", true, "A required option");
        Option bOption = new Option("b", "bOption", true, "B required option");

        options.addOption(aOption);
        options.addOption(bOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(0, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] arguments = new String[]{"--a", "arg1"};
        Options options = new Options();
        Option aOption = new Option("a", "aOption", true, "A option with an argument");

        options.addOption(aOption);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--x"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--foo=1", "--bar=2"};
        Properties properties = new Properties();
        properties.setProperty("foo", "1");
        properties.setProperty("bar", "2");

        Options options = new Options();
        Option fooOption = new Option("foo", "Foo", true, "Foo option");
        Option barOption = new Option("bar", "Bar", true, "Bar option");

        options.addOption(fooOption);
        options.addOption(barOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertEquals("1", cmd.getOptionValue("foo"));
        assertEquals("2", cmd.getOptionValue("bar"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] arguments = new String[]{"--group", "--myOption=value"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option group");
        Option myOption = new Option("myOption", "My option", true, "My option");

        group.addOption(myOption);
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(1, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(group.isSelected());
        assertTrue(cmd.hasOption("myOption"));
    }

}