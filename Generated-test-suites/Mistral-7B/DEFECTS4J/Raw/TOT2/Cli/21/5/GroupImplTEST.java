import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {
    private Options options;
    private GroupImpl group;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> list = new ArrayList<>();
        Option option1 = Option.builder("o").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o")
                .longOpt("option2")
                .required(true)
                .build();
        Option option3 = Option.builder("a").argName("arg")
                .longOpt("argument")
                .required(false)
                .build();
        list.add(option1);
        list.add(option2);
        list.add(option3);
        group = new GroupImpl(list, "Group", null, 1, 1, false);
        options = new Options();
        options.addGroup(group);
        formatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess_OneOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);

        assertTrue(group.canProcess(commandLine, "o"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TwoOptions() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);
        commandLine.addOption("o", true);

        group.canProcess(commandLine, "o");
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);
        commandLine.addOption("a", "arg");

        assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("i", true);

        assertFalse(group.canProcess(commandLine, "i"));
    }

    @Test
    public void testProcess_OneOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);

        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.process(commandLine, arguments);

        assertEquals("option1", group.getName());
        assertEquals(1, group.getOptions().size());
        assertFalse(group.isRequired());
    }

    @Test
    public void testProcess_AnonymousArgument() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);
        commandLine.addOption("a", "arg");

        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.process(commandLine, arguments);

        assertEquals("Group", group.getName());
        assertEquals(3, group.getOptions().size());
        assertFalse(group.isRequired());
    }

    @Test(expected = OptionException.class)
    public void testProcess_TooFewOptions() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("o", true);

        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.process(commandLine, arguments);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Group validation should not throw an exception");
        }

        commandLine.addOption("o", true);
        commandLine.addOption("o", true);

        try {
            group.validate(commandLine);
            fail("Group validation should have thrown an exception");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testHelp() {
        StringWriter writer = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();

        group.appendUsage(writer, null, null, "|");

        String help = writer.toString();

        String expected = "| Group\n" +
                "|   option1\n" +
                "|       This is option1\n" +
                "|   option2\n" +
                "|       This is option2\n" +
                "|   argument\n" +
                "|       This is argument\n" +
                "|";

        assertEquals(expected, help);
    }
}