package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplWithNoOptionsOrArguments() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        assertTrue(commandLine.isEmpty());
        assertNull(commandLine.getOptions());
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        assertNull(commandLine.getValues(null, Collections.emptyList()));
        assertNull(commandLine.getSwitch(null, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Collections.emptyList();
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithSingleArgument() {
        Option option = new Option("a", "arg");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Collections.singletonList("argValue"), commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(null));
        assertNull(commandLine.getSwitch(null, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithSingleOptionWithNoValue() {
        Option option = new Option("a", "arg");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertNull(commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(null));
        assertNull(commandLine.getSwitch(option, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithSingleOptionWithValue() {
        Option option = new Option("a", "arg");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Collections.singletonList("argValue"), commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(null));
        assertNull(commandLine.getSwitch(option, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithMultipleOptions() {
        Option option1 = new Option("a", "arg1");
        Option option2 = new Option("b", "arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1Value1"))
                .addOption(option2).addValue(option2, "arg2Value1");

        assertEquals(2, commandLine.size());
        assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
        assertEquals(Collections.singletonList("arg1Value1"), commandLine.getValues(option1, null));
        assertEquals(option2, commandLine.getOption(option2.getPreferredName()));
        assertEquals(Collections.singletonList("arg2Value1"), commandLine.getValues(option2, null));
        assertFalse(commandLine.hasOption(null));
        assertFalse(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
        assertNull(commandLine.getSwitch(null, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithMultipleOptionsAndArguments() {
        Option option1 = new Option("a", "arg1");
        Option option2 = new Option("b", "arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1Value1"))
                .addOption(option2).addValue(option2, "arg2Value1")
                .addArgument(new Argument("arg3", "arg3Value1"))
                .addArgument(new Argument("arg4", "arg4Value1"));

        assertEquals(4, commandLine.size());
        assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
        assertEquals(Collections.singletonList("arg1Value1"), commandLine.getValues(option1, null));
        assertEquals(option2, commandLine.getOption(option2.getPreferredName()));
        assertEquals(Collections.singletonList("arg2Value1"), commandLine.getValues(option2, null));
        assertFalse(commandLine.hasOption(null));
        assertFalse(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption("c"));
        assertEquals(2, commandLine.getValues(null, null).size());
        assertEquals(Boolean.FALSE, commandLine.getSwitch(option1, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithMultipleOptionsAndValues() {
        Option option1 = new Option("a", "arg1");
        Option option2 = new Option("b", "arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1Value1"))
                .addOption(option2).addValues(option2, Arrays.asList("arg2Value1", "arg2Value2"));

        assertEquals(2, commandLine.size());
        assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
        assertEquals(Collections.singletonList("arg1Value1"), commandLine.getValues(option1, null));
        assertEquals(option2, commandLine.getOption(option2.getPreferredName()));
        assertEquals(Arrays.asList("arg2Value1", "arg2Value2"), commandLine.getValues(option2, null));
        assertFalse(commandLine.hasOption(null));
        assertFalse(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
        assertNull(commandLine.getSwitch(null, null));
        assertNull(commandLine.getProperty(null));

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithProperty() {
        Option option = new Option("a", "arg")
                .setValueSeparator('=')
                .addOption(new PropertyOption("p", "property", true, "defaultValue"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1", "p=propertyValue1"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Collections.singletonList("argValue1"), commandLine.getValues(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals("propertyValue1", commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithPropertyAndValue() {
        Option option = new Option("a", "arg")
                .setValueSeparator('=')
                .addOption(new PropertyOption("p", "property", true, "defaultValue"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1=propertyValue1", "argValue2"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Collections.singletonList("argValue1"), commandLine.getValues(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals("propertyValue1", commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithValueAndProperty() {
        Option option = new Option("a", "arg")
                .setValueSeparator('=')
                .addOption(new PropertyOption("p", "property", true, "defaultValue"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1", "argValue2=propertyValue1"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Arrays.asList("argValue1", "argValue2"), commandLine.getValues(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals("propertyValue1", commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithPropertyAndValueAndOtherValues() {
        Option option = new Option("a", "arg")
                .setValueSeparator('=')
                .addOption(new PropertyOption("p", "property", true, "defaultValue"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1=propertyValue1", "argValue2", "argValue3"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Arrays.asList("argValue1"), commandLine.getValues(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals("propertyValue1", commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithMultipleValues() {
        Option option = new Option("a", "arg")
                .setValueSeparator(',');

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1", "argValue2", "argValue3"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Arrays.asList("argValue1", "argValue2", "argValue3"), commandLine.getValues(option, null));
        assertFalse(commandLine.getSwitch(option, null));
        assertNull(commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithMultipleValuesAndProperty() {
        Option option = new Option("a", "arg")
                .setValueSeparator(',')
                .addOption(new PropertyOption("p", "property", true, "defaultValue"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1,argValue2", "argValue3=propertyValue1"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Arrays.asList("argValue1", "argValue2"), commandLine.getValues(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals("propertyValue1", commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImplWithOptionWithMultipleValuesAndOtherValues() {
        Option option = new Option("a", "arg")
                .setValueSeparator(',');

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argValue1,argValue2", "argValue3", "argValue4"));

        assertEquals(1, commandLine.size());
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Arrays.asList("argValue1", "argValue2"), commandLine.getValues(option, null));
        assertFalse(commandLine.getSwitch(option, null));
        assertNull(commandLine.getProperty(option, "defaultValue"));

        List<Option> expectedOptions = Arrays.asList(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }
}