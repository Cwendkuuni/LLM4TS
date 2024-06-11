import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        
        commandLine.addValue(option, "value");
        
        assertEquals("value", commandLine.getValues(option).get(0));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("value1"));
        assertTrue(commandLine.getValues(option).contains("value2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        List defaultValues = Arrays.asList("default1", "default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        
        commandLine.addSwitch(option, true);
        
        assertEquals(true, commandLine.getSwitch(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        
        commandLine.addSwitch(option, true);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        
        commandLine.setDefaultSwitch(option, true);
        
        assertEquals(true, commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }
}