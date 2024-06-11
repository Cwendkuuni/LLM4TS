import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option, new ArrayList()).size());
        assertTrue(commandLine.getValues(option, new ArrayList()).contains("value1"));
        assertTrue(commandLine.getValues(option, new ArrayList()).contains("value2"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        
        assertEquals(option1, commandLine.getOption("testOption1"));
        assertNull(commandLine.getOption("testOption2"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        
        assertEquals(3, commandLine.getValues(option, new ArrayList()).size());
        assertTrue(commandLine.getValues(option, new ArrayList()).contains("value1"));
        assertTrue(commandLine.getValues(option, new ArrayList()).contains("value2"));
        assertTrue(commandLine.getValues(option, new ArrayList()).contains("value3"));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        assertEquals("value1", commandLine.getProperty("property1", ""));
        assertEquals("value2", commandLine.getProperty("property2", ""));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        assertTrue(commandLine.looksLikeOption("-testOption"));
        assertFalse(commandLine.looksLikeOption("testValue"));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");
        
        assertEquals("value1 value2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertTrue(commandLine.getOptionTriggers().contains("testOption1"));
        assertTrue(commandLine.getOptionTriggers().contains("testOption2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.setCreateDefaultValues(option1, new ArrayList());
        commandLine.setCreateDefaultValues(option2, new ArrayList());
        
        assertTrue(commandLine.getDefaultValues().contains(option1));
        assertTrue(commandLine.getDefaultValues().contains(option2));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.setCreateDefaultSwitch(option1, false);
        commandLine.setCreateDefaultSwitch(option2, true);
        
        assertFalse(commandLine.getDefaultSwitch(option1));
        assertTrue(commandLine.getDefaultSwitch(option2));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        commandLine.getNormalised().add("arg3");
        
        assertEquals(3, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains("arg1"));
        assertTrue(commandLine.getNormalised().contains("arg2"));
        assertTrue(commandLine.getNormalised().contains("arg3"));
    }
    
}