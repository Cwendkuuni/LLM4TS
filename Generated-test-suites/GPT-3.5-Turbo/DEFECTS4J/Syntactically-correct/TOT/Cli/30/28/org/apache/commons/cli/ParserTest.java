import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        options = new Options();
    }

    @Test
    public void testParse_withNullOptionsAndNullArguments_shouldReturnEmptyCommandLine() throws ParseException {
        CommandLine commandLine = parser.parse(null, null);

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testParse_withEmptyOptionsAndNullArguments_shouldReturnEmptyCommandLine() throws ParseException {
        CommandLine commandLine = parser.parse(options, null);

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testParse_withOptionsAndNullArguments_shouldReturnEmptyCommandLine() throws ParseException {
        options.addOption("a", false, "Option A");

        CommandLine commandLine = parser.parse(options, null);

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testParse_withOptionsAndEmptyArguments_shouldReturnEmptyCommandLine() throws ParseException {
        options.addOption("a", false, "Option A");

        CommandLine commandLine = parser.parse(options, new String[0]);

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testParse_withOptionsAndNonOptionArgument_shouldReturnCommandLineWithArguments() throws ParseException {
        options.addOption("a", false, "Option A");

        CommandLine commandLine = parser.parse(options, new String[]{"arg1", "arg2"});

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertEquals(2, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test
    public void testParse_withOptionsAndShortOption_shouldReturnCommandLineWithOption() throws ParseException {
        options.addOption("a", false, "Option A");

        CommandLine commandLine = parser.parse(options, new String[]{"-a"});

        assertNotNull(commandLine);
        assertEquals(1, commandLine.getOptions().getOptions().size());
        assertTrue(commandLine.hasOption("a"));
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testParse_withOptionsAndLongOption_shouldReturnCommandLineWithOption() throws ParseException {
        options.addOption("a", false, "Option A");

        CommandLine commandLine = parser.parse(options, new String[]{"--a"});

        assertNotNull(commandLine);
        assertEquals(1, commandLine.getOptions().getOptions().size());
        assertTrue(commandLine.hasOption("a"));
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testParse_withOptionsAndRequiredOption_shouldReturnMissingOptionException() {
        Option option = new Option("a", false, "Option A");
        option.setRequired(true);
        options.addOption(option);

        try {
            parser.parse(options, new String[]{"--b"});
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals(1, e.getMissingOptions().size());
            assertEquals("a", e.getMissingOptions().get(0).getKey());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testParse_withOptionsAndOptionGroup_shouldSetSelectedOptionInGroup() throws ParseException {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", false, "Option A"));
        optionGroup.addOption(new Option("b", false, "Option B"));
        options.addOptionGroup(optionGroup);

        CommandLine commandLine = parser.parse(options, new String[]{"--b"});

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("b"));
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testParse_withOptionsAndProperties_shouldProcessProperties() throws ParseException {
        Option option = new Option("a", true, "Option A");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine commandLine = parser.parse(options, null, properties);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }
}