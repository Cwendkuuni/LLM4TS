import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("option1", "o1", false);
        Option option2 = new Option("option2", "o2", true);
        Option option3 = new Option("option3", "o3", false);
        
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testAddOption() {
        Option option4 = new Option("option4", "o4", true);
        commandLine.addOption(option4);
        
        assertTrue(commandLine.hasOption(option4));
    }
    
    @Test
    public void testAddValue() {
        Option option5 = new Option("option5", "o5", true);
        commandLine.addValue(option5, "value1");
        commandLine.addValue(option5, "value2");
        
        assertEquals(2, commandLine.getValues(option5, new ArrayList<>()).size());
    }
    
    @Test
    public void testAddSwitch() {
        Option option6 = new Option("option6", "o6", false);
        commandLine.addSwitch(option6, true);
        
        assertTrue(commandLine.getSwitch(option6, false));
    }
    
    @Test
    public void testHasOption() {
        Option option7 = new Option("option7", "o7", false);
        commandLine.addOption(option7);
        
        assertTrue(commandLine.hasOption(option7));
    }
    
    @Test
    public void testGetOption() {
        Option option8 = new Option("option8", "o8", false);
        commandLine.addOption(option8);
        
        assertEquals(option8, commandLine.getOption("o8"));
    }
    
    @Test
    public void testGetValues() {
        Option option9 = new Option("option9", "o9", true);
        commandLine.addValue(option9, "value1");
        commandLine.addValue(option9, "value2");
        
        assertEquals(2, commandLine.getValues(option9, new ArrayList<>()).size());
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option10 = new Option("option10", "o10", true);
        commandLine.addValue(option10, "value1");
        commandLine.addValue(option10, "value2");
        
        assertEquals(2, commandLine.getUndefaultedValues(option10).size());
    }
    
    @Test
    public void testGetSwitch() {
        Option option11 = new Option("option11", "o11", false);
        commandLine.addSwitch(option11, true);
        
        assertTrue(commandLine.getSwitch(option11, false));
    }
    
    @Test
    public void testGetProperty() {
        String property1 = "property1";
        String value1 = "value1";
        
        commandLine.addProperty(property1, value1);
        
        assertEquals(value1, commandLine.getProperty(property1));
    }
    
    @Test
    public void testAddProperty() {
        String property2 = "property2";
        String value2 = "value2";
        
        commandLine.addProperty(property2, value2);
        
        assertEquals(value2, commandLine.getProperty(property2));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        String property3 = "property3";
        String value3 = "value3";
        
        assertEquals(value3, commandLine.getProperty(property3, value3));
    }
    
    @Test
    public void testGetProperties() {
        String property4 = "property4";
        String value4 = "value4";
        
        commandLine.addProperty(property4, value4);
        
        assertEquals(1, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }
    
    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(3, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(5, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option12 = new Option("option12", "o12", true);
        commandLine.setDefaultValues(option12, new ArrayList<>());
        
        assertEquals(0, commandLine.getValues(option12, new ArrayList<>()).size());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option13 = new Option("option13", "o13", false);
        commandLine.setDefaultSwitch(option13, true);
        
        assertTrue(commandLine.getSwitch(option13, false));
    }
    
    @Test
    public void testGetNormalised() {
        assertEquals(0, commandLine.getNormalised().size());
    }
}