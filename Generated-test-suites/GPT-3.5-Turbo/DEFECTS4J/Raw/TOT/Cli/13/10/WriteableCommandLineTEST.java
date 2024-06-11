import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        commandLine.addOption(option1);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option1));

        Option option2 = new Option("b", "option2", true, "Option 2");
        commandLine.addOption(option2);
        options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        List<Object> values = commandLine.getValues(option1);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        Option option2 = new Option("b", "option2", true, "Option 2");
        commandLine.addOption(option2);
        commandLine.addValue(option2, "value2");
        commandLine.addValue(option2, "value3");
        values = commandLine.getValues(option2);
        assertEquals(2, values.size());
        assertEquals("value2", values.get(0));
        assertEquals("value3", values.get(1));
    }

    @Test
    public void testGetArgumentValues() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        List<Object> values = commandLine.getArgumentValues(option1);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        Option option2 = new Option("b", "option2", true, "Option 2");
        commandLine.addOption(option2);
        commandLine.addValue(option2, "value2");
        commandLine.addValue(option2, "value3");
        values = commandLine.getArgumentValues(option2);
        assertEquals(2, values.size());
        assertEquals("value2", values.get(0));
        assertEquals("value3", values.get(1));

        Option option3 = new Option("c", "option3", true, "Option 3");
        values = commandLine.getArgumentValues(option3);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        commandLine.addOption(option1);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        commandLine.setDefaultValues(option1, defaultValues);
        List<Object> values = commandLine.getValues(option1);
        assertEquals(1, values.size());
        assertEquals("default1", values.get(0));

        Option option2 = new Option("b", "option2", true, "Option 2");
        commandLine.addOption(option2);
        defaultValues = new ArrayList<>();
        defaultValues.add("default2");
        defaultValues.add("default3");
        commandLine.setDefaultValues(option2, defaultValues);
        values = commandLine.getValues(option2);
        assertEquals(2, values.size());
        assertEquals("default2", values.get(0));
        assertEquals("default3", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("a", "option", true, "Option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitchValue(option));

        // Adding switch for an already added switch should throw IllegalStateException
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("a", "option1", true, "Option 1");
        commandLine.addOption(option1);
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getDefaultSwitch(option1));

        Option option2 = new Option("b", "option2", true, "Option 2");
        commandLine.addOption(option2);
        commandLine.setDefaultSwitch(option2, false);
        assertFalse(commandLine.getDefaultSwitch(option2));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));

        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getProperty("property2"));

        // Adding property with same name should replace the existing value
        commandLine.addProperty("property1", "value3");
        assertEquals("value3", commandLine.getProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> values;
        private List<Option> argumentOptions;
        private List<Object> argumentValues;
        private List<String> defaultValues;
        private Boolean defaultSwitch;
        private List<String> properties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            argumentOptions = new ArrayList<>();
            argumentValues = new ArrayList<>();
            defaultValues = new ArrayList<>();
            properties = new ArrayList<>();
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
        public List<Object> getValues(Option option) {
            return values;
        }

        @Override
        public List<Object> getArgumentValues(Option option) {
            if (argumentOptions.contains(option)) {
                return argumentValues;
            }
            return new ArrayList<>();
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (argumentOptions.contains(option)) {
                throw new IllegalStateException("Switch already added");
            }
            argumentOptions.add(option);
            argumentValues.add(value);
        }

        @Override
        public Boolean getSwitchValue(Option option) {
            int index = argumentOptions.indexOf(option);
            if (index >= 0) {
                return (Boolean) argumentValues.get(index);
            }
            return null;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }
        
        @Override
        public void addProperty(String property, String value) {
            properties.add(value);
        }

        @Override
        public String getProperty(String property) {
            for (int i = properties.size() - 1; i >= 0; i--) {
                if (properties.get(i).equals(property)) {
                    return properties.get(i);
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

        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }

    }
}