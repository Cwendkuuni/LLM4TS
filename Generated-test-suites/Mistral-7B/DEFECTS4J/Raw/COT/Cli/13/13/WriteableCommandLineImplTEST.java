package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Constructor() {
        Option rootOption = new Option("root", "rootOption", false, "root description");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertNotNull(writeableCommandLine);
        assertEquals(rootOption, writeableCommandLine.getOption());
        assertEquals(arguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = new Option("root", "rootOption", false, "root description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = new Option("opt", "opt", false, "option description");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertEquals(option, writeableCommandLine.getOption("opt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_SwitchAlreadySet() {
        Option rootOption = new Option("root", "rootOption", false, "root description");
        List arguments = Collections.emptyList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "opt", false, "option description");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("opt", "opt", true, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getValues(option, null);
        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));

        writeableCommandLine.addValue(option, "value2");
        valueList = writeableCommandLine.getValues(option, null);
        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(new Option("other", "other", false, "other option description")));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty("otherProperty", "defaultValue"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property", "value");
        assertEquals(expectedProperties, writeableCommandLine.getProperties());
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        String expectedValue = "arg1 opt value1 arg2";
        String actualValue = writeableCommandLine.toString();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List<Option> expectedOptions = Arrays.asList(option);
        List<Option> actualOptions = writeableCommandLine.getOptions();
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Set<String> expectedTriggers = new HashSet<>(Arrays.asList("opt"));
        Set<String> actualTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("opt", "opt", true, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<String> actualValues = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, actualValues);

        writeableCommandLine.addValue(option, "value1");
        actualValues = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "defaultValue1", "defaultValue2"), actualValues);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        Option option = new Option("opt", "opt", false, "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        assertFalse(writeableCommandLine.looksLikeOption("otherOption"));
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
    }
}