import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        List list = new ArrayList();
        list.add(new Option("a", "option A", true, "description A"));
        list.add(new Option("b", "option B", false, "description B"));
        list.add(new Option("c", "option C", false, "description C"));
        list.add(new Option("d", "option D", true, "description D"));
        list.add(new Option("e", "option E", false, "description E"));
        list.add(new Option("f", "option F", false, "description F"));
        list.add(new Argument("arg1"));
        list.add(new Argument("arg2"));

        this.options = new Options();
        this.options.addGroup(new GroupImpl(list, "myGroup", "myGroup", 1, 3));

        this.commandLine = new CommandLine();
    }

    @After
    public void tearDown() {
        this.commandLine = null;
        this.options = null;
    }

    @Test
    public void testCanProcess() {
        this.commandLine.addOption("a");
        this.commandLine.addOption("b");
        this.commandLine.addOption("c");

        assertTrue(this.options.canProcess(this.commandLine, "a"));
        assertTrue(this.options.canProcess(this.commandLine, "b"));
        assertTrue(this.options.canProcess(this.commandLine, "c"));
        assertTrue(this.options.canProcess(this.commandLine, "d"));
        assertFalse(this.options.canProcess(this.commandLine, "e"));
        assertFalse(this.options.canProcess(this.commandLine, "f"));
        assertFalse(this.options.canProcess(this.commandLine, "g"));

        this.commandLine.clear();
        this.commandLine.addOption("a");
        this.commandLine.addOption("a");

        // Should rollback and abort
        assertFalse(this.options.canProcess(this.commandLine, "a"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = this.options.getPrefixes();

        assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = this.options.getTriggers();

        assertEquals(Collections.singleton("a"), triggers);
    }

    @Test
    public void testProcess() {
        this.commandLine.addOption("a");
        this.commandLine.addOption("d");

        this.options.process(this.commandLine, this.commandLine.listIterator());

        assertEquals("description A", this.commandLine.getValue("a"));
        assertEquals("description D", this.commandLine.getValue("d"));
        assertTrue(this.commandLine.hasOption("b"));
        assertTrue(this.commandLine.hasOption("c"));
        assertTrue(this.commandLine.hasOption("e"));
        assertTrue(this.commandLine.hasOption("f"));
        assertFalse(this.commandLine.hasOption("g"));

        assertTrue(this.commandLine.getArgument(0).isPresent());
        assertEquals("arg1", this.commandLine.getArgument(0).get());
        assertTrue(this.commandLine.getArgument(1).isPresent());
        assertEquals("arg2", this.commandLine.getArgument(1).get());
    }

    @Test
    public void testValidate() throws OptionException {
        this.commandLine.addOption("a");

        this.options.validate(this.commandLine);

        assertEquals(2, this.commandLine.getNumberOfOptions());

        this.commandLine.clear();

        // Minimum is 2
        assertFalse(this.options.validate(this.commandLine));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("myGroup", this.options.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("myGroup", this.options.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        this.options.appendUsage(buffer, new HashSet(), null);

        String expected = "myGroup:\n" +
                "  option A <arg>   description A\n" +
                "  option B          description B\n" +
                "  option C          description C\n" +
                "  option D <arg>   description D\n" +
                "  option E          description E\n" +
                "  option F          description F\n" +
                "  arg1\n" +
                "  arg2\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List helpLines = this.options.helpLines(0, new HashSet(), null);

        assertEquals(11, helpLines.size());

        String expected = "myGroup:\n" +
                "  option A <arg>   description A\n" +
                "  option B          description B\n" +
                "  option C          description C\n" +
                "  option D <arg>   description D\n" +
                "  option E          description E\n" +
                "  option F          description F\n" +
                "  arg1\n" +
                "  arg2\n";

        StringUtils.join((List<String>) helpLines, System.lineSeparator()).trim();

        assertEquals(expected, helpLines.toString());
    }

    @Test
    public void testFindOption() {
        Option option = this.options.findOption("a");

        assertNotNull(option);
        assertEquals("option A", option.getLongOpt());
        assertEquals("a", option.getShortOpt());

        option = this.options.findOption("x");

        assertNull(option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, this.options.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, this.options.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(this.options.isRequired());
    }
}