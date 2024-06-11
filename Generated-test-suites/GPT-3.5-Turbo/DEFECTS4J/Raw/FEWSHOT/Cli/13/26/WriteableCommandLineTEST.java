import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Mock
    private Option optionMock;

    @Before
    public void setup() {
        writeableCommandLine = Mockito.spy(new WriteableCommandLineImpl());
    }
    
    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(optionMock);
        Mockito.verify(writeableCommandLine).addOption(optionMock);
    }
    
    @Test
    public void testAddValue() {
        Object value = new Object();
        writeableCommandLine.addValue(optionMock, value);
        Mockito.verify(writeableCommandLine).addValue(optionMock, value);
    }
    
    @Test
    public void testGetArgumentValues() {
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Mockito.when(writeableCommandLine.getArgumentValues(optionMock)).thenReturn(expectedValues);
        List<Object> actualValues = writeableCommandLine.getArgumentValues(optionMock);
        Mockito.verify(writeableCommandLine).getArgumentValues(optionMock);
        assertEquals(expectedValues, actualValues);
    }
    
    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        writeableCommandLine.setDefaultValues(optionMock, defaultValues);
        Mockito.verify(writeableCommandLine).setDefaultValues(optionMock, defaultValues);
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        writeableCommandLine.addSwitch(optionMock, value);
        Mockito.verify(writeableCommandLine).addSwitch(optionMock, value);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(optionMock, defaultSwitch);
        Mockito.verify(writeableCommandLine).setDefaultSwitch(optionMock, defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        Mockito.verify(writeableCommandLine).addProperty(property, value);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "--option";
        boolean expected = true;
        boolean actual = writeableCommandLine.looksLikeOption(argument);
        Mockito.verify(writeableCommandLine).looksLikeOption(argument);
        assertEquals(expected, actual);
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        @Override
        public void addOption(Option option) {
        }
        
        @Override
        public void addValue(Option option, Object value) {
        }
        
        @Override
        public List<Object> getArgumentValues(Option option) {
            return null;
        }
        
        @Override
        public void setDefaultValues(Option option, List<Object> defaultValues) {
        }
        
        @Override
        public void addSwitch(Option option, boolean value) {
        }
        
        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        }
        
        @Override
        public void addProperty(String property, String value) {
        }
        
        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("--") || argument.startsWith("-");
        }
        
        // Other methods of CommandLine interface
    }
}