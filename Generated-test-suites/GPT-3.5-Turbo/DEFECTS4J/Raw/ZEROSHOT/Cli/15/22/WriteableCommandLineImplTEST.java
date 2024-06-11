import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        Object value = new Object();
        commandLine.addValue(option, value);

        assertTrue(commandLine.hasOption(option));
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        List<Object> values = new ArrayList<>();
        values.add(new Object());
        commandLine.addValue(option, values.get(0));

        assertTrue(commandLine.hasOption(option));
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        List<Object> values = new ArrayList<>();
        values.add(new Object());
        commandLine.addValue(option, values.get(0));

        assertTrue(commandLine.hasOption(option));
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        Boolean defaultValue = null;
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String property = "key";
        String defaultValue = "defaultValue";

        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetPropertiesWithoutOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);

        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String trigger = "-option";
        commandLine.getPrefixes().add("-");

        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String argument = "argument";
        commandLine.getNormalised().add(argument);

        assertEquals(argument, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        List<Object> defaults = new ArrayList<>();
        defaults.add(new Object());
        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultValuesWithNull() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        List<Object> defaults = null;
        commandLine.setDefaultValues(option, defaults);

        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testSetDefaultSwitchWithNull() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        Boolean defaultSwitch = null;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String argument = "argument";
        commandLine.getNormalised().add(argument);

        assertTrue(commandLine.getNormalised().contains(argument));
    }
}