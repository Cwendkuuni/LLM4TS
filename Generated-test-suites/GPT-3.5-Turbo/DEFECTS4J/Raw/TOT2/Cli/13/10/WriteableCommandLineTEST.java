import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertEquals("value", cmd.getValue("test"));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        List values = cmd.getArgumentValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.setDefaultValues(option, List.of("default"));
        assertEquals("default", cmd.getValue("test"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch("test"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        cmd.addProperty("test", "value");
        assertEquals("value", cmd.getProperty("test"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-test"));
    }
}