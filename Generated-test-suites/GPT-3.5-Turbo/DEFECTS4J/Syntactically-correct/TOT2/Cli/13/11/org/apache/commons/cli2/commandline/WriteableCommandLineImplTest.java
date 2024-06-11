import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option();
        List arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    // Add more test cases for other methods...

}