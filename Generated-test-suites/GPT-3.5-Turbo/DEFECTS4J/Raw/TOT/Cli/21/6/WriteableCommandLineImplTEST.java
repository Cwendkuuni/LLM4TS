import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

    @Mock
    private Option optionMock;
    
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commandLine = new WriteableCommandLineImpl(optionMock, null);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(optionMock);
        assertTrue(commandLine.hasOption(optionMock));
    }
    
    @Test
    public void testAddValue() {
        String value = "testValue";
        commandLine.addValue(optionMock, value);
        assertEquals(value, commandLine.getValues(optionMock, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(optionMock, true);
        assertTrue(commandLine.getSwitch(optionMock, null));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(optionMock));
        commandLine.addOption(optionMock);
        assertTrue(commandLine.hasOption(optionMock));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("trigger"));
        when(optionMock.getPreferredName()).thenReturn("trigger");
        assertEquals(optionMock, commandLine.getOption("trigger"));
    }
    
    @Test
    public void testGetValues() {
        Option argumentOptionMock = mock(Argument.class);
        commandLine.addValue(argumentOptionMock, "value1");
        commandLine.addValue(argumentOptionMock, "value2");
        assertEquals(2, commandLine.getValues(argumentOptionMock, null).size());
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option argumentOptionMock = mock(Argument.class);
        commandLine.addValue(argumentOptionMock, "value1");
        commandLine.addValue(argumentOptionMock, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(argumentOptionMock).size());
    }
    
    @Test
    public void testGetSwitch() {
        Option switchOptionMock = mock(Option.class);
        commandLine.addSwitch(switchOptionMock, true);
        assertTrue(commandLine.getSwitch(switchOptionMock, null));
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
        commandLine.addProperty(property, "value");
        assertEquals("value", commandLine.getProperty(property, defaultValue));
    }
    
    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(optionMock, property, value);
        assertEquals(value, commandLine.getProperty(optionMock, property, null));
    }
    
    @Test
    public void testGetProperties() {
        assertTrue(commandLine.getProperties().isEmpty());
        assertTrue(commandLine.getProperties(optionMock).isEmpty());
        
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(optionMock, property, value);
        assertTrue(commandLine.getProperties().contains(property));
        assertTrue(commandLine.getProperties(optionMock).contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("trigger"));
        when(optionMock.getPrefixes()).thenReturn(Collections.singleton("prefix"));
        assertTrue(commandLine.looksLikeOption("prefixTrigger"));
    }
    
    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
        commandLine.addOption(optionMock);
        when(optionMock.getPreferredName()).thenReturn("option");
        assertEquals("option", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
        commandLine.addOption(optionMock);
        assertFalse(commandLine.getOptions().isEmpty());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        when(optionMock.getPreferredName()).thenReturn("trigger");
        commandLine.addOption(optionMock);
        assertFalse(commandLine.getOptionTriggers().isEmpty());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option argumentOptionMock = mock(Argument.class);
        commandLine.setDefaultValues(argumentOptionMock, Collections.singletonList("defaultValue"));
        assertEquals("defaultValue", commandLine.getValues(argumentOptionMock, null).get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option switchOptionMock = mock(Option.class);
        commandLine.setDefaultSwitch(switchOptionMock, true);
        assertTrue(commandLine.getSwitch(switchOptionMock, null));
    }
    
    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
        commandLine.addValue(optionMock, "value");
        assertFalse(commandLine.getNormalised().isEmpty());
    }
}