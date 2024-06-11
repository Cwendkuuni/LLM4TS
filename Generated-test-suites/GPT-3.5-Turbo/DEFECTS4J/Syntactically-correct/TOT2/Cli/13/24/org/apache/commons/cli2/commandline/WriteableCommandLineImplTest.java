import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        cmdLine.addOption(new Option("newOption"));
        assertTrue(cmdLine.hasOption(new Option("newOption")));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        cmdLine.addValue(new Option("testOption"), "value1");
        assertEquals("value1", cmdLine.getValues(new Option("testOption"), Arrays.asList("default")).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        cmdLine.addSwitch(new Option("testOption"), true);
        assertTrue(cmdLine.getSwitch(new Option("testOption"), false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        cmdLine.addProperty("key", "value");
        assertEquals("value", cmdLine.getProperty("key", "defaultValue"));
    }

    // Add more test cases for each method in WriteableCommandLineImpl

}