import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.OptionException;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        group = new GroupImpl(new ArrayList(), null, null, 0, 0, false);
    }

    @Test
    public void testCanProcessWithNullArg() {
        CommandLine commandLine = null;
        String arg = null;
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testCanProcessWithOptionArg() {
        CommandLine commandLine = null;
        String arg = "-a";
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testCanProcessWithAnonymousArg() {
        CommandLine commandLine = null;
        String arg = "arg";
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(group.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers() {
        assertTrue(group.getTriggers().isEmpty());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        group.process(commandLine, arguments);
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = null;
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(null, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        group.appendUsage(buffer, helpSettings, comp);
        assertEquals("[]", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = null;
        assertTrue(group.helpLines(depth, helpSettings, comp).isEmpty());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        group.defaults(commandLine);
    }

    @Test
    public void testFindOption() {
        String trigger = null;
        assertEquals(null, group.findOption(trigger));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testMemberOptions() {
        assertTrue(group.getOptions().isEmpty());
    }

    @Test
    public void testAnonymousArguments() {
        assertTrue(group.getAnonymous().isEmpty());
    }
}