package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
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
    public void testWriteableCommandLineImpl_1() {
        Option rootOption = new Option("root", "root", false, "root description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertNull(commandLine.getOption("nonExistentOption"));
        assertFalse(commandLine.hasOption(rootOption));
        assertEquals(Collections.emptyList(), commandLine.getValues(rootOption, null));
        assertNull(commandLine.getSwitch(rootOption, null));
        assertEquals(Collections.emptyMap(), commandLine.getProperties());
        assertFalse(commandLine.looksLikeOption("nonExistentOption"));
        assertNull(commandLine.toString());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), commandLine.getNormalised());
    }

    @Test
    public void testWriteableCommandLineImpl_2() {
        Option option1 = new Option("option1", "opt1", true, "option1 description");
        Option option2 = new Option("option2", "opt2", false, "option2 description");
        Option option3 = new Argument("arg1", "a1", true, "argument1 description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.singletonList("arg1"));

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addArgument(option3);

        // test getOption
        Option getOption1 = commandLine.getOption("option1");
        assertSame(option1, getOption1);

        Option getOption2 = commandLine.getOption("option2");
        assertSame(option2, getOption2);

        Option getOption3 = commandLine.getOption("nonExistentOption");
        assertNull(getOption3);

        // test hasOption
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new Option("nonExistentOption", "no", false, "no description")));

        // test getValues
        List option1Values = commandLine.getValues(option1, null);
        assertNull(option1Values);

        List option2Values = commandLine.getValues(option2, null);
        assertEquals(Collections.emptyList(), option2Values);

        List option1ValuesWithValue = commandLine.getValues(option1, Collections.singletonList("value1"));
        assertEquals(Collections.singletonList("value1"), option1ValuesWithValue);

        // test getSwitch
        Boolean option1Switch = commandLine.getSwitch(option1, null);
        assertNull(option1Switch);

        Boolean option2Switch = commandLine.getSwitch(option2, Boolean.FALSE);
        assertSame(Boolean.FALSE, option2Switch);

        Boolean option1SwitchWithValue = commandLine.getSwitch(option1, Boolean.TRUE);
        assertNull(option1SwitchWithValue);

        // test getProperty
        WriteableCommandLineImpl commandLineWithProperty = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.emptyList());
        commandLineWithProperty.addProperty(new PropertyOption(), "property1", "value1");
        String property1 = commandLineWithProperty.getProperty("property1");
        assertEquals("value1", property1);

        // test addProperty
        WriteableCommandLineImpl commandLineWithAddedProperty = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.emptyList());
        commandLineWithAddedProperty.addProperty(option1, "property1", "value1");
        String property1FromAdded = commandLineWithAddedProperty.getProperty("property1");
        assertEquals("value1", property1FromAdded);

        // test getProperties
        WriteableCommandLineImpl commandLineWithProperties = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.emptyList());
        commandLineWithProperties.addProperty(new PropertyOption(), "property1", "value1");
        commandLineWithProperties.addProperty(new PropertyOption(), "property2", "value2");
        Map properties = commandLineWithProperties.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        // test looksLikeOption
        assertTrue(commandLine.looksLikeOption("option1"));
        assertFalse(commandLine.looksLikeOption("option2"));
        assertFalse(commandLine.looksLikeOption("arg1"));
        assertFalse(commandLine.looksLikeOption("nonExistentOption"));

        // test toString
        String toString = commandLine.toString();
        assertEquals("option1 arg1 root", toString);

        // test getOptions
        List options = commandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));

        // test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));

        // test getNormalised
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("option1", normalised.get(0));
        assertEquals("arg1", normalised.get(1));
        assertEquals("root", normalised.get(2));

        // test setDefaultValues
        WriteableCommandLineImpl commandLineWithDefaultValues = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.emptyList());
        List defaultValues = Collections.singletonList("value1");
        commandLineWithDefaultValues.setDefaultValues(option1, defaultValues);
        List option1ValuesWithDefault = commandLineWithDefaultValues.getValues(option1, null);
        assertEquals(1, option1ValuesWithDefault.size());
        assertEquals("value1", option1ValuesWithDefault.get(0));

        // test setDefaultSwitch
        WriteableCommandLineImpl commandLineWithDefaultSwitch = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Collections.emptyList());
        commandLineWithDefaultSwitch.setDefaultSwitch(option1, Boolean.TRUE);
        Boolean option1SwitchWithDefault = commandLineWithDefaultSwitch.getSwitch(option1, null);
        assertSame(Boolean.TRUE, option1SwitchWithDefault);

        // test getDefaultValues
        Map defaultValuesMap = commandLineWithDefaultValues.getDefaultValues();
        assertEquals(1, defaultValuesMap.size());
        assertEquals(defaultValues, defaultValuesMap.get(option1));

        // test getDefaultSwitches
        Map defaultSwitchesMap = commandLineWithDefaultSwitch.getDefaultSwitches();
        assertEquals(1, defaultSwitchesMap.size());
        assertEquals(Boolean.TRUE, defaultSwitchesMap.get(option1));

        // test getNormalised
        List normalisedWithOptions = new WriteableCommandLineImpl(new Option("root", "root", false, "root description"),
                Arrays.asList("option1=value1", "arg1")).getNormalised();
        assertEquals(4, normalisedWithOptions.size());
        assertEquals("option1", normalisedWithOptions.get(0));
        assertEquals("value1", normalisedWithOptions.get(1));
        assertEquals("arg1", normalisedWithOptions.get(2));
        assertEquals("root", normalisedWithOptions.get(3));
    }
}