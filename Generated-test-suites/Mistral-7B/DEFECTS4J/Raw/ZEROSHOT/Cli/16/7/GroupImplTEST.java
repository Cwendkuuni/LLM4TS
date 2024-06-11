import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private Group group;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "A group option", "group-arg"));
        groupOptions.add(new Option("o", "option", true, "An option", "option-arg"));
        groupOptions.add(new Option("a", "anonymous", true, "An anonymous argument", "anonymous-arg"));
        groupOptions.add(new Option("h", "help", false, "Print help message"));

        options = new Options();
        options.addGroup(new Group("group", "Group with options and an anonymous argument", groupOptions, 1, 1));
        group = options.getOptionGroups().get(0);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNotNull(group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> optionsList = group.getOptions();
        assertEquals(3, optionsList.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymousList = group.getAnonymous();
        assertEquals(1, anonymousList.size());
    }

    @Test
    public void testCanProcessTrue() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-g", "group-arg", "--option", "option-arg"});
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessFalse() throws Exception {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessAnonymous() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-g", "--anonymous", "anonymous-arg"});
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewOptions() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-g"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-g", "group-arg", "--option", "option-arg", "--option"});
        group.validate(commandLine);
    }

    @Test
    public void testValidate() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-g", "group-arg", "--option", "option-arg"});
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertTrue(buffer.toString().contains("--group [group]"));
        assertTrue(buffer.toString().contains("--option <option-arg>"));
        assertTrue(buffer.toString().contains("--anonymous <anonymous-arg>"));
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("nonexistent"));
        Option option = group.findOption("option");
        assertNotNull(option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() throws Exception {
        CommandLine commandLine = new CommandLine();
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("group"));
    }

}