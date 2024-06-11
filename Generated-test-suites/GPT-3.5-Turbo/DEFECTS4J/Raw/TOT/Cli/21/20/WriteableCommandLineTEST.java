import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WriteableCommandLineTest {

    @Mock
    private Option mockOption;

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(mockOption);
        assertTrue(writeableCommandLine.getOptions().contains(mockOption));
    }

    @Test
    public void testAddValue() {
        Object valueToAdd = new Object();
        writeableCommandLine.addValue(mockOption, valueToAdd);

        List<Object> values = writeableCommandLine.getValues(mockOption);
        assertTrue(values.contains(valueToAdd));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);

        when(mockOption.getName()).thenReturn("testOption");
        writeableCommandLine.addValue(mockOption, value1);
        writeableCommandLine.addValue(mockOption, value2);

        assertEquals(values, writeableCommandLine.getUndefaultedValues(mockOption));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        Object defaultValue1 = new Object();
        Object defaultValue2 = new Object();
        defaultValues.add(defaultValue1);
        defaultValues.add(defaultValue2);

        writeableCommandLine.setDefaultValues(mockOption, defaultValues);

        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(mockOption));
    }

    @Test
    public void testAddSwitch() {
        boolean switchValue = true;
        writeableCommandLine.addSwitch(mockOption, switchValue);

        assertTrue(writeableCommandLine.getSwitch(mockOption));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(mockOption, defaultSwitch);

        assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(mockOption));
    }

    @Test
    public void testAddProperty() {
        String propertyName = "testProperty";
        String propertyValue = "testValue";
        writeableCommandLine.addProperty(mockOption, propertyName, propertyValue);

        assertEquals(propertyValue, writeableCommandLine.getProperty(mockOption, propertyName));
    }

    @Test
    public void testAddDefaultProperty() {
        String propertyName = "testProperty";
        String propertyValue = "testValue";
        writeableCommandLine.addProperty(propertyName, propertyValue);

        assertEquals(propertyValue, writeableCommandLine.getProperty(propertyName));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-testOption";
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = mock(Option.class);
        writeableCommandLine.setCurrentOption(currentOption);

        assertEquals(currentOption, writeableCommandLine.getCurrentOption());
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> values;
        private List<Object> defaultValues;
        private List<Boolean> switches;
        private List<Boolean> defaultSwitches;
        private List<String> properties;
        private List<String> defaultProperties;
        private String currentOption;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            defaultValues = new ArrayList<>();
            switches = new ArrayList<>();
            defaultSwitches = new ArrayList<>();
            properties = new ArrayList<>();
            defaultProperties = new ArrayList<>();
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
        }

        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switches.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }

        @Override
        public void addProperty(String property, String value) {
            defaultProperties.add(value);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            this.currentOption = currentOption;
        }

        public List<Option> getOptions() {
            return options;
        }

        public List<Object> getValues(Option option) {
            return values;
        }

        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }

        public boolean getSwitch(Option option) {
            return switches.get(0);
        }

        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitches.get(0);
        }

        public String getProperty(Option option, String property) {
            return properties.get(0);
        }

        public String getProperty(String property) {
            return defaultProperties.get(0);
        }
    }
}