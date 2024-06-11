package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = new String[] { "-a" };
        CommandLine cmd = new DefaultParser().parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("help", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = new String[] { "-a", "arg1", "arg2" };
        CommandLine cmd = new DefaultParser().parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
        assertArrayEquals(new String[] { "arg2" }, cmd.getArgs());
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        String[] args = new String[] { "--unknown" };
        assertThrows(UnrecognizedOptionException.class, () -> new DefaultParser().parse(options, args));
    }

    @Test
    public void testParseWithRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "help").required();
        String[] args = new String[] {};
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, args));
        String[] argsWithOption = new String[] { "-a" };
        CommandLine cmd = new DefaultParser().parse(options, argsWithOption);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("true", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group description");
        group.setRequired(true);

        Option opt1 = new Option("a", true, "help", "a-help");
        group.addOption(opt1);
        Option opt2 = new Option("b", true, "help", "b-help");
        group.addOption(opt2);

        options.addOptionGroup(group);

        String[] args = new String[] { "--group" };
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, args));

        String[] argsWithGroup = new String[] { "--group", "-a", "arg1", "-b", "arg2" };
        CommandLine cmd = new DefaultParser().parse(options, argsWithGroup);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValues("b").length > 0);
        assertEquals("arg2", cmd.getOptionValues("b")[0]);
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = new String[0];

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = new DefaultParser().parse(options, args, properties);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("value", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = new String[] { "--", "-a", "arg1", "--unknown", "arg2" };
        CommandLine cmd = new DefaultParser().parse(options, args, null, true);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
        assertArrayEquals(new String[] { "--unknown", "arg2" }, cmd.getArgs());
    }

    @Test
    public void testParseWithEmptyArgs() {
        Options options = new Options();
        String[] args = new String[0];
        CommandLine cmd = new DefaultParser().parse(options, args);
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithEmptyProperties() {
        Options options = new Options();
        String[] args = new String[0];
        Properties properties = new Properties();
        CommandLine cmd = new DefaultParser().parse(options, args, properties);
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithNullArgs() {
        Options options = new Options();
        CommandLine cmd = assertThrows(NullPointerException.class, () -> new DefaultParser().parse(options, null));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithNullProperties() {
        Options options = new Options();
        String[] args = new String[0];
        Properties properties = null;
        CommandLine cmd = new DefaultParser().parse(options, args, properties);
        assertNull(cmd.getArgs());
    }
}

class DefaultParser extends Parser {
    @Override
    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
    }
}