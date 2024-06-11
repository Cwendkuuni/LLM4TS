import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupImplTest {

    private List<Option> optionsList;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @BeforeEach
    public void setup() {
        optionsList = new ArrayList<>();
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGroupImplCanProcess() {
        // Test case for valid option processing
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        GroupImpl group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertFalse(group.canProcess(commandLine, "invalidOption"));

        // Test case for anonymous argument processing
        Argument anonymousArg1 = new ArgumentBuilder()
                .withLongOpt("anonymousArg1")
                .withDescription("This is anonymous argument 1")
                .build();
        optionsList.add(0, anonymousArg1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertTrue(group.canProcess(commandLine, "anonymousArg1"));
    }

    @Test
    public void testGroupImplProcess() throws OptionException {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);

        // Test case for valid options processing
        commandLine.parse("--option1 value1 --option2 value2");
        assertEquals(2, commandLine.getOptions().size());
        List<Option> parsedOptions = commandLine.getOptions();
        assertEquals(option1, parsedOptions.get(0));
        assertEquals(option2, parsedOptions.get(1));

        // Test case for anonymous argument processing
        optionsList.add(0, arg1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        commandLine.parse("--group argument1 value1 --option1 value1 --option2 value2");
        assertEquals(3, commandLine.getArgs().size());
        assertEquals("value1", commandLine.getArg(0));
        assertEquals("value1", commandLine.getArg(1));
        assertEquals("value2", commandLine.getArg(2));
    }

    @Test
    public void testGroupImplValidate() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .build();
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);

        // Test case for valid command line
        commandLine.parse("--option1 value1");
        group.validate(commandLine);

        // Test case for missing option
        commandLine.clear();
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test case for too many options
        commandLine.parse("--option1 value1 --option1 value2");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGroupImplAppendUsage() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);

        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        String expectedUsage = "group:\n" +
                "This is group\n" +
                "\n";
        expectedUsage += "options:\n";
        expectedUsage += "  option1\n";
        expectedUsage += "    This is option 1\n";
        expectedUsage += "  option2\n";
        expectedUsage += "    This is option 2\n";
        assertEquals(expectedUsage, buffer.toString());

        // Test case for anonymous argument
        optionsList.add(0, arg1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        buffer.setLength(0);
        group.appendUsage(buffer, null, null, "|");
        expectedUsage = "group:\n" +
                "This is group\n" +
                "\n";
        expectedUsage += "options:\n";
        expectedUsage += "  argument1\n";
        expectedUsage += "    This is argument 1\n";
        expectedUsage += "  option1\n";
        expectedUsage += "    This is option 1\n";
        expectedUsage += "  option2\n";
        expectedUsage += "    This is option 2\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGroupImplHelpLines() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);

        List<HelpLine> helpLines = group.helpLines(1, null, null);
        String expectedUsage = "group:\n" +
                "This is group\n" +
                "\n";
        expectedUsage += "options:\n";
        expectedUsage += "  option1\n";
        expectedUsage += "    This is option 1\n";
        expectedUsage += "  option2\n";
        expectedUsage += "    This is option 2\n";
        for (HelpLine helpLine : helpLines) {
            assertEquals(expectedUsage, helpLine.getHelp());
        }

        // Test case for anonymous argument
        optionsList.add(0, arg1);
        helpLines = group.helpLines(1, null, null);
        expectedUsage = "group:\n" +
                "This is group\n" +
                "\n";
        expectedUsage += "options:\n";
        expectedUsage += "  argument1\n";
        expectedUsage += "    This is argument 1\n";
        expectedUsage += "  option1\n";
        expectedUsage += "    This is option 1\n";
        expectedUsage += "  option2\n";
        expectedUsage += "    This is option 2\n";
        for (HelpLine helpLine : helpLines) {
            assertEquals(expectedUsage, helpLine.getHelp());
        }
    }

    @Test
    public void testGroupImplGetOptions() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        // Test case for anonymous argument
        optionsList.add(0, arg1);
        options = group.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(arg1));
    }

    @Test
    public void testGroupImplGetAnonymous() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        List<Option> anonymous = group.getAnonymous();
        assertTrue(anonymous.isEmpty());

        // Test case for anonymous argument
        optionsList.add(0, arg1);
        anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(arg1));
    }

    @Test
    public void testGroupImplFindOption() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("This is option 1")
                .build();
        Argument arg1 = new ArgumentBuilder()
                .withLongOpt("argument1")
                .withDescription("This is argument 1")
                .build();
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("This is option 2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        Option foundOption = group.findOption("option1");
        assertEquals(option1, foundOption);

        // Test case for anonymous argument
        optionsList.add(0, arg1);
        foundOption = group.findOption("argument1");
        assertEquals(arg1, foundOption);
    }

    @Test
    public void testGroupImplGetMinimum() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .build();
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertEquals(1, group.getMinimum());

        // Test case for required option group
        Option optionGroup = new OptionBuilder()
                .withLongOpt("group")
                .withDescription("This is group")
                .build();
        optionList.add(optionGroup);
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGroupImplGetMaximum() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .build();
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertEquals(1, group.getMaximum());

        // Test case for max 2 options
        Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .build();
        optionsList.add(option1);
        optionsList.add(option2);
        group = new GroupImpl(optionsList, "group", null, 1, 2);
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testGroupImplIsRequired() {
        Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .build();
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertFalse(group.isRequired());

        // Test case for required option group
        Option optionGroup = new OptionBuilder()
                .withLongOpt("group")
                .withDescription("This is group")
                .build();
        optionList.add(optionGroup);
        optionsList.add(option1);
        group = new GroupImpl(optionsList, "group", null, 1, 1);
        assertTrue(group.isRequired());
    }

    @AfterAll
    public void tearDown() {
        optionsList.clear();
        group = null;
        commandLine = null;
    }
}