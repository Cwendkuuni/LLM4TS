import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(argument, Arrays.asList("arg1", "arg2"));
        String value = "test value";
        cmdLine.addValue(argument, value);
        assertEquals(value, cmdLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmdLine.addSwitch(option, true);
        assertEquals(true, cmdLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        String property = "key";
        String value = "test value";
        cmdLine.addProperty(option, property, value);
        assertEquals(value, cmdLine.getProperty(option, property, null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        assertTrue(cmdLine.looksLikeOption("-option"));
    }

    // Add more test cases for other methods as necessary

}