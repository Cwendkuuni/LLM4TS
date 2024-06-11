import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertNull(commandLine.getOption("unknown"));

        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertNull(commandLine.getValues(option, null));

        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertEquals(0, commandLine.getUndefaultedValues(option).size());

        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertEquals(null, commandLine.getProperty("property"));

        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertEquals("default", commandLine.getProperty(option, "property", "default"));

        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", "default"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertNotNull(commandLine.getProperties());

        commandLine.addProperty(option, "property", "value");
        assertFalse(commandLine.getProperties().isEmpty());
        assertTrue(commandLine.getProperties().contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        assertFalse(commandLine.looksLikeOption("arg"));
        assertTrue(commandLine.looksLikeOption("-testOption"));
    }
}