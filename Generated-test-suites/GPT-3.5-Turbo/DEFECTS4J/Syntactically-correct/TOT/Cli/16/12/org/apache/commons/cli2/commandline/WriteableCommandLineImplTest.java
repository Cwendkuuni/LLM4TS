import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("r", "root", true, "Root option");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOption("t"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("t", "test", true, "Test option");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("t"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(commandLine.getValues(option, null).size(), 2);
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(commandLine.getUndefaultedValues(option).size(), 2);
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(commandLine.getProperty(option, "property1", null), "value1");
        assertEquals(commandLine.getProperty(option, "property2", null), "value2");
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addProperty(option, "property1", "value1");
        Properties properties = (Properties) commandLine.optionToProperties.get(option);
        assertNotNull(properties);
        assertEquals(properties.getProperty("property1"), "value1");
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(commandLine.getProperties(option).size(), 2);
        assertTrue(commandLine.getProperties(option).contains("property1"));
        assertTrue(commandLine.getProperties(option).contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg1"));
        assertTrue(commandLine.looksLikeOption("-r"));
    }

    @Test
    public void testToString() {
        assertEquals(commandLine.toString(), "arg1 arg2");
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        assertEquals(commandLine.getOptions().size(), 1);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        assertEquals(commandLine.getOptionTriggers().size(), 2);
        assertTrue(commandLine.getOptionTriggers().contains("t"));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("t", "test", true, "Test option");
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(commandLine.defaultValues.get(option), defaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("t", "test", true, "Test option");
        commandLine.setDefaultSwitch(option, true);
        assertEquals(commandLine.defaultSwitches.get(option), true);
    }

    @Test
    public void testGetNormalised() {
        assertEquals(commandLine.getNormalised(), commandLine.normalised);
    }
}