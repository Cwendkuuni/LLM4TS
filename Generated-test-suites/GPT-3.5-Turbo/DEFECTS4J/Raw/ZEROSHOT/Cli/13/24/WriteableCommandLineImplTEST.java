import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());

        commandLine.addValue(argument, "value");

        assertEquals("value", commandLine.getValues(argument, new ArrayList<>()).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        assertNull(commandLine.getOption("test2"));

        commandLine.addOption(option2);

        assertEquals(option2, commandLine.getOption("test2"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(values, commandLine.getValues(option, new ArrayList<>()));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertFalse(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.getNormalised().add("value1");
        commandLine.getNormalised().add("value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("test1"));
        assertTrue(commandLine.getOptionTriggers().contains("test2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, new ArrayList<>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.getNormalised().add("value1");
        commandLine.getNormalised().add("value2");

        assertEquals(2, commandLine.getNormalised().size());
        assertEquals("value1", commandLine.getNormalised().get(0));
        assertEquals("value2", commandLine.getNormalised().get(1));
    }
}