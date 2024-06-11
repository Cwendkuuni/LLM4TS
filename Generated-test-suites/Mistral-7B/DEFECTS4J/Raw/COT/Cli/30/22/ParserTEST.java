package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        assertNull(parser.getOptions());

        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("a.value"));
    }

    @Test
    public void testParseWithOptionAndArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithMultipleOptionsAndArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");
        options.addOption("b", true, "value", "Option B with value");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "-b", "arg2"});
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"--missing"}));
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "arg2"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals(1, cmd.getArgs().size() + cmd.getOptions().size());
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"--unrecognized"}));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[]{"-a"}));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "value", "Option A with value");

        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("a", "arg1");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, null, "Required Option A");
        options.addOption("b", true, null, "Required Option B");

        parser.setOptions(options);
        List<String> arguments = Arrays.asList("--a", "--b");
        CommandLine cmd = parser.parse(options, arguments.toArray(new String[0]));
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, null, "Required Option A");
        options.addOption("b", true, null, "Required Option B");

        parser.setOptions(options);
        List<String> arguments = Arrays.asList("--a");
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments.toArray(new String[0])));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(true);
        group.addOption("a", true, null, "Option A");
        group.addOption("b", true, null, "Option B");
        options.addOptionGroup(group);

        parser.setOptions(options);
        List<String> arguments = Arrays.asList("--group");
        CommandLine cmd = parser.parse(options, arguments.toArray(new String[0]));
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("group"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithMultipleOptionGroups() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Option Group 1");
        group1.addOption("a1", true, null, "Option A1");
        OptionGroup group2 = new OptionGroup("group2", "Option Group 2");
        group2.addOption("a2", true, null, "Option A2");
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        parser.setOptions(options);
        List<String> arguments = Arrays.asList("--group1", "--group2");
        CommandLine cmd = parser.parse(options, arguments.toArray(new String[0]));
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("group1"));
        assertTrue(cmd.hasOption("group2"));
        assertFalse(cmd.hasOption("a1"));
        assertFalse(cmd.hasOption("a2"));
    }
}