package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    @Test
    public void testParseWithOptions() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "--b"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", true, "longOptionA");
        options.addOption("longOptionB", false, "longOptionB");
        Parser parser = new DefaultParser();
        String[] arguments = {"--longOptionA", "--longOptionB"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
    }

    @Test
    public void testParseWithMixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "--b", "arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(1));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "a").required(true);
        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithExtraArguments() {
        Options options = new Options();
        options.addOption("a", true, "a");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithLongOptionArg() {
        Options options = new Options();
        options.addOption("a", true, "arg");
        Parser parser = new DefaultParser();
        String[] arguments = {"--a=arg"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg", args.get(0));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "a");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "arg");
        CommandLine cmd = parser.parse(options, (String[]) null, properties);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "a", "arg").setLongOpt("--arg"));
        group.addOption(new Option("b", true, "b", "arg").setLongOpt("--arg2"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] arguments = {"--group", "--arg=arg1", "--arg2=arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMultiValuedOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "arg1", "--a", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        List<String> values = cmd.getOptionValues("a");
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));
    }
}