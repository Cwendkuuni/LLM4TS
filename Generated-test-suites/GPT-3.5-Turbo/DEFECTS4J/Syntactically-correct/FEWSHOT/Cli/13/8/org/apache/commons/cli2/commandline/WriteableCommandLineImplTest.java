import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option;

    @Before
    public void setup() {
        option = new Argument("", "");
        option.addTrigger("-");
    }
    
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("-"));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty("property", "value");
        
        assertEquals("value", commandLine.getProperty("property", null));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertTrue(commandLine.looksLikeOption("-"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertEquals("", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("-"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, null);
        
        assertNull(commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, null);
        
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getNormalised().size());
        assertEquals(option, commandLine.getNormalised().get(0));
    }
}