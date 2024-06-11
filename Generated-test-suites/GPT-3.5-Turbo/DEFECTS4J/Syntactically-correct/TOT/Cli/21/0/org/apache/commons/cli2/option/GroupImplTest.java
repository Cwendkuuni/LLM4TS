import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> options = Arrays.asList(new OptionImpl("name", "description", true));
        group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(group.canProcess(commandLine, "name"));
        assertFalse(group.canProcess(commandLine, "age"));
        assertFalse(group.canProcess(commandLine, null));

        commandLine.addOption(new OptionImpl("name", "description", false));
        assertFalse(group.canProcess(commandLine, "name"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.singleton("name"), group.getTriggers());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        group.process(commandLine, "name");
        assertTrue(commandLine.hasOption("name"));
    }

    @Test(expected = OptionException.class)
    public void testValidateMissingOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        group.validate(commandLine);
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(new OptionImpl("name", "description", false));
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("groupDescription", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> expected = Arrays.asList(new OptionImpl("name", "description", true));
        assertEquals(expected, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("age"));
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
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        group.defaults(commandLine);
        assertTrue(commandLine.hasOption("name"));
    }
}