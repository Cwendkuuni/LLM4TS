import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    private static final String[] ARGUMENTS = {"--option1", "-o", "value1", "--option2", "-a", "arg1", "-b", "arg2"};
    private static final String[] REQUIRED_OPTIONS = {"--option1", "--option2"};
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("o", "option1", true, "Option 1");
        options.addOption("a", "option2", true, "Option 2");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithNoOptions() throws ParseException
    {
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithOptions() throws ParseException
    {
        CommandLine cmd = parser.parse(options, ARGUMENTS);

        assertEquals(2, cmd.getOptions().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("arg1", cmd.getOptionValue("option2"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg2", cmd.getArgs().get(0));
        assertEquals("arg1", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, ARGUMENTS);

        String unrecognizedOption = "--unrecognized";
        parser.parse(options, new String[]{unrecognizedOption});
        assertNull(parser.cmd);
        assertTrue(parser.getOptions().hasOption(unrecognizedOption));
        assertThrows(ParseException.class, () -> parser.parse(options, new String[]{unrecognizedOption}));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(REQUIRED_OPTIONS);

        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithMissingRequiredOptionAndOthers() throws ParseException
    {
        parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(REQUIRED_OPTIONS);

        CommandLine cmd = parser.parse(options, new String[]{"--option2", "arg2"});

        assertEquals(1, cmd.getOptions().size());
        assertFalse(cmd.hasOption("option1"));
        assertNotNull(cmd.getOptionValue("option2"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg2", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionsInGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup("group", "Group options");
        Options optionsWithGroup = new Options();
        optionsWithGroup.addOptionGroup(group);
        group.addOption(new Option("g", "groupOption", true, "Group option"));
        optionsWithGroup.addOption("o", "option1", true, "Option 1");

        parser = new DefaultParser();
        parser.setOptions(optionsWithGroup);

        CommandLine cmd = parser.parse(optionsWithGroup, new String[]{"--group", "--option1", "value1"});

        assertEquals(1, cmd.getOptions().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.getOptions().hasOption("groupOption"));
        assertTrue(cmd.getOptions().getOption("groupOption").isSelected());
    }
}