import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        Object value = "value";
        commandLine.addValue(option, value);
        
        assertTrue(commandLine.getValues(option, new ArrayList<>()).contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String trigger = "trigger";
        option.setPreferredName(trigger);
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption(trigger));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(values, commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testGetValuesWithDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testGetValuesWithDefaultValuesAndCommandLineValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testGetValuesWithDefaultValuesAndEmptyCommandLineValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "");
        
        assertEquals(defaultValues, commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetSwitchWithDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        commandLine.addSwitch(option, false);
        
        assertFalse(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        
        assertEquals(value, commandLine.getProperty(property, null));
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String property = "property";
        String defaultValue = "default";
        
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        
        option.addPrefix("-");
        commandLine.addOption(option);
        assertFalse(commandLine.looksLikeOption("value"));
        
        option.addPrefix("--");
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("--value"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine.addOption(new Option("--option"));
        commandLine.addValue(new Argument(), "value1");
        commandLine.addValue(new Argument(), "value2");
        
        assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        String trigger = "trigger";
        option.setPreferredName(trigger);
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptionTriggers().contains(trigger));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getValues(option, new ArrayList<>()));
        
        commandLine.setDefaultValues(option, null);
        
        assertNull(commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option();
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        assertTrue(commandLine.getSwitch(option, false));
        
        commandLine.setDefaultSwitch(option, null);
        
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
    
}