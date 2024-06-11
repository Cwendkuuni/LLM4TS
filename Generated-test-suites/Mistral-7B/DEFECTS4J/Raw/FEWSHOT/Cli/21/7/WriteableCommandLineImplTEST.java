package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "root", false, "root description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        assertEquals(rootOption, commandLine.getOption("root"));
        assertTrue(commandLine.hasOption(rootOption));
        assertTrue(commandLine.getOptionName().equals(rootOption.getLongOpt()));
        assertEquals(Collections.emptyList(), commandLine.getValues(rootOption));
        assertFalse(commandLine.getSwitch(rootOption, false));
        assertNull(commandLine.getProperty("property"));
        assertSame(commandLine, commandLine.addOption(rootOption));
        assertSame(commandLine, commandLine.addProperty(new PropertyOption(), "property", "value"));

        Option option1 = new Option("opt1", "opt1", false, "opt1 description");
        Option option2 = new Option("opt2", "opt2", false, "opt2 description");

        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOption("opt1"));
        assertEquals(option2, commandLine.getOption("opt2"));

        Option optionWithArgument = new Option("opt3", "opt3", true, "opt3 description");
        Argument argument = new Argument("arg1", "arg1 description");
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(optionWithArgument);
        commandLine.addValue(optionWithArgument, argument);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(optionWithArgument, commandLine.getOption("opt3"));
        assertEquals(argument, commandLine.getValues(optionWithArgument).get(0));
    }

    @Test
    public void testWriteableCommandLineImplWithSwitch() {
        Option rootOption = new Option("root", "root", false, "root description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        Option option = new Option("opt", "opt", false, "opt description");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testWriteableCommandLineImplWithProperties() {
        Option rootOption = new Option("root", "root", false, "root description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        Option option = new Option("opt", "opt", false, "opt description");

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");

        commandLine.addOption(option);
        commandLine.addProperty(option, "prop1", "value1");
        commandLine.addProperty(option, "prop2", "value2");

        Map propertiesMap = commandLine.getProperties(option);
        Set propertyNames = commandLine.getProperties(option).keySet();

        assertEquals(2, properties.size());
        assertEquals(2, propertyNames.size());
        assertEquals("value1", properties.getProperty("prop1"));
        assertEquals("value2", properties.getProperty("prop2"));
        assertEquals("value1", propertiesMap.get("prop1"));
        assertEquals("value2", propertiesMap.get("prop2"));
    }
}