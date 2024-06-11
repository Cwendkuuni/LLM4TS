package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("b", "b", false, "test");
        commandLine.addOption(option);

        Option result = commandLine.getOption("b");
        assertTrue(result != null);
        assertEquals("b", result.getLongOpt());
        assertEquals("test", result.getDescription());
        assertEquals(false, result.isRequired());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        Option result = commandLine.getOption("a");
        assertTrue(result != null);
        assertEquals("a", result.getLongOpt());
        assertEquals("test", result.getDescription());
        assertEquals(false, result.isRequired());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List unfaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, unfaultedValues.size());
        assertEquals("value1", unfaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        Boolean defaultSwitchValue = commandLine.getSwitch(option, null);
        assertNull(defaultSwitchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("p", "p", false, "test");
        PropertyOption propertyOption = new PropertyOption("p", "p", "value", "test");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String result = commandLine.getProperty("property");
        assertEquals("value", result);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("p", "p", false, "test");
        PropertyOption propertyOption = new PropertyOption("p", "p", "value", "test");
        commandLine.addOption(option);
        commandLine.addProperty(propertyOption, "property", "value");
        Map properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.containsKey("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        String result = commandLine.toString();
        String expected = "value1 a";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option1 = new Option("a1", "a1", false, "test1");
        Option option2 = new Option("a2", "a2", false, "test2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a1", "a1", false, "test1");
        Option option2 = new Option("a2", "a2", false, "test2");
        commandLine.addOption(option);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("a1", "a2")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Option option1 = new Option("a1", "a1", false, "test1");
        Option option2 = new Option("a2", "a2", false, "test2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List normalised = commandLine.getNormalised();
        assertEquals(4, normalised.size());
    }
}