package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * WriteableCommandLineImpl Test Cases
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Constructor() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertNotNull(writeableCommandLine);
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("test", "test", false, "Test option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        Option option = new Option("test", null, false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("test", "test", true, "Test option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("test", null, false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("test", "test", false, "Test option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("test", "test", false, "Test option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("test");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("test", "testValue");
        String propertyValue = writeableCommandLine.getProperty("test", "defaultValue");
        assertEquals("testValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("test1", "testValue1");
        writeableCommandLine.addProperty("test2", "testValue2");
        Set properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        List normalised = writeableCommandLine.getNormalised();
        String testTrigger = "test";
        assertTrue(writeableCommandLine.looksLikeOption(testTrigger));
        assertEquals(testTrigger, normalised.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList(option1, option2));
        String expected = String.join(" ", Arrays.asList("test1", "test2"));
        assertEquals(expected, writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList(option1, option2));
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("test", "test", true, "Test option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("test", null, false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList(option1, option2));
        List normalised = writeableCommandLine.getNormalised();
        List expected = Arrays.asList("test1", "test2");
        assertEquals(expected, normalised);
    }
}