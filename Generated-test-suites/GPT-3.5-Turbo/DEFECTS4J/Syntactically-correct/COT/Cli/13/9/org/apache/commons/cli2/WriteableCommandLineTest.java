
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new Option("test", "Test option");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "test value");
        List<Object> values = commandLine.getValues(option);
        assertEquals("test value", values.get(0));
    }

    @Test
    public void testGetValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "test value");
        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("test value", values.get(0));
    }

    @Test
    public void testGetValues_NoValuesFound() {
        List<Object> values = commandLine.getValues(option);
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_SwitchAlreadyAdded() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        List<Boolean> switchValues = commandLine.getSwitchValues(option);
        assertEquals(Arrays.asList(true), switchValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        List<Boolean> switchValues = commandLine.getSwitchValues(option);
        assertEquals(Arrays.asList(true), switchValues);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("testProperty", "testValue");
        String value = commandLine.getProperty("testProperty");
        assertEquals("testValue", value);
    }

    @Test
    public void testLooksLikeOption_OptionTrigger() {
        boolean result = commandLine.looksLikeOption("-option");
        assertTrue(result);
    }

    @Test
    public void testLooksLikeOption_NoOptionTrigger() {
        boolean result = commandLine.looksLikeOption("option");
        assertFalse(result);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Boolean> switchValues = new ArrayList<>();
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
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            values.addAll(defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switchValues.contains(value)) {
                throw new IllegalStateException("Switch already added");
            }
            switchValues.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            switchValues.add(defaultSwitch);
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

        @Override
        public List<Boolean> getSwitchValues(Option option) {
            return switchValues;
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
        public List values(Option option) {
            return values;
        }
    }
}
