import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    private Option option1;
    private Option option2;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option1 = new Option("option1", "Option 1");
        option2 = new Option("option2", "Option 2");
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        
        List<String> values = Arrays.asList("value1", "value2");
        assertEquals(values, commandLine.getValues(option1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option1);
        List<String> expected = Arrays.asList("value1");
        assertEquals(expected, undefaultedValues);
        
        commandLine.setDefaultValues(option1, Arrays.asList("default1", "default2"));
        undefaultedValues = commandLine.getUndefaultedValues(option1);
        assertEquals(expected, undefaultedValues);
    }
    
    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option1);
        commandLine.setDefaultValues(option1, Arrays.asList("default1", "default2"));
        
        List<String> expected = Arrays.asList("default1", "default2");
        assertEquals(expected, commandLine.getValues(option1));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);
        
        assertTrue(commandLine.hasSwitch(option1));
        assertTrue(commandLine.getSwitchValue(option1));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchMultipleTimes() {
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option1);
        
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.hasSwitch(option1));
        assertTrue(commandLine.getSwitchValue(option1));
        
        commandLine.setDefaultSwitch(option1, false);
        assertTrue(commandLine.hasSwitch(option1));
        assertFalse(commandLine.getSwitchValue(option1));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addOption(option1);
        commandLine.addProperty(option1, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option1, "property1"));
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property1", "value1");
        
        assertEquals("value1", commandLine.getProperty("property1"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }
    
    @Test
    public void testSetCurrentOption() {
        commandLine.setCurrentOption(option1);
        assertEquals(option1, commandLine.getCurrentOption());
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
    
        private Option currentOption;
        private List<Option> options;
        private List<Object> values;
        private List<Boolean> switches;
        private List<String> properties;
    
        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            switches = new ArrayList<>();
            properties = new ArrayList<>();
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
        public List getUndefaultedValues(final Option option) {
            if (values.contains(option)) {
                return values;
            } else {
                return new ArrayList();
            }
        }
    
        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            values.addAll(defaultValues);
        }
    
        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException{
            if (switches.contains(option)) {
                throw new IllegalStateException("Switch already added");
            } else {
                switches.add(value);
            }
        }
    
        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            switches.add(defaultSwitch);
        }
    
        @Override
        public void addProperty(final Option option, final String property, final String value) {
            properties.add(value);
        }
    
        @Override
        public void addProperty(final String property, final String value) {
            properties.add(value);
        }
    
        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }
    
        @Override
        public Option getCurrentOption() {
            return currentOption;
        }
    
        @Override
        public void setCurrentOption(final Option currentOption) {
            this.currentOption = currentOption;
        }
    }
}