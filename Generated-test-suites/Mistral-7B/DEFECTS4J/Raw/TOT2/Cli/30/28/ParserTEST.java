import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseWithBasicOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-i", "input.txt", "-o", "output.txt"};
        CommandLine commandLine = parser.parse(arguments);

        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertEquals("output.txt", commandLine.getOptionValue("output"));
        assertNull(commandLine.getOptionValue("unknown"));

        assertEquals(2, commandLine.getArgs().length);
        assertEquals("args", commandLine.getArgs()[0]);
        assertEquals("args2", commandLine.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", true, "input file");
        options.setRequiredOption("i", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {};
        parser.parse(arguments);

        assertFalse(options.isSetArgArray());
        assertFalse(parser.getCommandLine().hasOption("i"));
        assertFalse(parser.getCommandLine().getArgs().length > 0);
        fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", true, "input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--invalid"};
        parser.parse(arguments);

        fail("Expected ParseException");
    }

    @Test
    public void testParseWithMultipleValidOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", true, "input file");
        options.addOption("o", true, "output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-i", "input.txt", "-o", "output.txt", "--ignore"};
        CommandLine commandLine = parser.parse(arguments);

        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertEquals("output.txt", commandLine.getOptionValue("output"));
        assertNull(commandLine.getOptionValue("ignore"));

        assertEquals(1, commandLine.getArgs().length);
        assertEquals("--ignore", commandLine.getArgs()[0]);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", true, "input file");

        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine commandLine = parser.parse(null, properties);

        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertNull(commandLine.getOptionValue("unknown"));

        assertEquals(0, commandLine.getArgs().length);
    }
}