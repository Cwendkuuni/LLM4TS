import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = {"arg1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        options.addOption("a", "optionA", false, "Description of optionA");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("optionA"));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        options.addOption("a", "optionA", true, "Description of optionA");
        String[] arguments = {"-a", "valueA"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("optionA");
        assertNotNull(opt);
        assertArrayEquals(new String[]{"valueA"}, opt.getValues());
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException {
        options.addOption("a", "optionA", true, "Description of optionA");
        String[] arguments = {"-a", "valueA1", "-a", "valueA2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("optionA");
        assertNotNull(opt);
        String[] values = opt.getValues();
        assertArrayEquals(new String[]{"valueA1", "valueA2"}, values);
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        options.addOption("a", "optionA", false, "Description of optionA");
        options.addRequiredOption("b", "optionB", false, "Description of optionB");
        String[] arguments = {"-a"};
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option 'optionB'", e.getMessage());
        }

        arguments = new String[]{"-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.hasOption("optionB"));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        options.addOption("a", "optionA", false, "Description of optionA");
        options.addOption("b", "optionB", true, "Description of optionB");
        String[] arguments = {"-a", "-b", "arg1", "-a", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("optionA"));
        Option optB = cmd.getOption("optionB");
        assertNotNull(optB);
        assertArrayEquals(new String[]{"arg1", "arg2"}, optB.getValues());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        options.addOption("a", "optionA", false, "Description of optionA");
        String[] arguments = {"--invalidOption"};
        parser.parse(options, arguments);
        fail("UnrecognizedOptionException expected");
    }

    @Test
    public void testParseWithUnrecognizedOptionAndArguments() throws ParseException {
        options.addOption("a", "optionA", false, "Description of optionA");
        String[] arguments = {"--invalidOption", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertFalse(cmd.hasOption("invalidOption"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        options.addOption("longOptionA", "longOptionA", false, "Description of long optionA");
        String[] arguments = {"--longOptionA"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("longOptionA"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptionValue() throws ParseException {
        options.addOption("a", "optionA", true, "Description of optionA");
        String[] arguments = {"-a", "arg1", "-a"};
        parser.parse(options, arguments);
        fail("MissingArgumentException expected");
    }

    @Test
    public void testParseWithMultipleArgs() throws ParseException {
        String[] arguments = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(3, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals("arg3", cmd.getArgs().get(2));
    }
}