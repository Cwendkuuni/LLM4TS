import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("arg", "argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        commandLine.addValue(option, "value");
        
        assertEquals(Arrays.asList("value"), commandLine.getValues(option, new ArrayList<String>()));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("sw", "switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateSwitch() {
        Option option = new Option("sw", "switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        assertEquals(option1, commandLine.getOption("opt1"));
        assertNull(commandLine.getOption("opt2"));
        
        commandLine.addOption(option2);
        
        assertEquals(option2, commandLine.getOption("opt2"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("arg1", "argument1");
        Option option2 = new Option("arg2", "argument2");
        List<String> defaultValues = Arrays.asList("default");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        assertNull(commandLine.getValues(option1, new ArrayList<String>()));
        
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, new ArrayList<String>()));
        assertEquals(defaultValues, commandLine.getValues(option2, defaultValues));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("sw1", "switch1");
        Option option2 = new Option("sw2", "switch2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        assertNull(commandLine.getSwitch(option1, null));
        
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);
        
        assertTrue(commandLine.getSwitch(option1, false));
        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<Argument>());
        
        commandLine.addProperty("key", "value");
        
        assertEquals("value", commandLine.getProperty("key", "default"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<Argument>());
        
        assertNull(commandLine.getProperty("key", null));
        
        commandLine.addProperty("key", "value");
        
        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<Argument>());
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        
        Properties properties = commandLine.getProperties();
        
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value2", properties.getProperty("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<Argument>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        assertTrue(commandLine.getOptions().isEmpty());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<Option> expectedOptions = Arrays.asList(option1, option2);
        
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<String> expectedTriggers = Arrays.asList("opt1", "opt2");
        
        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("arg1", "argument1");
        Option option2 = new Option("arg2", "argument2");
        List<String> defaults = Arrays.asList("default");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        commandLine.setDefaultValues(option1, defaults);
        
        assertEquals(defaults, commandLine.getValues(option1, new ArrayList<String>()));
        
        commandLine.setDefaultValues(option1, null);
        
        assertNull(commandLine.getValues(option1, new ArrayList<String>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("sw1", "switch1");
        Option option2 = new Option("sw2", "switch2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<Argument>());
        
        commandLine.setDefaultSwitch(option1, true);
        
        assertTrue(commandLine.getSwitch(option1, null));
        
        commandLine.setDefaultSwitch(option1, null);
        
        assertNull(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        
        List<String> expectedNormalised = Arrays.asList("arg1", "arg2");
        
        assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}