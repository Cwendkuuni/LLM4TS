import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        cmd.addValue(option, "value1");
        assertEquals("value1", cmd.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        cmd.addProperty(option, "property1", "value1");
        assertEquals("value1", cmd.getProperty(option, "property1", ""));
    }

    // Add more test cases for other methods as needed

}