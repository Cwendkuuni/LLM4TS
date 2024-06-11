import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import java.util.List;
import java.util.ArrayList;

public class WriteableCommandLineTest {

    private WriteableCommandLine cmdLine;
    
    @Before
    public void setUp() {
        cmdLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "This is a test option");
        cmdLine.addOption(option);
        
        assertTrue(cmdLine.hasOption("testOption"));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("testOption", "This is a test option");
        
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        
        assertEquals(expectedValues, cmdLine.getOptionValues("testOption"));
    }
    
    @Test
    public void testGetOptionValues() {
        Option option = new Option("testOption", "This is a test option");
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        
        assertEquals(expectedValues, cmdLine.getOptionValues("testOption"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption", "This is a test option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        
        cmdLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, cmdLine.getDefaultOptionValues("testOption"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("testOption", "This is a test option");
        
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption", "This is a test option");
        
        cmdLine.setDefaultSwitch(option, true);
        
        assertTrue(cmdLine.getDefaultSwitchValue("testOption"));
    }
    
    @Test
    public void testAddProperty() {
        cmdLine.addProperty("testProperty", "testValue");
        
        assertEquals("testValue", cmdLine.getPropertyValue("testProperty"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(cmdLine.looksLikeOption("-testOption"));
        assertFalse(cmdLine.looksLikeOption("testOption"));
    }
}