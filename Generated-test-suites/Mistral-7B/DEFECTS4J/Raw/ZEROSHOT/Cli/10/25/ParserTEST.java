import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private static final String[] ARGS = {"--foo", "-a", "-b", "-c", "--bar"};
    private static final String[] REQUIRED_OPTIONS = {"--foo", "--bar"};
    private static final String[] ARGS_WITH_PROPERTIES = {"--foo=value1", "--bar=value2", "-a", "-b", "-c"};
    private static final String[] ARGS_STOP_AT_NON_OPTION = {"--foo", "-a", "-b", "-c--"};

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("foo", true, "f");
        options.addRequiredOption("bar", true, "b");
        options.addOption("a", false, "a");
        options.addOption("b", false, "b");
        options.addOption("c", false, "c");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParse() throws ParseException {
        CommandLine cmd = parser.parse(options, ARGS);

        assertEquals(ARGS.length, cmd.getArgs().size());
        assertEquals(REQUIRED_OPTIONS.length, cmd.getOptions().size());
        for (String option : REQUIRED_OPTIONS) {
            Option actualOption = cmd.getOption(option);
            Option expectedOption = options.getOption(option);
            assertSame(expectedOption, actualOption);
            assertTrue(actualOption.isSet());
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, ARGS);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        CommandLine cmd = parser.parse(options, null, new Properties() {{
            setProperty("foo", "value1");
            setProperty("bar", "value2");
        }});

        assertEquals(ARGS_WITH_PROPERTIES.length - 2, cmd.getArgs().size());
        assertEquals(REQUIRED_OPTIONS.length, cmd.getOptions().size());
        for (String option : REQUIRED_OPTIONS) {
            Option actualOption = cmd.getOption(option);
            Option expectedOption = options.getOption(option);
            assertSame(expectedOption, actualOption);
            assertTrue(actualOption.isSet());
        }
        assertEquals("value1", cmd.getOptionValue("foo"));
        assertEquals("value2", cmd.getOptionValue("bar"));
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(options, ARGS_STOP_AT_NON_OPTION, null, true);

        assertEquals(ARGS_STOP_AT_NON_OPTION.length - 1, cmd.getArgs().size());
        assertEquals(REQUIRED_OPTIONS.length, cmd.getOptions().size());
        for (String option : REQUIRED_OPTIONS) {
            Option actualOption = cmd.getOption(option);
            Option expectedOption = options.getOption(option);
            assertSame(expectedOption, actualOption);
            assertTrue(actualOption.isSet());
        }
        assertNull(cmd.getOptionValue("--"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[]{"--qux"});
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption("a", false, "a", "A");
        group.addOption("b", false, "b", "B");
        group.addOption("c", false, "c", "C");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"--group"});

        OptionGroup actualGroup = cmd.getOptionGroup("group");
        OptionGroup expectedGroup = options.getOptionGroup("group");
        assertSame(expectedGroup, actualGroup);
        assertTrue(actualGroup.isSelected());

        assertEquals(3, cmd.getOptions().size());
        assertEquals("A", cmd.getOptionValue("a"));
        assertEquals("B", cmd.getOptionValue("b"));
        assertEquals("C", cmd.getOptionValue("c"));
    }
}