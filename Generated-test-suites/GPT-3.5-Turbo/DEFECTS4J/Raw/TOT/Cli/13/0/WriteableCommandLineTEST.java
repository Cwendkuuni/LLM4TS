import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setup() {
        writeableCommandLine = new WriteableCommandLineImpl();
        option = new Option("test");
    }

    @Test
    public void testAddOption() {
        assertEquals(0, writeableCommandLine.getOptions().size());

        writeableCommandLine.addOption(option);

        assertEquals(1, writeableCommandLine.getOptions().size());
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        assertEquals(0, writeableCommandLine.getOptionValue(option).size());

        writeableCommandLine.addValue(option, "value1");

        assertEquals(1, writeableCommandLine.getOptionValue(option).size());
        assertTrue(writeableCommandLine.getOptionValue(option).contains("value1"));
    }

    @Test
    public void testGetArgumentValues() {
        assertEquals(0, writeableCommandLine.getArgumentValues(option).size());

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        assertEquals(2, writeableCommandLine.getArgumentValues(option).size());
        assertTrue(writeableCommandLine.getArgumentValues(option).contains("value1"));
        assertTrue(writeableCommandLine.getArgumentValues(option).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        assertEquals(0, writeableCommandLine.getDefaultValues(option).size());

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        assertEquals(2, writeableCommandLine.getDefaultValues(option).size());
        assertTrue(writeableCommandLine.getDefaultValues(option).contains("default1"));
        assertTrue(writeableCommandLine.getDefaultValues(option).contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(writeableCommandLine.hasSwitch(option));

        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.hasSwitch(option));
        assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        assertFalse(writeableCommandLine.hasDefaultSwitch(option));

        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.hasDefaultSwitch(option));
        assertTrue(writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        assertEquals(0, writeableCommandLine.getProperties().size());

        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        assertEquals(2, writeableCommandLine.getProperties().size());
        assertTrue(writeableCommandLine.getProperties().containsKey("property1"));
        assertTrue(writeableCommandLine.getProperties().containsKey("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertTrue(writeableCommandLine.looksLikeOption("-t"));
        assertTrue(writeableCommandLine.looksLikeOption("--t"));
        assertTrue(writeableCommandLine.looksLikeOption("-t2"));
        assertTrue(writeableCommandLine.looksLikeOption("--t2"));

        assertFalse(writeableCommandLine.looksLikeOption("test"));
        assertFalse(writeableCommandLine.looksLikeOption("t"));
        assertFalse(writeableCommandLine.looksLikeOption("t2"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    
    private List<Option> options;
    private List<Object> optionValues;
    private List<Object> argumentValues;
    private List<List<String>> defaultValues;
    private List<Boolean> switches;
    private List<Boolean> defaultSwitches;
    private List<String> properties;
    private List<String> values;

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
        optionValues = new ArrayList<>();
        argumentValues = new ArrayList<>();
        defaultValues = new ArrayList<>();
        switches = new ArrayList<>();
        defaultSwitches = new ArrayList<>();
        properties = new ArrayList<>();
        values = new ArrayList<>();
    }

    @Override
    public void addOption(final Option option) {
        options.add(option);
    }

    @Override
    public void addValue(final Option option, final Object value) {
        optionValues.add(value);
    }

    @Override
    public List<Object> getOptionValue(final Option option) {
        return optionValues;
    }

    @Override
    public List<Object> getArgumentValues(final Option option) {
        return argumentValues;
    }

    @Override
    public void setDefaultValues(final Option option, final List defaultValues) {
        this.defaultValues.add(defaultValues);
    }

    @Override
    public List<List<String>> getDefaultValues(final Option option) {
        return defaultValues;
    }

    @Override
    public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
        switches.add(value);
    }

    @Override
    public boolean hasSwitch(final Option option) {
        return !switches.isEmpty();
    }

    @Override
    public boolean getSwitch(final Option option) {
        return switches.get(0);
    }

    @Override
    public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
        defaultSwitches.add(defaultSwitch);
    }

    @Override
    public boolean hasDefaultSwitch(final Option option) {
        return !defaultSwitches.isEmpty();
    }

    @Override
    public boolean getDefaultSwitch(final Option option) {
        return defaultSwitches.get(0);
    }

    @Override
    public void addProperty(final String property, final String value) {
        properties.add(value);
    }

    @Override
    public List<String> getProperties() {
        return properties;
    }

    @Override
    public boolean looksLikeOption(final String argument) {
        return argument.startsWith("-");
    }
}