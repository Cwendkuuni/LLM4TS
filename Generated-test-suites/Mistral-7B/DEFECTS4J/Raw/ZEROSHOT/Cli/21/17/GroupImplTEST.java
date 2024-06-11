import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        List options = new ArrayList();
        options.add(new Option("a", "alpha", true, "an option with trigger a"));
        options.add(new Option("b", "beta", false, "an option with trigger b"));
        options.add(new Option("c", "char", false, "an option with trigger c"));

        List anonymous = new ArrayList();
        anonymous.add(new Option("x", "x-anon", false, "an anonymous option x"));

        group = new GroupImpl(options, "group", "description", 1, 2, false);
        group.setAnonymous(anonymous);

        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        commandLine.parse("-a -b c");

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "x"));
        assertFalse(group.canProcess(commandLine, "z"));
    }

    @Test
    public void testProcess() {
        commandLine.parse("-a -b c");

        group.process(commandLine, commandLine.getArgIterator());

        assertEquals("-a -b c", commandLine.getArguments().get(0));
        assertEquals("an option with trigger a", commandLine.getOption("a").getValue());
        assertEquals("an option with trigger b", commandLine.getOption("b").getValue());
        assertEquals("an option with trigger c", commandLine.getOption("c").getValue());
        assertEquals("an anonymous option x", commandLine.getOption("x").getValue());
    }

    @Test
    public void testValidate() {
        commandLine.parse("-a");

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            //Expected
        }

        commandLine.parse("-a -b");

        group.validate(commandLine);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", group.getDescription());
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertEquals("x-anon", ((Option) anonymous.get(0)).getLongOpt());
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        assertEquals(3, options.size());
        assertEquals("alpha", ((Option) options.get(0)).getLongOpt());
        assertEquals("beta", ((Option) options.get(1)).getLongOpt());
        assertEquals("char", ((Option) options.get(2)).getLongOpt());
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("beta");
        assertNotNull(option);
        assertEquals("beta", option.getLongOpt());
    }
}