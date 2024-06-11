import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(argument);
        commandLine.addValue(argument, "value");
        assertTrue(commandLine.getValues(argument, null).contains("value"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("-test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("test value", commandLine.toString());
    }
}