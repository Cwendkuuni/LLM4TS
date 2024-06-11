import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Argument argument1 = new Argument("argument1");
        Argument argument2 = new Argument("argument2");

        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);

        commandLine = new WriteableCommandLineImpl(null, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("option3");
        commandLine.addOption(option3);
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("option1");
        commandLine.addValue(option1, "value1");
        assertTrue(commandLine.getValues(option1, null).contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1");
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1");
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("option1");
        assertNotNull(option);
        assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1");
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getValues(option1, null);
        assertTrue(values.contains("value1"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option("option1");
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getUndefaultedValues(option1);
        assertTrue(values.contains("value1"));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("option1");
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property1");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        String propertyValue = commandLine.getProperty("property1");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String defaultValue = "default";
        String propertyValue = commandLine.getProperty("property1", defaultValue);
        assertEquals(defaultValue, propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        assertTrue(commandLine.getProperties().contains("property1"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1");
        commandLine.addValue(option1, "value1");
        Argument argument1 = new Argument("argument1");
        commandLine.addValue(argument1, "value2");
        Argument argument2 = new Argument("argument2");
        commandLine.addValue(argument2, "value3");
        assertEquals("value1 value2 value3", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1");
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        commandLine.setDefaultValues(option1, defaults);
        List<String> values = commandLine.getValues(option1, null);
        assertTrue(values.contains("default1"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("option1");
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        Argument argument1 = new Argument("argument1");
        commandLine.addValue(argument1, "value1");
        Argument argument2 = new Argument("argument2");
        commandLine.addValue(argument2, "value2");
        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
    }
}