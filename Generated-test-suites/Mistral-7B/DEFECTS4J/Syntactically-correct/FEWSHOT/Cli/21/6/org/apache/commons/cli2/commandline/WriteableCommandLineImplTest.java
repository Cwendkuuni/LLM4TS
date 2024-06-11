package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplCreation() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        Option option2 = new Option("opt2", "option two", true, "Option two");
        commandLine.addOption(option2);

        Option option1_long = new Option("longopt1", "long option one", false, "Long option one");
        commandLine.addOption(option1_long);

        Option option1_trig1 = new Option("trig1", "trig one", false, "Trig one");
        commandLine.addOption(option1_trig1);
        commandLine.addOption(option1_trig1);

        Option option1_trig2 = new Option("trig2", "trig two", false, "Trig two");
        commandLine.addOption(option1_trig2);

        assertEquals(4, commandLine.getOptions().size());
        assertEquals(new HashSet<>(Collections.singletonList("opt1")), commandLine.getOptionTriggers());

        Option rootOptionWithChildren = new Option("root_with_children", "root_with_children", false, "Root option with children");
        WriteableCommandLineImpl commandLineWithChildren = new WriteableCommandLineImpl(rootOptionWithChildren, new ArrayList<>());
        Option childOption = new Option("child", "child", false, "Child option");
        commandLineWithChildren.addOption(rootOptionWithChildren);
        commandLineWithChildren.addOption(childOption);
        assertEquals(2, commandLineWithChildren.getOptions().size());
        assertEquals(new HashSet<>(Collections.singletonList("child")), commandLineWithChildren.getOptionTriggers());
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new Option("opt2", "option two", false, "Option two")));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        Option option2 = new Option("opt2", "option two", true, "Option two");
        commandLine.addOption(option2);

        assertEquals(option1, commandLine.getOption("opt1"));
        assertNull(commandLine.getOption("opt3"));
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Argument arg1 = new Argument("arg1", "arg one");
        Argument arg2 = new Argument("arg2", "arg two");
        commandLine.addArgument(arg1);
        commandLine.addArgument(arg2);

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        Option optionValue1 = new Option("optValue1", "option value one", false, "Option value one");
        commandLine.addOption(optionValue1);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List emptyValues = commandLine.getValues(optionValue1, null);
        assertTrue(emptyValues.isEmpty());

        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        List valuesWithDefaults = commandLine.getValues(option1, defaultValues);
        assertEquals(4, valuesWithDefaults.size());
        assertEquals("value1", valuesWithDefaults.get(0));
        assertEquals("value2", valuesWithDefaults.get(1));
        assertEquals("defaultValue1", valuesWithDefaults.get(2));
        assertEquals("defaultValue2", valuesWithDefaults.get(3));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Argument arg1 = new Argument("arg1", "arg one");
        Argument arg2 = new Argument("arg2", "arg two");
        commandLine.addArgument(arg1);
        commandLine.addArgument(arg2);

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        Option optionValue1 = new Option("optValue1", "option value one", false, "Option value one");
        commandLine.addOption(optionValue1);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        List undefaultedValues = commandLine.getUndefaultedValues(option1);
        assertEquals(0, undefaultedValues.size());

        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaultValues);

        undefaultedValues = commandLine.getUndefaultedValues(option1);
        assertEquals(2, undefaultedValues.size());
        assertEquals("value1", undefaultedValues.get(0));
        assertEquals("value2", undefaultedValues.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);

        Option optionSwitch1 = new Option("optSwitch1", "opt switch one", false, "Opt switch one");
        commandLine.addOption(optionSwitch1);
        commandLine.addSwitch(optionSwitch1, true);

        Option optionSwitch2 = new Option("optSwitch2", "opt switch two", false, "Opt switch two");
        commandLine.addOption(optionSwitch2);
        commandLine.addSwitch(optionSwitch2, false);

        List switches = commandLine.getValues(optionSwitch1, null);
        assertTrue(switches.contains(Boolean.TRUE));
        assertTrue(switches.isEmpty() || !switches.contains(Boolean.FALSE));

        switches = commandLine.getValues(optionSwitch2, null);
        assertTrue(switches.isEmpty() || !switches.contains(Boolean.TRUE));
        assertTrue(switches.contains(Boolean.FALSE));
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option optionProperty = new PropertyOption("optProperty", "Option property", null);
        commandLine.addOption(optionProperty);

        commandLine.addProperty(optionProperty, "propertyValue1");
        assertEquals("propertyValue1", commandLine.getProperty("optProperty"));

        commandLine.addProperty(optionProperty, "propertyValue2");
        assertEquals("propertyValue2", commandLine.getProperty("optProperty"));

        commandLine.addProperty(optionProperty, "propertyValue3");
        assertEquals("propertyValue3", commandLine.getProperty("optProperty"));

        List properties = commandLine.getProperties(optionProperty);
        assertEquals(3, properties.size());

        Properties propertiesMap = new Properties();
        propertiesMap.setProperty("property1", "value1");
        propertiesMap.setProperty("property2", "value2");
        commandLine.addProperties(optionProperty, propertiesMap);
        assertEquals("value1", commandLine.getProperty("optProperty", "property1"));
        assertEquals("value2", commandLine.getProperty("optProperty", "property2"));
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Argument arg1 = new Argument("arg1", "arg one");
        Argument arg2 = new Argument("arg2", "arg two");
        commandLine.addArgument(arg1);
        commandLine.addArgument(arg2);

        Option option1 = new Option("opt1", "option one", false, "Option one");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        Option option2 = new Option("opt2", "option two", true, "Option two");
        commandLine.addOption(option2);

        Option optionProperty = new PropertyOption("optProperty", "Option property", null);
        commandLine.addOption(optionProperty);
        commandLine.addProperty(optionProperty, "propertyValue1");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "rootOption arg1 arg2 opt1 value1 optProperty propertyValue1";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptionsAndGetOptionTriggers() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option opt1 = new Option("opt1", "opt1", false, "Option 1");
        Option opt1_long = new Option("opt1-long", "opt1 long", false, "Long option 1");
        Option opt1_trig1 = new Option("trig1", "trig1", false, "Trig option 1");
        Option opt1_trig2 = new Option("trig2", "trig2", false, "Trig option 2");
        Option opt1_trig3 = new Option("trig3", "trig3", false, "Trig option 3");
        commandLine.addOption(opt1);
        commandLine.addOption(opt1_long);
        commandLine.addOption(opt1_trig1);
        commandLine.addOption(opt1_trig2);
        commandLine.addOption(opt1_trig3);

        assertEquals(5, commandLine.getOptions().size());
        assertEquals(new HashSet<>(Collections.singletonList("opt1")), commandLine.getOptionTriggers());
        assertEquals(new HashSet<>(Collections.singletonList("opt1-long")), commandLine.getOptionTriggers());
        assertEquals(new HashSet<>(Collections.singletonList("trig1")), commandLine.getOptionTriggers());
        assertEquals(new HashSet<>(Collections.singletonList("trig2")), commandLine.getOptionTriggers());
        assertEquals(new HashSet<>(Collections.singletonList("trig3")), commandLine.getOptionTriggers());
    }
}