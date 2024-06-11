import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        option = new Option("o", "output", true, "Specifies the output destination");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("o"));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        
        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("o"));
    }
    
    @Test
    public void testGetValuesWithDefaultValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        
        List<String> defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        commandLine.setDefaultValues(option, defaultValues);
        
        List<String> values = commandLine.getValues(option, defaultValues);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
        
        defaultValues.add("default2.txt");
        values = commandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("output.txt", values.get(0));
        assertEquals("default2.txt", values.get(1));
        
        values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
        
        commandLine.addValue(option, "output.txt");
        
        values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addOption(option);
        
        assertFalse(commandLine.getSwitch(option, false));
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        assertEquals(null, commandLine.getProperty("property1"));
        
        commandLine.addProperty(option, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option, "property1", null));
        assertEquals(null, commandLine.getProperty(option, "property2", null));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        
        assertEquals("value1", commandLine.getProperty(option, "property1", null));
    }
    
    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "value1");
        
        assertEquals(1, commandLine.getProperties(option).size());
        assertEquals("property1", commandLine.getProperties(option).iterator().next());
        
        assertEquals(0, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--output"));
        assertFalse(commandLine.looksLikeOption("output.txt"));
    }
    
    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        
        assertEquals("-o output.txt", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertEquals("o", commandLine.getOptionTriggers().iterator().next());
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getOptionTriggers());
        
        commandLine.setDefaultValues(option, null);
        
        assertEquals(0, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
        
        commandLine.setDefaultSwitch(option, null);
        
        assertFalse(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        commandLine.addValue(option, "output.txt");
        
        assertEquals(1, commandLine.getNormalised().size());
        assertEquals("output.txt", commandLine.getNormalised().get(0));
    }
}