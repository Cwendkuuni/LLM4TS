package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Constructor() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        List arguments = new ArrayList();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertNotNull(writeableCommandLine);
        assertEquals(rootOption, writeableCommandLine.getOption(rootOption.getPreferredName()));
        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getProperties());
        assertEquals(Collections.emptyList(), writeableCommandLine.getNormalised());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        Option option2 = new Option("option2", "opt2", true, "Option 2 description");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));

        Map nameToOption = writeableCommandLine.getNameToOption();
        assertEquals(2, nameToOption.size());
        assertEquals(option1, nameToOption.get(option1.getPreferredName()));
        assertEquals(option2, nameToOption.get(option2.getPreferredName()));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("option", "opt", false, "Option description");
        Argument argument = new Argument("arg", "argDesc", "argVal");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, argument);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(argument, values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        Option option2 = new Option("option2", "opt2", true, "Option 2 description");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Option retrievedOption1 = writeableCommandLine.getOption("opt1");
        Option retrievedOption2 = writeableCommandLine.getOption("opt2");

        assertEquals(option1, retrievedOption1);
        assertEquals(option2, retrievedOption2);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("option", "opt", false, "Option description");
        Argument arg1 = new Argument("arg1", "arg1Desc", "arg1Val");
        Argument arg2 = new Argument("arg2", "arg2Desc", "arg2Val");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals(arg1, values.get(0));
        assertEquals(arg2, values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("option", "opt", false, "Option description");
        Argument arg1 = new Argument("arg1", "arg1Desc", "arg1Val");
        Argument arg2 = new Argument("arg2", "arg2Desc", "arg2Val");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), null);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        writeableCommandLine.addProperty(option, "property3", "value3");

        String propertyValue = writeableCommandLine.getProperty(option, "property1");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, properties);

        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesMap.size());
        assertEquals("value1", propertiesMap.get("property1"));
        assertEquals("value2", propertiesMap.get("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty_withDefaultValue() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        writeableCommandLine.addProperty(option, "property3", "defaultValue3");

        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        assertEquals(3, propertiesMap.size());
        assertEquals("value1", propertiesMap.get("property1"));
        assertEquals("value2", propertiesMap.get("property2"));
        assertEquals("defaultValue3", propertiesMap.get("property3"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("option", "opt", false, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, properties);

        Set<String> propertiesKeys = writeableCommandLine.getProperties(option).keySet();
        assertEquals(2, propertiesKeys.size());
        assertTrue(propertiesKeys.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", true, "Root option description"), new ArrayList());

        assertTrue(writeableCommandLine.looksLikeOption("--opt"));
        assertTrue(writeableCommandLine.looksLikeOption("-o"));
        assertFalse(writeableCommandLine.looksLikeOption("--arg"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        String commandLineString = writeableCommandLine.toString();
        String expectedCommandLineString = rootOption.getLongOpt() + " " + String.join(" ", arguments);
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        Option option2 = new Option("option2", "opt2", true, "Option 2 description");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        Option option2 = new Option("option2", "opt2", true, "Option 2 description");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList(rootOption.getPreferredName(), "opt1", "opt2")));
    }
}