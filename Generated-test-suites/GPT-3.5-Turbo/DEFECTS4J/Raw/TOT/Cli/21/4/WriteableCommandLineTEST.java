import org.apache.commons.cli2.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        cmd.addOption(option);
        
        List<Option> options = cmd.getOptions();
        
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        Object value = "value";
        
        cmd.addValue(option, value);
        
        List<Object> values = cmd.getValues(option);
        
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        cmd.addValue(option, "value");
        
        List<String> undefaultedValues = cmd.getUndefaultedValues(option);
        
        assertNotNull(undefaultedValues);
        assertFalse(undefaultedValues.isEmpty());
        assertTrue(undefaultedValues.contains("value"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        
        cmd.setDefaultValues(option, defaultValues);
        
        List<String> optionDefaultValues = cmd.getDefaultValues(option);
        
        assertNotNull(optionDefaultValues);
        assertFalse(optionDefaultValues.isEmpty());
        assertTrue(optionDefaultValues.contains("default"));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testAddSwitch_ThrowIllegalStateException() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        cmd.setDefaultSwitch(option, true);
        
        Boolean defaultSwitch = cmd.getDefaultSwitch(option);
        
        assertNotNull(defaultSwitch);
        assertEquals(true, defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        String property = "property";
        String value = "value";
        
        cmd.addProperty(option, property, value);
        
        String optionPropertyValue = cmd.getPropertyValue(option, property);
        
        assertNotNull(optionPropertyValue);
        assertEquals(value, optionPropertyValue);
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        
        cmd.addProperty(property, value);
        
        String defaultPropertyValue = cmd.getPropertyValue(property);
        
        assertNotNull(defaultPropertyValue);
        assertEquals(value, defaultPropertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        
        assertTrue(cmd.looksLikeOption("-a"));
        assertFalse(cmd.looksLikeOption("value"));
    }
    
    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        cmd.setCurrentOption(option);
        
        Option currentOption = cmd.getCurrentOption();
        
        assertNotNull(currentOption);
        assertEquals(option, currentOption);
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        // Implement all methods of WriteableCommandLine interface
        
        @Override
        public void addOption(Option option) {
            // Implementation code
        }
        
        @Override
        public void addValue(Option option, Object value) {
            // Implementation code
        }
        
        @Override
        public List getUndefaultedValues(Option option) {
            // Implementation code
            return null;
        }
        
        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // Implementation code
        }
        
        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // Implementation code
        }
        
        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // Implementation code
        }
        
        @Override
        public void addProperty(Option option, String property, String value) {
            // Implementation code
        }
        
        @Override
        public void addProperty(String property, String value) {
            // Implementation code
        }
        
        @Override
        public boolean looksLikeOption(String argument) {
            // Implementation code
            return false;
        }
        
        @Override
        public Option getCurrentOption() {
            // Implementation code
            return null;
        }
        
        @Override
        public void setCurrentOption(Option currentOption) {
            // Implementation code
        }
    }
}