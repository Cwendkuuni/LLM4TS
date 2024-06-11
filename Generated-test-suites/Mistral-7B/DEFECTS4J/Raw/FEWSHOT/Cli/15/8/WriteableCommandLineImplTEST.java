package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("test").build(), new ArrayList<>());
        assertEquals(1, writeableCommandLine.getOptions().size());
        assertEquals("test", writeableCommandLine.getOptions().get(0).getLongOpt());
        assertEquals("test", writeableCommandLine.getOptions().get(0).getOptString());
        assertEquals("test", writeableCommandLine.getOption(OptionBuilder.create("test").build().getLongOpt()).getLongOpt());
        assertNull(writeableCommandLine.getOptions().get(0).getValue());
        assertNull(writeableCommandLine.getValues(OptionBuilder.create("test").build(), null));
        assertFalse(writeableCommandLine.hasOption(OptionBuilder.create("test2").build()));
    }

    @Test
    public void testAddOptionAndGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("test").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").build();
        writeableCommandLine.addOption(option);
        assertEquals(2, writeableCommandLine.getOptions().size());
        assertEquals("option", writeableCommandLine.getOptions().get(1).getLongOpt());
        assertEquals("option", writeableCommandLine.getOptions().get(1).getOptString());
        assertEquals("option", writeableCommandLine.getOption("option").getLongOpt());
        assertNull(writeableCommandLine.getValues(option, null));
        assertFalse(writeableCommandLine.hasOption(OptionBuilder.create("test2").build()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        assertEquals(1, writeableCommandLine.getOptions().size());
        List<Object> values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("switch").build(), new ArrayList<>());
        Option option = OptionBuilder.create("switch").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("switch").build(), new ArrayList<>());
        Option option = OptionBuilder.create("switch").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("test").build(), new ArrayList<>());
        Option option = OptionBuilder.create("test").build();
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(OptionBuilder.create("test2").build()));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("test").build(), new ArrayList<>());
        Option option = OptionBuilder.create("test").build();
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("test");
        assertEquals(option, retrievedOption);
        assertEquals("test", retrievedOption.getLongOpt());
        assertEquals("test", retrievedOption.getOptString());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<Object> values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<Object> values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("switch").build(), new ArrayList<>());
        Option option = OptionBuilder.create("switch").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
        assertFalse(writeableCommandLine.getSwitch(OptionBuilder.create("test").build(), false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("property").build(), new ArrayList<>());
        Option option = OptionBuilder.create("property").hasArg(true).build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "propertyName", "propertyValue");
        assertEquals("propertyValue", writeableCommandLine.getProperty("propertyName"));
        assertEquals(null, writeableCommandLine.getProperty("propertyName2"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("property").build(), new ArrayList<>());
        Option option = OptionBuilder.create("property").hasArg(true).build();
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyName", "propertyValue");
        writeableCommandLine.addProperty(option, "propertyName", "propertyValue");
        Map<String, Object> optionProperties = writeableCommandLine.getProperties(option);
        assertEquals(1, optionProperties.size());
        assertEquals("propertyValue", optionProperties.get("propertyName"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("property").build(), new ArrayList<>());
        Option option = OptionBuilder.create("property").hasArg(true).build();
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyName1", "propertyValue1");
        properties.setProperty("propertyName2", "propertyValue2");
        writeableCommandLine.addProperty(option, "propertyName1", "propertyValue1");
        writeableCommandLine.addProperty(option, "propertyName2", "propertyValue2");
        Set<String> propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(Arrays.asList("propertyName1", "propertyName2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("test").build(), new ArrayList<>());
        assertTrue(writeableCommandLine.looksLikeOption("test"));
        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertFalse(writeableCommandLine.looksLikeOption("--test2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").hasArg(true).build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").hasArg(true).build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        String toString = writeableCommandLine.toString();
        String expected = "option value1";
        assertEquals(expected, toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option1").build(), new ArrayList<>());
        Option option1 = OptionBuilder.create("option1").build();
        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(OptionBuilder.create("option2").build(), new ArrayList<>());
        Option option2 = OptionBuilder.create("option2").build();
        writeableCommandLine.addOption(option1);
        writeableCommandLine2.addOption(option2);
        WriteableCommandLineImpl compositeCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("composite").build(), new ArrayList<>());
        compositeCommandLine.addOption(writeableCommandLine);
        compositeCommandLine.addOption(writeableCommandLine2);
        List<Option> options = compositeCommandLine.getOptions();
        assertEquals(3, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
        assertEquals(OptionBuilder.create("composite").build(), options.get(2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option1").build(), new ArrayList<>());
        Option option1 = OptionBuilder.create("option1").build();
        Option option2 = OptionBuilder.create("option2").build();
        Option option3 = OptionBuilder.create("option3").build();
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);
        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("option1", "option2", "option3")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").hasArg(true).build();
        writeableCommandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<Object> values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("switch").build(), new ArrayList<>());
        Option option = OptionBuilder.create("switch").build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(OptionBuilder.create("option").build(), new ArrayList<>());
        Option option = OptionBuilder.create("option").hasArg(true).build();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List<String> normalised = writeableCommandLine.getNormalised();
        List<String> expected = new ArrayList<>();
        expected.add("option value1");
        assertEquals(expected, normalised);
    }
}