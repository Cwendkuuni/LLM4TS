import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create a new instance of WriteableCommandLineImpl for each test case
        commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        // Test adding an option to the writeable command line
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to the writeable command line
        Option option = new Argument();
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch to the writeable command line
        Option option = new Option("switch");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        // Test getting a property from the writeable command line
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");

        String property = commandLine.getProperty(option, "property", null);

        assertEquals("value", property);
    }

    @Test
    public void testLooksLikeOption() {
        // Test checking if a trigger looks like an option
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        // Test converting the writeable command line to a string
        commandLine.addValue(new Argument(), "arg1");
        commandLine.addValue(new Argument(), "arg2");

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        // Test getting the list of options from the writeable command line
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        // Test getting the set of option triggers from the writeable command line
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Test getting the undefaulted values from the writeable command line
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);

        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        // Test getting a switch from the writeable command line
        Option option = new Option("switch");

        // Test getting switch value when not present
        assertNull(commandLine.getSwitch(option, null));

        // Test getting switch value when present
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        // Test getting a property from the writeable command line
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");

        String property = commandLine.getProperty(option, "property", null);

        assertEquals("value", property);
    }

    @Test
    public void testGetProperties() {
        // Test getting the set of properties from the writeable command line
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);

        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetNormalised() {
        // Test getting the normalised list from the writeable command line
        Option option = new Argument();
        commandLine.addValue(option, "arg1");
        commandLine.addValue(option, "arg2");

        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
    }
}