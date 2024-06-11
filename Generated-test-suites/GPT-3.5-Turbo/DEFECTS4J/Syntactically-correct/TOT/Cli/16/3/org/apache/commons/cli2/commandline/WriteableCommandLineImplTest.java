import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option testOption = new Option("t", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(testOption, new ArrayList<String>());
        commandLine.addOption(testOption);

        assertTrue(commandLine.hasOption(testOption));
    }

    @Test
    public void testAddValue() {
        Argument testArgument = new Argument("arg", false, "Test argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(testArgument, new ArrayList<String>());
        commandLine.addValue(testArgument, "value");

        List<String> values = commandLine.getValues(testArgument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option testOption = new Option("t", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(testOption, new ArrayList<String>());
        commandLine.addSwitch(testOption, true);

        assertTrue(commandLine.getSwitch(testOption, false));
    }

    @Test
    public void testHasOption() {
        Option testOption = new Option("t", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(testOption, new ArrayList<String>());
        commandLine.addOption(testOption);

        assertTrue(commandLine.hasOption(testOption));
    }

    @Test
    public void testGetOption() {
        Option testOption = new Option("t", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(testOption, new ArrayList<String>());
        commandLine.addOption(testOption);

        assertEquals(testOption, commandLine.getOption("t"));
        assertEquals(testOption, commandLine.getOption("test"));
    }

}