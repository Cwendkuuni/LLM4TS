package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();

        Options options = new Options();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);

        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().length);
        assertEquals(0, cmd.getOptions().size());
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        String[] arguments = {"arg1", "--invalid-option", "arg3"};
        parser.parse(options, arguments, false);

        fail("A ParseException should have been thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        Options options = new Options();
        String[] arguments = {"--", "arg1", "-o", "opt", "arg2"};
        parser.parse(options, arguments, true);

        fail("A ParseException should have been thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("o", "option", true, "Option description.");

        String[] arguments = {"--", "-o", "arg"};
        parser.parse(options, arguments);

        fail("A MissingOptionException should have been thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("r", "required-option", true, "Required option.");
        options.addRequiredOption("r");

        String[] arguments = {"--", "arg1", "arg2"};
        parser.parse(options, arguments);

        fail("A MissingOptionException should have been thrown.");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", "option", true, "Option description.");
        options.addOption("g", "group", new OptionGroup("group", "Group description.", false));
        options.addOption("g", "group-option", true, "Option in group.", 1);
        options.addRequiredOption("a");

        String[] arguments = {"--", "-a", "--group", "group-option"};
        parser.parse(options, arguments);

        fail("A MissingArgumentException should have been thrown.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("o", "option", true, "Option description.");

        String[] arguments = {"--", "-u", "arg"};
        parser.parse(options, arguments);

        fail("An UnrecognizedOptionException should have been thrown.");
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseWithNoSuchOption() throws ParseException {
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("o", "option", true, "Option description.");

        String[] arguments = {"--", "-n", "arg"};
        parser.parse(options, arguments);

        fail("A NoSuchOptionException should have been thrown.");
    }
}