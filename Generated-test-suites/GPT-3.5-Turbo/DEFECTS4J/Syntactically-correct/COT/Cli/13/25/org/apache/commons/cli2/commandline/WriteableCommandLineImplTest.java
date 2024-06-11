import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument("testArgument");
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertFalse(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getOption("testTrigger"));
    }
    
    @Test
    public void testGetValues() {
        Argument argument = new Argument("testArgument");
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getValues(argument, null));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty("testProperty", null));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertFalse(commandLine.looksLikeOption("testTrigger"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals("", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals(1, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals(0, commandLine.getNormalised().size());
    }
}