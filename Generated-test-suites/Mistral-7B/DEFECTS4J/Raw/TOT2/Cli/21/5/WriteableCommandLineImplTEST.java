package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
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

    /**
     * Test method for {@link WriteableCommandLineImpl#addOption(Option)}
     */
    @Test
    public void testAddOption() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOption(String)}
     */
    @Test
    public void testGetOption() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOptions()}
     */
    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#hasOption(Option)}
     */
    @Test
    public void testHasOption() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#toString()}
     */
    @Test
    public void testToString() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        String expected = "option";
        String commandLineString = commandLine.toString();
        Assert.assertEquals(expected, commandLineString);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getValues(Option, List)}
     */
    @Test
    public void testGetValues() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Collections.emptyList(), values);

        commandLine.addValue(option, "value1");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(Collections.singletonList("value1"), values);

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(Collections.singletonList("value2"), values);

        commandLine.addValue(option, "value1");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getUndefaultedValues(Option)}
     */
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option")
                .setValueSeparator(' ')
                .setDefaultValues(new String[]{"defaultValue1", "defaultValue2"});
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);

        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.emptyList(), values);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getSwitch(Option, Boolean)}
     */
    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, null));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(String)}
     */
    @Test
    public void testGetProperty() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        commandLine.addProperty(option, "property", properties);
        Assert.assertNull(commandLine.getProperty("property"));
        Assert.assertEquals(properties, commandLine.getProperty("property", new Properties()));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addProperty(Option, String, String)}
     */
    @Test
    public void testAddProperty() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);
        Assert.assertEquals("value1", commandLine.getProperty("property"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addProperty(String, String)}
     */
    @Test
    public void testAddPropertyWithOption() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty("property", properties);
        Assert.assertEquals("value1", commandLine.getProperty("property"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties(Option)}
     */
    @Test
    public void testGetProperties() {
        Option option = new Option("o", "option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        commandLine.addProperty(option, "property1", properties);
        Assert.assertEquals(Collections.singletonMap("property1", properties), commandLine.getProperties(option));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties()}
     */
    @Test
    public void testGetPropertiesWithNoOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Assert.assertEquals(Collections.emptyMap(), commandLine.getProperties());
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#looksLikeOption(String)}
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Assert.assertFalse(commandLine.looksLikeOption("option"));
        commandLine.addOption(new Option("o", "option"));
        Assert.assertTrue(commandLine.looksLikeOption("option"));
    }
}