import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        assertEquals(1, cmdLine.getOptions().size());
        assertEquals("testOption", cmdLine.getOption("testOption").getPreferredName());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, Arrays.asList("defaultValue"));
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmdLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, cmdLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        cmdLine.addProperty("property1", "value1");
        assertEquals("value1", cmdLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("testOption"), Arrays.asList("arg1", "arg2"));
        assertTrue(cmdLine.looksLikeOption("-t"));
    }

}