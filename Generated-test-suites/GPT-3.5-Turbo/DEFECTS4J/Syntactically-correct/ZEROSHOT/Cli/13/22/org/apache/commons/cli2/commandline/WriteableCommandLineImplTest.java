import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        
        Option retrievedOption = commandLine.getOption("test");
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value");
        
        List<String> values = commandLine.getValues(option, new ArrayList());
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        Boolean bool = commandLine.getSwitch(option, false);
        assertTrue(bool);
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        Option retrievedOption = commandLine.getOption("test");
        assertNull(retrievedOption);
        
        commandLine.addOption(option);
        
        retrievedOption = commandLine.getOption("test");
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        List<String> values = commandLine.getValues(option, new ArrayList());
        assertEquals(0, values.size());
        
        commandLine.addValue(option, "value");
        
        values = commandLine.getValues(option, new ArrayList());
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        Boolean bool = commandLine.getSwitch(option, false);
        assertNull(bool);
        
        commandLine.addSwitch(option, true);
        
        bool = commandLine.getSwitch(option, false);
        assertTrue(bool);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty("property", "value");
        
        String propertyValue = commandLine.getProperty("property", "default");
        assertEquals("value", propertyValue);
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        String propertyValue = commandLine.getProperty("property", "default");
        assertEquals("default", propertyValue);
        
        commandLine.addProperty("property", "value");
        
        propertyValue = commandLine.getProperty("property", "default");
        assertEquals("value", propertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        boolean looksLikeOption = commandLine.looksLikeOption("-t");
        assertTrue(looksLikeOption);
        
        looksLikeOption = commandLine.looksLikeOption("test");
        assertFalse(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        String str = commandLine.toString();
        assertEquals("", str);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("test1"));
        assertTrue(triggers.contains("test2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.setDefaultValues(option, new ArrayList());
        
        List<String> defaultValues = commandLine.getValues(option, new ArrayList());
        assertEquals(0, defaultValues.size());
        
        List<String> newDefaultValues = new ArrayList();
        newDefaultValues.add("default");
        commandLine.setDefaultValues(option, newDefaultValues);
        
        defaultValues = commandLine.getValues(option, new ArrayList());
        assertEquals(1, defaultValues.size());
        assertEquals("default", defaultValues.get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.setDefaultSwitch(option, false);
        
        Boolean bool = commandLine.getSwitch(option, true);
        assertNull(bool);
        
        commandLine.setDefaultSwitch(option, true);
        
        bool = commandLine.getSwitch(option, false);
        assertTrue(bool);
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        List<String> normalised = commandLine.getNormalised();
        assertEquals(0, normalised.size());
    }
}