import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "Group option", 1));
        groupOptions.add(new Option("a", "arg", true, "Argument option", 1));
        groupOptions.add(new Option("p", "prefix", true, "Prefix option", 1));
        groupOptions.add(new Option("i", "int", true, "Integer option", 1));

        options = new Options();
        options.addGroup(new GroupImpl(groupOptions, "MyGroup", "My group", 1, 1));
        options.addOption(new Option("e", "email", true, "Email option", 1));
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options.getOptions());

        // Test with valid options
        assertTrue(options.findGroup("MyGroup").canProcess(commandLine, "g"));
        assertTrue(options.findGroup("MyGroup").canProcess(commandLine, "a"));
        assertTrue(options.findGroup("MyGroup").canProcess(commandLine, "p"));
        assertTrue(options.findGroup("MyGroup").canProcess(commandLine, "i"));

        // Test with invalid option
        assertFalse(options.findGroup("MyGroup").canProcess(commandLine, "e"));

        // Test with empty command line
        assertFalse(options.findGroup("MyGroup").canProcess(new CommandLine(), null));
    }

    @Test
    public void testProcess() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options.getOptions());

        // Test with valid options
        List<String> arguments = new ArrayList<>();
        arguments.add("g");
        arguments.add("arg1");
        options.findGroup("MyGroup").process(commandLine, arguments.listIterator());
        assertSame("arg1", commandLine.getOptionValue("group"));

        arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("arg1");
        options.findGroup("MyGroup").process(commandLine, arguments.listIterator());
        assertSame("arg1", commandLine.getOptionValue("arg"));

        arguments = new ArrayList<>();
        arguments.add("p");
        arguments.add("prefix1");
        options.findGroup("MyGroup").process(commandLine, arguments.listIterator());
        assertSame("prefix1", commandLine.getOptionValue("prefix"));

        arguments = new ArrayList<>();
        arguments.add("i");
        arguments.add("5");
        options.findGroup("MyGroup").process(commandLine, arguments.listIterator());
        assertEquals(5, commandLine.getOptionValue("int", Integer.class));

        // Test with invalid option
        arguments = new ArrayList<>();
        arguments.add("e");
        arguments.add("email");
        try {
            options.findGroup("MyGroup").process(commandLine, arguments.listIterator());
            fail();
        } catch (OptionException ex) {
            // Expected
        }

        // Test with anonymous arguments
        assertSame("arg1", commandLine.getOptionValue("MyGroup.arg"));
        assertSame("prefix1", commandLine.getOptionValue("MyGroup.prefix"));

        // Test with missing option
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        assertNull(commandLine.getOptionValue("group"));
        assertNull(commandLine.getOptionValue("arg"));
        assertNull(commandLine.getOptionValue("prefix"));
        assertNull(commandLine.getOptionValue("int"));
        assertNull(commandLine.getOptionValue("email"));

        // Test with too many options
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        commandLine.getParsedArgs().add("g");
        commandLine.getParsedArgs().add("g");
        try {
            options.findGroup("MyGroup").process(commandLine, commandLine.listIterator());
            fail();
        } catch (OptionException ex) {
            // Expected
        }
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine();

        // Test with valid options
        List<String> arguments = new ArrayList<>();
        arguments.add("g");
        arguments.add("arg1");
        options.findGroup("MyGroup").validate(commandLine);

        // Test with missing options
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        try {
            options.findGroup("MyGroup").validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // Expected
        }

        // Test with too few options
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        commandLine.getParsedArgs().add("g");
        try {
            options.findGroup("MyGroup").validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // Expected
        }

        // Test with too many options
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        commandLine.getParsedArgs().add("g");
        commandLine.getParsedArgs().add("g");
        try {
            options.findGroup("MyGroup").validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // Expected
        }

        // Test with valid anonymous arguments
        arguments = new ArrayList<>();
        arguments.add("MyGroup.arg");
        arguments.add("arg1");
        options.findGroup("MyGroup").validate(commandLine);

        // Test with missing anonymous arguments
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        try {
            options.findGroup("MyGroup").validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // Expected
        }

        // Test with too few anonymous arguments
        commandLine = new CommandLine();
        commandLine.parse(options.getOptions());
        commandLine.getParsedArgs().add("g");
        commandLine.getParsedArgs().add("MyGroup.arg");
        try {
            options.findGroup("MyGroup").validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // Expected
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();

        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("MyGroup:\n" +
                "  group GROUP    Group option (required)\n" +
                "  arg ARG       Argument option (required)\n" +
                "  prefix PREFIX  Prefix option (required)\n" +
                "  int INTEGER   Integer option (required)\n" +
                "\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("MyGroup:\n" +
                "  [group GROUP]        Group option\n" +
                "  arg ARG            Argument option (required)\n" +
                "  prefix PREFIX      Prefix option (required)\n" +
                "  int INTEGER        Integer option (required)\n" +
                "\n", buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("MyGroup:\n" +
                "  group:\n" +
                "    group GROUP    Group option (required)\n" +
                "    arg ARG       Argument option (required)\n" +
                "    prefix PREFIX  Prefix option (required)\n" +
                "    int INTEGER   Integer option (required)\n" +
                "\n", buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("MyGroup:\n" +
                "My group:\n" +
                "  group GROUP    Group option (required)\n" +
                "\n", buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("My group:\n" +
                "My group:\n" +
                "  group:\n" +
                "    group GROUP    Group option (required)\n" +
                "    arg ARG       Argument option (required)\n" +
                "    prefix PREFIX  Prefix option (required)\n" +
                "    int INTEGER   Integer option (required)\n" +
                "\n", buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, null);
        assertEquals("My group:\n" +
                "My group:\n" +
                "  [group GROUP]        Group option\n" +
                "  arg ARG            Argument option (required)\n" +
                "  prefix PREFIX      Prefix option (required)\n" +
                "  int INTEGER        Integer option (required)\n" +
                "\n", buffer.toString());

        buffer = new StringBuffer();
        options.findGroup("MyGroup").appendUsage(buffer, helpSettings, new StringComparators.ReverseStringComparator());
        assertEquals("My group:\n" +
                "My group:\n" +
                "  int INTEGER        Integer option (required)\n" +
                "  prefix PREFIX      Prefix option (required)\n" +
                "  arg ARG            Argument option (required)\n" +
                "  group GROUP        Group option\n" +
                "\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();

        List<HelpLine> helpLines = options.findGroup("MyGroup").helpLines(0, null, null);
        assertEquals(5, helpLines.size());
        HelpLine firstHelpLine = helpLines.get(0);
        assertEquals("My group:\n" +
                "My group:\n" +
                "  group GROUP    Group option (required)\n" +
                "  arg ARG       Argument option (required)\n" +
                "  prefix PREFIX  Prefix option (required)\n" +
                "  int INTEGER   Integer option (required)\n" +
                "\n", firstHelpLine.getLongOptionDescription());
        assertEquals("My group:\n" +
                "group GROUP    Group option (required)\n" +
                "arg ARG       Argument option (required)\n" +
                "prefix PREFIX  Prefix option (required)\n" +
                "int INTEGER   Integer option (required)\n", firstHelpLine.getShortOptionDescription());
        assertEquals("My group\n", firstHelpLine.getOptionStr());

        HelpLine lastHelpLine = helpLines.get(4);
        assertEquals("", lastHelpLine.getLongOptionDescription());
        assertEquals("", lastHelpLine.getShortOptionDescription());
        assertEquals("", lastHelpLine.getOptionStr());

        // Test with expanded groups
        helpLines = options.findGroup("MyGroup").helpLines(1, null, null);
        assertEquals(14, helpLines.size());
        HelpLine expandedGroupHelpLine = helpLines.get(0);
        assertEquals("My group:\n" +
                "My group:\n" +
                "  group:\n" +
                "    group GROUP    Group option (required)\n" +
                "    arg ARG       Argument option (required)\n" +
                "    prefix PREFIX  Prefix option (required)\n" +
                "    int INTEGER   Integer option (required)\n" +
                "\n", expandedGroupHelpLine.getLongOptionDescription());
        assertEquals("My group:\n" +
                "My group:\n" +
                "  group:\n" +
                "    group GROUP    Group option (required)\n" +
                "    arg ARG       Argument option (required)\n" +
                "    prefix PREFIX  Prefix option (required)\n" +
                "    int INTEGER   Integer option (required)\n" +
                "\n", firstHelpLine.getLongOptionDescription());
        HelpLine expandedGroupArgHelpLine = helpLines.get(11);
        assertEquals("My group.arg ARG       Argument option (required)\n", expandedGroupArgHelpLine.getLongOptionDescription());
        assertEquals("My group.arg ARG       ARG\n" +
                "  ARG is a required argument.\n", expandedGroupArgHelpLine.getShortOptionDescription());
        assertEquals("My group.arg\n", expandedGroupArgHelpLine.getOptionStr());

        // Test with just the name
        helpLines = options.findGroup("MyGroup").helpLines(1, new HashSet<>(Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME)), null);
        assertEquals(3, helpLines.size());
        HelpLine expandedGroupNameHelpLine = helpLines.get(0);
        assertEquals("My group:\n" +
                "My group:\n" +
                "My group\n", expandedGroupNameHelpLine.getLongOptionDescription());
        assertEquals("My group\n", expandedGroupNameHelpLine.getShortOptionDescription());
        assertEquals("My group\n", expandedGroupNameHelpLine.getOptionStr());
    }
}

class ReverseStringComparator implements Comparator<String> {
    public static final Comparator INSTANCE = new ReverseStringComparator();

    private ReverseStringComparator() {
    }

    public int compare(String o1, String o2) {
        return -o1.compareTo(o2);
    }
}