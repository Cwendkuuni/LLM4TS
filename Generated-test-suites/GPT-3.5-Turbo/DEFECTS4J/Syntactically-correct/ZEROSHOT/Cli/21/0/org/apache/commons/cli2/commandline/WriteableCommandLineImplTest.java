import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addOption(option);
        assertTrue(cmdLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals(2, cmdLine.getValues(option, null).size());
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        assertFalse(cmdLine.hasOption(option));
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        assertEquals(option1, cmdLine.getOption("test1"));
        assertEquals(option2, cmdLine.getOption("test2"));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals(2, cmdLine.getValues(option, null).size());
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals(2, cmdLine.getUndefaultedValues(option).size());
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        assertEquals(null, cmdLine.getProperty("test"));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        cmdLine.addProperty("test", "value");
        assertEquals("value", cmdLine.getProperty("test"));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addProperty(option, "property1", "value1");
        cmdLine.addProperty(option, "property2", "value2");
        assertEquals("value1", cmdLine.getProperty(option, "property1", null));
        assertEquals("value2", cmdLine.getProperty(option, "property2", null));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmdLine.addProperty(option, "property1", "value1");
        cmdLine.addProperty(option, "property2", "value2");
        assertEquals(2, cmdLine.getProperties(option).size());
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, cmdLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        assertFalse(cmdLine.looksLikeOption("test"));
        assertTrue(cmdLine.looksLikeOption("-test"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        cmdLine.addOption(new Option("test"));
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals("value1 value2", cmdLine.toString());
    }
}