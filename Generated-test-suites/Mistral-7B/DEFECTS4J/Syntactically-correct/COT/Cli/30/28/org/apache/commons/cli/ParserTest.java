package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--option3=value3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", "argA", true, "Option A");
        Option option3 = new Option("option3", "option3", true, "Option 3");
        options.addOption(optionA);
        options.addOption(option3);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertFalse(cmd.hasOption("unknown"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("option3"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("value3", cmd.getOptionValue("option3"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        String[] arguments = new String[]{"--optionA=valueA", "-b"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", "argA", true, "Option A");
        Option optionB = new Option("b", "argB", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        parser.setOptions(options);
        parser.parse(options, arguments);

        fail("ParseException expected but not thrown.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"--unknown"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        parser.parse(options, arguments);

        fail("UnrecognizedOptionException expected but not thrown.");
    }

    @Test
    public void testParseEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        assertFalse(cmd.hasOption("unknown"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--option1=value1", "--option2=value2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("option1", true, true, "Option 1");
        Option option2 = new Option("option2", true, true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, new Properties(), false);

        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertEquals(0, cmd.getArgs().size());
    }
}