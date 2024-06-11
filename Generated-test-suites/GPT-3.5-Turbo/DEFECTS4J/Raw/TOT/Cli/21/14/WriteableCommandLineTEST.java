import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine writeableCommandLine;
    
    @Before
    public void setup() {
        writeableCommandLine = mock(WriteableCommandLine.class);
    }
    
    @Test
    public void addOption_validOption_success() {
        Option option = mock(Option.class);
        
        writeableCommandLine.addOption(option);
        
        verify(writeableCommandLine).addOption(option);
    }
    
    @Test
    public void addValue_validOptionAndValue_success() {
        Option option = mock(Option.class);
        Object value = "value";
        
        writeableCommandLine.addValue(option, value);
        
        verify(writeableCommandLine).addValue(option, value);
    }
    
    @Test
    public void getUndefaultedValues_validOption_existingValuesReturned() {
        Option option = mock(Option.class);
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(writeableCommandLine.getUndefaultedValues(option)).thenReturn(values);
        
        List<Object> result = writeableCommandLine.getUndefaultedValues(option);
        
        verify(writeableCommandLine).getUndefaultedValues(option);
        assertEquals(values, result);
    }
    
    @Test
    public void getUndefaultedValues_invalidOption_emptyListReturned() {
        Option option = mock(Option.class);
        List<Object> values = new ArrayList<>();
        when(writeableCommandLine.getUndefaultedValues(option)).thenReturn(values);
        
        List<Object> result = writeableCommandLine.getUndefaultedValues(option);
        
        verify(writeableCommandLine).getUndefaultedValues(option);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void setDefaultValues_validOptionAndDefaultValues_success() {
        Option option = mock(Option.class);
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        writeableCommandLine.setDefaultValues(option, defaultValues);
        
        verify(writeableCommandLine).setDefaultValues(option, defaultValues);
    }
    
    @Test(expected = IllegalStateException.class)
    public void addSwitch_switchAlreadyAdded_exceptionThrown() {
        Option option = mock(Option.class);
        boolean value = true;
        doThrow(IllegalStateException.class).when(writeableCommandLine).addSwitch(option, value);
        
        writeableCommandLine.addSwitch(option, value);
    }
    
    @Test
    public void addSwitch_switchNotAdded_success() {
        Option option = mock(Option.class);
        boolean value = true;
        
        writeableCommandLine.addSwitch(option, value);
        
        verify(writeableCommandLine).addSwitch(option, value);
    }
    
    @Test
    public void setDefaultSwitch_validOptionAndDefaultSwitch_success() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;
        
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        
        verify(writeableCommandLine).setDefaultSwitch(option, defaultSwitch);
    }
    
    @Test
    public void addProperty_validOptionAndPropertyAndValue_success() {
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";
        
        writeableCommandLine.addProperty(option, property, value);
        
        verify(writeableCommandLine).addProperty(option, property, value);
    }
    
    @Test
    public void addProperty_validPropertyAndValue_success() {
        String property = "property";
        String value = "value";
        
        writeableCommandLine.addProperty(property, value);
        
        verify(writeableCommandLine).addProperty(property, value);
    }
    
    @Test
    public void looksLikeOption_argumentOptionTrigger_trueReturned() {
        String argument = "-option";
        
        boolean result = writeableCommandLine.looksLikeOption(argument);
        
        assertTrue(result);
    }
    
    @Test
    public void looksLikeOption_argumentNotOptionTrigger_falseReturned() {
        String argument = "notOption";
        
        boolean result = writeableCommandLine.looksLikeOption(argument);
        
        assertFalse(result);
    }
    
    @Test
    public void setCurrentOption_validOption_success() {
        Option option = mock(Option.class);
        
        writeableCommandLine.setCurrentOption(option);
        
        verify(writeableCommandLine).setCurrentOption(option);
    }
}