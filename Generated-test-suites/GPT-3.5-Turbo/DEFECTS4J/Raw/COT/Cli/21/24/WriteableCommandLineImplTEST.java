import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        
        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, null));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("testOption"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, Collections.singletonList("defaultValue")));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty(option, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option, "property1", "defaultValue"));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty(option, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option, "property1", "defaultValue"));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        
        assertEquals(2, commandLine.getProperties(option).size());
    }
    
    @Test
    public void testGetPropertiesDefaultOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        assertEquals(2, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        assertTrue(commandLine.looksLikeOption("-testOption"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        
        commandLine.addValue(new Option("argument"), "value1");
        commandLine.addValue(new Option("argument"), "value2");
        
        assertEquals("value1 value2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(2, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(2, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.setDefaultValues(option, Collections.singletonList("defaultValue"));
        
        assertEquals(Collections.singletonList("defaultValue"), commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), Collections.singletonList("argument1 argument2"));
        
        assertEquals(Collections.singletonList("argument1 argument2"), commandLine.getNormalised());
    }
    
}