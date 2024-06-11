import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "group name").setRequired(false));
        groupOptions.add(new Option("a", "anonymous", true, "anonymous argument").setRequired(false));

        Option groupOption = new Option("g", "group", null, null, null, groupOptions, "Group Option");
        groupOption.setMinValues(1);
        groupOption.setMaxValues(3);

        Option argOption = new Option("a", "arg", true, "arg name").setRequired(false);
        argOption.setArgName("argValue");

        Option groupArgOption = new Option("", "groupArg", null, null, null, Collections.singletonList(argOption), "Group Arg Option");
        groupArgOption.setArgs(1);

        options = new Options();
        options.addOption(groupOption);
        options.addOption(groupArgOption);

        commandLine = new CommandLine();
    }

    @Test
    public void testCanProcess() throws Exception {
        // test canProcess with valid options
        boolean result = groupOption.canProcess(commandLine, "g");
        assertTrue(result);

        result = groupOption.canProcess(commandLine, "a");
        assertTrue(result);

        result = groupArgOption.canProcess(commandLine, "argValue");
        assertTrue(result);

        // test canProcess with invalid options
        result = groupOption.canProcess(commandLine, "invalid");
        assertFalse(result);

        result = groupArgOption.canProcess(commandLine, "invalidArg");
        assertFalse(result);

        // test canProcess with empty command line
        commandLine.clear();
        assertFalse(groupOption.canProcess(commandLine, "g"));
        assertFalse(groupArgOption.canProcess(commandLine, "argValue"));
    }

    @Test
    public void testProcess() throws Exception {
        // test process with valid options
        commandLine.parse(options, new String[]{"g", "groupName", "g", "groupName2", "a", "argValue1", "a", "argValue2"});
        GroupImpl group = (GroupImpl) groupOption.getValue(commandLine);
        assertEquals(2, group.getOptions().size());
        assertEquals("groupName2", group.getOptions().get(1).getValue());

        // test process with invalid options
        commandLine.clear();
        try {
            group.process(commandLine, commandLine.iterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testValidate() throws Exception {
        // test validate with valid command line
        commandLine.parse(options, new String[]{"g", "groupName", "g", "groupName"});
        GroupImpl group = (GroupImpl) groupOption.getValue(commandLine);
        assertEquals(2, group.getOptions().size());

        // test validate with invalid command line
        commandLine.clear();
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetPreferredName() {
        String name = "groupName";
        GroupImpl group = new GroupImpl(Collections.emptyList(), name, null, 1, 3);
        assertEquals(name, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        String description = "group description";
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, description, 1, 3);
        assertEquals(description, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);

        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("", buffer.toString());

        Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group:\n", buffer.toString());

        buffer.setLength(0);
        helpSettings.clear();
        helpSettings.addAll(Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL));

        group = new GroupImpl(Collections.emptyList(), "groupName", null, 1, 3);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("groupName:\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(0, helpLines.size());

        Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("group:\n  group:\n", helpLine.getLongOpt());
        assertEquals("group\n  group\n", helpLine.getOpt());
        assertEquals("group\n", helpLine.getShortOpt());
        assertEquals("optional group\n", helpLine.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "arg", true, "arg name").setRequired(false));

        GroupImpl group = new GroupImpl(options, null, null, 1, 3);
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = new ArrayList<>();
        anonymous.add(new Option("a", "anonymousArg", true, "anonymous arg name").setRequired(false));

        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        group.setAnonymous(anonymous);
        assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        List<Option> options = new ArrayList<>();
        Option option = new Option("a", "arg", true, "arg name").setRequired(false);
        options.add(option);

        GroupImpl group = new GroupImpl(options, null, null, 1, 3);
        Option result = group.findOption("arg");
        assertEquals(option, result);
    }

    @Test
    public void testGetMinimum() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() throws Exception {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 3);
        group.defaults(commandLine);

        assertEquals(0, commandLine.getArgs().size());
    }
}