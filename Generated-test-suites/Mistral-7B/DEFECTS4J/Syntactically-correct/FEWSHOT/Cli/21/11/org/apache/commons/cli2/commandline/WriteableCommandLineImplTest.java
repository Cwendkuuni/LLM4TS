package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmdLine;
    private Option rootOption;

    @Before
    public void setup() {
        rootOption = new Option("root", "rootOption", false, "Root option");
        cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        cmdLine.addOption(option1);
        assertSame(option1, cmdLine.getOption("opt1"));
        assertTrue(cmdLine.getOptionTriggers().contains("opt1"));
        assertEquals(1, cmdLine.getOptions().size());

        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option2);
        assertEquals(2, cmdLine.getOptions().size());

        Option option3 = new Option("opt1", "opt1", true, "Option 1");
        cmdLine.addOption(option3);
        assertSame(option1, cmdLine.getOption("opt1"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        assertEquals(1, ((List) cmdLine.getValues(option, null)).size());
        assertEquals("value1", ((List) cmdLine.getValues(option, null)).get(0));

        cmdLine.addValue(option, "value2");
        assertEquals(2, ((List) cmdLine.getValues(option, null)).size());
        assertEquals("value1", ((List) cmdLine.getValues(option, null)).get(0));
        assertEquals("value2", ((List) cmdLine.getValues(option, null)).get(1));

        cmdLine.addValue(option, null);
        assertEquals(3, ((List) cmdLine.getValues(option, null)).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, false));

        cmdLine.addSwitch(option, false);
        assertFalse(cmdLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        assertTrue(cmdLine.hasOption(option1));
        assertFalse(cmdLine.hasOption(new Option("opt3", "opt3", false, "Option 3")));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Option actual = cmdLine.getOption("opt1");
        assertSame(option1, actual);
        Option expected = new Option("opt1", "opt1", true, "Option 1");
        assertSame(expected, option1);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        List values = cmdLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = Arrays.asList("default1", "default2");
        List actualValues = cmdLine.getValues(option, defaultValues);
        assertEquals(2, actualValues.size());
        assertEquals("value1", actualValues.get(0));
        assertEquals("value2", actualValues.get(1));
        assertEquals(2, defaultValues.size());
        assertEquals(2, cmdLine.getOptions().size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, null);
        assertEquals(1, values.size());
        List undefaultedValues = cmdLine.getUndefaultedValues(option);
        assertEquals(0, undefaultedValues.size());

        cmdLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        values = cmdLine.getValues(option, null);
        assertEquals(2, values.size());
        undefaultedValues = cmdLine.getUndefaultedValues(option);
        assertEquals(1, undefaultedValues.size());
        assertEquals("value1", undefaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, false));

        cmdLine.addSwitch(option, false);
        assertFalse(cmdLine.getSwitch(option, false));

        cmdLine.setDefaultSwitch(option, Boolean.TRUE);
        assertTrue(cmdLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testGetProperty() {
        Option propertyOption = new Option("prop", "prop", false, "Property option");
        cmdLine.addOption(propertyOption);
        cmdLine.addProperty(propertyOption, "prop1", "Property value 1");

        assertEquals("Property value 1", cmdLine.getProperty("prop", null));
        assertEquals("Property value 1", cmdLine.getProperty("prop", "prop1"));
        assertEquals(null, cmdLine.getProperty("prop", "prop2"));

        cmdLine.addProperty(propertyOption, "prop2", "Property value 2");
        assertEquals("Property value 1", cmdLine.getProperty("prop", "prop1"));
        assertEquals("Property value 2", cmdLine.getProperty("prop", "prop2"));
    }

    @Test
    public void testAddProperty() {
        Option propertyOption = new Option("prop", "prop", false, "Property option");
        cmdLine.addOption(propertyOption);

        Properties properties = new Properties();
        properties.setProperty("prop1", "Property value 1");
        cmdLine.addProperty(propertyOption, properties);

        assertEquals(1, cmdLine.getProperties(propertyOption).size());
        assertEquals("Property value 1", cmdLine.getProperty(propertyOption, "prop1"));
        assertEquals(null, cmdLine.getProperty(propertyOption, "prop2"));

        properties.setProperty("prop2", "Property value 2");
        assertEquals(1, cmdLine.getProperties(propertyOption).size());
        assertEquals("Property value 1", cmdLine.getProperty(propertyOption, "prop1"));
        assertEquals("Property value 2", cmdLine.getProperty(propertyOption, "prop2"));
    }

    @Test
    public void testGetProperties() {
        Option propertyOption = new Option("prop", "prop", false, "Property option");
        cmdLine.addOption(propertyOption);

        Properties properties = new Properties();
        properties.setProperty("prop1", "Property value 1");
        cmdLine.addProperty(propertyOption, properties);

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("prop1", "Property value 1");

        Map<String, String> actualProperties = cmdLine.getProperties(propertyOption);
        assertEquals(expectedProperties, actualProperties);
        assertEquals(1, actualProperties.size());
        assertEquals("Property value 1", actualProperties.get("prop1"));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger1 = "opt1";
        String trigger2 = "--opt1";
        String trigger3 = "--opt1-arg=value";
        String trigger4 = "opt1 --opt2";

        assertTrue(cmdLine.looksLikeOption(trigger1));
        assertTrue(cmdLine.looksLikeOption(trigger2));
        assertTrue(cmdLine.looksLikeOption(trigger3));
        assertTrue(cmdLine.looksLikeOption(trigger4));

        assertFalse(cmdLine.looksLikeOption("arg1"));
        assertFalse(cmdLine.looksLikeOption("--"));
        assertFalse(cmdLine.looksLikeOption(""));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        cmdLine.addValue(option1, "value1");
        cmdLine.addValue(option1, "value2");
        cmdLine.addValue(option2, "value3");

        String expected = "opt1 value1 value2 opt2 value3";
        String actual = cmdLine.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        List<Option> actualOptions = cmdLine.getOptions();

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Set<String> expectedTriggers = new HashSet<>(Arrays.asList("opt1", "opt2"));
        Set<String> actualTriggers = cmdLine.getOptionTriggers();

        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        List<String> values = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, values);
        List<Object> actualValues = cmdLine.getValues(option, null);

        assertEquals(values, actualValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, false);
        cmdLine.setDefaultSwitch(option, Boolean.TRUE);
        Boolean actualSwitch = cmdLine.getSwitch(option, Boolean.FALSE);

        assertTrue(actualSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("opt", "opt", false, "Option");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");

        List<String> expectedNormalised = Arrays.asList("opt value1");
        List<String> actualNormalised = cmdLine.getNormalised();

        assertEquals(expectedNormalised, actualNormalised);
    }
}