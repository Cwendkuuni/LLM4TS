package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.Argument;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli.WriteableCommandLine.WriteableCommandLineBuilder;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateWriteableCommandLine() {
        Option rootOption = OptionBuilder.withLongOpt("rootOption")
                .hasArg(true)
                .withArgName("arg")
                .create("rootOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertNull(commandLine.getOption("nonExistentOption"));
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptyMap(), commandLine.getProperties());
        assertEquals(Collections.emptyMap(), commandLine.getNameToOption());
        assertEquals(Collections.emptyMap(), commandLine.getValues());
        assertEquals(Collections.emptyMap(), commandLine.getSwitches());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singletonMap("option", option), commandLine.getNameToOption());
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        Option returnedOption = commandLine.getOption("option");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);

        List emptyValues = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Collections.emptyList(), emptyValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("option")
                .argName("arg")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--option", "arg1", "arg2"));
        commandLine.addOption(option);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        Boolean defaultSwitchValue = commandLine.getSwitch(option, false);
        assertFalse(defaultSwitchValue);
    }

    @Test
    public void testgetProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Properties properties = commandLine.getProperties(option);
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        expectedProperties.put("property2", "value2");
        assertEquals(expectedProperties, properties);
    }

    @Test
    public void testGetProperties() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        Properties properties = commandLine.getProperties();
        assertEquals(Collections.emptyMap(), properties);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Properties returnedProperties = commandLine.getProperties();
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        expectedProperties.put("property2", "value2");
        assertEquals(expectedProperties, returnedProperties);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--option", "arg"));

        assertTrue(commandLine.looksLikeOption("option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("--otherOption"));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .create("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--option", "arg1", "arg2"));
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "--option arg1 value1 value2 arg2";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg(false)
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg(true)
                .create("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg(false)
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg(true)
                .create("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        Set expectedOptionTriggers = new HashSet<>();
        expectedOptionTriggers.add("option1");
        expectedOptionTriggers.add("option2");
        assertEquals(expectedOptionTriggers, optionTriggers);
    }
}