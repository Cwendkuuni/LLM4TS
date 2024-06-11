package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    

    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] arguments = new String[0];
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] arguments = {"arg1", "--nonexistent"};
        Options options = new Options();
        parser.parse(options, arguments, null, false);
        fail("Expected ParseException, but no exception was thrown.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] arguments = {"--unknownOption"};
        Options options = new Options();
        parser.parse(options, arguments, null, false);
        fail("Expected UnrecognizedOptionException, but no exception was thrown.");
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] arguments = {"--requiredOption"};
        Options options = new Options();
        options.addRequiredOption("requiredOption", true, null, "requiredValue");
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals("requiredValue", cmd.getOptionValue("requiredOption"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] arguments = {"--optionWithArg"};
        Options options = new Options();
        options.addOption("optionWithArg", true, "a", "argument");
        parser.parse(options, arguments, null, false);
        fail("Expected MissingArgumentException, but no exception was thrown.");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"--property=value"};
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        assertEquals(1, cmd.getOptions().size());
        Option option = cmd.getOptions().get(0);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"--option", "arg1", "--", "arg2"};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments, null, true);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    
}