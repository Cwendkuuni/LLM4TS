import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private GroupImpl group;
    private OptionImpl option1;
    private OptionImpl option2;

    @Before
    public void setUp() {
        option1 = new OptionImpl("o1");
        option2 = new OptionImpl("o2");
        List<OptionImpl> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, "group", "group description", 1, 1);
    }

    @Test
    public void testCanProcess_WithValidOption_ReturnsTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test
    public void testCanProcess_WithOptionNotInGroup_ReturnsFalse() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        assertFalse(group.canProcess(commandLine, "o2"));
    }

    @Test
    public void testCanProcess_WithOptionInGroup_ReturnsTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        assertTrue(group.canProcess(commandLine, "group:o1"));
    }

    @Test
    public void testGetTriggers_ReturnsCorrectTriggers() {
        assertEquals(2, group.getTriggers().size());
        assertTrue(group.getTriggers().contains("o1"));
        assertTrue(group.getTriggers().contains("o2"));
    }

    @Test
    public void testGetOptions_ReturnsCorrectOptions() {
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testProcess_WithValidOption_ProcessesOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("o1");

        group.process(commandLine, arguments);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testProcess_WithInvalidOption_DoesNotProcessOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("o3");

        group.process(commandLine, arguments);

        assertFalse(commandLine.hasOption(option1));
    }

    @Test
    public void testValidate_WithValidOptions_DoesNotThrowOptionException() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithInvalidOptions_ThrowsOptionException() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName_ReturnsName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription_ReturnsDescription() {
        assertEquals("group description", group.getDescription());
    }

    @Test
    public void testGetMinimum_ReturnsMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum_ReturnsMaximum() {
        assertEquals(1, group.getMaximum());
    }

}