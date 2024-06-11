import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testCanProcessValidArgument() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();

        assertTrue(group.canProcess(commandLine, "-test"));
    }

    @Test
    public void testCanProcessInvalidArgument() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();

        assertFalse(group.canProcess(commandLine, "--invalid"));
    }

    @Test
    public void testProcessOptionFound() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();

        ListIterator arguments = Arrays.asList("-option").listIterator();

        group.process(commandLine, arguments);

        assertTrue(commandLine.hasOption("-option"));
    }

    @Test
    public void testProcessOptionNotFound() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();

        ListIterator arguments = Arrays.asList("--notop").listIterator();

        group.process(commandLine, arguments);

        assertFalse(commandLine.hasOption("--notop"));
    }

    @Test
    public void testValidateValidOptions() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption("-option");

        assertDoesNotThrow(() -> group.validate(commandLine));
    }

    @Test
    public void testValidateInvalidOptions() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 1, 5, true);
        WriteableCommandLine commandLine = new DefaultCommandLine();

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGetPreferredName() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), "TestGroup", "Test Group Description", 0, 5, true);

        assertEquals("TestGroup", group.getPreferredName());
    }

}