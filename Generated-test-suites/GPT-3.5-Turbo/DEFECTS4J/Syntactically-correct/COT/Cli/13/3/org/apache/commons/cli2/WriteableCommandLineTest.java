import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("option"));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        
        assertEquals("value", commandLine.getValue(option));
    }
    
    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getArgumentValues(option).size());
        assertTrue(commandLine.getArgumentValues(option).contains("value1"));
        assertTrue(commandLine.getArgumentValues(option).contains("value2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, Arrays.asList("value1", "value2"));
        
        assertEquals(2, commandLine.getArgumentValues(option).size());
        assertTrue(commandLine.getArgumentValues(option).contains("value1"));
        assertTrue(commandLine.getArgumentValues(option).contains("value2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        
        // Adding the switch again should throw an exception
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property", "value");
        
        assertEquals("value", commandLine.getProperty("property"));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("-Option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}



// WriteableCommandLineTest test cases go here

