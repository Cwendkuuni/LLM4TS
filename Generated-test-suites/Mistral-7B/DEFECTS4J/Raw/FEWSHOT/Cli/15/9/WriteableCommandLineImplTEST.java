package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.PropertyOption;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLine.WriteableOption;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateInstance() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .withDescription("The root option")
                .withValueSeparator(':')
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2:value1:value2"));

        assertEquals("arg1 arg2:value1:value2", commandLine.toString());
    }

    @Test
    public void testAddOption() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertSame(option1, commandLine.getOption(option1.getLongOpt()));
        assertSame(option2, commandLine.getOption(option2.getLongOpt()));
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine1 = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine1.addOption(option);

        WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(null, new ArrayList<>());

        assertTrue(commandLine1.hasOption(option));
        assertFalse(commandLine2.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");

        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, ""));
    }

    @Test
    public void testAddProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        commandLine.addProperty(option, "property", "value");
        commandLine.addProperty(option, properties);

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property", "value");
        expectedProperties.putAll(properties);

        Map<String, String> propertiesMap = commandLine.getProperties(option);
        assertEquals(expectedProperties, propertiesMap);
    }

    @Test
    public void testAddPropertyWithSameName() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");

        commandLine.addProperty(option, "property", "value");
        commandLine.addProperty(option, "property", "value2");
        commandLine.addProperty(option, properties);

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property", "value2");
        expectedProperties.putAll(properties);

        Map<String, String> propertiesMap = commandLine.getProperties(option);
        assertEquals(expectedProperties, propertiesMap);
    }

    @Test
    public void testGetProperties() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        expectedProperties.put("property2", "value2");

        Map<String, String> propertiesMap = commandLine.getProperties(option);
        assertEquals(expectedProperties, propertiesMap);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("--option="));
        assertTrue(commandLine.looksLikeOption("--option=" + "value"));
        assertFalse(commandLine.looksLikeOption("--"));
        assertFalse(commandLine.looksLikeOption(""));
        assertFalse(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("option="));
        assertFalse(commandLine.looksLikeOption("option=" + "value"));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals("--option value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add(option1.getLongOpt());
        expectedTriggers.add(option2.getLongOpt());

        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(expectedTriggers, triggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitchFalse() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("Option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        commandLine.setDefaultSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, false));
    }
}