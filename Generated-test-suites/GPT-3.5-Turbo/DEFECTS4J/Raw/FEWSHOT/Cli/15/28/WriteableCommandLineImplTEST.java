package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("f", "file", true, "File");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new Argument(true, "Argument");

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument(true, "Argument");
        Object value = "value";

        assertNull(commandLine.getValues(option, null));
        
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("h", "help", false, "Help");
        boolean value = true;

        assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, value);

        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("f", "file", true, "File");

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "file", true, "File");

        assertNull(commandLine.getOption("f"));

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("f"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument(true, "Argument");
        List<String> defaultValues = Arrays.asList("default1", "default2");

        assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
        
        Object value = "value";
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValues(option, defaultValues).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument(true, "Argument");

        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option));
        
        Object value = "value";
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("h", "help", false, "Help");
        Boolean defaultValue = true;

        assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));

        commandLine.addSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";

        assertNull(commandLine.getProperty(property));

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        assertNull(commandLine.getProperty(property));

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "property";
        String defaultValue = "defaultValue";

        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));

        commandLine.addProperty(property, "value");

        assertEquals("value", commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";

        assertEquals(Collections.emptySet(), commandLine.getProperties(option));

        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    @Test
    public void testGetPropertiesWithDefault() {
        Option option = new Option("p", "property", true, "Property");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        Set<String> defaultProperties = new HashSet<>(Arrays.asList(property1, property2));

        assertEquals(defaultProperties, commandLine.getProperties(option));

        commandLine.addProperty(option, property1, value1);

        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-f";

        assertTrue(commandLine.looksLikeOption(trigger));

        trigger = "--file";

        assertTrue(commandLine.looksLikeOption(trigger));

        trigger = "argument";

        assertFalse(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Option option = new Argument(true, "Argument");
        List<String> arguments = Arrays.asList("value1", "value2");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "File");
        Option option2 = new Option("h", "help", false, "Help");

        assertEquals(Collections.emptyList(), commandLine.getOptions());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("f", "file", true, "File");
        Option option2 = new Option("h", "help", false, "Help");

        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("f"));
        assertTrue(optionTriggers.contains("file"));
        assertTrue(optionTriggers.contains("h"));
        assertTrue(optionTriggers.contains("help"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument(true, "Argument");
        List<String> defaults = Arrays.asList("default1", "default2");

        assertNull(commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("h", "help", false, "Help");
        Boolean defaultSwitch = true;

        assertNull(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Argument(true, "Argument");
        List<String> arguments = Arrays.asList("value1", "value2");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}