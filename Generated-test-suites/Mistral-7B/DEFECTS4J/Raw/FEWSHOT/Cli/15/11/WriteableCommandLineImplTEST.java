package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", true, "o");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", true, "o");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", false, "o");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", true, "o");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", false, "o");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", false, "o");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Map properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("property1"));
        assertTrue(properties.containsKey("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        String commandLineString = writeableCommandLine.toString();
        assertTrue(commandLineString.startsWith("arg1"));
        assertTrue(commandLineString.endsWith("arg2"));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        List options = writeableCommandLine.getOptions();
        assertTrue(options.size() > 0);
        assertTrue(options.get(0) instanceof Option);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertTrue(optionTriggers.size() > 0);
        assertTrue(optionTriggers.contains("option"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", true, "o");
        writeableCommandLine.addOption(option);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", false, "o");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean defaultSwitch = writeableCommandLine.getSwitch(option, false);
        assertTrue(defaultSwitch);
    }
}