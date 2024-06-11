package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        List valueList = commandLine.getValues(option, null);

        assertNull(valueList);

        commandLine.addValue(option, "output.txt");
        valueList = commandLine.getValues(option, null);

        assertEquals(Collections.singletonList("output.txt"), valueList);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertSame(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        List valueList = commandLine.getValues(option, null);

        assertNull(valueList);

        commandLine.addValue(option, "output.txt");
        valueList = commandLine.getValues(option, null);

        assertEquals(Collections.singletonList("output.txt"), valueList);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("output.txt"));

        List valueList = commandLine.getUndefaultedValues(option);

        assertEquals(Collections.emptyList(), valueList);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "property value");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName", "propertyValue");

        assertEquals("propertyValue", commandLine.getProperty(option, "propertyName", null));

        PropertyOption propertyOption = new PropertyOption("p", "property", true, "property value");
        commandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        commandLine.addOption(propertyOption);

        commandLine.addProperty(propertyOption, "propertyName", "propertyValue");

        assertEquals("propertyValue", commandLine.getProperty(propertyOption, "propertyName", null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "property value");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName1", "propertyValue1");
        commandLine.addProperty(option, "propertyName2", "propertyValue2");

        Set properties = commandLine.getProperties(option);

        assertEquals(2, properties.size());
        assertTrue(properties.contains("propertyName1"));
        assertTrue(properties.contains("propertyName2"));

        PropertyOption propertyOption = new PropertyOption("p", "property", true, "property value");
        commandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        commandLine.addOption(propertyOption);

        commandLine.addProperty(propertyOption, "propertyName1", "propertyValue1");
        commandLine.addProperty(propertyOption, "propertyName2", "propertyValue2");

        properties = commandLine.getProperties(propertyOption);

        assertEquals(2, properties.size());
        assertTrue(properties.contains("propertyName1"));
        assertTrue(properties.contains("propertyName2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "output file"), Collections.emptyList());

        assertTrue(commandLine.looksLikeOption("o"));
        assertTrue(commandLine.looksLikeOption("output"));
        assertFalse(commandLine.looksLikeOption("--output"));
        assertFalse(commandLine.looksLikeOption("--no-output"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("output.txt"));

        assertEquals("output.txt", commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = new Option("o1", "option1", false, "option1 description");
        Option option2 = new Option("o2", "option2", false, "option2 description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "option1 description");
        Option option2 = new Option("o2", "option2", false, "option2 description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "output file");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        List defaultValues = Collections.singletonList("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);

        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "output file");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("output.txt"));

        List normalised = commandLine.getNormalised();

        assertEquals(1, normalised.size());
        assertTrue(normalised.get(0).equals("output.txt"));
    }
}