
package org.apache.commons.cli2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();
        Object value = new Object();

        commandLine.addValue(option, value);

        assertTrue(commandLine.hasValue(option, value));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();
        List<Object> values = Arrays.asList(new Object(), new Object());

        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));

        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();
        List<Object> defaultValues = Arrays.asList(new Object(), new Object());

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsExceptionWhenSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = createOption();
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createCommandLine();
        String property = "key";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createCommandLine();
        String argument = "-option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    private WriteableCommandLine createCommandLine() {
        return new WriteableCommandLineImpl();
    }

    private Option createOption() {
        return new OptionImpl("option", true);
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<Object> defaultValues = new ArrayList<>();
    private List<Boolean> switches = new ArrayList<>();
    private List<Boolean> defaultSwitches = new ArrayList<>();
    private List<String> properties = new ArrayList<>();

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        values.add(value);
    }

    @Override
    public List<Object> getValues(Option option) {
        List<Object> optionValues = new ArrayList<>();
        for (Object value : values) {
            if (value.equals(option)) {
                optionValues.add(value);
            }
        }
        return optionValues;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        this.defaultValues = defaultValues;
    }

    @Override
    public List getDefaultValues(Option option) {
        return defaultValues;
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switches.add(value);
    }

    @Override
    public boolean getSwitch(Option option) {
        return switches.get(0);
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
        properties.add(value);
    }

    @Override
    public String getProperty(String property) {
        for (String prop : properties) {
            if (prop.equals(property)) {
                return prop;
            }
        }
        return null;
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    // Other methods from CommandLine interface
    // Implement them accordingly
}

class OptionImpl implements Option {
    private String name;
    private boolean hasArg;

    public OptionImpl(String name, boolean hasArg) {
        this.name = name;
        this.hasArg = hasArg;
    }

    // Implement other methods from Option interface
}
