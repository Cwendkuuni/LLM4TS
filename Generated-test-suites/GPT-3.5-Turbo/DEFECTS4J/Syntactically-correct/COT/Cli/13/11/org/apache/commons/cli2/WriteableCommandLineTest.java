
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value");

        List<String> values = cmdLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.setDefaultValues(option, Arrays.asList("default"));

        List<String> values = cmdLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);

        assertTrue(cmdLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmdLine.setDefaultSwitch(option, true);

        assertTrue(cmdLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        cmdLine.addProperty("key", "value");

        assertEquals("value", cmdLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();

        assertFalse(cmdLine.looksLikeOption("test argument"));
        assertTrue(cmdLine.looksLikeOption("-option"));
    }
}

