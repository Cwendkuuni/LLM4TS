import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupImplTest {
    private BaseParser parser;

    @Before
    public void setUp() {
        Options options = new Options();

        Option option1 = new Option("o1", "Option1")
                .required(false)
                .argName("value1")
                .desc("Option 1 description");
        Option option2 = new Option("o2", "Option2")
                .required(false)
                .argName("value2")
                .desc("Option 2 description");
        Option groupOption = new Option("g", "Group")
                .required(false)
                .desc("Group option description")
                .group(new GroupImpl(
                        Collections.singletonList(option1),
                        "MyGroup",
                        "My Group Description",
                        1,
                        Integer.MAX_VALUE));

        options.addOption(groupOption);
        options.addOption(option2);

        parser = new BaseParser();
    }

    @Test
    public void testGroupProcess() throws Exception {
        String[] args = {"g", "o1", "value1", "o2", "value2"};
        CommandLine commandLine = parser.parse(options, args);

        assertEquals("value1", commandLine.getOptionValue("o1"));
        assertEquals("value2", commandLine.getOptionValue("o2"));
        assertEquals("value1", groupOptionValue(commandLine, "o1"));
        assertEquals("value2", groupOptionValue(commandLine, "o2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupProcessInvalid() throws Exception {
        String[] args = {"g", "o1", "value1", "o1"};
        parser.parse(options, args);
    }

    @Test
    public void testGroupValidate() throws Exception {
        CommandLine commandLine = new TestableCommandLine();

        try {
            groupOption.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("The following required options are missing: g", e.getMessage());
            throw e;
        }

        commandLine.addOption("g");
        try {
            groupOption.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("Minimum number of values for option 'g' is 1, but it was invoked 1 times", e.getMessage());
            throw e;
        }

        commandLine.addOption("o1");
        try {
            groupOption.validate(commandLine);
        } catch (OptionException e) {
            throw new AssertionError("Option 'g' should be valid after adding option 'o1'");
        }

        commandLine.addOption("o2");
        commandLine.addOption("o1");
        try {
            groupOption.validate(commandLine);
            fail("Option 'g' should not be valid after adding option 'o1' twice");
        } catch (OptionException e) {
            assertEquals("Maximum number of values for option 'g' is unlimited (0), but it was invoked 3 times", e.getMessage());
        }
    }

    @Test
    public void testGroupUsage() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();

        String usage = formatter.printHelp("Test", options);
        assertEquals("Test [options]...\n" +
                "My Group Description:\n" +
                "  My Group Description\n" +
                "  --o1 VALUE1        Option 1 description\n" +
                "  --o2 VALUE2        Option 2 description\n" +
                "  --g                 My Group\n", usage);

        usage = formatter.printHelp("Test", options, true);
        assertTrue(usage.contains("--o1 VALUE1\n"));
        assertTrue(usage.contains("--o2 VALUE2\n"));
        assertTrue(usage.contains("--g [--o1 VALUE1]\n"));
    }

    @Test
    public void testGroupHelp() throws Exception {
        CommandLine commandLine = new TestableCommandLine();
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();

        Option option1 = new Option("o1", "Option1")
                .required(false)
                .desc("Option 1 description")
                .group(new GroupImpl(
                        Collections.singletonList(option1),
                        "MyGroup",
                        "My Group Description",
                        1,
                        Integer.MAX_VALUE));

        Option option2 = new Option("o2", "Option2")
                .required(false)
                .desc("Option 2 description");

        options.addOption(option1);
        options.addOption(option2);

        String help = formatter.printHelp("Test", options, 1, 1, "Test", true);

        String[] lines = help.split("\n");

        assertEquals(11, lines.length);
        assertEquals("Test [options]...\n" +
                "My Group Description:\n" +
                "  My Group Description\n" +
                "  --o1 VALUE1        Option 1 description\n" +
                "  --o2 VALUE2        Option 2 description\n" +
                "  --g                 My Group\n" +
                "                      My Group Description\n" +
                "                      --o1 VALUE1        Option 1 description\n" +
                "                      --o2 VALUE2        Option 2 description\n" +
                "", help);

        String groupHelp = "My Group\n" +
                "  My Group Description\n" +
                "  --o1 VALUE1        Option 1 description\n";

        int index = -1;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("  " + option1.getLongOpt())) {
                index = i;
                break;
            }
        }

        assertEquals("--g\n" +
                "  My Group Description\n" +
                "  --o1 VALUE1        Option 1 description\n", lines[index] + "\n" +
                lines[index + 1]);
    }

    @Test
    public void testGroupAnonymous() throws Exception {
        Options options = new Options();

        Option groupOption = new Option("g", "Group")
                .required(false)
                .desc("Group option description")
                .group(new GroupImpl(
                        Collections.singletonList(new Argument("arg1")),
                        "MyGroup",
                        "My Group Description",
                        1,
                        Integer.MAX_VALUE));

        options.addOption(groupOption);

        CommandLine commandLine = parser.parse(options, new String[]{});
        assertEquals("", commandLine.getOptionValue("arg1"));

        commandLine.parseArgument(0, "arg1Value");
        assertEquals("arg1Value", commandLine.getOptionValue("arg1"));
    }

    private Object groupOptionValue(CommandLine commandLine, String name) {
        List<Option> options = commandLine.getOptions();
        for (Option option : options) {
            if (option.getLongOpt().equals(name)) {
                return option;
            }

            if (option instanceof Group) {
                GroupImpl group = (GroupImpl) option;
                return groupOptionValue(group.getCommandLine(), name);
            }
        }

        return null;
    }
}