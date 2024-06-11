import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "option", true, "an option");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option result = commandLine.getOption("o");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        String result = commandLine.getProperty(option, "property1", "defaultValue");
        assertEquals("value1", result);
    }

    @Test
    public void testAddProperty() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Map result = commandLine.getProperties(option);
        assertEquals(1, result.size());
        assertEquals("property1", result.keySet().iterator().next());
        assertEquals("value1", result.get("property1"));
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Set result = commandLine.getProperties(option);
        assertEquals(1, result.size());
        assertTrue(result.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("a"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String result = commandLine.toString();
        String expected = "option value1 value2";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(option, "value1");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("option", normalised.get(0).toString());
        assertEquals("value1", normalised.get(1).toString());
    }
}