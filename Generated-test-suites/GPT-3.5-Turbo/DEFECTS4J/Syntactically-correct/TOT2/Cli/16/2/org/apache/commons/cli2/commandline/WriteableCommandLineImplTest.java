import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl("-f", "File option"), Arrays.asList("file.txt"));
        Option option = new OptionImpl("-v", "Verbose option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl("-f", "File option"), Arrays.asList("file.txt"));
        Option option = new OptionImpl("-n", "Number option");
        commandLine.addValue(option, 10);
        List<Integer> values = commandLine.getValues(option, null);
        assertTrue(values.contains(10));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl("-f", "File option"), Arrays.asList("file.txt"));
        Option option = new OptionImpl("-v", "Verbose option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl("-f", "File option"), Arrays.asList("file.txt"));
        commandLine.addProperty("output", "result.txt");
        assertEquals("result.txt", commandLine.getProperty("output"));
    }

    @Test
    public void testOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl("-f", "File option"), Arrays.asList("file.txt"));
        Option option = new OptionImpl("-v", "Verbose option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains("-v"));
    }
}