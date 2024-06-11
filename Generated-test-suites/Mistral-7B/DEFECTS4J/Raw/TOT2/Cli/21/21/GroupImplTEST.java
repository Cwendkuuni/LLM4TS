import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {
    private CommandLine cmd;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Option A"));
        options.add(new Option("b", "optionB", false, "Option B"));
        Group group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE, false);
        cmd = new CommandLine();
        formatter = new HelpFormatter();
    }

    @Test
    public void testGroupCanProcessValidArgs() {
        String[] args = {"group", "a", "true"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        assertTrue(group.canProcess(cmd, "a"));
    }

    @Test
    public void testGroupCanProcessInvalidArgs() {
        String[] args = {"group", "x"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        assertFalse(group.canProcess(cmd, "x"));
    }

    @Test
    public void testGroupProcessValidArgs() {
        String[] args = {"group", "a", "true"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        group.process(cmd, cmd.listIterator());
        assertTrue(cmd.hasOption("a"));
        assertEquals("true", cmd.getOptionValue("a"));
    }

    @Test(expected = OptionException.class)
    public void testGroupProcessInvalidArgs() {
        String[] args = {"group", "a", "true", "x"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        group.process(cmd, cmd.listIterator());
    }

    @Test
    public void testGroupValidateValidArgs() {
        String[] args = {"group", "a", "true"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testGroupValidateInvalidArgs() {
        String[] args = {"group", "a", "true", "x"};
        cmd.parse(group.parse(args, new WriteableCommandLine()));
        group.validate(cmd);
    }

    @Test
    public void testGroupUsage() {
        StringWriter writer = new StringWriter();
        formatter.printHelp(1.0, "group", group, writer, true);
        String help = writer.toString();
        assertTrue(help.contains("Option A"));
        assertTrue(help.contains("Option B"));
        assertTrue(help.contains("true"));
    }
}