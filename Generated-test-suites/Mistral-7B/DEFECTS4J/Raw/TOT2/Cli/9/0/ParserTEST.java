import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the Parser class.
 */
public class ParserTest
{
    /**
     * Test case for the parse method with no arguments and no options.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseNoArgsNoOptions() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[0]);

        // assert that the CommandLine is empty
        assertTrue(cmd.getArgs().isEmpty());
        assertFalse(cmd.getOptions().hasOptions());
    }

    /**
     * Test case for the parse method with no arguments and one required option.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseNoArgsOneRequiredOption() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add a required option
        Option requiredOption = new Option("r", "required", true, "Required option");
        options.addOption(requiredOption);

        // call the parse method
        parser.parse(options, new String[0]);

        // the test should throw a MissingOptionException
    }

    /**
     * Test case for the parse method with one argument and no options.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseOneArgNoOptions() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[]{"arg1"});

        // assert that the CommandLine has one argument
        assertSame("arg1", cmd.getArgs().get(0));
        assertFalse(cmd.getOptions().hasOptions());
    }

    /**
     * Test case for the parse method with one argument and one option.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseOneArgOneOption() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add an option
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "--option", "value"});

        // assert that the CommandLine has one argument and one option
        assertSame("arg1", cmd.getArgs().get(0));
        assertSame("value", cmd.getOptions().getOptionValues("option")[0]);
    }

    /**
     * Test case for the parse method with multiple arguments and no options.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseMultipleArgsNoOptions() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "arg3"});

        // assert that the CommandLine has three arguments
        List<String> args = cmd.getArgs();
        assertEquals(3, args.size());
        assertSame("arg1", args.get(0));
        assertSame("arg2", args.get(1));
        assertSame("arg3", args.get(2));
        assertFalse(cmd.getOptions().hasOptions());
    }

    /**
     * Test case for the parse method with multiple arguments and one option.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseMultipleArgsOneOption() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add an option
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "--option", "value"});

        // assert that the CommandLine has three arguments and one option
        List<String> args = cmd.getArgs();
        assertEquals(3, args.size());
        assertSame("arg1", args.get(0));
        assertSame("arg2", args.get(1));
        assertSame("value", cmd.getOptions().getOptionValues("option")[0]);
    }

    /**
     * Test case for the parse method with multiple arguments and multiple options.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseMultipleArgsMultipleOptions() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add two options
        Option opt1 = new Option("o1", "option1", true, "Option1 value");
        Option opt2 = new Option("o2", "option2", true, "Option2 value");
        options.addOption(opt1);
        options.addOption(opt2);

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "--o1", "value1", "--o2", "value2"});

        // assert that the CommandLine has three arguments and two options
        List<String> args = cmd.getArgs();
        assertEquals(3, args.size());
        assertSame("arg1", args.get(0));
        assertSame("arg2", args.get(1));
        List<String> optValues = cmd.getOptions().getOptionValues("o1");
        assertEquals(1, optValues.size());
        assertSame("value1", optValues.get(0));
        optValues = cmd.getOptions().getOptionValues("o2");
        assertEquals(1, optValues.size());
        assertSame("value2", optValues.get(0));
    }

    /**
     * Test case for the parse method with an invalid argument.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArg() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // call the parse method with an invalid argument
        parser.parse(options, new String[]{"arg1", "--invalid"});

        // the test should throw a ParseException
    }

    /**
     * Test case for the parse method with an invalid option.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // call the parse method with an invalid option
        parser.parse(options, new String[]{"--invalid"});

        // the test should throw an UnrecognizedOptionException
    }

    /**
     * Test case for the parse method with a missing required option.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add a required option
        Option option = new Option("r", "required", true, "Required option");
        options.addOption(option);

        // call the parse method without the required option
        parser.parse(options, new String[0]);

        // the test should throw a MissingOptionException
    }

    /**
     * Test case for the parse method with an option group.
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseOptionGroup() throws ParseException
    {
        // set up the Parser
        Parser parser = new DefaultParser();

        // initialize the Options
        Options options = new Options();

        // add an option group with two options
        OptionGroup group = new OptionGroup("group", "Group options");
        Option opt1 = new Option("g1", "Option1", true, "Option1");
        Option opt2 = new Option("g2", "Option2", true, "Option2");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);

        // call the parse method
        CommandLine cmd = parser.parse(options, new String[0]);

        // assert that the CommandLine has no arguments and no standalone options
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().getOptions().isEmpty());

        // assert that the CommandLine has the two options from the group
        List<Option> groupOptions = cmd.getOptions().getOptions(group);
        assertEquals(2, groupOptions.size());
        Option firstOption = groupOptions.get(0);
        assertTrue(firstOption.getValueSpecifier().equals(opt1.getValueSpecifier()));
        Option secondOption = groupOptions.get(1);
        assertTrue(secondOption.getValueSpecifier().equals(opt2.getValueSpecifier()));
    }
}