import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import org.junit.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }
    
    @Test
    public void testAddValue() {
        Argument argument1 = new DefaultArgumentBuilder().create();
        Argument argument2 = new DefaultArgumentBuilder().create();
        Option option = new DefaultOptionBuilder().withArgument(argument1).create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(commandLine.getValues(option, null).size(), 2);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsExceptionWhenSwitchAlreadySet() {
        Option option = new DefaultOptionBuilder().create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }
    
    @Test
    public void testAddSwitch() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
        
        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        
        assertEquals(commandLine.getOption("option1"), option1);
        assertNull(commandLine.getOption("option2"));
    }
    
    @Test
    public void testGetValues() {
        Argument argument1 = new DefaultArgumentBuilder().create();
        Argument argument2 = new DefaultArgumentBuilder().create();
        Option option1 = new DefaultOptionBuilder().withLongName("option1").withArgument(argument1).create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").withArgument(argument2).create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        
        assertEquals(commandLine.getValues(option1, null).size(), 2);
        assertEquals(commandLine.getValues(option2, null).size(), 1);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Argument argument1 = new DefaultArgumentBuilder().create();
        Argument argument2 = new DefaultArgumentBuilder().create();
        Option option1 = new DefaultOptionBuilder().withLongName("option1").withArgument(argument1).create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").withArgument(argument2).create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        
        assertEquals(commandLine.getUndefaultedValues(option1).size(), 2);
        assertEquals(commandLine.getUndefaultedValues(option2).size(), 1);
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new DefaultOptionBuilder().withLongName("option").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        assertNull(commandLine.getProperty("property"));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        commandLine.addProperty("property", "value");
        assertEquals(commandLine.getProperty("property"), "value");
    }
    
    @Test
    public void testGetPropertyWithOption() {
        Option option = new DefaultOptionBuilder().create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option, "property", "value");
        
        assertEquals(commandLine.getProperty(option, "property", ""), "value");
    }
    
    @Test
    public void testGetProperties() {
        Option option = new DefaultOptionBuilder().create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        
        assertEquals(commandLine.getProperties(option).size(), 2);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(commandLine.getOptions().size(), 2);
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new DefaultOptionBuilder().withLongName("option1").create();
        Option option2 = new DefaultOptionBuilder().withLongName("option2").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(commandLine.getOptionTriggers().size(), 2);
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new DefaultOptionBuilder().create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        Option option = new DefaultOptionBuilder().withLongName("option").create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        
        assertEquals(commandLine.toString(), "-option value");
    }
    
    @Test
    public void testSetDefaultValues() {
        Argument argument = new DefaultArgumentBuilder().create();
        Option option = new DefaultOptionBuilder().withArgument(argument).create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        
        assertEquals(commandLine.getValues(option, null).size(), 2);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new DefaultOptionBuilder().create();
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, List.of("arg1", "arg2"));
        
        assertEquals(commandLine.getNormalised().size(), 2);
    }

}