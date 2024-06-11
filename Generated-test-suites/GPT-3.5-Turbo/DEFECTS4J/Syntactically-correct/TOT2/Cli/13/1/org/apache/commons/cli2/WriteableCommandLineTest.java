import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertTrue(cmd.getValues(option).contains("value"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        List<String> defaultValues = List.of("default");
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitchValues().containsKey(option));
        assertTrue(cmd.getSwitchValues().get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmd.setDefaultSwitch(option, false);
        assertEquals(false, cmd.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        cmd.addProperty("property", "value");
        assertEquals("value", cmd.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        // Implement methods for testing purposes
    }
}