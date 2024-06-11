package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParsingCompletionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

/**
 * Test Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Option fooOption = new Option("f", "foo", true, "Print Foo");
        Option barOption = new Option("b", "bar", true, "Print Bar");
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption(fooOption);
        group.addOption(barOption);
        Options options = new Options();
        options.addOptionGroup(group);

        String[] args = {"-f", "--foo", "-b", "--bar"};
        CommandLine cmd = new DefaultParser().parse(options, args);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertTrue(cmd.hasOption("group"));
        assertTrue(group.isSelected());
        assertEquals("Print Foo", cmd.getOptionValue("foo"));
        assertEquals("Print Bar", cmd.getOptionValue("bar"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        Option option = new Option("r", "required", true, "A required option");
        Options options = new Options();
        options.addOption(option);

        String[] args = {"--otherOption"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Option option = new Option("a", "arg", true, "An argument option");
        Options options = new Options();
        options.addOption(option);

        String[] args = {"-a"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();

        String[] args = {"--unrecognizedOption"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOption() throws Exception {
        Option option = new Option("o", "option", true, "An option");
        Options options = new Options();
        options.addOption(option);

        String[] args = {"--noSuchOption"};
        new DefaultParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws Exception {
        Option option = new Option("a", "arg", true, "An argument option");
        Options options = new Options();
        options.addOption(option);

        String[] args = {"-a", "--arg", "arg1", "arg2", "--otherOption"};
        try {
            new DefaultParser().parse(options, args);
            fail();
        } catch (ParseException e) {
            assertEquals(ParsingCompletionException.class, e.getClass());
        }
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Option option = new Option("p", "prop", true, "A property option");
        Options options = new Options();

        Properties properties = new Properties();
        properties.put("prop", "value");

        String[] args = {"--prop=value"};
        CommandLine cmd = new DefaultParser().parse(options, args, properties);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("prop"));
        assertEquals("value", cmd.getOptionValue("prop"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Option option = new Option("a", "arg", true, "An argument option");
        Options options = new Options();
        options.addOption(option);

        String[] args = {"arg1", "--option", "arg2"};
        CommandLine cmd = new DefaultParser().parse(options, args, null, true);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.getArgs().get(0).equals("arg1"));
        assertTrue(cmd.getArgs().get(1).equals("--option"));
        assertFalse(cmd.getArgs().get(1).startsWith("-"));
        assertTrue(cmd.getArgs().get(1).startsWith("--"));
        assertTrue(cmd.hasOption("option"));
        assertNull(cmd.getOptionValue("option"));
        assertEquals(1, cmd.getNumOptions());
        assertEquals(Arrays.asList(cmd.getOptions().get(0)), cmd.getAllOptions());
    }
}