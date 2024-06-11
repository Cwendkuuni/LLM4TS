import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private GroupImpl group;
    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        options = new Options();
        List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "optionA", true, "Option A description"));
        optionsList.add(new Option("b", "optionB", false, "Option B description"));
        group = new GroupImpl(optionsList, "Group 1", "Description of Group 1");
        formatter = new HelpFormatter();
    }

    

    @Test
    public void testGroupImpl_name() {
        assertEquals("Group 1", group.getPreferredName());
    }

    @Test
    public void testGroupImpl_description() {
        assertEquals("Description of Group 1", group.getDescription());
    }

    @Test
    public void testGroupImpl_canProcess_EmptyList() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testGroupImpl_canProcess_ValidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions().toArray(new Option[0]));
        assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testGroupImpl_canProcess_InvalidOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, "c"));
    }

    @Test
    public void testGroupImpl_process_ValidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions().toArray(new Option[0]));
        group.process(commandLine, commandLine.iterator());
        assertTrue(commandLine.hasOption("optionA"));
    }

    @Test
    public void testGroupImpl_process_InvalidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions().toArray(new Option[0]));
        group.process(commandLine, commandLine.iterator());
        assertFalse(commandLine.hasOption("c"));
    }

    @Test
    public void testGroupImpl_process_AnonymousArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions().toArray(new Option[0]));
        group.process(commandLine, commandLine.iterator());
        assertNotNull(commandLine.getArgList().get(0));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_process_AnonymousArgument_TooMany() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions().toArray(new Option[0]));
        commandLine.addOption("a");
        group.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_validate_MissingOption() {
        CommandLine commandLine = new CommandLine();
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_validate_TooManyOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A description");
        options.addOption("a", "optionA", true, "Option A description");
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options.toArray(new Option[0]));
        GroupImpl group = new GroupImpl(Collections.emptyList(), "Group 1", "Description of Group 1");
        group.validate(commandLine);
    }

    @Test
    public void testGroupImpl_appendUsage() {
        StringWriter writer = new StringWriter();
        group.appendUsage(writer, DisplaySetting.UNSET, null);
        String expectedUsage = "[Group 1]\n" +
                "Description of Group 1:\n" +
                "optionA  Option A description\n";
        assertEquals(expectedUsage, writer.toString());
    }

    @Test
    public void testGroupImpl_helpLines() {
        List<HelpLine> helpLines = group.helpLines(0, CollectionUtils.emptySet(), null);
        String expectedUsage = "[Group 1]\n" +
                "Description of Group 1:\n" +
                "optionA  Option A description";
        String expectedHelp = "usage: GroupImpl [-optionA]\n" +
                "\n" +
                "Options:\n" +
                "  -optionA value\n" +
                "        Option A description\n";
        String expectedDetailedHelp = "optionA\n" +
                "        Option A description\n";
        assertEquals(expectedUsage, helpLines.get(0).getShortDisplayText());
        assertEquals(expectedHelp, helpLines.get(0).getLongDisplayText());
        assertEquals(expectedDetailedHelp, helpLines.get(0).getNextOption().getShortDisplayText());
    }

    @Test
    public void testGroupImpl_getOptions() {
        List<Option> options = group.getOptions();
        assertEquals(1, options.size());
    }

    @Test
    public void testGroupImpl_findOption() {
        Option optionA = group.findOption("a");
        assertNotNull(optionA);
    }

    @Test
    public void testGroupImpl_isRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupImpl_defaults() {
        CommandLine commandLine = new CommandLine();
        group.defaults(commandLine);
        assertNull(commandLine.getArgList());
    }

    
}