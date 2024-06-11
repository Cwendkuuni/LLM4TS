import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option).contains("value"));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getUndefaultedValues(option).contains("value"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertEquals(commandLine.getDefaultValues(option), Arrays.asList("default1", "default2"));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addProperty(option, "property1", "value1");
        assertEquals(commandLine.getProperty(option, "property1"), "value1");
    }
    
    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property1", "value1");
        assertEquals(commandLine.getDefaultProperty("property1"), "value1");
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
    }
    
    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        assertEquals(commandLine.getCurrentOption(), option);
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        
        private Option currentOption;
        private Map<Option, List<Object>> valuesMap;
        private Map<Option, List<Object>> defaultValuesMap;
        private Map<Option, List<Object>> undefaultedValuesMap;
        private Map<Option, Boolean> switchMap;
        private Map<Option, Boolean> defaultSwitchMap;
        private Map<Option, Map<String, String>> propertyMap;
        private Map<String, String> defaultPropertyMap;
        
        public void addOption(Option option) {
            // Implementation
        }
        
        public void addValue(Option option, Object value) {
            // Implementation
        }
        
        public List<Object> getValues(Option option) {
            // Implementation
        }
        
        public List<Object> getUndefaultedValues(Option option) {
            // Implementation
        }
        
        public void setDefaultValues(Option option, List<Object> defaultValues) {
            // Implementation
        }
        
        public List<Object> getDefaultValues(Option option) {
            // Implementation
        }
        
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // Implementation
        }
        
        public boolean getSwitch(Option option) {
            // Implementation
        }
        
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // Implementation
        }
        
        public Boolean getDefaultSwitch(Option option) {
            // Implementation
        }
        
        public void addProperty(Option option, String property, String value) {
            // Implementation
        }
        
        public String getProperty(Option option, String property) {
            // Implementation
        }
        
        public void addProperty(String property, String value) {
            // Implementation
        }
        
        public String getDefaultProperty(String property) {
            // Implementation
        }
        
        public boolean looksLikeOption(String argument) {
            // Implementation
        }
        
        public void setCurrentOption(Option currentOption) {
            // Implementation
        }
        
        public Option getCurrentOption() {
            // Implementation
        }
    
    }
    
}