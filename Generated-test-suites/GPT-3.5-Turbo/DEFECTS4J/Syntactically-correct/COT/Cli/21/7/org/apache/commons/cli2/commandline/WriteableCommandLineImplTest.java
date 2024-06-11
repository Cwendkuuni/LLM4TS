
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("o", "option", true, "Option");
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "Option");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "Option");
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        Option option = new Option("p", "property", true, "Property");
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property");
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        List<String> properties = commandLine.getProperties(option);
        assertEquals(Arrays.asList("key1", "key2"), properties);
    }

    @Test
    public void testGetProperties_All() {
        Option option = new Option("p", "property", true, "Property");
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        commandLine.addProperty("key3", "value3");
        List<String> properties = commandLine.getProperties();
        assertEquals(Arrays.asList("key1", "key2", "key3"), properties);
    }
}
