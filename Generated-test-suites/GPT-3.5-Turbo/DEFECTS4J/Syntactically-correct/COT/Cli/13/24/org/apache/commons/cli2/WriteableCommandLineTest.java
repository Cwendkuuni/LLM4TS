
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List<Object> values = writeableCommandLine.getValues(option);
        assertNotNull(values);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<Object> defaultValues = List.of("default1", "default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertEquals(value, writeableCommandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertFalse(writeableCommandLine.looksLikeOption("test"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options;
    private List<List<Object>> values;
    private List<List<Object>> defaultValues;
    private List<Boolean> switchValues;
    private List<Boolean> defaultSwitches;
    private List<String> properties;

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        int index = options.indexOf(option);
        values.get(index).add(value);
    }

    @Override
    public List<Object> getValues(Option option) {
        int index = options.indexOf(option);
        return values.get(index);
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        int index = options.indexOf(option);
        this.defaultValues.set(index, defaultValues);
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        int index = options.indexOf(option);
        if (switchValues.get(index) != null) {
            throw new IllegalStateException("Switch already added");
        }
        switchValues.set(index, value);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        int index = options.indexOf(option);
        defaultSwitches.set(index, defaultSwitch);
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(value);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }

    public List<List<Object>> getValues() {
        return values;
    }

    public List<List<Object>> getDefaultValues() {
        return defaultValues;
    }

    public List<Boolean> getSwitchValues() {
        return switchValues;
    }

    public List<Boolean> getDefaultSwitches() {
        return defaultSwitches;
    }

    public String getProperty(String property) {
        // Find index of property and return corresponding value
    }
}
