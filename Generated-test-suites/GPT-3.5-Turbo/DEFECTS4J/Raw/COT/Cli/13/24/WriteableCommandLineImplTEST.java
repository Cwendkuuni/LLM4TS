import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value");
        assertEquals(commandLine.getValues(option, null).get(0), "value");
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertEquals(commandLine.getSwitch(option, null), true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertEquals(commandLine.getOption("test"), option);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value");
        assertEquals(commandLine.getValues(option, null).get(0), "value");
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertEquals(commandLine.getSwitch(option, null), true);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");
        assertEquals(commandLine.getProperty("property", null), "value");
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("-test"));
    }
}