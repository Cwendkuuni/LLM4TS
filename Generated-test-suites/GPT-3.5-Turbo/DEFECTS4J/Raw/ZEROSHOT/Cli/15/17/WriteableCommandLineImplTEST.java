import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option, null).size());
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertNull(commandLine.getOption("test"));
        
        commandLine.addOption(option);
        
        assertNotNull(commandLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertEquals(0, commandLine.getValues(option, null).size());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getValues(option, null).size());
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertEquals(0, commandLine.getUndefaultedValues(option).size());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertNull(commandLine.getSwitch(option, null));
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertNull(commandLine.getProperty("property1"));
        
        Option propertyOption = new PropertyOption();
        commandLine.addProperty(propertyOption, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(propertyOption, "property1", null));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertNull(commandLine.getProperty("property1"));
        
        commandLine.addProperty("property1", "value1");
        
        assertEquals("value1", commandLine.getProperty("property1"));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals("default", commandLine.getProperty("property1", "default"));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals(0, commandLine.getProperties().size());
        
        Option propertyOption = new PropertyOption();
        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");
        
        assertEquals(2, commandLine.getProperties(propertyOption).size());
        assertTrue(commandLine.getProperties(propertyOption).contains("property1"));
        assertTrue(commandLine.getProperties(propertyOption).contains("property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals("", commandLine.toString());
        
        List<String> arguments = new ArrayList<String>();
        arguments.add("-option1");
        arguments.add("value1");
        arguments.add("value2");
        
        commandLine = new WriteableCommandLineImpl(new Option("test"), arguments);
        
        assertEquals("-option1 value1 value2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals(0, commandLine.getOptions().size());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals(0, commandLine.getOptionTriggers().size());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertNull(commandLine.getValues(option, null));
        
        commandLine.setDefaultValues(option, Collections.singletonList("default1"));
        
        assertEquals(1, commandLine.getValues(option, null).size());
        assertTrue(commandLine.getValues(option, null).contains("default1"));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        
        assertNull(commandLine.getSwitch(option, null));
        
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), null);
        
        assertEquals(0, commandLine.getNormalised().size());
        
        List<String> arguments = new ArrayList<String>();
        arguments.add("-option1");
        arguments.add("value1");
        arguments.add("value2");
        
        commandLine = new WriteableCommandLineImpl(new Option("test"), arguments);
        
        assertEquals(3, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains("-option1"));
        assertTrue(commandLine.getNormalised().contains("value1"));
        assertTrue(commandLine.getNormalised().contains("value2"));
    }
}