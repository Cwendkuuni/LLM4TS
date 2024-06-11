
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        commandLine.addOption(option);
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertNotNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        commandLine.addOption(option);
        Object value = new Object();
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        commandLine.addOption(option);
        Object value = new Object();
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new PropertyOption();
        commandLine.addProperty(option, "test", "value");
        assertEquals("value", commandLine.getProperty(option, "test", null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new PropertyOption();
        commandLine.addProperty(option, "test", "value");
        assertTrue(commandLine.getProperties(option).contains("test"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new PropertyOption();
        commandLine.addProperty(option, "test", "value");
        assertTrue(commandLine.getProperties().contains("test"));
    }
}

