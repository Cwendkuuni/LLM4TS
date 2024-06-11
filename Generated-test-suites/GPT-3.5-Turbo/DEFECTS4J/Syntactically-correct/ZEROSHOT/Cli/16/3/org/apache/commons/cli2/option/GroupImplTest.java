import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setup() {
        List options = new ArrayList();
        options.add(new OptionImpl("option1", "Option 1", false));
        options.add(new OptionImpl("option2", "Option 2", false));

        group = new GroupImpl(options, "group", "Test Group", 1, 2);
    }

    @Test
    public void testCanProcessOptionInGroup() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcessOptionNotInGroup() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertFalse(group.canProcess(commandLine, "option3"));
    }

    @Test
    public void testCanProcessNullArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessAnonymousArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(group.canProcess(commandLine, "argument1"));
    }

    @Test
    public void testCanProcessNamedArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertFalse(group.canProcess(commandLine, "-argument1"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();

        assertEquals(0, prefixes.size());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testProcessOptionInGroup() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.process(commandLine, null);

        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testProcessOptionNotInGroup() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.process(commandLine, null);

        assertFalse(commandLine.hasOption("option3"));
    }

    @Test
    public void testValidateMinimumOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption("option1");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateMissingOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Group", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOptionInGroup() {
        Option option = group.findOption("option1");
        assertNotNull(option);
        assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void testFindOptionNotInGroup() {
        Option option = group.findOption("option3");
        assertNull(option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

}