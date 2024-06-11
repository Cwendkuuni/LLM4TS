import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOption;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.CommandLine;
import org.apache.commons.cli2.option.PropertyOption;


public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Argument argument = new Argument();
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Argument argument = new Argument();
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Argument argument = new Argument();
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getUndefaultedValues(argument).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(null, commandLine.getProperty("property"));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("default", commandLine.getProperty("property", "default"));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");
        assertEquals(1, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(true, commandLine.looksLikeOption("--option"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.setDefaultValues(option, null);
        assertEquals(null, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new DefaultOption();
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getNormalised().size());
    }
}