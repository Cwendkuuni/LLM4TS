package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    private Option option;
    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setup() {
        option = mock(Option.class);
        writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        List<String> valueList = new ArrayList<>();
        valueList.add("value1");
        writeableCommandLine.addValue(option, valueList);
        assertEquals(valueList, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.hasOption(option));
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        writeableCommandLine.addSwitch(option, false);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testGetOption() {
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        List<String> valueList1 = new ArrayList<>();
        valueList1.add("value1");
        writeableCommandLine.addValue(option, valueList1);

        List<String> valueList2 = new ArrayList<>();
        valueList2.add("value2");
        writeableCommandLine.addValue(option, valueList2);

        List<String> expectedValues = Arrays.asList("value1", "value2");
        assertEquals(expectedValues, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.setProperties(properties);

        Set<String> expectedProperties = Arrays.asList("property1", "property2");
        assertEquals(expectedProperties, writeableCommandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertEquals(true, writeableCommandLine.looksLikeOption("--option"));
        assertEquals(false, writeableCommandLine.looksLikeOption("--no-option"));
    }

    @Test
    public void testToString() {
        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("value1");
        args.add("--another-option");

        writeableCommandLine = new WriteableCommandLineImpl(null, args);
        String expected = "--option value1 --another-option";
        assertEquals(expected, writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        assertEquals(expectedOptions, writeableCommandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option(null, "o1", false, "Option 1");
        Option option2 = new Option(null, "o2", false, "Option 2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set<String> expectedOptionTriggers = Arrays.asList("o1", "o2");
        assertEquals(expectedOptionTriggers, writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("value1");
        args.add("--another-option");

        writeableCommandLine = new WriteableCommandLineImpl(null, args);
        List<String> expectedNormalised = Arrays.asList("--option", "value1", "--another-option");
        assertEquals(expectedNormalised, writeableCommandLine.getNormalised());
    }
}