import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");
        Object value = "value";
        
        commandLine.addOption(option);
        commandLine.addValue(option, value);
        
        assertTrue(commandLine.getValues(option).contains(value));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");
        Object value1 = "value1";
        Object value2 = "value2";
        
        commandLine.addOption(option);
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);
        
        assertEquals(expectedValues, commandLine.getValues(option));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("s", "switch", false, "Switch");
        
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false); // Should throw IllegalStateException
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("s", "switch", false, "Switch");
        Boolean defaultSwitch = true;
        
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(property, value);
        
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        
        assertTrue(commandLine.looksLikeOption(argument));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        
        private List<Option> options;
        private List<Object> values;
        private List<List<Object>> defaultValues;
        private List<Boolean> switches;
        private List<Boolean> defaultSwitches;
        private List<String> properties;
        
        public WriteableCommandLineImpl() {
            this.options = new ArrayList<>();
            this.values = new ArrayList<>();
            this.defaultValues = new ArrayList<>();
            this.switches = new ArrayList<>();
            this.defaultSwitches = new ArrayList<>();
            this.properties = new ArrayList<>();
        }
        
        @Override
        public void addOption(final Option option) {
            options.add(option);
        }
        
        @Override
        public void addValue(final Option option, final Object value) {
            values.add(value);
        }
        
        @Override
        public List<Object> getValues(final Option option) {
            return values;
        }
        
        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues.add(defaultValues);
        }
        
        @Override
        public List<List<Object>> getDefaultValues(final Option option) {
            return defaultValues;
        }
        
        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            switches.add(value);
        }
        
        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }
        
        @Override
        public Boolean getDefaultSwitch(final Option option) {
            return defaultSwitches.get(0);
        }
        
        @Override
        public void addProperty(final String property, final String value) {
            properties.add(value);
        }
        
        @Override
        public String getProperty(final String property) {
            return properties.get(0);
        }
        
        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }
        
        @Override
        public List<Option> getOptions() {
            return options;
        }
    }
}