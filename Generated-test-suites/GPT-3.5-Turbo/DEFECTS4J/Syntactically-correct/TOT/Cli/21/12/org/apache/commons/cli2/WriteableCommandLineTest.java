import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import java.util.List;
import java.util.ArrayList;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    // Tests for addOption() method
    @Test
    public void testAddOption() {
        Option option = new Option("-a");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    // Tests for addValue() method
    @Test
    public void testAddValue() {
        Option option = new Option("-a");
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue(option));
    }

    // Tests for getUndefaultedValues() method
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-a");
        List expected = new ArrayList();
        expected.add("value1");
        expected.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(expected, commandLine.getUndefaultedValues(option));
    }

    // Tests for setDefaultValues() method
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-a");
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    // Tests for addSwitch() method
    @Test
    public void testAddSwitch() {
        Option option = new Option("-a");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    // Tests for setDefaultSwitch() method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-a");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    // Tests for addProperty() method
    @Test
    public void testAddProperty() {
        Option option = new Option("-a");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }

    // Tests for addProperty() method with default property set
    @Test
    public void testAddPropertyDefaultSet() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    // Tests for looksLikeOption() method
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    // Tests for setCurrentOption() and getCurrentOption() methods
    @Test
    public void testCurrentOption() {
        Option option = new Option("-a");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private Option currentOption;
        private List<Option> options;
        private List<String> values;
        private List<Object> defaultValues;
        private List<Boolean> switches;
        private List<Boolean> defaultSwitches;
        private List<List<String>> properties;
        private List<String> defaultProperties;

        public WriteableCommandLineImpl() {
            this.options = new ArrayList<>();
            this.values = new ArrayList<>();
            this.defaultValues = new ArrayList<>();
            this.switches = new ArrayList<>();
            this.defaultSwitches = new ArrayList<>();
            this.properties = new ArrayList<>();
            this.defaultProperties = new ArrayList<>();
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value.toString());
        }

        @Override
        public List getUndefaultedValues(Option option) {
            List<String> result = new ArrayList<>();
            for (String value : values) {
                if (value.contains(option.getOpt())) {
                    result.add(value);
                }
            }
            return result;
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
            List<String> propertyValue = new ArrayList<>();
            propertyValue.add(property);
            propertyValue.add(value);
            properties.add(propertyValue);
        }

        @Override
        public void addProperty(String property, String value) {
            defaultProperties.add(property);
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
    }
}