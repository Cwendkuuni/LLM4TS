import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private Option rootOption;
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        rootOption = new Option("r", "root", false, "Root option");
        commandLine = new WriteableCommandLineImpl(rootOption, null);
    }
    
    // Testing addOption() method
    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    // Testing addValue() method
    @Test
    public void testAddValue() {
        Option option = new Option("a", "argument", true, "Argument option");
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
    }
    
    // Testing addSwitch() method
    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    // Testing hasOption() method
    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Regular option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    // Testing getOption() method
    @Test
    public void testGetOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(option1, commandLine.getOption("o1"));
    }
    
    // Testing getValues() method
    @Test
    public void testGetValues() {
        Option option = new Option("v", "value", true, "Value option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }
    
    // Testing getUndefaultedValues() method
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("u", "undefaulted", true, "Undefaulted option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }
    
    // Testing getSwitch() method
    @Test
    public void testGetSwitch() {
        Option option = new Option("sw", "switch", false, "Switch option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    // Testing getProperty() method
    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addProperty(option, "key", "value");
        assertEquals("value", commandLine.getProperty(option, "key", null));
    }
    
    // Testing setDefaultValues() method
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("d", "default", true, "Default option");
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
    }
    
    // Testing setDefaultSwitch() method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("ds", "defaultSwitch", false, "Default Switch option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    // Testing looksLikeOption() method
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("string"));
    }
    
    // Testing toString() method
    @Test
    public void testToString() {
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1 arg2"));
        assertEquals("arg1 arg2", commandLine.toString());
    }   
    
    // Testing getOptions() method
    @Test
    public void testGetOptions() {
        Option option1 = new Option("op1", "option1", false, "Option 1");
        Option option2 = new Option("op2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }
    
    // Testing getOptionTriggers() method
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("ot1", "option1", false, "Option 1");
        Option option2 = new Option("ot2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptionTriggers().contains("ot1"));
        assertTrue(commandLine.getOptionTriggers().contains("ot2"));
    }
    
    // Testing getProperties() method
    @Test
    public void testGetProperties() {
        Option option = new Option("pro", "property", true, "Property option");
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        assertTrue(commandLine.getProperties(option).contains("key1"));
        assertTrue(commandLine.getProperties(option).contains("key2"));
    }
    
    // Testing getNormalised() method
    @Test
    public void testGetNormalised() {
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1 arg2"));
        assertEquals("arg1 arg2", commandLine.getNormalised().get(0));
    }
}