import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertEquals("value", cmd.getValue("test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        cmd.addOption(option);
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        assertEquals(values, cmd.getValues("test"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmd.getValues("test"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "testing");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch("test"));
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
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        // Implement interface methods for testing purposes
    }

}