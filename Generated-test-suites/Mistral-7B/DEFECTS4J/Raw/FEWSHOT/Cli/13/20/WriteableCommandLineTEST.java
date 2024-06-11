package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "help", false, "Print help message.");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionForReadOnlyInterface() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "help", false, "Print help message.");
        try {
            commandLine.setOptions(new ArrayList<>()); // set to read only list
            commandLine.addOption(option);
        } catch (UnsupportedOperationException e) {
            // expected
            throw e;
        }
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "File to read.");
        commandLine.addOption(option);
        commandLine.addValue(option, "input.txt");
        List<Object> values = commandLine.getOptionValue(option);
        assertEquals(1, values.size());
        assertEquals("input.txt", values.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueThrowsExceptionForNonExistentOption() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "File to read.");
        commandLine.addValue(option, "input.txt");
        assertNull(commandLine.getOption(option));
        throw new IllegalArgumentException("Option doesn't exist.");
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "File to read.");
        commandLine.addOption(option);
        commandLine.addValue(option, "input.txt");
        commandLine.addValue(option, "output.txt");
        List<Object> values = commandLine.getOptionValues(option);
        assertEquals(2, values.size());
        assertEquals("input.txt", values.get(0));
        assertEquals("output.txt", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "File to read.");
        List<Object> defaults = new ArrayList<>();
        defaults.add("default1.txt");
        defaults.add("default2.txt");
        commandLine.setDefaultValues(option, defaults);
        List<Object> values = commandLine.getOptionValues(option);
        assertEquals(2, values.size());
        assertEquals("default1.txt", values.get(0));
        assertEquals("default2.txt", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "help", false, "Print help message.");
        commandLine.addOption(option);
        assertFalse(option.getValue().isPresent());
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionForExistingSwitch() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "help", false, "Print help message.");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
        throw new IllegalStateException("Switch already exists.");
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        Option option = new Option("f", "help", false, "Print help message.");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        commandLine.addProperty("test", "testValue");
        assertEquals("testValue", commandLine.getProperty("test"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        assertTrue(commandLine.looksLikeOption("--help"));
        assertTrue(commandLine.looksLikeOption("--foo"));
        assertFalse(commandLine.looksLikeOption("--foo="));
        assertFalse(commandLine.looksLikeOption("--foo="));
        assertFalse(commandLine.looksLikeOption("-f"));
        assertFalse(commandLine.looksLikeOption("--foo -b"));
    }
}

class MockWriteableCommandLine implements WriteableCommandLine {
    private List<Option> options = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public Option getOption(String longOpt) {
        return options.stream()
                .filter(opt -> opt.getLongOpt().equals(longOpt))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getOptionValue(Option option) {
        List<Object> values = getOptionValues(option);
        return values != null && !values.isEmpty() ? values.get(0).toString() : null;
    }

    @Override
    public List<Object> getOptionValues(Option option) {
        List<Object> values = new ArrayList<>();
        if (option.getValue().isPresent()) {
            values.add(option.getValue().get());
        }
        return values;
    }

    @Override
    public void addProperty(String property, String value) {
        properties.put(property, value);
    }

    @Override
    public String getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        List<Object> values = getOptionValues(option);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(value);
        setOptionValues(option, values);
    }

    @Override
    public void setDefaultValues(Option option, List<Object> defaultValues) {
        setOptionValues(option, defaultValues);
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        List<Object> values = getOptionValues(option);
        if (values != null && values.size() > 0) {
            throw new IllegalStateException("Switch already exists.");
        }
        values = new ArrayList<>();
        values.add(value);
        setOptionValues(option, values);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        List<Object> values = getOptionValues(option);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.set(0, defaultSwitch);
        setOptionValues(option, values);
    }

    private void setOptionValues(Option option, List<Object> values) {
        OptionBuilder builder = new OptionBuilder(option);
        if (values != null && !values.isEmpty()) {
            builder.setValue(values.get(0));
        }
        Option newOption = builder.build();
        setOption(option, newOption);
    }

    private void setOption(Option oldOption, Option newOption) {
        // replace old option with new one in options list
        options.set(options.indexOf(oldOption), newOption);
    }
}