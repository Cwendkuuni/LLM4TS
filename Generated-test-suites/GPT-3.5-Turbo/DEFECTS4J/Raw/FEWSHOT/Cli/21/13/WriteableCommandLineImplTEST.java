import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setup() {
        Option rootOption = new Option("rootOption");
        Argument argument1 = new Argument();
        Argument argument2 = new Argument();
        commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList(argument1, argument2));
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertEquals(true, commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("option");
        commandLine.addValue(option, "value");
        List<Object> values = commandLine.getValues(option, null);
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        assertEquals(true, value);
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertEquals(true, commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        Option result = commandLine.getOption("option");
        assertEquals(option, result);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<Object> values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        assertEquals(true, value);
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("option");
        commandLine.addProperty(option, "property", "value");
        String result = commandLine.getProperty(option, "property", "default");
        assertEquals("value", result);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("option");
        commandLine.addProperty(option, "property", "value");
        String result = commandLine.getProperty(option, "property", "default");
        assertEquals("value", result);
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set<String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals(true, properties.contains("property1"));
        assertEquals(true, properties.contains("property2"));
    }
    
    @Test
    public void testGetPropertiesWithoutOption() {
        Set<String> properties = commandLine.getProperties();
        assertEquals(0, properties.size());
    }
    
    @Test
    public void testLooksLikeOption() {
        boolean result = commandLine.looksLikeOption("--option");
        assertEquals(true, result);
    }
    
    @Test
    public void testToString() {
        Option option = new Option("--option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        commandLine.addSwitch(option, true);
        String result = commandLine.toString();
        assertEquals("--option value true", result);
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("option");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option");
        commandLine.addOption(option);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertEquals(true, triggers.contains("option"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<Object> defaults = Arrays.asList("default");
        commandLine.setDefaultValues(option, defaults);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals("default", values.get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        commandLine.setDefaultSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        assertEquals(true, value);
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("option");
        Argument argument1 = new Argument();
        Argument argument2 = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(argument1, argument2));
        assertEquals(Arrays.asList(argument1, argument2), commandLine.getNormalised());
    }
}