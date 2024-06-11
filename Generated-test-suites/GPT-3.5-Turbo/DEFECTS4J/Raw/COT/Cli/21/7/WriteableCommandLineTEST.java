

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
    Option option;
    
    private WriteableCommandLine writeableCommandLine;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        writeableCommandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        List<Option> options = writeableCommandLine.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        List<Object> values = writeableCommandLine.getValues(option);
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);
        List<Object> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }
    
    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<Object> values = writeableCommandLine.getValues(option);
        assertEquals(defaultValues.size(), values.size());
        for (int i = 0; i < defaultValues.size(); i++) {
            assertTrue(values.contains(defaultValues.get(i)));
        }
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = false;
        writeableCommandLine.addSwitch(option, value);
        Boolean switchValue = writeableCommandLine.getSwitchValue(option);
        assertEquals(value, switchValue.booleanValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        boolean value = false;
        writeableCommandLine.addSwitch(option, value);
        writeableCommandLine.addSwitch(option, value);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = writeableCommandLine.getSwitchValue(option);
        assertEquals(defaultSwitch.booleanValue(), switchValue.booleanValue());
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(option, property, value);
        String propertyValue = writeableCommandLine.getProperty(option, property);
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testAddPropertyDefault() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        String propertyValue = writeableCommandLine.getProperty(null, property);
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(writeableCommandLine.locksLikeOption(argument));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        
        private List<Option> options;
        private List<Object> values;
        private Boolean switchValue;
        private List<String> properties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            switchValue = null;
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
        public List<Object> getUndefaultedValues(Option option) {
            return new ArrayList<>(values);
        }

        @Override
        public void setDefaultValues(Option option, List<Object> defaultValues) {
            values.addAll(defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switchValue != null) {
                throw new IllegalStateException("Switch already added");
            }
            switchValue = value;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            switchValue = defaultSwitch;
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(property + "=" + value);
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(property + "=" + value);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            return null;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
        
        }
    }
}

