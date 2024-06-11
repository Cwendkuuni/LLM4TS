import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertEquals("value", cmd.getValue("test"));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        cmd.addValue(option, "value3");

        List<String> values = cmd.getArgumentValues(option);
        assertEquals(3, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertTrue(values.contains("value3"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        List<String> defaultValues = List.of("default1", "default2");
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmd.getArgumentValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertEquals(true, cmd.getSwitch("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, false);
        assertEquals(false, cmd.getSwitch("test"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        cmd.addProperty("key", "value");
        assertEquals("value", cmd.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-test"));
        assertFalse(cmd.looksLikeOption("test"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    // Implement methods of the WriteableCommandLine interface
}