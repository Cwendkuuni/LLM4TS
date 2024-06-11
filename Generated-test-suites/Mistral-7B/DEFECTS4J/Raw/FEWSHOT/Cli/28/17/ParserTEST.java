package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-a", "--option", "-b", "--option=value", "--no-option", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "A value");
        options.addOption("b", "optionB", false, "B value");
        options.addOption("c", "optionC", true, "C value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true, true);

        assertFalse(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("z"));
        assertTrue(cmd.getOptionValues("a").length > 0);
        assertTrue(cmd.getOptionValues("b") == null);
        assertNull(cmd.getOptionValues("z"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--foo=1", "--bar=2"};
        Properties properties = new Properties();
        properties.setProperty("foo", "1");
        properties.setProperty("bar", "2");

        Options options = new Options();
        options.addRequiredOption("f", "foo", true, "Foo Option");
        options.addOption("b", "bar", false, "Bar Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, properties, true);

        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.getOptionValues("foo").length > 0);
        assertEquals("1", cmd.getOptionValues("foo")[0]);
        assertFalse(cmd.hasOption("bar"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"-a", "-b=value"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "A value");
        options.addOption("b", "optionB", true, "B value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"-z", "-a", "--option"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addRequiredOption("r", "requiredOption", true, "Required option");
        options.addOption("a", "optionA", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionWithProperties() throws ParseException {
        String[] args = new String[]{"--foo=1"};
        Properties properties = new Properties();
        properties.setProperty("foo", "1");
        Options options = new Options();
        options.addRequiredOption("r", "requiredOption", true, "Required option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, properties, true);
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        String[] args = new String[]{"--group", "-g", "-a", "-b", "--no-group"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("groupName", "Group Description");
        group.setRequired(false);
        group.addOption(new Option("a", "optionA", true, "Option A"));
        group.addOption(new Option("b", "optionB", true, "Option B"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getOptionValues("groupName") == null);
        assertEquals(2, cmd.getOptionValues("groupName").length);
        assertFalse(cmd.hasOption("noGroup"));
    }

    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        String[] args = new String[0];
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArgs() throws ParseException {
        String[] args = null;
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, true);
    }

    @Test
    public void testParseWithEmptyProperties() throws ParseException {
        Properties properties = new Properties();
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(new String[0], properties, true);
    }
}