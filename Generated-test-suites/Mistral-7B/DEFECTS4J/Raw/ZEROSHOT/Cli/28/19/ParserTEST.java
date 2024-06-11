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
    public void testParse() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        // create test arguments
        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt", "--"};

        // create Parser instance and parse command line
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        // verify parsed command line
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
        assertFalse(cmd.hasOption("unknown"));
        assertTrue(cmd.hasOption("input"));
        assertTrue(cmd.hasOption("output"));
        assertEquals(2, cmd.getArgs().size());

        // test parse with properties
        Properties properties = new Properties();
        properties.put("input", "input.properties");
        properties.put("output", "output.properties");
        CommandLine parsedCmd = parser.parse(options, arguments, properties, false);
        assertEquals(4, parsedCmd.getOptions().size());
        assertEquals("input.properties", cmd.getOptionValue("input"));
        assertEquals("output.properties", cmd.getOptionValue("output"));

        // test parse with stopAtNonOption
        Options optionsStopAtNonOption = new Options();
        optionsStopAtNonOption.addRequiredOption("i", "input", true, "input file");
        optionsStopAtNonOption.addOption("o", "output", true, "output file");
        optionsStopAtNonOption.addOption("x", "extra", false, "extra option");

        String[] argumentsStopAtNonOption = new String[]{"-i", "input.txt", "-o", "output.txt", "-x", "extra", "--"};
        CommandLine cmdStopAtNonOption = parser.parse(optionsStopAtNonOption, argumentsStopAtNonOption, null, true);
        assertEquals(5, cmdStopAtNonOption.getArgs().size());
        assertEquals("input.txt", cmdStopAtNonOption.getOptionValue("input"));
        assertEquals("output.txt", cmdStopAtNonOption.getOptionValue("output"));
        assertNull(cmdStopAtNonOption.getOptionValue("unknown"));
        assertFalse(cmdStopAtNonOption.hasOption("unknown"));
        assertTrue(cmdStopAtNonOption.hasOption("input"));
        assertTrue(cmdStopAtNonOption.hasOption("output"));
        assertSame(cmdStopAtNonOption.getOption("x"), optionsStopAtNonOption.getOption("x"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");

        // create test arguments
        String[] arguments = new String[]{"--"};

        // create Parser instance and parse command line
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        // verify exception is thrown
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        // create test options
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .setRequired(true);
        options.addOption(option);

        // create test arguments
        String[] arguments = new String[]{"-o", "output.txt"};

        // create Parser instance and parse command line
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        // verify exception is thrown
        parser.checkRequiredOptions();
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("i", "input", true, "input file");

        // create test arguments
        String[] arguments = new String[]{"-u", "unknown"};

        // create Parser instance and parse command line
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        // verify exception is thrown
        parser.checkRequiredOptions();
    }
}