import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option("rootOption", "Root Option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("testArgument", "Test Argument");
        commandLine.addValue(argument, "value");
        assertTrue(commandLine.hasOption(argument));
        assertEquals(1, commandLine.getValues(argument, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption", "Test Option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption", "Test Option");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption", "Test Option");
        assertNull(commandLine.getOption(option.getPreferredName()));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("testArgument", "Test Argument");
        assertNull(commandLine.getValues(argument, null));
        commandLine.addValue(argument, "value");
        assertEquals(1, commandLine.getValues(argument, null).size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("testArgument", "Test Argument");
        assertEquals(Arrays.asList(), commandLine.getUndefaultedValues(argument));
        commandLine.addValue(argument, "value");
        assertEquals(1, commandLine.getUndefaultedValues(argument).size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption", "Test Option");
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        assertNull(commandLine.getProperty(property));
        commandLine.addProperty(property, "value");
        assertEquals("value", commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption", "Test Option");
        String property = "testProperty";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyDefault() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("testOption", "Test Option");
        String property = "testProperty";
        assertNull(commandLine.getProperties(option));
        commandLine.addProperty(option, property, "value");
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-testOption";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Argument argument = new Argument("testArgument", "Test Argument");
        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption", "Test Option");
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption", "Test Option");
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Argument argument = new Argument("testArgument", "Test Argument");
        commandLine.addValue(argument, "value");
        assertEquals(Collections.singletonList("value"), commandLine.getNormalised());
    }

}