import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

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
    public void setup() {
        Option option = new Option("testOption");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument("testArgument");
        Object value = "testValue";
        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(switchValue, Boolean.TRUE);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Option returnedOption = commandLine.getOption("testOption");
        assertEquals(returnedOption, option);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("testArgument");
        Object value = "testValue";
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, defaultValues);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        boolean defaultValue = true;
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);

        assertEquals(switchValue, defaultValue);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);

        String propertyValue = commandLine.getProperty(property, null);
        assertEquals(propertyValue, value);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        String propertyValue = commandLine.getProperty(property, defaultValue);

        assertEquals(propertyValue, defaultValue);
    }

    @Test
    public void testGetProperties() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);

        Set properties = commandLine.getProperties();
        assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        boolean looksLikeOption = commandLine.looksLikeOption(trigger);

        assertFalse(looksLikeOption);
    }

    @Test
    public void testToString() {
        String expected = "testArgument";
        Option option = new Argument("testArgument");
        List arguments = new ArrayList();
        arguments.add(expected);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        String commandLineString = commandLine.toString();
        assertEquals(commandLineString, expected);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Set triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("testOption"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument("testArgument");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);

        List defaultValues = commandLine.getValues(option, null);
        assertTrue(defaultValues.contains("defaultValue"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(switchValue, defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption");
        List arguments = new ArrayList();
        arguments.add("testArgument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        List normalised = commandLine.getNormalised();
        assertTrue(normalised.contains("testArgument"));
    }
}