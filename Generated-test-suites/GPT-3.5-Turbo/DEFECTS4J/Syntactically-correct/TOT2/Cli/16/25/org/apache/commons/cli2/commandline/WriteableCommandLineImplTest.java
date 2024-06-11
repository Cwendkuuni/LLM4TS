import static org.junit.Assert.*;
import org.junit.Test;

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
        Option option = new Argument("arg1");
        cmd.addValue(option, "value1");
        assertEquals(cmd.getValues(option, null).get(0), "value1");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("testSwitch");
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new PropertyOption("testProperty");
        cmd.addProperty(option, "key", "value");
        assertEquals(cmd.getProperty(option, "key", ""), "value");
    }

    // Add more test cases for other methods as needed

}