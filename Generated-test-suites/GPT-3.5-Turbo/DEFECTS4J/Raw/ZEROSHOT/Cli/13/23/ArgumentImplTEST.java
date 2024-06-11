import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", null, 0);
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", null, 0);
        ListIterator arguments = Arrays.asList("value1").listIterator();
        Option option = null;
        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException ex) {
            fail("Exception thrown");
        }
        assertEquals("value1", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", null, 0);
        assertTrue(argument.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", null, 0);
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ',', null, "", null, 0);
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", Arrays.asList("default"), 0);
        assertEquals(Arrays.asList("default"), argument.getDefaultValues());
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", Arrays.asList("default"), 0);
        Option option = null;
        argument.defaultValues(commandLine, option);
        assertEquals(Arrays.asList("default"), commandLine.getValues(argument));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "", null, 0);
        Option option = null;
        try {
            commandLine.addValue(option, "value");
            argument.validate(commandLine, option);
        } catch (OptionException ex) {
            fail("Exception thrown");
        }
    }
}