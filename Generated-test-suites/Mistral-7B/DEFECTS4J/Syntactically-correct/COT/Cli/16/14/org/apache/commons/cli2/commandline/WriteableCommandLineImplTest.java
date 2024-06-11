import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("root", "", false, "root description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(arguments, commandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
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
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("test");
        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        Option optionWithDefaultValues = new Option("test", "t", false, "test option");
        commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(optionWithDefaultValues);
        commandLine.setDefaultValues(optionWithDefaultValues, Collections.singletonList("default1"));
        values = commandLine.getValues(optionWithDefaultValues, null);
        assertEquals(1, values.size());
        assertEquals("default1", values.get(0));

        commandLine.addValue(optionWithDefaultValues, "value1");
        values = commandLine.getValues(optionWithDefaultValues, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("value1", values.get(1));

        commandLine.setDefaultValues(optionWithDefaultValues, null);
        values = commandLine.getValues(optionWithDefaultValues, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        assertNull(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Properties properties = new Properties();
        properties.put("property", "value");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", properties);
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Properties retrievedProperties = commandLine.getProperties(option);
        assertEquals(1, retrievedProperties.size());
        assertEquals("value1", retrievedProperties.getProperty("property1"));

        commandLine.addProperty(option, "property2", "value2");
        retrievedProperties = commandLine.getProperties(option);
        assertEquals(2, retrievedProperties.size());
        assertEquals("value1", retrievedProperties.getProperty("property1"));
        assertEquals("value2", retrievedProperties.getProperty("property2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        Assert.assertTrue(propertiesSet.contains("property1"));
        Assert.assertEquals("value1", commandLine.getProperty(option, "property1"));

        commandLine.addProperty(option, "property2", "value2");
        propertiesSet = commandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        Assert.assertTrue(propertiesSet.contains("property1"));
        Assert.assertTrue(propertiesSet.contains("property2"));
        Assert.assertEquals("value1", commandLine.getProperty(option, "property1"));
        Assert.assertEquals("value2", commandLine.getProperty(option, "property2"));

        commandLine.addProperty(option, "property1", new Properties());
        propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        Assert.assertEquals(Collections.emptyMap(), commandLine.getProperties(option));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("test1", "t1", false, "test option 1");
        Option option2 = new Option("test2", "t2", false, "test option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        String result = commandLine.toString();
        String expected = Arrays.toString(new String[]{"arg1", "arg2", "--test1", "--test2"});
        assertEquals(expected, result);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertTrue(commandLine.looksLikeOption("-t"));
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--test1"));
        assertFalse(commandLine.looksLikeOption("test"));
        assertFalse(commandLine.looksLikeOption("test1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(defaultValues, values);

        commandLine.addValue(option, "value1");
        values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("default1", "default2", "value1"), values);

        commandLine.setDefaultValues(option, null);
        values = commandLine.getValues(option, null);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("test", "t", false, "test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List normalised = commandLine.getNormalised();
        List expected = Arrays.asList("--test", "value1");
        assertEquals(expected, normalised);

        commandLine.addValue(option, "value2");
        normalised = commandLine.getNormalised();
        expected = Arrays.asList("--test", "value1", "value2");
        assertEquals(expected, normalised);
    }
}