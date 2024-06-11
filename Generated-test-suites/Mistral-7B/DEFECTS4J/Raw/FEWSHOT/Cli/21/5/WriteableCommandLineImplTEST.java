package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "Description for root option");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        assertEquals(rootOption, writeableCommandLine.getOption("root"));
        assertEquals(Collections.singletonList(rootOption), writeableCommandLine.getOptions());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "rootOption", false, "Description for root option");
        Option childOption = new Option("child", "childOption", false, "Description for child option");
        childOption.setParent(rootOption);

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        writeableCommandLine.addOption(childOption);

        assertEquals(childOption, writeableCommandLine.getOption("child"));
        assertEquals(Collections.singletonList(childOption), writeableCommandLine.getOptions());

        Option optionWithSameName = new Option("child", "childOption", false, "Description for child option with same name");
        writeableCommandLine.addOption(optionWithSameName);

        assertEquals(2, writeableCommandLine.getOptions().size());
        assertEquals(childOption, writeableCommandLine.getOption("child"));
        assertEquals(Collections.singletonList(childOption), writeableCommandLine.getOptions(Option.class));
    }

    @Test
    public void testAddValue() {
        Option argOption = new Argument("arg", "argument", true, "Description for argument option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(argOption);

        Object value1 = "value1";
        Object value2 = "value2";

        writeableCommandLine.addValue(argOption, value1);
        writeableCommandLine.addValue(argOption, value2);

        List values = writeableCommandLine.getValues(argOption, null);

        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option("switch", "switchOption", false, "Description for switch option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(switchOption);

        writeableCommandLine.addSwitch(switchOption, true);

        Boolean switchValue = writeableCommandLine.getSwitch(switchOption, null);

        assertTrue(switchValue != null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "option", false, "Description for option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(option);

        Option retrievedOption = writeableCommandLine.getOption("option");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option argOption = new Argument("arg", "argument", true, "Description for argument option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(argOption);

        Object value1 = "value1";
        Object value2 = "value2";

        writeableCommandLine.addValue(argOption, value1);
        writeableCommandLine.addValue(argOption, value2);

        List values = writeableCommandLine.getValues(argOption, null);

        assertIterableEquals(Arrays.asList(value1, value2), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argOption = new Argument("arg", "argument", true, "Description for argument option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(argOption);

        Object value1 = "value1";
        Object value2 = "value2";

        writeableCommandLine.addValue(argOption, value1);
        writeableCommandLine.addValue(argOption, value2);

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(argOption);

        assertIterableEquals(Collections.singletonList(value1), unfaultedValues);
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = new Option("switch", "switchOption", false, "Description for switch option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(switchOption);

        writeableCommandLine.addSwitch(switchOption, true);

        Boolean switchValue = writeableCommandLine.getSwitch(switchOption, null);

        assertTrue(switchValue != null);
        assertTrue(switchValue);

        writeableCommandLine.addSwitch(switchOption, false);

        Boolean switchValue2 = writeableCommandLine.getSwitch(switchOption, null);

        assertTrue(switchValue2 != null);
        assertFalse(switchValue2);
    }

    @Test
    public void testGetProperty() {
        Option propertyOption = new PropertyOption("property", "property", null, "Description for property option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(propertyOption);

        String propertyValue = "propertyValue";

        writeableCommandLine.addProperty(propertyOption, "property", propertyValue);

        String retrievedPropertyValue = writeableCommandLine.getProperty("property");

        assertEquals(propertyValue, retrievedPropertyValue);
    }

    @Test
    public void testAddProperty() {
        Option propertyOption = new PropertyOption("property", "property", null, "Description for property option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(propertyOption);

        String propertyKey = "propertyKey";
        String propertyValue = "propertyValue";

        writeableCommandLine.addProperty(propertyOption, propertyKey, propertyValue);

        Properties properties = writeableCommandLine.getProperties(propertyOption);

        assertEquals(propertyValue, properties.getProperty(propertyKey));
    }

    @Test
    public void testGetProperties() {
        Option propertyOption = new PropertyOption("property", "property", null, "Description for property option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(propertyOption);

        String propertyKey1 = "propertyKey1";
        String propertyValue1 = "propertyValue1";
        String propertyKey2 = "propertyKey2";
        String propertyValue2 = "propertyValue2";

        writeableCommandLine.addProperty(propertyOption, propertyKey1, propertyValue1);
        writeableCommandLine.addProperty(propertyOption, propertyKey2, propertyValue2);

        Properties properties = writeableCommandLine.getProperties(propertyOption);

        Set propertyNames = properties.keySet();

        assertEquals(2, propertyNames.size());
        assertTrue(propertyNames.contains(propertyKey1));
        assertTrue(propertyNames.contains(propertyKey2));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        String arg1 = "arg1";
        String arg2 = "arg2";
        String arg3 = "--arg3";
        String arg4 = "-arg4";

        for (String arg : Arrays.asList(arg1, arg2, arg3, arg4)) {
            boolean looksLikeOption = writeableCommandLine.looksLikeOption(arg);

            if (arg.startsWith("--")) {
                assertTrue(looksLikeOption);
            } else {
                assertFalse(looksLikeOption);
            }
        }
    }

    @Test
    public void testToString() {
        Option argOption = new Argument("arg", "argument", true, "Description for argument option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(argOption);

        String argValue1 = "value1";
        String argValue2 = "value2";

        writeableCommandLine.addValue(argOption, argValue1);
        writeableCommandLine.addValue(argOption, argValue2);

        String commandLineString = writeableCommandLine.toString();

        String expectedCommandLineString = "rootOption arg1 value1 value2";

        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        Option argOption1 = new Argument("arg1", "argument1", true, "Description for argument option 1");
        Option argOption2 = new Argument("arg2", "argument2", true, "Description for argument option 2");
        Option optionOption1 = new Option("option1", "option1", false, "Description for option option 1");
        Option optionOption2 = new Option("option2", "option2", false, "Description for option option 2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Description for root option"), new ArrayList<>());

        writeableCommandLine.addOption(optionOption1);
        writeableCommandLine.addOption(optionOption2);
        writeableCommandLine.addOption(argOption1);
        writeableCommandLine.addOption(argOption2);

        List optionsList = writeableCommandLine.getOptions();

        List expectedOptionsList = Arrays.asList(optionOption1, optionOption2, argOption1, argOption2);

        assertEquals(expectedOptionsList, optionsList);
    }
}