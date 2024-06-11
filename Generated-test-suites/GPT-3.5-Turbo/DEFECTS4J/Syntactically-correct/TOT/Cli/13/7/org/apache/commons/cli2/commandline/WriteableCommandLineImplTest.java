import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testOptionTrigger() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("t"));
        assertFalse(commandLine.looksLikeOption("x"));
    }

    @Test
    public void testOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new OptionImpl("test", "t", "Test option");
        Option option2 = new OptionImpl("example", "e", "Example option");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains("t"));
        assertTrue(commandLine.getOptionTriggers().contains("e"));
        assertFalse(commandLine.getOptionTriggers().contains("x"));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("t"));
        assertNull(commandLine.getOption("x"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

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
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");

        assertTrue(commandLine.getProperties().contains("key1"));
        assertTrue(commandLine.getProperties().contains("key2"));
        assertFalse(commandLine.getProperties().contains("key3"));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");

        assertEquals(normalised, commandLine.getNormalised());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");

        String expected = "arg1 arg2";

        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new OptionImpl("test", "t", "Test option");
        Option option2 = new OptionImpl("example", "e", "Example option");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
        assertFalse(commandLine.getOptions().contains(null));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test", "t", "Test option");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }
}