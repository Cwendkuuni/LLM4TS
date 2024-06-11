package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = new String[] {"-f", "file.txt"};
        Parser parser = new DefaultParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("file.txt", flattenedArgs[1]);
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] arguments = new String[] {"file.txt"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("file.txt", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithOption() throws ParseException {
        options.addOption("f", "file", true, "File to process");
        String[] arguments = new String[] {"-f", "file.txt"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        options.addOption("f", "file", true, "File to process");
        options.addOption("v", "verbose", false, "Enable verbose mode");
        String[] arguments = new String[] {"-f", "file.txt", "-v"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("v"));
    }

    @Test
    public void testParseWithOptionAndArgs() throws ParseException {
        options.addOption("f", "file", true, "File to process");
        String[] arguments = new String[] {"-f", "file.txt", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] arguments = new String[] {"-o", "file.txt"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        options.addOption("f", "file", true, "File to process");
        String[] arguments = new String[] {"-f"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addRequiredOption("f", "file", true, "File to process");
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        options.addOption("f", "file", true, "File to process");
        String[] arguments = new String[] {"-f", "file.txt", "--", "extraArg"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }
}