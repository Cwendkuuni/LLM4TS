import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl cmdLine;
    private Option option;
    
    @Before
    public void setUp() {
        option = new Argument().withPreferredName("arg");
        cmdLine = new WriteableCommandLineImpl(option, new ArrayList());
    }
    
    @Test
    public void testAddOption() {
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        String value = "value";
        cmdLine.addValue(option, value);
        assertEquals(value, cmdLine.getValues(option, new ArrayList()));
        assertFalse(cmdLine.getValues(option, null).isEmpty());
        assertNull(cmdLine.getValues(option, null));
    }
    
    @Test
    public void testAddSwitch() {
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, Boolean.FALSE));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(cmdLine.hasOption(option));
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertNull(cmdLine.getOption("arg"));
        cmdLine.addOption(option);
        assertEquals(option, cmdLine.getOption("arg"));
    }
    
    @Test
    public void testGetValues() {
        Option option2 = new Argument().withPreferredName("arg2");
        
        List<String> defaultValues = Arrays.asList("default1", "default2");
        List<String> commandLineValues = Arrays.asList("value1", "value2");
        cmdLine.setDefaultValues(option, defaultValues);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        
        // Test command line values
        assertEquals(commandLineValues, cmdLine.getValues(option, new ArrayList()));
        assertFalse(cmdLine.getValues(option, null).isEmpty());
        
        // Test default values
        assertEquals(defaultValues, cmdLine.getValues(option2, defaultValues));
        assertFalse(cmdLine.getValues(option2, defaultValues).isEmpty());
        
        // Test option's default values
        assertEquals(defaultValues, cmdLine.getValues(option2, null));
        assertFalse(cmdLine.getValues(option2, null).isEmpty());
        
        // Test empty list
        assertTrue(cmdLine.getValues(option2, null).isEmpty());
    }
    
    @Test
    public void testGetSwitch() {
        Option option2 = new Argument().withPreferredName("arg2");
        
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, Boolean.FALSE));
        
        cmdLine.setDefaultSwitch(option2, Boolean.TRUE);
        assertTrue(cmdLine.getSwitch(option2, Boolean.FALSE));
        
        assertNull(cmdLine.getSwitch(option2, null));
    }
    
    @Test
    public void testAddProperty() {
        cmdLine.addProperty("property", "value");
        assertEquals("value", cmdLine.getProperty("property", null));
    }
    
    @Test
    public void testGetProperty() {
        assertEquals("defaultValue", cmdLine.getProperty("property", "defaultValue"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(cmdLine.looksLikeOption("--option"));
        assertFalse(cmdLine.looksLikeOption("option"));
    }
    
    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        
        cmdLine.addOption(option);
        cmdLine.addValue(option, arg1);
        cmdLine.addValue(option, arg2);
        
        assertEquals("\"" + arg1 + "\" \"" + arg2 + "\"", cmdLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option), cmdLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(Arrays.asList("arg"), cmdLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmdLine.getValues(option, new ArrayList()));
        
        cmdLine.setDefaultValues(option, null);
        assertTrue(cmdLine.getValues(option, defaultValues).isEmpty());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        cmdLine.setDefaultSwitch(option, Boolean.TRUE);
        assertTrue(cmdLine.getSwitch(option, Boolean.FALSE));
        
        cmdLine.setDefaultSwitch(option, null);
        assertFalse(cmdLine.getSwitch(option, Boolean.TRUE));
    }
    
    @Test
    public void testGetNormalised() {
        Option option2 = new Argument().withPreferredName("arg2");
        List<String> arguments = Arrays.asList("arg1", "arg2");
        
        WriteableCommandLineImpl cmdLine2 = new WriteableCommandLineImpl(option2, arguments);
        
        assertEquals(arguments, cmdLine2.getNormalised());
    }

}