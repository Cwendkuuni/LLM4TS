import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertNull(commandLine.getOption("o"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("arg");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        assertEquals("value1", commandLine.getValues(argument, null).get(0));
        assertEquals("value2", commandLine.getValues(argument, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("arg");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        assertEquals("value1", commandLine.getUndefaultedValues(argument).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(argument).get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertNull(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertNull(commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertEquals("default", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertEquals(0, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertEquals("", commandLine.toString());
        commandLine.addValue(new Argument("arg"), "value");
        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", false, "Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultValues(option, new ArrayList<>());
        assertEquals(0, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertEquals(0, commandLine.getNormalised().size());
    }
}