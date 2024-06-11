import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertFalse(commandLine.hasOption(new Option()));
        
        Option option = new Option();
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getValues(new Option(), new ArrayList<>()));
        
        Option option = new Option();
        String value = "value";
        commandLine.addValue(option, value);
        
        assertEquals(value, commandLine.getValues(option, new ArrayList<>()).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertFalse(commandLine.getSwitch(new Option(), null));
        
        Option option = new Option();
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertFalse(commandLine.hasOption(new Option()));
        
        Option option = new Option();
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getOption("trigger"));
        
        Option option = new Option();
        option.setPreferredName("trigger");
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("trigger"));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getValues(new Option(), new ArrayList<>()).size());
        
        Option option = new Option();
        String value = "value";
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(option, value);
        
        assertEquals(value, commandLine.getValues(option, defaultValues).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getUndefaultedValues(new Option()).size());
        
        Option option = new Option();
        String value = "value";
        commandLine.addValue(option, value);
        
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertFalse(commandLine.getSwitch(new Option(), false));
        
        Option option = new Option();
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getProperty("property"));
        
        Option option = new Option();
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getProperty("property"));
        
        Option option = new Option();
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals("", commandLine.toString());
        
        Option option = new Option();
        String value = "value";
        commandLine.addValue(option, value);
        
        assertEquals(value, commandLine.toString());
    }
    
    @Test
    public void testAddOptionToProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getProperty(new Option(), "property", null));
        
        Option option = new Option();
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getProperties(new Option()).size());
        
        Option option = new Option();
        commandLine.addProperty(option, "property", "value");
        
        assertEquals(1, commandLine.getProperties(option).size());
    }
    
    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getOptions().size());
        
        Option option = new Option();
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getOptionTriggers().size());
        
        Option option = new Option();
        option.setPreferredName("trigger");
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getValues(new Option(), new ArrayList<>()));
        
        Option option = new Option();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertNull(commandLine.getSwitch(new Option(), null));
        
        Option option = new Option();
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertEquals(0, commandLine.getNormalised().size());
        
        Option option = new Option();
        String value = "value";
        commandLine.addValue(option, value);
        
        assertEquals(1, commandLine.getNormalised().size());
    }
}