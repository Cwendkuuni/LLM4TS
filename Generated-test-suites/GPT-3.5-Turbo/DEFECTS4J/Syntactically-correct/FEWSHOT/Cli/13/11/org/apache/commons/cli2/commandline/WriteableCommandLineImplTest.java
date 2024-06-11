import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl(null, null);
    }

    @Test
    public void testAddOption() {
        Option option = new Option();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option();
        Object value = new Object();
        commandLine.addValue(option, value);

        List valueList = commandLine.getValues(option, null);
        assertEquals(1, valueList.size());
        assertTrue(valueList.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, bool);
    }

    @Test
    public void testHasOption() {
        Option option = new Option();
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option();
        commandLine.addOption(option);

        Option foundOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, foundOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        List<Object> commandLineValues = new ArrayList<>();
        commandLineValues.add("value");

        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getValues(option, defaultValues);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option();
        Boolean defaultValue = false;

        commandLine.addSwitch(option, true);

        Boolean bool = commandLine.getSwitch(option, defaultValue);
        assertEquals(Boolean.TRUE, bool);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        String retrievedValue = commandLine.getProperty(property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-option";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        commandLine.addValue(null, "value1");
        commandLine.addValue(null, "value2");

        String expected = "value1 value2";
        String output = commandLine.toString();

        assertEquals(expected, output);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option();
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option();
        commandLine.addOption(option);

        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        List<Object> defaults = new ArrayList<>();
        defaults.add("default");

        commandLine.setDefaultValues(option, defaults);

        List<Object> retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(1, retrievedDefaults.size());
        assertTrue(retrievedDefaults.contains("default"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        Boolean defaultValue = true;

        commandLine.setDefaultSwitch(option, defaultValue);

        Boolean retrievedDefaultValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, retrievedDefaultValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");

        commandLine = new WriteableCommandLineImpl(null, normalised);

        List<String> retrievedNormalised = commandLine.getNormalised();
        assertEquals(2, retrievedNormalised.size());
        assertTrue(retrievedNormalised.contains("arg1"));
        assertTrue(retrievedNormalised.contains("arg2"));
    }
}