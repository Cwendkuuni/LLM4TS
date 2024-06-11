
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        writeableCommandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }
    
    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        
        verify(writeableCommandLine).addOption(option);
    }
    
    @Test
    public void testAddValue() {
        Object value = new Object();
        
        writeableCommandLine.addValue(option, value);
        
        verify(writeableCommandLine).addValue(option, value);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        writeableCommandLine.getUndefaultedValues(option);
        
        verify(writeableCommandLine).getUndefaultedValues(option);
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaultValues = mock(List.class);
        
        writeableCommandLine.setDefaultValues(option, defaultValues);
        
        verify(writeableCommandLine).setDefaultValues(option, defaultValues);
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        
        writeableCommandLine.addSwitch(option, value);
        
        verify(writeableCommandLine).addSwitch(option, value);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        boolean value = true;
        doThrow(IllegalStateException.class).when(writeableCommandLine).addSwitch(option, value);
        
        writeableCommandLine.addSwitch(option, value);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        
        verify(writeableCommandLine).setDefaultSwitch(option, defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        
        writeableCommandLine.addProperty(option, property, value);
        
        verify(writeableCommandLine).addProperty(option, property, value);
    }
    
    @Test
    public void testAddProperty_NoOption() {
        String property = "property";
        String value = "value";
        
        writeableCommandLine.addProperty(property, value);
        
        verify(writeableCommandLine).addProperty(property, value);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        
        writeableCommandLine.looksLikeOption(argument);
        
        verify(writeableCommandLine).looksLikeOption(argument);
    }
    
}
