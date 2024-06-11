package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

public class WriteableCommandLineImplTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = Option.builder("root").build();

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertNull(commandLine.getOption("nonExistentOption"));
        assertNull(commandLine.getValue("nonExistentOption"));
        assertFalse(commandLine.hasSwitch("nonExistentSwitch"));

        Option option = Option.builder("test").withLongOpt("longTest")
                .withValues(new String[]{"value1", "value2"}).build();

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
        assertEquals(Arrays.asList(new String[]{"value1", "value2"}), commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(Option.builder("test2").build()));
        assertNull(commandLine.getValue("test2"));

        commandLine.addValue(option, "newValue");

        assertEquals(Arrays.asList(new String[]{"value1", "value2", "newValue"}), commandLine.getValues(option, null));

        commandLine.addSwitch(Option.builder("switchTest").build(), true);

        assertTrue(commandLine.hasOption(Option.builder("switchTest").build()));
        assertTrue(commandLine.getSwitch(Option.builder("switchTest").build(), Boolean.FALSE));

        Option propertyOption = Option.builder("property")
                .withLongOpt("longProperty").isRequired(false)
                .withValues(new String[0]).build();

        commandLine.addProperty(propertyOption, "property", "value");

        assertEquals("value", commandLine.getProperty("property"));
        assertEquals(propertyOption, commandLine.getOption("property"));
        assertEquals(Collections.singleton("property"), commandLine.getProperties(propertyOption));

        Properties properties = new Properties();
        properties.put("testProperty", "testValue");
        commandLine.addProperty(propertyOption, "testProperty", properties);

        assertEquals(properties, commandLine.getProperties(propertyOption));
        assertEquals("testValue", commandLine.getProperty("testProperty"));

        Set propertiesSet = commandLine.getProperties();
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property"));
        assertTrue(propertiesSet.contains("testProperty"));
    }
}