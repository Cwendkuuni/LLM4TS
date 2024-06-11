
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root Option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "Test Option");
        String value = "value";
        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test", "Test Option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--option";
        boolean result = commandLine.looksLikeOption(trigger);

        assertTrue(result);
    }

    @Test
    public void testToString() {
        String argument1 = "argument1";
        String argument2 = "argument2";

        commandLine.addArgument(argument1);
        commandLine.addArgument(argument2);

        String expected = argument1 + " " + argument2;
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "Test Option");
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        List result = commandLine.getValues(option, null);
        assertEquals(defaults, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "Test Option");
        boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        String argument1 = "argument1";
        String argument2 = "argument2";

        commandLine.addArgument(argument1);
        commandLine.addArgument(argument2);

        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains(argument1));
        assertTrue(normalised.contains(argument2));
    }
}
