import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        
        assertTrue(cmdLine.hasOption(option));
        assertEquals(option, cmdLine.getOption("test"));
        assertEquals(option, cmdLine.getOption("t"));
    }
    
    @Test
    public void testAddValue() {
        Option argument = new Argument();
        Object value = "test";
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        cmdLine.addValue(argument, value);
        
        List<List> values = (List<List>) cmdLine.getValues(argument, new ArrayList<>());
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addSwitch(option, true);
        
        assertTrue(cmdLine.hasOption(option));
        assertEquals(Boolean.TRUE, cmdLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        assertFalse(cmdLine.hasOption(option));
        
        cmdLine.addOption(option);
        
        assertTrue(cmdLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        assertNull(cmdLine.getOption("test"));
        
        cmdLine.addOption(option);
        
        assertEquals(option, cmdLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        Option argument = new Argument();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        List<String> values = (List<String>) cmdLine.getValues(argument, defaultValues);
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option argument = new Argument();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        List<String> values = (List<String>) cmdLine.getUndefaultedValues(argument);
        assertEquals(new ArrayList<>(), values);
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        assertNull(cmdLine.getSwitch(option, null));
        
        cmdLine.addSwitch(option, false);
        
        assertEquals(Boolean.FALSE, cmdLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        assertNull(cmdLine.getProperty("test"));
        
        cmdLine.addProperty("test", "value");
        
        assertEquals("value", cmdLine.getProperty("test"));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addProperty(option, "test", "value");
        
        Set<String> properties = (Set<String>) cmdLine.getProperties(option);
        assertTrue(properties.contains("test"));
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        assertEquals("default", cmdLine.getProperty(new PropertyOption(), "test", "default"));
    }
    
    @Test
    public void testGetPropertiesOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        Set<String> properties = (Set<String>) cmdLine.getProperties(new PropertyOption());
        assertNotNull(properties);
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        Set<String> properties = (Set<String>) cmdLine.getProperties();
        assertNotNull(properties);
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        
        assertFalse(cmdLine.looksLikeOption("test"));
        
        Set<String> prefixes = Set.of("-", "--");
        Option option = new Option("test", prefixes);
        
        cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertTrue(cmdLine.looksLikeOption("--test"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), List.of("arg1", "arg2"));
        assertEquals("arg1 arg2", cmdLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        
        List<Option> options = cmdLine.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        
        Set<String> triggers = cmdLine.getOptionTriggers();
        assertTrue(triggers.contains("test"));
        assertTrue(triggers.contains("t"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        
        cmdLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, cmdLine.getValues(option, new ArrayList<>()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        Boolean defaultSwitch = false;
        
        cmdLine.setDefaultSwitch(option, defaultSwitch);
        
        assertEquals(defaultSwitch, cmdLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("test");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, List.of("arg1", "arg2"));
        
        List<String> args = cmdLine.getNormalised();
        assertEquals(List.of("arg1", "arg2"), args);
    }
}