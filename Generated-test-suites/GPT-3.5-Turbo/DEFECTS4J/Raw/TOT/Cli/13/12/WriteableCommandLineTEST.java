import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "testValue");
        assertEquals("testValue", writeableCommandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, writeableCommandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", writeableCommandLine.getProperty("testProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-t"));
        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
        assertFalse(writeableCommandLine.looksLikeOption("-"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private Option option;
        private List<Object> values;
        private List<Object> defaultValues;
        private boolean switchValue;
        private Boolean defaultSwitch;
        private String property;
        private String value;

        @Override
        public void addOption(Option option) {
            this.option = option;
        }

        @Override
        public void addValue(Option option, Object value) {
            this.option = option;
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(value);
        }

        @Override
        public List<Object> getArgumentValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.option = option;
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switchValue) {
                throw new IllegalStateException();
            }
            this.option = option;
            switchValue = value;
        }

        @Override
        public boolean getSwitch(Option option) {
            return switchValue;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.option = option;
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }

        @Override
        public void addProperty(String property, String value) {
            this.property = property;
            this.value = value;
        }

        @Override
        public String getProperty(String property) {
            if (property.equals(this.property)) {
                return value;
            }
            return null;
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public boolean hasOption(String optionName) {
            return option != null && option.getName().equals(optionName);
        }

        @Override
        public Object getValue(Option option) {
            return values.get(0);
        }
    }

}