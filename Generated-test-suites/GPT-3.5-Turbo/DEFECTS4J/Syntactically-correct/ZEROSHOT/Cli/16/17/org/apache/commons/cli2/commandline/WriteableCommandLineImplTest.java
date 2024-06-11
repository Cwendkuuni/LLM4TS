import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root Option");
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "Test Option");
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option, null).contains("value"));
        assertFalse(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "Test Option");
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test", "Test Option");
        assertTrue(commandLine.getValues(option, null).isEmpty());
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = new ArrayList<>();
        values.add("value");
        commandLine.addValue(option, "value");
        values.add("default");
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "Test Option");
        assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
        commandLine.addValue(option, "value");
        List<String> values = new ArrayList<>();
        values.add("value");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "Test Option");
        assertTrue(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        assertNull(commandLine.getProperty(property));
        Option option = new Option("test", "Test Option");
        commandLine.addProperty(option, property, "value");
        assertEquals("value", commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetProperties() {
        assertTrue(commandLine.getProperties().isEmpty());
        Option option = new Option("test", "Test Option");
        String property = "testProperty";
        commandLine.addProperty(option, property, "value");
        assertEquals(1, commandLine.getProperties(option).size());
        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-test";
        assertFalse(commandLine.looksLikeOption(trigger));
        Option option = new Option(trigger, "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
        commandLine.addValue(new Option("test", "Test Option"), "value");
        assertEquals("value", commandLine.toString());
        commandLine.addValue(new Option("test", "Test Option"), "value 2");
        assertEquals("value \"value 2\"", commandLine.toString());
    }

    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
        Option option = new Option("test", "Test Option");
        commandLine.addValue(option, "value");
        List<String> normalised = new ArrayList<>();
        normalised.add("value");
        assertEquals(normalised, commandLine.getNormalised());
    }
}