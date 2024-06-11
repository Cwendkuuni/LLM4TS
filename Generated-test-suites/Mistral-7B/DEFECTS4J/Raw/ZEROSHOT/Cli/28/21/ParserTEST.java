import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    private Options options;
    private Parser parser;
    private CommandLine commandLine;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
        commandLine = new CommandLine();
    }

    @Test
    public void testParse_emptyArguments() throws ParseException
    {
        String[] arguments = {};
        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(0, result.getOptions().size());
    }

    @Test
    public void testParse_onlyOptionalOptions() throws ParseException
    {
        Option option = new Option("o", "option", true, "An optional option");
        options.addOption(option);
        String[] arguments = {"--option"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(1, result.getOptions().size());
        Option optionResult = result.getOption("o");
        assertSame(option, optionResult);
        assertTrue(optionResult.isSet());
    }

    @Test
    public void testParse_onlyRequiredOptions() throws ParseException
    {
        Option option = new Option("r", "requiredOption", true, "A required option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = {};

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParse_bothOptionalAndRequiredOptions() throws ParseException
    {
        Option option1 = new Option("o1", "optionalOption1", true, "An optional option 1");
        Option option2 = new Option("r", "requiredOption", true, "A required option");
        option2.setRequired(true);
        options.addOptions(Arrays.asList(option1, option2));
        String[] arguments = {"--requiredOption", "--optionalOption1"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(2, result.getOptions().size());
        Option option1Result = result.getOption("o1");
        assertSame(option1, option1Result);
        assertTrue(option1Result.isSet());
        Option option2Result = result.getOption("r");
        assertSame(option2, option2Result);
        assertTrue(option2Result.isSet());
    }

    @Test
    public void testParse_arguments() throws ParseException
    {
        Option option = new Option("a", "arg", true, "An argument");
        options.addOption(option);
        String[] arguments = {"--arg", "value"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(1, result.getArgs().size());
        assertEquals("value", result.getArgs().get(0));
        assertEquals(1, result.getOptions().size());
        Option optionResult = result.getOption("a");
        assertSame(option, optionResult);
        assertTrue(optionResult.isSet());
        assertEquals("value", optionResult.getValue());
    }

    @Test
    public void testParse_properties() throws ParseException
    {
        Option option = new Option("p", "property", true, "A property");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("p", "value");

        CommandLine result = parser.parse(options, null, properties, false);
        assertEquals(0, result.getArgs().size());
        assertEquals(1, result.getOptions().size());
        Option optionResult = result.getOption("p");
        assertSame(option, optionResult);
        assertTrue(optionResult.isSet());
        assertEquals("value", optionResult.getValue());
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException
    {
        Option option = new Option("o", "option", true, "An optional option");
        options.addOption(option);
        String[] arguments = {"--option", "value1", "--nonOption", "value2"};

        CommandLine result = parser.parse(options, arguments, true);
        assertEquals(1, result.getArgs().size());
        assertEquals("value1", result.getArgs().get(0));
        assertEquals(1, result.getOptions().size());
        Option optionResult = result.getOption("o");
        assertSame(option, optionResult);
        assertTrue(optionResult.isSet());
        assertEquals("value1", optionResult.getValue());
        assertEquals(1, result.getArgList().size());
        assertEquals("value2", result.getArgList().get(0));
    }

    @Test
    public void testParse_group() throws ParseException
    {
        OptionGroup group = new OptionGroup("g", "group");
        Option option = new Option("g", "groupOption", true, "A group option");
        group.addOption(option);
        options.addOptionGroup(group);
        String[] arguments = {"--g", "--groupOption"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(2, result.getOptions().size());
        Option groupOption = result.getOption("g");
        Option groupOptionResult = result.getOption("g");
        assertSame(group, groupOption);
        assertTrue(groupOption.isSet());
        assertTrue(groupOptionResult.isSet());
        Option optionResult = result.getOption("g.groupOption");
        assertSame(option, optionResult);
        assertTrue(optionResult.isSet());
    }

    @Test
    public void testParse_help() throws ParseException
    {
        Option helpOption = new Option("h", "help", false, "Prints help message.");
        helpOption.setRequired(false);
        options.addOption(helpOption);
        String[] arguments = {"--help"};

        CommandLine result = parser.parse(options, arguments);
        assertTrue(helpOption.isSet());
        assertEquals("Print help on parsing", result.getGlobalOptions().getDescription());
    }

    @Test
    public void testParse_multipleHelpOptions() throws ParseException
    {
        Option helpOption = new Option("h", "help", false, "Prints help message.");
        helpOption.setRequired(false);
        options.addOption(helpOption);
        String[] arguments = {"--help", "--help"};

        CommandLine result = parser.parse(options, arguments);
        assertTrue(helpOption.isSet());
        assertEquals("Print help on parsing", result.getGlobalOptions().getDescription());
        assertEquals(1, result.getOptions().size());
    }

    @Test
    public void testParse_helpWithArguments() throws ParseException
    {
        Option helpOption = new Option("h", "help", true, "Prints help message.");
        helpOption.setRequired(false);
        options.addOption(helpOption);
        String[] arguments = {"--help", "arg1", "arg2"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals("arg1, arg2", result.getGlobalOptions().getLongOpt());
        assertEquals("Print help message.", result.getGlobalOptions().getName());
        assertEquals("arg1, arg2", result.getGlobalOptions().getOpt());
        assertEquals("Prints help message.", result.getGlobalOptions().getDescription());
        assertTrue(helpOption.isSet());
        assertEquals("arg1, arg2", helpOption.getValue());
    }

    @Test
    public void testParse_helpWithOption() throws ParseException
    {
        Option helpOption = new Option("h", "help", false, "Prints help message.");
        helpOption.setRequired(false);
        Option option = new Option("o", "option", true, "An optional option");
        options.addOptions(Arrays.asList(helpOption, option));
        String[] arguments = {"--help", "--option"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(2, result.getOptions().size());
        Option helpOptionResult = result.getOption("h");
        assertTrue(helpOptionResult.isSet());
        Option optionResult = result.getOption("o");
        assertTrue(optionResult.isSet());
        assertEquals("", helpOptionResult.getValue());
        assertEquals("", optionResult.getValue());
    }

    @Test
    public void testParse_helpWithArg() throws ParseException
    {
        Option helpOption = new Option("h", "help", true, "Prints help message.");
        helpOption.setRequired(false);
        Option option = new Option("o", "option", true, "An optional option");
        Option optionArg = new Option("a", "argument", true, "An argument for option");
        option.addOption(optionArg);
        options.addOptions(Arrays.asList(helpOption, option));
        String[] arguments = {"--help", "--option", "--argument", "arg"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(1, result.getArgs().size());
        assertEquals("arg", result.getArgs().get(0));
        assertEquals(3, result.getOptions().size());
        Option helpOptionResult = result.getOption("h");
        assertTrue(helpOptionResult.isSet());
        Option optionResult = result.getOption("o");
        assertTrue(optionResult.isSet());
        Option optionArgResult = result.getOption("o.argument");
        assertSame(optionArg, optionArgResult);
        assertTrue(optionArgResult.isSet());
        assertEquals("arg", optionArgResult.getValue());
    }

    @Test
    public void testParse_helpWithGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup("g", "group");
        Option helpOption = new Option("h", "help", false, "Prints help message.");
        helpOption.setRequired(false);
        Option option = new Option("o", "option", true, "An optional option");
        group.addOption(helpOption);
        group.addOption(option);
        options.addOptionGroup(group);
        String[] arguments = {"--g", "--help", "--option"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(3, result.getOptions().size());
        Option helpOptionResult = result.getOption("h");
        assertTrue(helpOptionResult.isSet());
        Option optionResult = result.getOption("o");
        assertTrue(optionResult.isSet());
        Option groupHelpOption = result.getOption("g.help");
        assertTrue(groupHelpOption.isSet());
    }

    @Test
    public void testParse_helpWithMultipleGroups() throws ParseException
    {
        OptionGroup group1 = new OptionGroup("g1", "group1");
        OptionGroup group2 = new OptionGroup("g2", "group2");
        Option helpOption = new Option("h", "help", false, "Prints help message.");
        helpOption.setRequired(false);
        Option option = new Option("o", "option", true, "An optional option");
        group1.addOption(helpOption);
        group1.addOption(option);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        String[] arguments = {"--g1", "--help", "--option"};

        CommandLine result = parser.parse(options, arguments);
        assertEquals(0, result.getArgs().size());
        assertEquals(3, result.getOptions().size());
        Option helpOptionResult = result.getOption("h");
        assertTrue(helpOptionResult.isSet());
        Option optionResult = result.getOption("o");
        assertTrue(optionResult.isSet());
        Option groupHelpOption = result.getOption("g1.help");
        assertTrue(groupHelpOption.isSet());
    }

    @After
    public void tearDown()
    {
        parser = null;
        commandLine = null;
        options = null;
    }
}