import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addOption(new Option());
        assertTrue(commandLine.getOptions().size() == 1);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addValue(new Argument(), "value");
        assertTrue(commandLine.getValues(new Argument(), new ArrayList()).size() == 1);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addSwitch(new Option(), true);
        assertTrue(commandLine.getSwitch(new Option(), false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addOption(new Option());
        assertTrue(commandLine.hasOption(new Option()));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Argument argument = new Argument();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        List<String> values = commandLine.getValues(argument, defaultValues);
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertFalse(values.contains("default1"));
        assertFalse(values.contains("default2"));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", null));
        assertEquals("default", commandLine.getProperty("nonexistent", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertFalse(commandLine.looksLikeOption("argument"));
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }
}