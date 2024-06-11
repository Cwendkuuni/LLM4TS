import org.apache.commons.cli2.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));

        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getBooleanSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_duplicateSwitchValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));

        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options = new ArrayList<>();
    private List<Option> optionValues = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<Object> defaultValues = new ArrayList<>();
    private List<Boolean> switches = new ArrayList<>();
    private List<Boolean> defaultSwitches = new ArrayList<>();
    private List<String> properties = new ArrayList<>();
    private List<String> propertyValues = new ArrayList<>();

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        optionValues.add(option);
        values.add(value);
    }

    @Override
    public List<Object> getValues(Option option) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < optionValues.size(); i++) {
            if (optionValues.get(i).equals(option)) {
                result.add(values.get(i));
            }
        }
        return result;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        this.defaultValues.add(defaultValues);
    }

    @Override
    public List<Object> getDefaultValues(Option option) {
        return defaultValues;
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        if (switches.contains(option)) {
            throw new IllegalStateException("Switch already added");
        } else {
            switches.add(value);
        }
    }

    @Override
    public boolean getBooleanSwitch(Option option) {
        for (int i = 0; i < optionValues.size(); i++) {
            if (optionValues.get(i).equals(option)) {
                return switches.get(i);
            }
        }
        return false;
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        defaultSwitches.add(defaultSwitch);
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        return defaultSwitches.get(0);
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(property);
        propertyValues.add(value);
    }

    @Override
    public String getProperty(String property) {
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals(property)) {
                return propertyValues.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }
}