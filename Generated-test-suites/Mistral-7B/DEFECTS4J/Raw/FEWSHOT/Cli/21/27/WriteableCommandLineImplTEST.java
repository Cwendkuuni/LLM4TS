package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWritableCommandLineImpl() {
        Option rootOption = new Option("root", "root option", true, "default-value");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals("root option", commandLine.getOption(0).getLongOpt());
        assertEquals("root option", commandLine.getOption(0).getOpt());
        assertEquals("default-value", commandLine.getDefaultValue(commandLine.getOption(0)));
        assertEquals(true, commandLine.getDefaultSwitch(commandLine.getOption(0), true));
        assertEquals(Collections.emptyList(), commandLine.getProperties(commandLine.getOption(0)));
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(1, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption(0);
        assertEquals("opt1", opt1.getLongOpt());
        assertEquals("opt1", opt1.getOpt());
        assertFalse(opt1.isRequired());
        assertEquals(Collections.emptyList(), opt1.getValues());
        assertEquals(Collections.emptyMap(), opt1.getValuesMap());
        assertEquals(Collections.emptyMap(), opt1.getSwitches());
        assertEquals(Collections.emptyMap(), opt1.getProperties());

        Option opt2 = new Option("opt2", "option2", true, "default-value");
        commandLine.addOption(opt2);

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(2, commandLine.getOptions().size());
        Option opt3 = commandLine.getOption(1);
        assertEquals("opt2", opt3.getLongOpt());
        assertEquals("opt2", opt3.getOpt());
        assertTrue(opt3.isRequired());
        assertEquals(Collections.singletonList("default-value"), opt3.getValues());
        assertEquals(Collections.emptyMap(), opt3.getValuesMap());
        assertEquals(Collections.emptyMap(), opt3.getSwitches());
        assertEquals(Collections.emptyMap(), opt3.getProperties());
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(1, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption(0);
        assertEquals("opt1", opt1.getLongOpt());
        assertEquals("opt1", opt1.getOpt());
        assertFalse(opt1.isRequired());
        assertEquals(Arrays.asList("value1", "value2"), opt1.getValues());
        assertEquals(Collections.singletonMap("value1", "value1"), opt1.getValuesMap());
        assertEquals(Collections.emptyMap(), opt1.getSwitches());
        assertEquals(Collections.emptyMap(), opt1.getProperties());
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addSwitch(option1, true);

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(1, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption(0);
        assertEquals("opt1", opt1.getLongOpt());
        assertEquals("opt1", opt1.getOpt());
        assertFalse(opt1.isRequired());
        assertEquals(Collections.emptyList(), opt1.getValues());
        assertEquals(Boolean.TRUE, opt1.getSwitchValue());
        assertEquals(Collections.emptyMap(), opt1.getSwitches());
        assertEquals(Collections.emptyMap(), opt1.getProperties());
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
        Option option2 = new Option("opt2", "option2", false);
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        Option option2 = new Option("opt2", "option2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option opt1 = commandLine.getOption("opt1");
        Option opt2 = commandLine.getOption("opt2");

        assertEquals(option1, opt1);
        assertEquals(option2, opt2);
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List values = commandLine.getValues(option1, Collections.emptyList());

        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");

        List values = commandLine.getUndefaultedValues(option1);

        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addSwitch(option1, true);

        Boolean switchValue = commandLine.getSwitch(option1, false);

        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        commandLine.addProperty(option1, "property1", "value1");

        String propertyValue = commandLine.getProperty(option1, "property1");

        assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");

        commandLine.addProperty(option1, "property1", "value1");

        Properties optionProperties = commandLine.getProperties(option1);

        assertEquals(properties, optionProperties);
    }

    @Test
    public void testGetProperties() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        Option option1 = new Option("opt1", "option1", false);
        commandLine.addOption(option1);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");

        commandLine.addProperty(option1, "property1", "value1");

        Map optionProperties = commandLine.getProperties(option1);

        assertEquals(properties, optionProperties);

        Map allProperties = commandLine.getProperties();

        assertEquals(1, allProperties.size());
        assertEquals(properties, allProperties.get(option1.getOpt()));
    }

    @Test
    public void testLooksLikeOption() {
        Option rootOption = new Option("root", "root option", true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        assertTrue(commandLine.looksLikeOption("opt1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }
}