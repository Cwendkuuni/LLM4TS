import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        final String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        final String option1 = "-opt1";
        final String option2 = "--option2";
        options.addOption(new Option(option1, "option1", false, "Option 1"));
        options.addOption(new Option(option2, "option2", true, "Option 2"));

        final String[] arguments = {"-opt1", "--option2", "arg"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(2, cmd.getOptions().size());

        Option opt1 = cmd.getOption(option1);
        assertTrue(opt1.isSet());
        assertFalse(opt1.hasArg());

        Option opt2 = cmd.getOption(option2);
        assertTrue(opt2.isSet());
        assertEquals("arg", opt2.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        final String unrecognizedOption = "-unknown";
        options.addOption(new Option("opt", "option", false, "Option"));

        final String[] arguments = {"-unknown"};
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        final String option = "--option";
        options.addOption(new Option(option, "option", true, "Option"));

        final String[] arguments = {"--option"};
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        final String option1 = "-opt1";
        final String option2 = "--option2";
        options.addOption(new Option(option1, "option1", false, "Option 1"));

        final Properties properties = new Properties();
        properties.setProperty("--option2", "value");

        final String[] arguments = {"-opt1"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(1, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());

        Option opt1 = cmd.getOption(option1);
        assertTrue(opt1.isSet());
        assertFalse(opt1.hasArg());

        Option opt2 = cmd.getOption(option2);
        assertTrue(opt2.isSet());
        assertEquals("value", opt2.getValue());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        final String option1 = "--opt1";
        options.addOption(new Option(option1, "opt1", true, "Option 1"));
        options.addOption(new Option("opt2", "opt2", true, "Option 2"));
        options.addOption(new Option("opt3", "opt3", true, "Option 3"));

        final String[] arguments = {"--opt2"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals(Arrays.asList(option1), e.getMissingOptions());
        }
    }

    @Test
    public void testParseWithMixedArgumentsAndOptions() throws ParseException {
        final String option1 = "--option1";
        final String option2 = "--option2";
        options.addOption(new Option(option1, "option1", false, "Option 1"));
        options.addOption(new Option(option2, "option2", true, "Option 2"));

        final String[] arguments = {"--option1", "arg1", "--option2", 
                "arg2", "--option1", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        Option opt1 = cmd.getOption(option1);
        assertTrue(opt1.isSet());
        assertFalse(opt1.hasArg());

        Option opt2 = cmd.getOption(option2);
        assertTrue(opt2.isSet());
        assertEquals("arg3", opt2.getValue());
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        final String groupName = "group";
        final String option1 = "--option1";
        final String option2 = "--option2";

        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(false);
        group.addOption(new Option(option1, "option1", false, "Option 1"));
        group.addOption(new Option(option2, "option2", false, "Option 2"));
        options.addOption(group);

        final String[] arguments = {"--group", "--option1", "--option2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());

        Option opt1 = cmd.getOption(option1);
        assertTrue(opt1.isSet());
        assertFalse(opt1.hasArg());

        Option opt2 = cmd.getOption(option2);
        assertTrue(opt2.isSet());
        assertFalse(opt2.hasArg());

        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithEmptyOptionGroup() throws ParseException {
        final String groupName = "group";
        OptionGroup group = new OptionGroup(groupName, "Group");
        options.addOption(group);

        final String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
        assertNull(group.getSelectedOption());
    }

    @Test
    public void testParseWithGroupAndIndividualOptions() throws ParseException {
        final String groupName = "group";
        final String option1 = "--option1";
        final String option2 = "--option2";

        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(false);
        group.addOption(new Option(option1, "option1", false, "Option 1"));
        options.addOption(group);
        options.addOption(new Option(option2, "option2", true, "Option 2"));

        final String[] arguments = {"--group", "--option1", "--option2", "arg"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(2, cmd.getOptions().size());

        Option opt1 = cmd.getOption(option1);
        assertTrue(opt1.isSet());
        assertFalse(opt1.hasArg());

        Option opt2 = cmd.getOption(option2);
        assertTrue(opt2.isSet());
        assertEquals("arg", opt2.getValue());

        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithGroupAndEmptyArguments() throws ParseException {
        final String groupName = "group";
        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(false);
        options.addOption(group);

        final String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
        assertNull(group.getSelectedOption());
    }

    @Test
    public void testParseWithGroupAndMissingOption() throws ParseException {
        final String groupName = "group";
        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(false);
        options.addOption(group);
        options.addOption(new Option("opt", "opt", true, "Option"));

        final String[] arguments = {"--group"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertNull(group.getSelectedOption());
        assertFalse(cmd.getOption("opt").isSet());
    }

    @Test
    public void testParseWithGroupAndMissingGroup() throws ParseException {
        final String option1 = "--option1";
        options.addOption(new Option(option1, "option1", false, "Option 1"));

        final String[] arguments = {"--option1"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertNull(cmd.getOptionGroup("group"));
        assertTrue(cmd.getOption(option1).isSet());
    }

    @Test
    public void testParseWithGroupAndOptionalArgument() throws ParseException {
        final String groupName = "group";
        final String option1 = "--option1";

        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(false);
        group.addOption(new Option(option1, "option1", true, "Option 1"));
        options.addOption(group);

        final String[] arguments = {"--group", "--option1"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());

        Option opt = cmd.getOption(option1);
        assertTrue(opt.isSet());
        assertNull(opt.getValue());
    }

    @Test
    public void testParseWithGroupAndRequiredArgument() throws ParseException {
        final String groupName = "group";
        final String option1 = "--option1";

        OptionGroup group = new OptionGroup(groupName, "Group");
        group.setRequired(true);
        group.addOption(new Option(option1, "option1", true, "Option 1"));
        options.addOption(group);

        final String[] arguments = {};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(Collections.singletonList(option1), missingOptions);
        }
    }
}