import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {
    @Mock
    private Option mockOption;
    
    private WriteableCommandLine commandLine;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(mockOption);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(mockOption, options.get(0));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(mockOption, value);
        List<Object> values = commandLine.getValues(mockOption);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(mockOption, defaultValues);
        
        List<Object> values = commandLine.getValues(mockOption);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(mockOption, true);
        Boolean switchValue = commandLine.getSwitchValue(mockOption);
        assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(mockOption, false);
        Boolean defaultSwitch = commandLine.getDefaultSwitch(mockOption);
        assertEquals(false, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        String propertyValue1 = commandLine.getPropertyValue("property1");
        assertEquals("value1", propertyValue1);
        
        String propertyValue2 = commandLine.getPropertyValue("property2");
        assertEquals("value2", propertyValue2);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--long-option"));
    }
}