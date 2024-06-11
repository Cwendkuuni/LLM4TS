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
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .create();
        writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        writeableCommandLine.addOption(option);
        assertSame(option, writeableCommandLine.getOption("test"));
    }

    @Test
    public void testHasOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetValues() {
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .create();
        writeableCommandLine.addOption(argOption);
        writeableCommandLine.addValue(argOption, "value");
        List values = writeableCommandLine.getUndefaultedValues(argOption);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchDefaultValue() {
        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testAddProperty() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        assertThat(propertiesMap, is(notNullValue()));
        assertEquals(1, propertiesMap.size());
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testAddPropertyWithEmptyKey() {
        try {
            writeableCommandLine.addProperty(option, "", "value");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddPropertyWithEmptyValue() {
        try {
            writeableCommandLine.addProperty(option, "key", "");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addSwitch(option, true);
        String result = writeableCommandLine.toString();
        String expected = "test value1 value2 --test";
        assertEquals(expected, result);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");
        Map<String, String> propertiesMap = writeableCommandLine.getProperties();
        assertEquals(1, propertiesMap.size());
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        assertEquals(expectedProperties, propertiesMap);
    }

    @Test
    public void testGetOptionsList() {
        Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .create();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(argOption);
        List<Option> options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option, options.get(0));
        assertEquals(argOption, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .create();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(argOption);
        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("test");
        expectedTriggers.add("arg");
        Set<String> triggers = writeableCommandLine.getOptionTriggers();
        assertEquals(expectedTriggers, triggers);
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitchFalse() {
        writeableCommandLine.setDefaultSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        Option argOption = OptionBuilder.withLongOpt("arg")
                .isRequired(true)
                .create();
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(argOption);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(argOption, "value2");
        writeableCommandLine.addValue(argOption, "value3");
        List normalised = writeableCommandLine.getNormalised();
        List expectedNormalised = new ArrayList<>();
        expectedNormalised.add("value1");
        expectedNormalised.add("value2");
        expectedNormalised.add("value3");
        expectedNormalised.add("arg value2");
        expectedNormalised.add("arg value3");
        assertEquals(expectedNormalised, normalised);
    }
}