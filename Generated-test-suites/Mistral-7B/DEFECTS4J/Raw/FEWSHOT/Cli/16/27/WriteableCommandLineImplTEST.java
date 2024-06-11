package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@RunWith(Parameterized.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option rootOption;
    private List arguments;

    public WriteableCommandLineImplTest(Option rootOption, List arguments) {
        this.rootOption = rootOption;
        this.arguments = arguments;
    }

    @Parameterized.Parameters
    public static Iterable data() {
        Option rootOption = new Option("root", "rootOption", false, "root option");

        List arguments1 = Arrays.asList("arg1", "arg2");
        List arguments2 = Collections.singletonList("arg3");

        return Arrays.asList(new Object[][]{
                {rootOption, arguments1},
                {rootOption, arguments2}
        });
    }

    @Test
    public void testWriteableCommandLineImpl() {
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, commandLine.getCommandLine());
        assertEquals(arguments, commandLine.getArguments());
        assertEquals(rootOption.getPreferredName(), commandLine.getPrefixes().iterator().next());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(new Option("opt", "option", true, "an option"));
        assertTrue(commandLine.hasOption(new Option("opt", "option", true, "an option")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(new Option("opt", "option", true, "an option"));
        commandLine.addOption(new Option("opt", "option", true, "an option"));
    }

    @Test
    public void testAddValue() {
        Argument arg = new Argument("arg", Integer.TYPE, "an argument");
        commandLine.addOption(arg);
        commandLine.addValue(arg, "value1");
        List values = commandLine.getValues(arg, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option opt = new Option("opt", "option", true, "an option");
        commandLine.addOption(opt);
        commandLine.addSwitch(opt, true);
        assertTrue(commandLine.getSwitch(opt, false));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(rootOption));
        assertFalse(commandLine.hasOption(new Option("opt2", "opt2", false, "option2")));
    }

    @Test
    public void testGetOption() {
        Option opt = new Option("opt", "option", true, "an option");
        commandLine.addOption(opt);
        assertEquals(opt, commandLine.getOption("opt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        commandLine.getOption(null);
    }

    @Test
    public void testGetValues() {
        Argument arg = new Argument("arg", Integer.TYPE, "an argument");
        commandLine.addOption(arg);
        commandLine.addValue(arg, "value1");
        commandLine.addValue(arg, "value2");
        List values = commandLine.getValues(arg, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument arg = new Argument("arg", Integer.TYPE, "an argument");
        commandLine.addOption(arg);
        List values = commandLine.getUndefaultedValues(arg);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option opt = new Option("opt", "opt", false, "an opt");
        commandLine.addOption(opt);
        commandLine.addSwitch(opt, true);
        assertTrue(commandLine.getSwitch(opt, false));
        assertFalse(commandLine.getSwitch(opt, true));
    }

    @Test
    public void testGetSwitchDefaultTrue() {
        Option opt = new Option("opt", "opt", false, "an opt");
        commandLine.addOption(opt);
        commandLine.addSwitch(opt, null);
        assertTrue(commandLine.getSwitch(opt, true));
    }

    @Test
    public void testGetSwitchDefaultFalse() {
        Option opt = new Option("opt", "opt", false, "an opt");
        commandLine.addOption(opt);
        commandLine.addSwitch(opt, null);
        assertFalse(commandLine.getSwitch(opt, false));
    }

    @Test
    public void testGetProperty() {
        PropertyOption propOption = new PropertyOption("prop", "property", true, "a property");
        commandLine.addOption(propOption);
        commandLine.addProperty(propOption, "value");
        assertEquals("value", commandLine.getProperty(propOption, "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        PropertyOption propOption1 = new PropertyOption("prop1", "property1", true, "property1");
        PropertyOption propOption2 = new PropertyOption("prop2", "property2", true, "property2");
        commandLine.addOption(propOption1);
        commandLine.addProperty(propOption1, "value1");
        commandLine.addOption(propOption2);
        commandLine.addProperty(propOption2, "value2");
        Set properties = commandLine.getProperties(propOption1);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
    }

    @Test
    public void testGetPropertiesEmpty() {
        PropertyOption propOption = new PropertyOption("prop", "property", true, "property");
        commandLine.addOption(propOption);
        Set properties = commandLine.getProperties(propOption);
        assertTrue(properties.isEmpty());
    }
}