import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        Option option1 = new Argument();
        Option option2 = new OptionImpl("-a");
        Option option3 = new OptionImpl("-b");
        
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        Option option4 = new OptionImpl("-c");
        commandLine.addOption(option4);
        
        assertTrue(commandLine.hasOption(option4));
    }
    
    @Test
    public void testAddValue() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addValue(option1, "value1");
        
        assertEquals("value1", commandLine.getValues(option1, new ArrayList<String>()).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option2 = commandLine.getOption("-a");
        
        commandLine.addSwitch(option2, true);
        
        assertTrue(commandLine.getSwitch(option2, false));
    }
    
    @Test
    public void testHasOption() {
        Option option3 = commandLine.getOption("-b");
        
        assertTrue(commandLine.hasOption(option3));
    }
    
    @Test
    public void testGetOption() {
        Option option2 = commandLine.getOption("-a");
        
        assertEquals(option2, commandLine.getOption("-a"));
    }
    
    @Test
    public void testGetValues() {
        Option option1 = commandLine.getOption("option1");
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");
        
        commandLine.addValue(option1, "value1");
        
        assertEquals("value1", commandLine.getValues(option1, defaultValues).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addValue(option1, "value1");
        
        assertEquals("value1", commandLine.getUndefaultedValues(option1).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option2 = commandLine.getOption("-a");
        
        commandLine.addSwitch(option2, true);
        
        assertTrue(commandLine.getSwitch(option2, false));
    }
    
    @Test
    public void testGetProperty() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addProperty(option1, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option1, "property1", "defaultValue1"));
    }
    
    @Test
    public void testAddProperty() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addProperty(option1, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option1, "property1", "defaultValue1"));
    }
    
    @Test
    public void testGetProperties() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addProperty(option1, "property1", "value1");
        
        assertEquals(1, commandLine.getProperties(option1).size());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }
    
    @Test
    public void testToString() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option1, "value3");
        
        assertEquals("value1 value2 value3", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(3, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(3, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option1 = commandLine.getOption("option1");
        List<String> defaults = new ArrayList<String>();
        defaults.add("default1");
        defaults.add("default2");
        
        commandLine.setDefaultValues(option1, defaults);
        
        assertTrue(commandLine.getValues(option1, new ArrayList<String>()).contains("default1"));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option2 = commandLine.getOption("-a");
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option2, defaultSwitch);
        
        assertTrue(commandLine.getSwitch(option2, false));
    }
    
    @Test
    public void testGetNormalised() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        
        assertEquals(2, commandLine.getNormalised().size());
    }
}