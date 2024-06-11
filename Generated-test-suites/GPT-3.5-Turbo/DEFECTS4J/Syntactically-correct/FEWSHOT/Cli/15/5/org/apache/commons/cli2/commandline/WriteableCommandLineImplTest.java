import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option = new Option("f", "file", true, "File option");
        commandLine = new WriteableCommandLineImpl(option, null);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        assertNull(commandLine.getOption("verbose"));
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("verbose"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("v", "verbose", true, "Verbose option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("v", "verbose", true, "Verbose option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        assertFalse(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        assertEquals("", commandLine.getProperty("property"));
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("default", commandLine.getProperty("property", "default"));
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        assertEquals(0, commandLine.getProperties().size());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("argument"));
        assertTrue(commandLine.looksLikeOption("-opt"));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "file", true, "File option");
        commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals("", commandLine.toString());

        Option option1 = new Option("v", "verbose", false, "Verbose option");
        List<String> arguments = new ArrayList<>();
        arguments.add("argument1");
        arguments.add("argument2");
        commandLine = new WriteableCommandLineImpl(option1, arguments);
        assertEquals("argument1 argument2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("v", "verbose", false, "Verbose option");
        Option option2 = new Option("f", "file", true, "File option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("v", "verbose", false, "Verbose option");
        Option option2 = new Option("f", "file", true, "File option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("v", "verbose", true, "Verbose option");
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        List<String> arguments = new ArrayList<>();
        arguments.add("-v");
        commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals(1, commandLine.getNormalised().size());
    }
}