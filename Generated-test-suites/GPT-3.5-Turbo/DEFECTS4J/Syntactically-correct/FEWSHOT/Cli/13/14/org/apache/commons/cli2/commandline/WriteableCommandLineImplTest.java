import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option rootOption;

    @Before
    public void setUp() {
        rootOption = new Option("r", "root", true);
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains("t"));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        assertEquals(2, commandLine.getValues(argument, null).size());
        assertTrue(commandLine.getValues(argument, null).contains("value1"));
        assertTrue(commandLine.getValues(argument, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false);
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("h", "help", false);
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true);
        assertNull(commandLine.getOption("o"));

        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("v", "values", true);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        assertNull(commandLine.getValues(option, null));
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));

        assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("sw", "switch", false);
        assertNull(commandLine.getSwitch(option, null));
        
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");
        
        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testGetProperty() {
        assertEquals("default", commandLine.getProperty("key", "default"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        
        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("key1"));
        assertTrue(commandLine.getProperties().contains("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(rootOption, "value1");
        commandLine.addValue(rootOption, "value2");
        
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true);
        Option option2 = new Option("o2", "option2", true);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", true);
        Option option2 = new Option("o2", "option2", true);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(4, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o1"));
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("o2"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("dv", "defaultValues", true);
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
        
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("ds", "defaultSwitch", false);
        
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
        
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, normalised);
        
        assertEquals(normalised, commandLine.getNormalised());
    }

}