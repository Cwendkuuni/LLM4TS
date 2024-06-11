import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option.Builder("o")
                .withDescription("Option")
                .create();
        
        commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = "value";
        
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, Collections.EMPTY_LIST);
        
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        
        assertTrue(switchValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "property";
        String defaultValue = "default";
        
        String value = commandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, value);
        
        commandLine.addProperty(property, "value");
        value = commandLine.getProperty(property, defaultValue);
        
        assertNotEquals(defaultValue, value);
    }
    
    @Test
    public void testLooksLikeOption() {
        String optionTrigger = "-o";
        assertTrue(commandLine.looksLikeOption(optionTrigger));
        
        optionTrigger = "--o";
        assertTrue(commandLine.looksLikeOption(optionTrigger));
        
        optionTrigger = "/o";
        assertFalse(commandLine.looksLikeOption(optionTrigger));
    }
    
    // Add more test cases for other methods
    
}