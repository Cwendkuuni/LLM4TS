import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WriteableCommandLineTest {

    @Mock
    private Option optionMock;

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(optionMock);
        List<Option> options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        assertSame(optionMock, options.get(0));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        writeableCommandLine.addValue(optionMock, value);
        List<Object> values = writeableCommandLine.getValues(optionMock);
        assertEquals(1, values.size());
        assertSame(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        writeableCommandLine.addValue(optionMock, value1);
        writeableCommandLine.addValue(optionMock, value2);
        List<Object> undefaultedValues = writeableCommandLine.getUndefaultedValues(optionMock);
        assertEquals(2, undefaultedValues.size());
        assertSame(value1, undefaultedValues.get(0));
        assertSame(value2, undefaultedValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        writeableCommandLine.setDefaultValues(optionMock, defaultValues);
        List<Object> values = writeableCommandLine.getValues(optionMock);
        assertEquals(defaultValues.size(), values.size());
        assertSame(defaultValues.get(0), values.get(0));
        assertSame(defaultValues.get(1), values.get(1));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(optionMock, true);
        assertTrue(writeableCommandLine.getSwitch(optionMock));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(optionMock, true);
        assertTrue(writeableCommandLine.getSwitch(optionMock));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(optionMock, property, value);
        String retrievedValue = writeableCommandLine.getProperty(optionMock, property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        String retrievedValue = writeableCommandLine.getDefaultProperty(property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = new Option();
        writeableCommandLine.setCurrentOption(currentOption);
        assertSame(currentOption, writeableCommandLine.getCurrentOption());
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Option> currentOptions = new ArrayList<>();
        private boolean switchValue;
        private List<String> properties = new ArrayList<>();
        private List<String> defaultProperties = new ArrayList<>();
        private Option currentOption;

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
        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            values = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switchValue = value;
        }

        @Override
        public boolean getSwitch(Option option) {
            return switchValue;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            switchValue = defaultSwitch;
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
        public String getProperty(Option option, String property) {
            return properties.get(0);
        }

        @Override
        public String getDefaultProperty(String property) {
            return defaultProperties.get(0);
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

        @Override
        public List<Option> getOptions() {
            return options;
        }
    }
}