import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);

        Option option = new Option("test", "Test Option");
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption(option));
        assertEquals(option, cmdLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);

        Option option = new Option("test", "Test Option");
        Object value = "value";

        cmdLine.addValue(option, value);

        assertEquals(value, cmdLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);

        Option option = new Option("test", "Test Option");

        cmdLine.addSwitch(option, true);

        assertTrue(cmdLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);

        Option option = new Option("test", "Test Option");
        String property = "key";
        String value = "value";

        cmdLine.addProperty(option, property, value);

        assertEquals(value, cmdLine.getProperty(option, property, null));
    }
}