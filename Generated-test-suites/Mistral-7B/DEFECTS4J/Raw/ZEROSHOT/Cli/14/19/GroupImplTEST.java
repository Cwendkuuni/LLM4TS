import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.ext.MultiValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupImpl.class})
public class GroupImplTest {

    private Options options;
    private Group group;

    @Before
    public void setUp() {
        final Option option1 = new Option("option1", "Option 1", false, "o1");
        final Option option2 = new Option("option2", "Option 2", true, "o2");
        final Option option3 = new Option("option3", "Option 3", true, "o3")
                .required(true)
                .argName("arg")
                .valueSeparator('-')
                .multiValued(true)
                .numberOfValues(2);

        final Argument arg1 = new Argument("arg1", "Argument 1", false, "a1");
        final Argument arg2 = new Argument("arg2", "Argument 2", true, "a2")
                .required(true)
                .implicitValue("defaultValue");

        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(option1);
        optionsList.add(option2);
        optionsList.add(option3);

        final List<Argument> anonymousList = new ArrayList<>();
        anonymousList.add(arg1);
        anonymousList.add(arg2);

        this.options = new Options();
        this.options.addOption(option1);
        this.options.addOption(option2);
        this.options.addOption(option3);
        this.options.addArguments(anonymousList);

        this.group = new GroupImpl(optionsList, "Group", "Test Group", 1, 3) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String arg) {
                return false;
            }
        };
    }

    @Test
    public void testGetName() {
        assertEquals("Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(options.getOptions().size(), group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(options.getArgList().size(), group.getAnonymous().size());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidConstruction() {
        new GroupImpl(null, null, null, 1, 3);
    }

    @Test
    public void testProcess() {
        final ParseContext parseContext = new ParseContext();
        final CommandLine commandLine = new CommandLine();

        final String[] arguments = {"-o1", "--option2", "-o1", "--option2", "--o3", "arg1", "arg2"};

        group.process(commandLine, List.of(arguments).iterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasOption("o3"));
        assertTrue(commandLine.hasOption("o3", new String[]{"arg1", "arg2"}));
        assertNull(commandLine.getOptionValue("o1"));
        assertEquals(2, commandLine.getValues("o3", new MultiValues(new String[0])).size());
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() {
        final ParseContext parseContext = new ParseContext();
        final CommandLine commandLine = new CommandLine();

        final String[] arguments = {"-o1", "--option2", "-o1", "--option2", "--o3", "arg1"};

        group.process(commandLine, List.of(arguments).iterator());

        assertFalse(commandLine.hasOption("o3"));
        assertFalse(commandLine.hasOption("o3", new String[0]));
        assertFalse(commandLine.hasOption("o3", new String[]{"arg2"}));

        group.process(commandLine, List.of(arguments).iterator());

        throw new AssertionError("Process should have thrown an OptionException");
    }

    @Test
    public void testValidate() {
        final ParseContext parseContext = new ParseContext();
        final CommandLine commandLine = new CommandLine();

        final String[] arguments = {"--option1", "--option2", "--option3", "-o3", "arg1", "arg2"};

        group.validate(commandLine);

        assertEquals(3, commandLine.getValues("o3", new MultiValues(new String[0])).size());
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        final ParseContext parseContext = new ParseContext();
        final CommandLine commandLine = new CommandLine();

        final String[] arguments = {"--option1", "--option2", "--option3"};

        group.validate(commandLine);

        assertFalse(commandLine.hasOption("o3"));
        assertFalse(commandLine.hasOption("o3", new String[0]));
        assertFalse(commandLine.hasOption("o3", new String[]{"arg2"}));

        group.validate(commandLine);

        throw new AssertionError("Validate should have thrown an OptionException");
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, DisplaySetting.UNSET, null);

        assertTrue(sb.toString().contains("Group"));
        assertTrue(sb.toString().contains("Option 1"));
        assertTrue(sb.toString().contains("Option 2"));
        assertTrue(sb.toString().contains("Option 3"));
        assertTrue(sb.toString().contains("arg1"));
        assertTrue(sb.toString().contains("arg2"));
    }

    @Test
    public void testHelpLines() {
        final StringBuilder sb = new StringBuilder();
        final List<HelpLine> helpLines = group.helpLines(0, DisplaySetting.UNSET, null);

        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(sb, "java", group, helpLines.toArray(new HelpLine[0]));

        assertTrue(sb.toString().contains("Group"));
        assertTrue(sb.toString().contains("Option 1"));
        assertTrue(sb.toString().contains("Option 2"));
        assertTrue(sb.toString().contains("Option 3"));
        assertTrue(sb.toString().contains("arg1"));
        assertTrue(sb.toString().contains("arg2"));
    }
}