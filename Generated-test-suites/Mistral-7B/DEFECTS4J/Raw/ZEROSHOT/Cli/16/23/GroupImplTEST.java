import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    private GroupImpl group;
    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        options = new Options();

        List<Option> optionsList = Arrays.asList(
                new Option("o", "option", false, "an option"),
                new Option("g", "group", 3, "a group option"),
                new Option("a", "anonymous", 1, "an anonymous argument"),
                new Option("h", "help", false, "display help"),
                new Option("arg", "argument", 1, "an argument"),
                new Option("another-arg", "another-arg", 1, "another argument")
        );

        group = new GroupImpl(optionsList, "group", "a group", 1, 5);

        formatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = new CommandLine();
        cmd.addOption("o");
        cmd.addOption("g");
        cmd.addOption("g");
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("h");

        // test that canProcess returns true for trigger and option
        assertTrue(group.canProcess(cmd, "g"));

        // test that canProcess returns true for anonymous argument
        assertTrue(group.canProcess(cmd, "a"));

        // test that canProcess returns false for invalid argument
        assertFalse(group.canProcess(cmd, "x"));

        // test that canProcess returns false for help option
        assertFalse(group.canProcess(cmd, "h"));
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("a group", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(5, options.size());
        assertTrue(options.contains(group.findOption("g")));
        assertTrue(options.contains(group.findOption("o")));
        assertNull(options.get(2)); // anonymous argument not included
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(2, anonymous.size());
        assertTrue(anonymous.get(0) instanceof Argument);
        assertTrue(anonymous.get(1) instanceof Option);
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine cmd = new CommandLine();
        cmd.addOption("h");
        cmd.addOption("g");
        cmd.addOption("g");
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("o");

        List<String> arguments = Arrays.asList("help", "group", "group", "a", "a", "option");

        group.process(cmd, arguments.listIterator());

        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("a"));

        assertEquals(3, cmd.getOptions().size());
    }

    @Test
    public void testValidate() throws OptionException {
        CommandLine cmd = new CommandLine();

        try {
            group.validate(cmd);
        } catch (OptionException e) {
            assertEquals("Too few options. At least 1 option is required", e.getMessage());
            return;
        }

        // validation should have thrown an exception
        fail();
    }

    @Test
    public void testValidateWithOptions() throws OptionException {
        CommandLine cmd = new CommandLine();
        cmd.addOption("h");

        group.validate(cmd);

        // validation should not have thrown an exception
    }

    @Test
    public void testAppendUsage() {
        StringWriter writer = new StringWriter();

        group.appendUsage(writer, Collections.emptySet(), null);

        String usage = writer.toString();
        assertTrue(usage.contains("OPTIONS"));
        assertTrue(usage.contains("a group"));
        assertTrue(usage.contains("group ("));
        assertTrue(usage.contains("option"));
        assertTrue(usage.contains("anonymous"));
        assertTrue(usage.contains("help"));
        assertTrue(usage.contains("[option]"));
        assertTrue(usage.contains("[anonymous]"));
    }

    @Test
    public void testHelpLines() {
        StringWriter writer = new StringWriter();

        List<HelpLine> lines = group.helpLines(0, Collections.emptySet(), null);

        for (HelpLine line : lines) {
            line.printHelp(writer, 80, null, null, false);
        }

        String help = writer.toString();
        assertTrue(help.contains("OPTIONS"));
        assertTrue(help.contains("a group"));
        assertTrue(help.contains("group ("));
        assertTrue(help.contains("option"));
        assertTrue(help.contains("anonymous"));
        assertTrue(help.contains("help"));
        assertTrue(help.contains("[option]"));
        assertTrue(help.contains("[anonymous]"));
    }
}

class StringWriter extends StringBuffer {
    public StringWriter() {
        super();
    }

    public StringWriter(int size) {
        super(size);
    }

    public StringWriter(String str) {
        super(str);
    }

    public String toString() {
        return super.toString();
    }
}