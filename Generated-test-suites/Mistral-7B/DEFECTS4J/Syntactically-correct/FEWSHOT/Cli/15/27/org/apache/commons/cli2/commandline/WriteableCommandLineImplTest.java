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
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLine.WriteableCommandLineBuilder;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.AbstractCommandLineParser;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        assertEquals(option, writeableCommandLine.getCommandLine());
        assertEquals(Arrays.asList("arg1", "arg2"), writeableCommandLine.getNormalised());
        assertEquals(Collections.unmodifiableSet(Arrays.asList("option")), writeableCommandLine.getOptionTriggers());
        assertFalse(writeableCommandLine.hasOption(new Option("anotherOption")));
        assertNull(writeableCommandLine.getOption("anotherOption"));
        assertNull(writeableCommandLine.getValues(new Option("option"), null));
        assertNull(writeableCommandLine.getSwitch(new Option("option"), null));
        assertNull(writeableCommandLine.getProperty(new Option("option"), ""));
        assertNull(writeableCommandLine.getProperty(new Option("option"), "key", ""));
        assertTrue(writeableCommandLine.looksLikeOption("option"));
        assertTrue(writeableCommandLine.toString().contains("arg1"));
        assertTrue(writeableCommandLine.toString().contains("arg2"));
    }

    @Test
    public void testWriteableCommandLineImplAddValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1"), valueList);

        writeableCommandLine.addValue(option, "value2");
        valueList = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), valueList);

        writeableCommandLine.addValue(option, "value3");
        valueList = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2", "value3"), valueList);
    }

    @Test
    public void testWriteableCommandLineImplAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addSwitch(option, true);
        Boolean value = writeableCommandLine.getSwitch(option, false);
        assertTrue(value);

        writeableCommandLine.addSwitch(option, false);
        value = writeableCommandLine.getSwitch(option, true);
        assertFalse(value);
    }

    @Test
    public void testWriteableCommandLineImplAddProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addProperty(option, "property", "value1");
        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1"), properties.stringPropertyNames());
        assertEquals("value1", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, "property", "value2");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1", "value2"), properties.stringPropertyNames());
        assertEquals("value2", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, "property", "value3");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1", "value2", "value3"), properties.stringPropertyNames());
        assertEquals("value3", properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImplAddPropertyWithDefaultValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(null)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addProperty(option, "property", "value1");
        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1"), properties.stringPropertyNames());
        assertEquals("value1", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, "property", "value2");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1", "value2"), properties.stringPropertyNames());
        assertEquals("value2", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, null, "value3");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("value1", "value2", "property"), properties.stringPropertyNames());
        assertEquals("value3", properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImplAddPropertyWithDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(null)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), properties.stringPropertyNames());
        assertEquals("defaultValue1", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, "property", "value1");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2", "value1"), properties.stringPropertyNames());
        assertEquals("value1", properties.getProperty("property"));

        writeableCommandLine.addProperty(option, null, "value2");
        properties = writeableCommandLine.getProperties(option);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2", "value1", "value2"), properties.stringPropertyNames());
        assertEquals("value2", properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImplSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addSwitch(option, true);
        Boolean defaultSwitch = writeableCommandLine.getDefaultSwitch(option);
        assertTrue(defaultSwitch);

        writeableCommandLine.setDefaultSwitch(option, false);
        defaultSwitch = writeableCommandLine.getDefaultSwitch(option);
        assertFalse(defaultSwitch);
    }

    @Test
    public void testWriteableCommandLineImplSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, values);

        writeableCommandLine.addValue(option, "value1");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2", "value1"), values);

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2", "value1", "value2"), values);
    }

    @Test
    public void testWriteableCommandLineImplAddOption() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg(null)
                .isRequired(false)
                .withDescription("A test option 2")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(Arrays.asList(option1, option2), options);
        assertEquals(option2, writeableCommandLine.getOption("option2"));

        writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option1);
        options = writeableCommandLine.getOptions();
        assertEquals(Arrays.asList(option1, option1, option2), options);
        assertEquals(option1, writeableCommandLine.getOption("option1"));
    }

    @Test
    public void testWriteableCommandLineImplToString() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        List args = writeableCommandLine.getNormalised();
        String toString = writeableCommandLine.toString();

        assertEquals("arg1 arg2", toString);

        writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2", "arg3"));
        toString = writeableCommandLine.toString();

        assertEquals("arg1 arg2 arg3", toString);

        writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg\"2", "arg3"));
        toString = writeableCommandLine.toString();

        assertEquals("arg1 arg\"2 arg3", toString);
    }

    @Test
    public void testWriteableCommandLineImplWithCustomPrefixes() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .isRequired(false)
                .withDescription("A test option")
                .create();

        WriteableCommandLineBuilder builder = new WriteableCommandLineBuilder();
        builder.setPrefixes(Arrays.asList("--", "-c-"));
        WriteableCommandLine writeableCommandLine = builder.parse(new AbstractCommandLineParser(), new String[]{"--option", "arg1", "--option", "arg2", "-c-option", "arg3"});

        assertEquals(option, writeableCommandLine.getCommandLine());
        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), writeableCommandLine.getNormalised());
        assertEquals(Collections.unmodifiableSet(Arrays.asList("option")), writeableCommandLine.getOptionTriggers());
        assertFalse(writeableCommandLine.hasOption(new Option("anotherOption")));
        assertNull(writeableCommandLine.getOption("anotherOption"));
        assertNull(writeableCommandLine.getValues(new Option("option"), null));
        assertNull(writeableCommandLine.getSwitch(new Option("option"), null));
        assertNull(writeableCommandLine.getProperty(new Option("option"), ""));
        assertNull(writeableCommandLine.getProperty(new Option("option"), "key", ""));
        assertTrue(writeableCommandLine.looksLikeOption("option"));
        assertTrue(writeableCommandLine.toString().contains("arg1"));
        assertTrue(writeableCommandLine.toString().contains("arg2"));
        assertTrue(writeableCommandLine.toString().contains("arg3"));
    }
}