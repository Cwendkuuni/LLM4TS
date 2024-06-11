import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import java.util.*;

public class WriteableCommandLineImplTest {
    private Option rootOption;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Argument();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }
    
    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("switch");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new OptionImpl("test");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new OptionImpl("test");
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Argument();
        List defaultValues = Arrays.asList("default");
        List values = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List result = commandLine.getValues(option, null);
        assertEquals(values, result);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        List values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List result = commandLine.getUndefaultedValues(option);
        assertEquals(values, result);
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl("switch");
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new PropertyOption();
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties().contains(property));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-test";
        assertFalse(commandLine.looksLikeOption(trigger));
        rootOption.addPrefix("-");
        assertTrue(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        List arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(expected, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        List options = new ArrayList();
        commandLine.addOption(new OptionImpl("test"));
        options.add(new OptionImpl("test"));
        assertEquals(options, commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(new OptionImpl("test"));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List defaultValues = Arrays.asList("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("switch");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(arguments, commandLine.getNormalised());
    }
}